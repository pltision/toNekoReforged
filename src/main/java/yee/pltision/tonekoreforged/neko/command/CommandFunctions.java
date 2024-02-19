package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.util.NekoConnectUtil;
import yee.pltision.tonekoreforged.neko.util.NekoModifyUtil;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

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

        NekoModifyUtil.remove(player, NekoModifyUtil.OperatorState.OWNER,neko, Config.removeStateWhenRemovedAllOwner);  //为player移除主人
        //TODO: 如果移除了集合向neko发送信息说明它不是猫猫了

        AtomicBoolean isSuccess = new AtomicBoolean(false);
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap-> isSuccess.set(cap.removeNeko(neko)));

        return isSuccess.get();
    }


    /**
     * @return 如果成功移除。否则就是未找到。
     */
    public static boolean removeOwner(Player player, UUID owner) throws CommandSyntaxException {
        AtomicReference<NekoState> state = new AtomicReference<>();
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(state::set);
        if (state.get() != null) {
            Set<? extends NekoRecord> owners = state.get().getOwners();
            if (owners != null) {
                if(state.get().removeOwner(owner,Config.removeStateWhenRemovedAllOwner)){   //如果成功移除
                    //TODO: 如果移除了集合向neko发送信息说明它不是猫猫了

                    NekoModifyUtil.remove(player, NekoModifyUtil.OperatorState.NEKO,owner,false/*主人移除猫猫不需要移除集合，此值形参无效*/);
                    return true;
                }
                else return false;
            }
            else throw CommandExceptions.PLAYER_NOT_NEKO.create();

        }
        return false;
    }

}
