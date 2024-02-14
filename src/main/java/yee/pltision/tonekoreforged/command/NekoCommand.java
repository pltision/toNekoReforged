package yee.pltision.tonekoreforged.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.Config;
import yee.pltision.tonekoreforged.capability.NekoCapabilityProvider;
import yee.pltision.tonekoreforged.interfaces.NekoRecord;
import yee.pltision.tonekoreforged.interfaces.NekoState;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber
public class NekoCommand {
    private static final int DEFAULT_TIME = -1;

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("toneko")
                        .then(Commands.literal("getNeko").executes(context ->  listNekos(context.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  getNeko(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("getOwner").executes(context ->  listOwners(context.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  getOwner(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("removeNeko")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  removeNeko(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("removeOwner")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  removeOwner(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )



        );
    }

    public static final SimpleCommandExceptionType LIST_NEKO_NOT_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.toneko.list_neko.not_player"));

    /**
     * 用于列出玩家的所有猫猫。当尝试列出的uuid的玩家离线时，会尝试从GameProfileCache中获取。若仍未找点直接输出uuid。
     */
    public static int listNekos(CommandSourceStack context) throws CommandSyntaxException {
        if(context.getEntityOrException()instanceof Player player){
            context.sendSuccess(()->{
                MutableComponent component=Component.translatable("commands.toneko.list_neko.info");
                player.getCapability(NekoCapabilityProvider.NEKO_STATE).ifPresent(cap->{
                    Iterator<UUID> it=cap.getNekos().iterator();
                    listPlayers(component,it,context.getServer());
                });
                return component;
            },false);
        }
        else throw LIST_NEKO_NOT_PLAYER.create();

        return 0;
    }

    /**
     * 用于通过uuid列表中的获取玩家的名字并添加到component里面
     */
    public static void listPlayers(MutableComponent component, Iterator<UUID> it, MinecraftServer server){
        component.append("[");
        PlayerList playerList= server.getPlayerList();
        while (it.hasNext()){   //遍历集合
            UUID uuid=it.next();
            Player neko=playerList.getPlayer(uuid);
            if(neko==null){    //如果无法从playerList获取到玩家，尝试从GameProfileCache中获取名称
                GameProfileCache gameProfileCache= server.getProfileCache();
                if(gameProfileCache!=null){
//                    System.out.println(uuid);
                    gameProfileCache.get(uuid).ifPresentOrElse(
                            profile->component.append(profile.getName()),
                            ()->component.append(uuid.toString()));
                }
                else component.append(uuid.toString());  //都找不到直接输出uuid
            }
            else component.append(neko.getName());
            if(it.hasNext()) component.append(", "); //如果不是最后一个的话输出逗号
        }
        component.append("]");
    }

    public static final SimpleCommandExceptionType ENTITY_NOT_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.toneko.entity_not_player"));
    public static final SimpleCommandExceptionType ENTITY_NOT_NEKO = new SimpleCommandExceptionType(Component.translatable("commands.toneko.entity_not_neko"));

    public static final SimpleCommandExceptionType GET_NEKO_CONNECTED = new SimpleCommandExceptionType(Component.translatable("commands.toneko.get_neko.connected"));

    public static int getNeko(CommandSourceStack context, ServerPlayer neko) throws CommandSyntaxException {
        if(context.getEntityOrException()instanceof Player player){
//            System.out.println(player+" "+neko);
            PlayerNekoUtils.connect(player, PlayerNekoUtils.OperatorState.OWNER,neko);  //将player添加为neko的主人

            AtomicBoolean isSuccess = new AtomicBoolean(false);
            player.getCapability(NekoCapabilityProvider.NEKO_STATE).ifPresent(cap->{
                isSuccess.set(cap.addNeko(neko.getUUID()));     //将neko添加为player的猫猫
            });

            if(isSuccess.get()) {
                context.sendSuccess(() -> Component.translatable("commands.toneko.get_neko.info").append(neko.getName()), false);
            }
            else {
                throw GET_NEKO_CONNECTED.create();
            }

        }
        else throw ENTITY_NOT_PLAYER.create();

        return 0;
    }
    public static int listOwners(CommandSourceStack context) throws CommandSyntaxException {
//        try {
        if (context.getEntityOrException() instanceof Player player) {
            AtomicReference<NekoState> state = new AtomicReference<>();
            player.getCapability(NekoCapabilityProvider.NEKO_STATE).ifPresent(state::set);
            if (state.get() != null) {
                Set<? extends NekoRecord> owners = state.get().getOwners();
                if (owners == null) {
                    throw ENTITY_NOT_NEKO.create();
                } else {
                    context.sendSuccess(() -> {
                        MutableComponent component = Component.translatable("commands.toneko.list_owner.info");
                        Iterator<UUID> it = new NekoRecord.UUIDIterator(owners.iterator());
                        listPlayers(component, it, context.getServer());
                        return component;
                    }, false);
                }

            }
        } else throw ENTITY_NOT_PLAYER.create();
//        }
//        catch (Exception e){e.printStackTrace();}
        return 0;
    }

    public static final SimpleCommandExceptionType GET_OWNER_CONNECTED = new SimpleCommandExceptionType(Component.translatable("commands.toneko.get_owner.connected"));
    public static int getOwner(CommandSourceStack context, ServerPlayer owner) throws CommandSyntaxException {
        if(context.getEntityOrException()instanceof Player player){
//            System.out.println(player+" "+neko);
            PlayerNekoUtils.connect(player, PlayerNekoUtils.OperatorState.NEKO,owner);  //将player添加为over的猫猫

            AtomicBoolean isSuccess = new AtomicBoolean(false);
            player.getCapability(NekoCapabilityProvider.NEKO_STATE).ifPresent(cap->{
                isSuccess.set(cap.addOwner(owner.getUUID()));     //将owner添加为player的主人
            });

            if(isSuccess.get()) {
                context.sendSuccess(() -> Component.translatable("commands.toneko.get_owner.info").append(owner.getName()), false);
            }
            else {
                throw GET_OWNER_CONNECTED.create();
            }

        }
        else throw ENTITY_NOT_PLAYER.create();

        return 0;
    }

    public static final SimpleCommandExceptionType REMOVE_NEKO_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("commands.toneko.remove_neko.not_found"));
    public static int removeNeko(CommandSourceStack context, ServerPlayer neko) throws CommandSyntaxException {
        if(context.getEntityOrException()instanceof Player player){
//            System.out.println(player+" "+neko);
            PlayerNekoUtils.remove(player, PlayerNekoUtils.OperatorState.OWNER,neko.getUUID(), Config.removeSetWhenRemovedAllOwner);  //为player移除主人
            //TODO: 如果移除了集合向neko发送信息说明它不是猫猫了

            AtomicBoolean isSuccess = new AtomicBoolean(false);
            player.getCapability(NekoCapabilityProvider.NEKO_STATE).ifPresent(cap-> isSuccess.set(cap.removeNeko(neko.getUUID())));

            if(isSuccess.get()) {
                context.sendSuccess(() -> Component.translatable("commands.toneko.remove_neko.info").append(neko.getName()), false);
            }
            else {
                throw REMOVE_NEKO_NOT_FOUND.create();
            }

        }
        else throw ENTITY_NOT_PLAYER.create();

        return 0;
    }

    public static final SimpleCommandExceptionType REMOVE_OWNER_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("commands.toneko.remove_owner.not_found"));
    public static int removeOwner(CommandSourceStack context, ServerPlayer owner) throws CommandSyntaxException {

        if (context.getEntityOrException() instanceof Player player) {

            AtomicReference<NekoState> state = new AtomicReference<>();
            player.getCapability(NekoCapabilityProvider.NEKO_STATE).ifPresent(state::set);
            if (state.get() != null) {
                Set<? extends NekoRecord> owners = state.get().getOwners();
                if (owners != null) {
                    if(state.get().removeOwner(owner.getUUID(),Config.removeSetWhenRemovedAllOwner)){   //如果成功移除
                        PlayerNekoUtils.remove(player, PlayerNekoUtils.OperatorState.NEKO,owner.getUUID(),false/*主人移除猫猫不需要移除集合，此值形参无效*/);
                        context.sendSuccess(() -> Component.translatable("commands.toneko.remove_owner.info").append(owner.getName()), false);
                        if(state.get().getOwners()==null){
                            context.sendSuccess(() -> Component.translatable("commands.toneko.remove_neko.removed_neko_state"), false);
                        }
                    }
                    else throw REMOVE_OWNER_NOT_FOUND.create();

                } else throw ENTITY_NOT_NEKO.create();

            }
        } else throw ENTITY_NOT_PLAYER.create();

        return 0;

    }

}