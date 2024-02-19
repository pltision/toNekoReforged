package yee.pltision.tonekoreforged.neko.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.command.CommandExceptions;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NekoConnectUtil {
    /**
     * @return 如果成功添加了。（失败的话说明neko已经是player的猫猫了
     */
    public static boolean getNeko(ServerPlayer player, ServerPlayer neko){

        NekoModifyUtil.connect(player, NekoModifyUtil.OperatorState.OWNER,neko);  //将player添加为neko的主人

        AtomicBoolean isSuccess = new AtomicBoolean(false);
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            isSuccess.set(cap.addNeko(neko.getUUID()));     //将neko添加为player的猫猫
        });

        return isSuccess.get();
    }

    public static boolean getOwner(ServerPlayer player, ServerPlayer owner) {
        NekoModifyUtil.connect(player, NekoModifyUtil.OperatorState.NEKO,owner);  //将player添加为over的猫猫

        AtomicBoolean isSuccess = new AtomicBoolean(false);
        player.getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            isSuccess.set(cap.addOwner(owner.getUUID()));     //将owner添加为player的主人
        });

        return isSuccess.get();
    }

    public static boolean removeNeko(ServerPlayer player, UUID neko){

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
