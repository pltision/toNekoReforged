package yee.pltision.tonekoreforged.neko.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class NekoModifyUtil {
    /**
     * 此方法可以读取已离线的玩家数据（暂未实现）
     */
    public static  void modifyPlayerState(MinecraftServer server, UUID uuid, NonNullConsumer<NekoState> consumer){
        Player player =server.getPlayerList().getPlayer(uuid);
        if(player!=null){
            LazyOptional<NekoState> state= player.getCapability(NekoCapability.NEKO_STATE);
            if(state.isPresent())
                state.ifPresent(consumer);
            else throw new RuntimeException("玩家"+player+"没有NekoState能力");
            //理应不会不会没有能力喵
        }
        //TODO: 读取存档中的状态，若存档也没有抛出一个异常
    }

    public static void connect(Player player ,OperatorState operatorState,Player other){
        LazyOptional<NekoState> stateOptional= other.getCapability(NekoCapability.NEKO_STATE);
        if(stateOptional.isPresent())
            stateOptional.ifPresent(otherState->{
                switch (operatorState){
                    case NEKO -> otherState.addNeko(player.getUUID());
                    case OWNER -> otherState.addOwner(player.getUUID());
                }
            });
        else throw new RuntimeException("玩家"+player+"没有NekoState能力");
    }

    /**
     * @return 是否移除了猫猫的集合
     */
    public static boolean remove(Player player,OperatorState operatorState,UUID other,boolean removeSet){
        MinecraftServer server=player.getServer();
        if(server==null) return false;
        AtomicBoolean removedSet=new AtomicBoolean(false);

        modifyPlayerState(server,other,otherState->{
            switch (operatorState){
                case NEKO -> otherState.removeNeko(player.getUUID());
                case OWNER -> {
                    otherState.removeOwner(player.getUUID(),removeSet);
                    removedSet.set(otherState.getOwners()==null);
                }
            }
        });

        return removedSet.get();
    }

    /**
     * 可以修改离线玩家的数据（未实现）
     * @return 是否成功执行consumer，false则未找到记录
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean modifyStateRecord(Player player, UUID get, NonNullConsumer<NekoRecord> consumer){
        LazyOptional<NekoState> state= player.getCapability(NekoCapability.NEKO_STATE);
        AtomicBoolean success=new AtomicBoolean(false);
        if(state.isPresent())
            state.ifPresent(cap->{
                NekoRecord playerRecord=cap.getOwner(get);
                if(playerRecord==null){
                    if(cap.checkNeko(get)&&player.getServer()!=null){
                        modifyPlayerState(player.getServer(), get, otherState -> {
                            NekoRecord otherRecord = otherState.getOwner(player.getUUID());
                            if (otherRecord != null) {
                                consumer.accept(otherRecord);
                                success.set(true);
                            }
                        });
                    }
                    //否则失败
                }
                else {
                    consumer.accept(playerRecord);
                    success.set(true);
                }
            });
        return success.get();
    }

    public enum OperatorState{
        NEKO,OWNER
    }
}
