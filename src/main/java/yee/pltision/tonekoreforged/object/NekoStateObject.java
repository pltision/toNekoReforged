package yee.pltision.tonekoreforged.object;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.api.NekoState;
import yee.pltision.tonekoreforged.capability.NekoCapabilityProvider;

import java.util.*;

public class NekoStateObject implements NekoState , INBTSerializable<CompoundTag> {
    public static int DEFAULT_EXP=10;

    public BiMap<UUID, NekoRecordObject> nekoMap;
    public @Nullable BiMap<UUID,NekoRecordObject> ownerMap;

    //如果有需要的话，Player可以泛化成Entity。
    public Player player;

    public NekoStateObject(){
        nekoMap=null;
        ownerMap= HashBiMap.create();
    }

    @Override
    public @Nullable Set<NekoRecordObject> getOwners() {
        return ownerMap==null?null:ownerMap.values();
    }

    @Override
    public void addOwner(UUID owner) {
        if(ownerMap==null) ownerMap=HashBiMap.create();
        ownerMap.putIfAbsent(owner,new NekoRecordObject(owner,DEFAULT_EXP));
    }

    /*@Override
    public void setOwners(@Nullable Collection<UUID> set) {
        if(set==null) {
            ownerMap=null;
            return;
        }

        Map<UUID,NekoRecordObject> old=ownerMap==null?Map.of():ownerMap;
        ownerMap=HashBiMap.create();

        for(UUID uuid:set){
            NekoRecordObject object=old.get(uuid);
            object=object==null?new NekoRecordObject(uuid,DEFAULT_EXP):object;
            ownerMap.put(uuid,object);
        }
    }*/

    @Override
    public @Nullable NekoRecordObject getOwner(UUID uuid) {
        return ownerMap==null?null:ownerMap.get(uuid);
    }

    @Override
    public NekoRecordObject removeOwner(UUID owner) {
        return ownerMap==null?null:ownerMap.remove(owner);
    }

    @Override
    public NekoRecordObject removeOwnerAndSet(UUID owner) {
        if(ownerMap==null)return null;
        NekoRecordObject removed=ownerMap.remove(owner);
        if(removed==null)return null;
        else {
            if(ownerMap.size()==0) ownerMap=null;
            return removed;
        }
    }

    @Override
    public @NotNull Set<NekoRecordObject> getNekos() {
        return nekoMap.values();
    }

    @Override
    public void addNeko(UUID neko) {
        nekoMap.putIfAbsent(neko,new NekoRecordObject(neko,DEFAULT_EXP));
    }


    @Override
    public @Nullable NekoRecordObject getNeko(UUID uuid) {
        return nekoMap.get(uuid);
    }

    @Override
    public NekoRecordObject removeNeko(UUID neko) {
        return nekoMap.remove(neko);
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    public void modifyPlayerState(MinecraftServer server, UUID uuid, NonNullConsumer<NekoState> consumer){
        Player player =server.getPlayerList().getPlayer(uuid);
        if(player!=null){
            LazyOptional<NekoState> state= player.getCapability(NekoCapabilityProvider.NEKO_STATE);
            if(state.isPresent())
                state.ifPresent(consumer);
            else{
                //TODO: 抛出异常
            }
        }
        //TODO: 读取存档中的
    }

    public void connect(OperatorState operatorState,UUID other){
        MinecraftServer server=player.getServer();
        if(server==null) return;
        modifyPlayerState(server,other,state->{
            switch (operatorState){
                case NEKO -> state.addOwner(other);
                case OWNER -> state.addNeko(other);
            }
        });
    }

    public void remove(OperatorState operatorState,UUID other){
        MinecraftServer server=player.getServer();
        if(server==null) return;
        modifyPlayerState(server,other,state->{
            switch (operatorState){
                case NEKO -> state.removeOwner(other);
                case OWNER -> state.removeNeko(other);
            }
        });
    }

    public enum OperatorState{
        NEKO,OWNER;
    }
}
