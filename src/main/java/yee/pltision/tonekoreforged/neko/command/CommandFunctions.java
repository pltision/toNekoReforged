package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
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
            source.sendSuccess(() -> Component.empty().append(neko.getName()).append(Lang.GET_NEKO_INFO.component()), false);   //向命令发送者（主人）发送
            neko.sendSystemMessage(Component.empty().append(player.getName()).append(Lang.GET_OWNER_INFO.component())); //向猫娘发送
        }
    }

    public static void getOwner(CommandSourceStack source, ServerPlayer player, ServerPlayer owner){
        if(NekoConnectUtil.getOwner(player,owner)){
            source.sendSuccess(() -> Component.empty().append(owner.getName()).append(Lang.GET_OWNER_INFO.component()), false);
            owner.sendSystemMessage(Component.empty().append(player.getName()).append(Lang.GET_NEKO_INFO.component()));
        }
    }

    public static void removeNeko(CommandSourceStack source, ServerPlayer player, UUID nekoUuid, String input){
        if(removeNeko(player,nekoUuid)){
            source.sendSuccess(() -> Component.empty().append(input).append(Lang.REMOVE_NEKO_INFO.component()), false);   //向命令发送者（主人）发送
            ServerPlayer neko=source.getServer().getPlayerList().getPlayer(nekoUuid);
            if(neko!=null) neko.sendSystemMessage(Component.empty().append(player.getName()).append(Lang.REMOVE_OWNER_INFO.component())); //向猫娘发送
        }
    }

    public static void removeOwner(CommandSourceStack source, ServerPlayer player, UUID ownerUuid, String input) throws CommandSyntaxException {
        if(removeOwner(player,ownerUuid)){
            source.sendSuccess(() -> Component.empty().append(input).append(Lang.REMOVE_OWNER_INFO.component()), false);   //向命令发送者（主人）发送
            ServerPlayer owner=source.getServer().getPlayerList().getPlayer(ownerUuid);
            if(owner!=null) owner.sendSystemMessage(Component.empty().append(player.getName()).append(Lang.REMOVE_NEKO_INFO.component())); //向猫娘发送
        }
    }

    public static boolean removeNeko(ServerPlayer player, UUID neko) {
        //TODO: 如果移除了集合向neko发送信息说明它不是猫猫了
        return NekoStateApi.removeNeko(player,neko,Config.removeStateWhenRemovedAllOwner);
    }


    /**
     * @return 如果成功移除。否则就是未找到。
     */
    public static boolean removeOwner(Player player, UUID owner) throws CommandSyntaxException {
        //TODO: 如果移除了集合向neko发送信息说明它不是猫猫了

        if (NekoStateApi.isNeko(player)) {
            return NekoStateApi.removeOwner(player,owner,Config.removeStateWhenRemovedAllOwner);
        }
        else throw CommandExceptions.PLAYER_NOT_NEKO.create();
    }

}
