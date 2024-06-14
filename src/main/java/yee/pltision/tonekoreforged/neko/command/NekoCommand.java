package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.event.ToNekoCommandEvent;
import yee.pltision.tonekoreforged.neko.object.NekoRequest;
import yee.pltision.tonekoreforged.neko.util.NekoStateApi;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings({"StringConcatenationArgumentToLogCall", "LoggingSimilarMessage", "SameReturnValue"})
@Mod.EventBusSubscriber
public class NekoCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("toneko")
                        .then(Commands.literal("getNeko").executes(context -> listNekos(context.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .requires(source -> Config.enableGetNekoOrOwner || source.hasPermission(2))
                                        .executes(context -> getNeko(context.getSource(), EntityArgument.getPlayer(context, "player")))
                                )
                        )
                        .then(Commands.literal("getOwner").executes(context -> listOwners(context.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .requires(source -> Config.enableGetNekoOrOwner || source.hasPermission(2))
                                        .executes(context -> getOwner(context.getSource(), EntityArgument.getPlayer(context, "player")))
                                )
                        )
                        .then(Commands.literal("removeNeko")
                                .requires(source -> Config.enableRemoveNeko || source.hasPermission(2))
                                .then(Commands.argument("player", StringArgumentType.string())
                                        .executes((TryWithPrintException)context -> removeNeko(context.getSource(), StringArgumentType.getString(context,"player")))    //暂时改为Entity
                                )
                        )
                        .then(Commands.literal("removeOwner")
                                .requires(source -> Config.enableRemoveOwner || source.hasPermission(2))
                                .then(Commands.argument("player", StringArgumentType.string())
                                        .executes((TryWithPrintException)context -> removeOwner(context.getSource(), StringArgumentType.getString(context,"player")))   //暂时改为Entity
                                )
                        )
                        .then(Commands.literal("getExp").then(Commands.argument("player", StringArgumentType.string())
                                        .executes((TryWithPrintException)context -> getExp(context.getSource(), StringArgumentType.getString(context, "player")))
                                )
                        )
                        .then(Commands.literal("setExp").requires((p_139171_) -> p_139171_.hasPermission(2))
                                .then(Commands.argument("player1", StringArgumentType.string()).then(Commands.argument("player2", StringArgumentType.string()).then(Commands.argument("value", FloatArgumentType.floatArg())
                                        .executes((TryWithPrintException)context -> setExp(context.getSource(), getUuidOrException(context.getSource().getServer(), StringArgumentType.getString(context,"player1")), getUuidOrException(context.getSource().getServer(), StringArgumentType.getString(context,"player2")), FloatArgumentType.getFloat(context, "value")))
                                )))
                        )
                        .then(Commands.literal("petPhrase")
                                .executes(context -> getPetPhrase(context.getSource(), context.getSource().getPlayerOrException()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> getPetPhrase(context.getSource(), EntityArgument.getPlayer(context, "player")))
                                        .then(Commands.argument("phrase", StringArgumentType.string())
                                                .executes(context -> setPetPhrase(context.getSource(), EntityArgument.getPlayer(context, "player"), StringArgumentType.getString(context, "phrase"), !PetPhrase.isTextEnglish(StringArgumentType.getString(context, "phrase")), 0))
                                                .then(Commands.argument("ignore_english", BoolArgumentType.bool())
                                                        .executes(context -> setPetPhrase(context.getSource(), EntityArgument.getPlayer(context, "player"), StringArgumentType.getString(context, "phrase"), BoolArgumentType.getBool(context, "ignore_english"), 0))
                                                        .then(Commands.argument("ignore_after", IntegerArgumentType.integer(0))
                                                                .executes(context -> setPetPhrase(context.getSource(), EntityArgument.getPlayer(context, "player"), StringArgumentType.getString(context, "phrase"), BoolArgumentType.getBool(context, "ignore_english"), IntegerArgumentType.getInteger(context, "ignore_after")))
                                                        )
                                                )
                                        )

                                )

                        )
                        .then(Commands.literal("accept")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> acceptPlayer(context.getSource(), EntityArgument.getPlayer(context, "player")))
                                )
                        )
                        .then(Commands.literal("deny")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(context -> denyPlayer(context.getSource(), EntityArgument.getPlayer(context, "player")))
                                )
                        )
                        .then(Commands.literal("help")
                                .executes(context -> help(context.getSource()))
                        )
                        .then(Commands.literal("nekoRite")
                                .executes(context -> help(context.getSource()))
                        )

        );
    }

    /*@SubscribeEvent
    public static void test(ToNekoCommandEvent.AddNekoToExecutorCommandEvent event){
        System.out.println("喵呜呜呜呜呜呜呜呜呜呜");
        event.setCanceled(true);
    }*/

    //⟳∅Ø

    /**
     * 用于通过uuid列表中的获取玩家的名字并添加到component里面
     */
    public static void listPlayers(MutableComponent component, Iterator<UUID> it, MinecraftServer server) {
        component.append("[");
        PlayerList playerList = server.getPlayerList();
        while (it.hasNext()) {   //遍历集合
            UUID uuid = it.next();
            Player neko = playerList.getPlayer(uuid);
            if (neko == null) {    //如果无法从playerList获取到玩家，尝试从GameProfileCache中获取名称
                GameProfileCache gameProfileCache = server.getProfileCache();
                if (gameProfileCache != null) {
                    gameProfileCache.get(uuid).ifPresentOrElse(
                            profile -> component.append(profile.getName()),
                            () -> component.append(uuid.toString()));
                } else component.append(uuid.toString());  //都找不到直接输出uuid
            } else component.append(neko.getName());
            if (it.hasNext()) component.append(", "); //如果不是最后一个的话输出逗号
        }
        component.append("]");
    }

    /**
     * 用于列出玩家的所有猫猫。当尝试列出的uuid的玩家离线时，会尝试从GameProfileCache中获取。若仍未找点直接输出uuid。
     */
    public static int listNekos(CommandSourceStack context) throws CommandSyntaxException
    {
        if(postEvent(new ToNekoCommandEvent.ListNekoOfExecutorCommandEvent(context))) return 0;

        Player player = context.getPlayerOrException();

        context.sendSuccess(() -> {
            MutableComponent component = Lang.LIST_NEKO_INFO.component();
            listPlayers(component, NekoStateApi.getNekos(player).iterator(), context.getServer());
            return component;
        }, false);

        return 0;
    }

    public static int listOwners(CommandSourceStack context) throws CommandSyntaxException
    {
        if(postEvent(new ToNekoCommandEvent.ListOwnerOfExecutorCommandEvent(context))) return 0;

        Player player = context.getPlayerOrException();

        Set<UUID> owners = NekoStateApi.getOwners(player);
        if (owners == null)
            throw CommandExceptions.PLAYER_NOT_NEKO.create();
        else {
            context.sendSuccess(() -> {
                MutableComponent component = Lang.LIST_OWNER_INFO.component();
                Iterator<UUID> it = owners.iterator();
                listPlayers(component, it, context.getServer());
                return component;
            }, false);
        }

        return 0;
    }

    public static int getNeko(CommandSourceStack context, ServerPlayer neko) throws CommandSyntaxException {
        var event=new ToNekoCommandEvent.AddNekoToExecutorCommandEvent(context,neko,Config.addOrRemoveNeedRequest && !context.hasPermission(2));
        if(postEvent(event)) return 0;
        boolean doSendRequest=event.shouldSendRequest();
        boolean ignoreDefaultException=event.isIgnoreDefaultException();

        ServerPlayer player = context.getPlayerOrException();

        CommandSyntaxException exception = CommandTester.canAddNeko(player, neko);
        if (exception != null && !(context.hasPermission(2)||ignoreDefaultException)) throw exception;

        if (doSendRequest) {
            NekoRequest.trySendAndReturn(context, player, neko, (source, sender, accept) -> {
                CommandSyntaxException e = CommandTester.canAddOwner(accept, sender);
                if (e != null) throw e;
                CommandFunctions.getNeko(source, sender, accept);
            }, Lang.GET_NEKO_REQUEST.component());
//            context.sendSuccess(() -> Lang.SEND_REQUEST_INFO.component().append(neko.getName()), false);
        } else {
            CommandFunctions.getNeko(context, player, neko);
        }

        return 0;
    }

    public static int getOwner(CommandSourceStack context, ServerPlayer owner) throws CommandSyntaxException {
        var event=new ToNekoCommandEvent.AddOwnerToExecutorCommandEvent(context,owner,Config.addOrRemoveNeedRequest && !context.hasPermission(2));
        if(postEvent(event)) return 0;
        boolean doSendRequest=event.shouldSendRequest();
        boolean ignoreDefaultException=event.isIgnoreDefaultException();

        ServerPlayer player = context.getPlayerOrException();

        CommandSyntaxException exception = CommandTester.canAddOwner(player, owner);
        if (exception != null && !(context.hasPermission(2)||ignoreDefaultException)) throw exception;

        if (doSendRequest) {
            NekoRequest.trySendAndReturn(context, player, owner, (source, sender, accept) -> {
                CommandSyntaxException e = CommandTester.canAddNeko(accept, sender);
                if (e != null) throw e;
                CommandFunctions.getOwner(source, sender, accept);
            }, Lang.GET_OWNER_REQUEST.component());
//            context.sendSuccess(() -> Lang.SEND_REQUEST_INFO.component().append(owner.getName()), false);
        } else {
            CommandFunctions.getOwner(context, player, owner);
        }

        return 0;
    }

    public static UUID getUuidOrException(MinecraftServer server,String str) throws CommandSyntaxException{
        UUID uuid= OldUsersConverter.convertMobOwnerIfNecessary(server, str);
        if (uuid == null) throw EntityArgument.NO_PLAYERS_FOUND.create();
        return uuid;
    }

    public static int removeNeko(CommandSourceStack stack, String str) throws CommandSyntaxException {
        UUID neko= getUuidOrException(stack.getServer(), str);

        var event=new ToNekoCommandEvent.RemoveNekoToExecutorCommandEvent(stack,neko,str,Config.addOrRemoveNeedRequest && !stack.hasPermission(2));
        if(postEvent(event)) return 0;
        boolean doSendRequest=event.shouldSendRequest();

        ServerPlayer player = stack.getPlayerOrException();
        if (!NekoStateApi.containsNeko(player, neko)) {
            throw CommandExceptions.REMOVE_NEKO_NOT_FOUND.create();
        } else {
            if (doSendRequest) {
                ServerPlayer nekoPlayer = stack.getServer().getPlayerList().getPlayer(neko);
                if (nekoPlayer == null) throw EntityArgument.NO_PLAYERS_FOUND.create();
                NekoRequest.trySendAndReturn(stack, player, nekoPlayer, (source, sender, accept) -> CommandFunctions.removeNeko(source, sender, accept.getUUID(), str), Lang.REMOVE_REQUEST.component());
            } else {
                CommandFunctions.removeNeko(stack, player, neko, str);
            }
        }

        return 0;
    }

    public static int removeOwner(CommandSourceStack stack, String str) throws CommandSyntaxException {
        UUID owner= getUuidOrException(stack.getServer(), str);

        var event=new ToNekoCommandEvent.RemoveOwnerToExecutorCommandEvent(stack,owner,str,Config.addOrRemoveNeedRequest && !stack.hasPermission(2));
        if(postEvent(event)) return 0;
        boolean doSendRequest=event.shouldSendRequest();

        ServerPlayer player = stack.getPlayerOrException();
        if (!NekoStateApi.containsOwner(player,owner)) {
            throw CommandExceptions.REMOVE_OWNER_NOT_FOUND.create();
        } else {
            if (doSendRequest) {
                ServerPlayer ownerPlayer = stack.getServer().getPlayerList().getPlayer(owner);
                if (ownerPlayer == null) throw EntityArgument.NO_PLAYERS_FOUND.create();
                NekoRequest.trySendAndReturn(stack, player, ownerPlayer, (source, sender, accept) -> CommandFunctions.removeOwner(source, sender, accept.getUUID(), str), Lang.REMOVE_REQUEST.component());
            } else {
                CommandFunctions.removeOwner(stack, player, owner, str);
            }
        }

        return 0;
    }

    public static int getExp(CommandSourceStack context, String get) throws CommandSyntaxException {
        Player player = context.getPlayerOrException();

        float exp= NekoStateApi.getExp(player,getUuidOrException(context.getServer(), get));
        if(Float.isNaN(exp)) throw CommandExceptions.GET_EXP_NOT_FOUND.create();
        context.sendSuccess(() -> Lang.GET_EXP_INFO.component().append(String.valueOf(exp)), false);

        return 0;
    }

    public static int setExp(CommandSourceStack context, UUID a, UUID b, float set) throws CommandSyntaxException {
        if (NekoStateApi.setExp(a, b, set))
            throw CommandExceptions.SET_EXP_NOT_CONNECTED.create();
        context.sendSuccess(() -> Lang.SET_EXP_INFO.component().append(String.valueOf(set)), true);

        return 0;
    }

    public static int getPetPhrase(CommandSourceStack context, ServerPlayer player) {
        context.sendSuccess(() -> Component.empty().append(player.getName()).append(Lang.GET_PET_PHRASE_INFO.component()).append(String.valueOf(NekoStateApi.getPetPhrase(player))), false);

        return 0;
    }

    public static int setPetPhrase(CommandSourceStack context, ServerPlayer set, String phrase, boolean ignoreEnglish, int ignoreAfter) throws CommandSyntaxException {
        var event=new ToNekoCommandEvent.SetPetPhraseEvent(context,set,phrase,ignoreEnglish,ignoreAfter);
        if(postEvent(event)) return 0;
        boolean ignoreDefaultException=event.isIgnoreDefaultException();

        ServerPlayer player = context.getPlayerOrException();

        CommandSyntaxException exception = CommandTester.canModifyPetPhrase(player, set);
        if (exception != null && !(context.hasPermission(2)||ignoreDefaultException)) throw exception;


        if (phrase == null) {
            NekoStateApi.setPetPhrase(set, null);
            context.sendSuccess(() -> Component.empty().append(set.getName()).append(Lang.SET_PET_PHRASE_INFO.component()).append("null"), false);
        } else {
            int ignoreAfterMax = PetPhrase.getLastIndexOfNotIgnoreCharacter(phrase) + 1;
            if (ignoreAfter > ignoreAfterMax) throw CommandExceptions.SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL.create();
            PetPhrase petPhrase = new PetPhrase(phrase, ignoreEnglish, ignoreAfter);
            NekoStateApi.setPetPhrase(set, petPhrase);
            context.sendSuccess(() -> Component.empty().append(set.getName()).append(Lang.SET_PET_PHRASE_INFO.component()).append(petPhrase.toString()), false);
        }

        return 0;
    }

    public static int acceptPlayer(CommandSourceStack source, ServerPlayer sender) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        if (NekoRequest.tryAccept(source, sender, player)) {
            source.sendSuccess(Lang.ACCEPT_INFO::component, false);
            return 0;
        } else throw CommandExceptions.ACCEPT_FAIL.create();
    }

    public static int denyPlayer(CommandSourceStack source, ServerPlayer sender) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        if (NekoRequest.deny(source.getServer(), sender, player)) {
            source.sendSuccess(Lang.DENY_INFO::component, false);
            return 0;
        } else throw CommandExceptions.ACCEPT_FAIL.create();
    }

    public static int help(CommandSourceStack stack) {
        stack.sendSuccess(Config.usingRite, false);
//        stack.sendSuccess(()->Component.translatableWithFallback("a.toneko.command","%s 喵喵","asdgweg"),false);
        return 0;
    }

    interface TryWithPrintException extends Command<CommandSourceStack>{
        @Override
        default int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
            try {
                return tryRun(commandContext);
            } catch (Exception e) {
                if (!(e instanceof CommandSyntaxException)) ToNeko.LOGGER.error("[ToNeko] Exception when execute command: " + e);
                throw e;
            }
        }
        int tryRun(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }

    //返回ture则return掉方法
    public static boolean postEvent(ToNekoCommandEvent event) throws CommandSyntaxException{
        boolean cancel =MinecraftForge.EVENT_BUS.post(event);
        if(event.getException() !=null) throw event.getException();
        return cancel;
    }
}