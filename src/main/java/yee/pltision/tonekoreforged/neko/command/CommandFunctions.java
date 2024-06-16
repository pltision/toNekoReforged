package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.util.NekoStateApi;
import yee.pltision.tonekoreforged.neko.util.NekoConnectUtil;

import java.util.UUID;

public class CommandFunctions {

    public static void getNeko(CommandSourceStack source, ServerPlayer player, ServerPlayer neko){
        if(NekoConnectUtil.getNeko(player,neko)){
            player.sendSystemMessage(Lang.GET_NEKO_INFO.component(neko.getName()));
            neko.sendSystemMessage(Lang.GET_OWNER_INFO.component(player.getName())); //向猫娘发送
        }
    }

    public static void getOwner(CommandSourceStack source, ServerPlayer player, ServerPlayer owner){
        if(NekoConnectUtil.getOwner(player,owner)){
            player.sendSystemMessage(Lang.GET_OWNER_INFO.component(owner.getName()));
            owner.sendSystemMessage(Lang.GET_NEKO_INFO.component(player.getName()));
        }
    }

    public static void removeNeko(CommandSourceStack source, ServerPlayer player, UUID nekoUuid, String input){
        if(removeNeko(source.getServer(),player,nekoUuid)){
            player.sendSystemMessage(Lang.REMOVE_NEKO_INFO.component(input));
            ServerPlayer neko=source.getServer().getPlayerList().getPlayer(nekoUuid);
            if(neko!=null) neko.sendSystemMessage(Lang.REMOVE_OWNER_INFO.component(player.getName())); //向猫娘发送
        }
    }

    public static void removeOwner(CommandSourceStack source, ServerPlayer player, UUID ownerUuid, String input) throws CommandSyntaxException {
        if(removeOwner(player,ownerUuid)){
            player.sendSystemMessage(Lang.REMOVE_OWNER_INFO.component(input));
            ServerPlayer owner=source.getServer().getPlayerList().getPlayer(ownerUuid);
            if(owner!=null) owner.sendSystemMessage(Lang.REMOVE_NEKO_INFO.component(player.getName())); //向主人发送
        }
    }

    public static boolean removeNeko(MinecraftServer server, ServerPlayer player, UUID neko) {
        //如果移除了集合向neko发送信息说明它不是猫猫了
        if(NekoStateApi.removeNeko(player,neko,Config.removeStateWhenRemovedAllOwner)){
            if(Config.removeStateWhenRemovedAllOwner){
                ServerPlayer nekoPlayer= server.getPlayerList().getPlayer(neko);
                if(nekoPlayer!=null&&!NekoStateApi.isNeko(nekoPlayer)){
                    nekoPlayer.sendSystemMessage(Lang.REMOVED_NEKO_STATE_INFO.component());
                }
            }
            return true;
        }
        return false;
    }


    /**
     * @return 如果成功移除。否则就是未找到。
     */
    public static boolean removeOwner(Player player, UUID owner) throws CommandSyntaxException {

        if (NekoStateApi.isNeko(player)) {

            //如果移除了集合向neko发送信息说明它不是猫猫了
            if(NekoStateApi.removeOwner(player,owner,Config.removeStateWhenRemovedAllOwner)){
                if(Config.removeStateWhenRemovedAllOwner)
                    if(!NekoStateApi.isNeko(player))
                        player.sendSystemMessage(Lang.REMOVED_NEKO_STATE_INFO.component());
                return true;
            }
            return false;
        }
        else throw CommandExceptions.PLAYER_NOT_NEKO.create();


    }

}
