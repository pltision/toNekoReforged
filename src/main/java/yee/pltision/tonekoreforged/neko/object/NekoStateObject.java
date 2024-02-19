package yee.pltision.tonekoreforged.neko.object;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;

import java.util.*;

public class NekoStateObject implements NekoState , INBTSerializable<CompoundTag> {
    public static int DEFAULT_EXP=10;
    public Set<UUID> nekoSet;
    public @Nullable BiMap<UUID,NekoRecordObject> ownerMap;

    public PetPhrase phrase=null;

    public NekoStateObject(){
        nekoSet =new HashSet<>();
        ownerMap= null;
    }

    @Override
    public @Nullable Set<NekoRecordObject> getOwners() {
        return ownerMap==null?null:ownerMap.values();
    }

    @Override
    public boolean addOwner(UUID owner){
        beNeko();

        if(ownerMap==null) ownerMap=HashBiMap.create();
        if(ownerMap.containsKey(owner)) return false;
        ownerMap.put(owner,new NekoRecordObject(owner,DEFAULT_EXP));
        return true;
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
    public boolean removeOwner(UUID owner) {
        if(ownerMap==null)return false;
        NekoRecord removed=ownerMap.remove(owner);
        return removed != null;
    }

    @Override
    public boolean removeOwnerAndState(UUID owner) {
        if(ownerMap==null)return false;
        NekoRecordObject removed=ownerMap.remove(owner);
        if(removed==null){
            return false;
        }
        else {
            if(ownerMap.isEmpty()) beNotNeko();
            return true;
        }
    }

    @Override
    public @NotNull Set<UUID> getNekos() {
        return Collections.unmodifiableSet(nekoSet);
    }

    @Override
    public boolean addNeko(UUID neko){
        return nekoSet.add(neko);
    }


    @Override
    public boolean checkNeko(UUID uuid) {
        return nekoSet.contains(uuid);
    }

    @Override
    public boolean removeNeko(UUID neko) {
        return nekoSet.remove(neko);
    }

    @Override
    public void tick(Player player) {}

    @Override
    public @Nullable PetPhrase getPetPhrase() {
        return phrase;
    }

    @Override
    public void setPetPhrase(@Nullable PetPhrase petPhrase){
        this.phrase=petPhrase;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    public void beNeko(){
        if(Config.addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase) phrase=defaultPhrase();
    }

    public void beNotNeko(){
        ownerMap=null;

    }

    public static PetPhrase defaultPhrase(){
        return new PetPhrase(Config.defaultPetPhrase,Config.defaultPetPhraseIgnoreEnglishText,Config.petPhraseIgnoreAfter);
    }

}
