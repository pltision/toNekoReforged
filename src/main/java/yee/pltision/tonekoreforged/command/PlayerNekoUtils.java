package yee.pltision.tonekoreforged.command;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import yee.pltision.tonekoreforged.interfaces.NekoState;
import yee.pltision.tonekoreforged.capability.NekoCapabilityProvider;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerNekoUtils {
    /**
     * 此方法可以读取已离线的玩家数据（暂未实现）
     */
    public static  void modifyPlayerState(MinecraftServer server, UUID uuid, NonNullConsumer<NekoState> consumer){
        Player player =server.getPlayerList().getPlayer(uuid);
        if(player!=null){
            LazyOptional<NekoState> state= player.getCapability(NekoCapabilityProvider.NEKO_STATE);
            if(state.isPresent())
                state.ifPresent(consumer);
            else throw new RuntimeException("玩家"+player+"没有NekoState能力");
            //理应不会不会没有能力喵
        }
        //TODO: 读取存档中的状态，若存档也没有抛出一个异常
    }

    public static void connect(Player player ,OperatorState operatorState,Player other){
        LazyOptional<NekoState> stateOptional= other.getCapability(NekoCapabilityProvider.NEKO_STATE);
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


    public enum OperatorState{
        NEKO,OWNER
    }
}
