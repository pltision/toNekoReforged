package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.util.NekoConnectUtil;
import yee.pltision.tonekoreforged.neko.util.NekoModifyUtil;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.api.NekoRecord;
import yee.pltision.tonekoreforged.neko.api.NekoState;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
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
                                .then(Commands.argument("player", StringArgumentType.string())
                                        .executes(context ->  removeNeko(context.getSource(), OldUsersConverter.convertMobOwnerIfNecessary(context.getSource().getServer(), StringArgumentType.getString(context,"player")),StringArgumentType.getString(context,"player")))
                                )
                        )
                        .then(Commands.literal("removeOwner")
                                .then(Commands.argument("player", StringArgumentType.string())
                                        .executes(context ->  removeOwner(context.getSource(), OldUsersConverter.convertMobOwnerIfNecessary(context.getSource().getServer(), StringArgumentType.getString(context,"player")),StringArgumentType.getString(context,"player")))
                                )
                        )
                        .then(Commands.literal("getExp").then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  getExp(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("setExp").requires((p_139171_) -> p_139171_.hasPermission(2))
                                .then(Commands.argument("neko", EntityArgument.player()).then(Commands.argument("owner", EntityArgument.player()).then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(context ->  setExp(context.getSource(),EntityArgument.getPlayer(context,"neko"),EntityArgument.getPlayer(context,"owner"),IntegerArgumentType.getInteger(context,"value")))
                                )))
                        )



        );
    }

    //⟳∅Ø

    /**
     * 用于列出玩家的所有猫猫。当尝试列出的uuid的玩家离线时，会尝试从GameProfileCache中获取。若仍未找点直接输出uuid。
     */
    public static int listNekos(CommandSourceStack context) throws CommandSyntaxException {
        Player player=context.getPlayerOrException();

        context.sendSuccess(()->{
            MutableComponent component= Lang.LIST_NEKO_INFO.component();
            player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
                Iterator<UUID> it=cap.getNekos().iterator();
                listPlayers(component,it,context.getServer());
            });
            return component;
        },false);

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

    public static int getNeko(CommandSourceStack context, ServerPlayer neko) throws CommandSyntaxException {
        ServerPlayer player=context.getPlayerOrException();

        if(NekoConnectUtil.getNeko(player,neko)) {
            context.sendSuccess(() -> Component.empty().append(neko.getName()).append(Lang.GET_NEKO_INFO.component()), false);
        }
        else {
            throw CommandException.GET_NEKO_ALREADY.create();
        }

        return 0;
    }
    public static int listOwners(CommandSourceStack context) throws CommandSyntaxException {
        Player player=context.getPlayerOrException();

        AtomicReference<NekoState> state = new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(state::set);
        if (state.get() != null) {
            Set<? extends NekoRecord> owners = state.get().getOwners();
            if (owners == null) {
                throw CommandException.PLAYER_NOT_NEKO.create();
            } else {
                context.sendSuccess(() -> {
                    MutableComponent component = Lang.LIST_OWNER_INFO.component();
                    Iterator<UUID> it = new NekoRecord.UUIDIterator(owners.iterator());
                    listPlayers(component, it, context.getServer());
                    return component;
                }, false);
            }
        }
        return 0;
    }

    public static int getOwner(CommandSourceStack context, ServerPlayer owner) throws CommandSyntaxException {
        ServerPlayer player=context.getPlayerOrException();

        if(NekoConnectUtil.getOwner(player,owner)) {
            context.sendSuccess(() -> Component.empty().append(owner.getName()).append(Lang.GET_OWNER_INFO.component()), true);
        }
        else {
            throw CommandException.GET_OWNER_ALREADY.create();
        }

        return 0;
    }

    public static int removeNeko(CommandSourceStack context, UUID neko,String input) throws CommandSyntaxException {
        ServerPlayer player=context.getPlayerOrException();

        if(NekoConnectUtil.removeNeko(player,neko)) {
            context.sendSuccess(() ->Component.empty().append(input).append(Lang.REMOVE_NEKO_INFO.component()), false);
        }
        else {
            throw CommandException.REMOVE_NEKO_NOT_FOUND.create();
        }

        return 0;
    }

    public static int removeOwner(CommandSourceStack context, UUID owner, String input) throws CommandSyntaxException {
        ServerPlayer player=context.getPlayerOrException();

        if(NekoConnectUtil.removeNeko(player,owner)) {
            context.sendSuccess(() ->Component.empty().append(input).append(Lang.REMOVE_OWNER_INFO.component()), false);
        }
        else {
            throw CommandException.REMOVE_OWNER_NOT_FOUND.create();
        }

        return 0;
    }

    public static int getExp(CommandSourceStack context, ServerPlayer get) throws CommandSyntaxException {
        Player player=context.getPlayerOrException();

        if(!NekoModifyUtil.modifyStateRecord(player,get.getUUID(),
                nekoRecord -> context.sendSuccess(() -> Lang.GET_EXP_INFO.component().append(String.valueOf(nekoRecord.getExp())), false)))
        {
            throw CommandException.GET_EXP_NOT_FOUND.create();
        }

        return 0;
    }

    public static int setExp(CommandSourceStack context,ServerPlayer neko, ServerPlayer owner,float set) throws CommandSyntaxException {
        if(!NekoModifyUtil.modifyStateRecord(neko,owner.getUUID(), nekoRecord -> {
                    nekoRecord.setExp(set);
                    context.sendSuccess(() -> Lang.SET_EXP_INFO.component().append(String.valueOf(set)), true);
                }))
        {
            throw CommandException.SET_EXP_NOT_CONNECTED.create();
        }

        return 0;
    }
}