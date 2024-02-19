package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
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
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.neko.object.NekoRequest;
import yee.pltision.tonekoreforged.neko.util.NekoModifyUtil;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber
public class NekoCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("toneko")
                        .then(Commands.literal("getNeko").executes(context ->  listNekos(context.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .requires(source->Config.enableGetNekoOrOwner||source.hasPermission(2))
                                        .executes(context ->  getNeko(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("getOwner").executes(context ->  listOwners(context.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .requires(source->Config.enableGetNekoOrOwner||source.hasPermission(2))
                                        .executes(context ->  getOwner(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("removeNeko")
                                .requires(source->Config.enableRemoveNeko||source.hasPermission(2))
                                .then(Commands.argument("player", StringArgumentType.string())
                                        .executes(context ->  removeNeko(context, OldUsersConverter.convertMobOwnerIfNecessary(context.getSource().getServer(), StringArgumentType.getString(context,"player")),StringArgumentType.getString(context,"player")))
                                )
                        )
                        .then(Commands.literal("removeOwner")
                                .requires(source->Config.enableRemoveOwner||source.hasPermission(2))
                                .then(Commands.argument("player", StringArgumentType.string())
                                        .executes(context ->  removeOwner(context, OldUsersConverter.convertMobOwnerIfNecessary(context.getSource().getServer(), StringArgumentType.getString(context,"player")),StringArgumentType.getString(context,"player")))
                                )
                        )
                        .then(Commands.literal("getExp").then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  getExp(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("setExp").requires((p_139171_) -> p_139171_.hasPermission(2))
                                .then(Commands.argument("player1", EntityArgument.player()).then(Commands.argument("player2", EntityArgument.player()).then(Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(context ->  setExp(context.getSource(),EntityArgument.getPlayer(context,"player1"),EntityArgument.getPlayer(context,"player2"),IntegerArgumentType.getInteger(context,"value")))
                                )))
                        )
                        .then(Commands.literal("petPhrase")
                                .executes(context ->  getPetPhrase(context.getSource(),context.getSource().getPlayerOrException()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  getPetPhrase(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                        .then(Commands.argument("phrase", StringArgumentType.string())
                                                .executes(context ->  setPetPhrase(context.getSource(),EntityArgument.getPlayer(context,"player"),StringArgumentType.getString(context,"phrase"),!PetPhrase.isTextEnglish(StringArgumentType.getString(context,"phrase")),0))
                                                .then(Commands.argument("ignore_english", BoolArgumentType.bool())
                                                        .executes(context ->  setPetPhrase(context.getSource(),EntityArgument.getPlayer(context,"player"),StringArgumentType.getString(context,"phrase"),BoolArgumentType.getBool(context,"ignore_english"),0))
                                                        .then(Commands.argument("ignore_after",IntegerArgumentType.integer(0))
                                                                .executes(context ->  setPetPhrase(context.getSource(),EntityArgument.getPlayer(context,"player"),StringArgumentType.getString(context,"phrase"),BoolArgumentType.getBool(context,"ignore_english"),IntegerArgumentType.getInteger(context,"ignore_after")))
                                                        )
                                                )
                                        )

                                )

                        )
                        .then(Commands.literal("accept")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  acceptPlayer(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
                        )
                        .then(Commands.literal("deny")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context ->  denyPlayer(context.getSource(),EntityArgument.getPlayer(context,"player")))
                                )
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

        AtomicReference<CommandSyntaxException> exception=new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            if(cap.checkNeko(neko.getUUID())){
                exception.set(CommandExceptions.GET_NEKO_ALREADY.create());
            }
            else{
                if(Config.addOrRemoveNeedRequest&&context.hasPermission(2)) {
                    try {
                        NekoRequest.trySendAndReturn(context,player,neko, CommandFunctions::getNeko,Lang.GET_NEKO_REQUEST.component());
                        context.sendSuccess(() -> Lang.SEND_REQUEST_INFO.component().append(neko.getName()), false);
                    } catch (CommandSyntaxException e) {
                        exception.set(e);
                    }
                }
                else {  //若不需要请求直接添加
                    CommandFunctions.getNeko(context,player,neko);
                }
            }
        });
        if(exception.get()!=null)throw exception.get();

        return 0;
    }
    public static int listOwners(CommandSourceStack context) throws CommandSyntaxException {
        Player player=context.getPlayerOrException();

        AtomicReference<NekoState> state = new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(state::set);
        if (state.get() != null) {
            Set<? extends NekoRecord> owners = state.get().getOwners();
            if (owners == null) {
                throw CommandExceptions.PLAYER_NOT_NEKO.create();
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

        AtomicReference<CommandSyntaxException> exception=new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            if(cap.getOwner(owner.getUUID())!=null){
                exception.set(CommandExceptions.GET_OWNER_ALREADY.create());
            }
            else{
                if(Config.addOrRemoveNeedRequest&&context.hasPermission(2)) {
                    try {
                        NekoRequest.trySendAndReturn(context,player,owner, CommandFunctions::getOwner,Lang.GET_OWNER_REQUEST.component());
                        context.sendSuccess(() -> Lang.SEND_REQUEST_INFO.component().append(owner.getName()), false);
                    } catch (CommandSyntaxException e) {
                        exception.set(e);
                    }
                }
                else {
                    CommandFunctions.getOwner(context,player,owner);
                }
            }
        });
        if(exception.get()!=null)throw exception.get();

        return 0;
    }

    public static int removeNeko(CommandContext<CommandSourceStack> context, UUID neko, String input) throws CommandSyntaxException {
        ServerPlayer player=context.getSource().getPlayerOrException();

        AtomicReference<CommandSyntaxException> exception=new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            if(!cap.checkNeko(neko)){
                exception.set(CommandExceptions.REMOVE_NEKO_NOT_FOUND.create());
            }
            else{
                try {
                    if(Config.addOrRemoveNeedRequest&&!context.getSource().hasPermission(2)) {
                        ServerPlayer nekoPlayer=context.getSource().getServer().getPlayerList().getPlayer(neko);
                        if(nekoPlayer==null) throw EntityArgument.NO_PLAYERS_FOUND.create();
                        NekoRequest.trySendAndReturn(context.getSource(),player,nekoPlayer, (source, sender, accept) -> CommandFunctions.removeNeko(source,sender,accept.getUUID(),input),Lang.REMOVE_REQUEST.component());
                    }
                    else{
                        CommandFunctions.removeNeko(context.getSource(),player,neko,input);
                    }
                } catch (CommandSyntaxException e) {
                    exception.set(e);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        if(exception.get()!=null)throw exception.get();

        return 0;
    }

    public static int removeOwner(CommandContext<CommandSourceStack> context, UUID owner, String input) throws CommandSyntaxException {
        ServerPlayer player=context.getSource().getPlayerOrException();

        AtomicReference<CommandSyntaxException> exception=new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            if(!cap.checkNeko(owner)){
                exception.set(CommandExceptions.REMOVE_NEKO_NOT_FOUND.create());
            }
            else{
                try {
                    if(Config.addOrRemoveNeedRequest&&!context.getSource().hasPermission(2)) {
                        ServerPlayer ownerPlayer=context.getSource().getServer().getPlayerList().getPlayer(owner);
                        if(ownerPlayer==null) throw EntityArgument.NO_PLAYERS_FOUND.create();
                        NekoRequest.trySendAndReturn(context.getSource(),player,ownerPlayer, (source, sender, accept) -> CommandFunctions.removeOwner(source,sender,accept.getUUID(),input),Lang.REMOVE_REQUEST.component());
                    }
                    else{
                        CommandFunctions.removeOwner(context.getSource(),player,owner,input);
                    }
                } catch (CommandSyntaxException e) {
                    exception.set(e);
                }
            }
        });
        if(exception.get()!=null)throw exception.get();

        return 0;
    }

    public static int getExp(CommandSourceStack context, ServerPlayer get) throws CommandSyntaxException {
        Player player=context.getPlayerOrException();

        if(!NekoModifyUtil.modifyStateRecord(player,get.getUUID(),
                nekoRecord -> context.sendSuccess(() -> Lang.GET_EXP_INFO.component().append(String.valueOf(nekoRecord.getExp())), false)))
        {
            throw CommandExceptions.GET_EXP_NOT_FOUND.create();
        }

        return 0;
    }

    public static int setExp(CommandSourceStack context,ServerPlayer neko, ServerPlayer owner,float set) throws CommandSyntaxException {
        if(!NekoModifyUtil.modifyStateRecord(neko,owner.getUUID(), nekoRecord -> {
                    nekoRecord.setExp(set);
                    context.sendSuccess(() -> Lang.SET_EXP_INFO.component().append(String.valueOf(set)), true);
                }))
        {
            throw CommandExceptions.SET_EXP_NOT_CONNECTED.create();
        }

        return 0;
    }

    public static int getPetPhrase(CommandSourceStack context, ServerPlayer player) {
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap-> context.sendSuccess(() -> Component.empty().append(player.getName()).append(Lang.GET_PET_PHRASE_INFO.component()).append(String.valueOf(cap.getPetPhrase())), false));

        return 0;
    }
    public static int setPetPhrase(CommandSourceStack context, ServerPlayer set,String phrase,boolean ignoreEnglish,int ignoreAfter) throws CommandSyntaxException {
        ServerPlayer player=context.getPlayerOrException();

        AtomicReference<NekoState> stateAtomicReference=new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(stateAtomicReference::set);
        NekoState state=stateAtomicReference.get();
        if(state!=null){
            if(!context.hasPermission(2)){
                if(player==set){
                    if((!Config.everyoneCanModifyTheirPetPhrase)&&!Config.nekoCanModifyTheirPetPhrase) throw CommandExceptions.SET_PET_PHRASE_EVERYONE_CANNOT.create();
                    if( (state.getOwners()!=null && !Config.nekoCanModifyTheirPetPhrase) &&
                            !( Config.ownerCanModifyTheirNekoPetPhrase && state.checkNeko(player.getUUID()) ) )
                        throw CommandExceptions.SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO.create();
                    if(!Config.everyoneCanModifyTheirPetPhrase)throw CommandExceptions.SET_PET_PHRASE_ONLY_NEKO.create();
                }
                else{
                    if(Config.ownerCanModifyTheirNekoPetPhrase){
                        if(!state.checkNeko(set.getUUID())) throw CommandExceptions.SET_PET_PHRASE_NOT_OWNER.create();
                    }
                    else throw CommandExceptions.SET_PET_PHRASE_CANNOT_MODIFY_OTHER.create();
                }


            }

            AtomicReference<CommandSyntaxException> throwException=new AtomicReference<>();
            set.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
                if(phrase==null){
                    cap.setPetPhrase(null);
                    context.sendSuccess(() -> Component.empty().append(set.getName()).append(Lang.SET_PET_PHRASE_INFO.component()).append("null"), false);
                }
                else{
                    int ignoreAfterMax= PetPhrase.getLastIndexOfNotIgnoreCharacter(phrase)+1;
                    if(ignoreAfter>ignoreAfterMax)throwException.set(CommandExceptions.SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL.create());
                    PetPhrase petPhrase=new PetPhrase(phrase,ignoreEnglish,ignoreAfter);
                    cap.setPetPhrase(petPhrase);
                    context.sendSuccess(() -> Component.empty().append(set.getName()).append(Lang.SET_PET_PHRASE_INFO.component()).append(petPhrase.toString()), false);

                }
            });
            if(throwException.get()!=null)throw throwException.get();
        }

        return 0;
    }

    public static int acceptPlayer(CommandSourceStack source,ServerPlayer sender) throws CommandSyntaxException {
        ServerPlayer player= source.getPlayerOrException();

        if(NekoRequest.tryAccept(source,player,sender)){
            source.sendSuccess(Lang.ACCEPT_INFO::component,false);
            return 0;
        }
        else throw CommandExceptions.ACCEPT_FAIL.create();
    }

    public static int denyPlayer(CommandSourceStack source,ServerPlayer sender) throws CommandSyntaxException {
        ServerPlayer player= source.getPlayerOrException();

        if(NekoRequest.deny(source.getServer(),player,sender)){
            source.sendSuccess(Lang.DENY_INFO::component,false);
            return 0;
        }
        else throw CommandExceptions.ACCEPT_FAIL.create();
    }
}