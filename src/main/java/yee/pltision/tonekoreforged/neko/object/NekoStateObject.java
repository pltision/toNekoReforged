package yee.pltision.tonekoreforged.neko.object;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;

import java.util.*;
import java.util.function.BiFunction;

public class NekoStateObject implements NekoState {
    public static int DEFAULT_EXP=10;
    public Map<UUID,NekoRecord> nekos;
    public BiMap<UUID,NekoState> owners;

    public PetPhrase phrase=null;

    public NekoStateObject(){
        nekos =new HashMap<>();
        owners = null;
    }

    @Override
    public Map<UUID,NekoState> getOwners() {
        return owners;
    }

    @Override
    public boolean addOwner(UUID owner,NekoState state){
        beNeko();
        return owners.put(owner,state)==null;
    }
    @Override
    public boolean addNeko(UUID neko,NekoState state){
        return nekos.put(neko, new NekoRecordObject(neko,state,DEFAULT_EXP))==null;
    }

    @Override
    public void computeNekoState(UUID uuid, BiFunction<? super UUID, ? super NekoRecord, ? extends NekoRecord> function) {
        nekos.compute(uuid,function);
    }

    @Override
    public @Nullable NekoState getOwner(UUID uuid) {
        return owners ==null?null: owners.get(uuid);
    }
    @Override
    public @Nullable NekoRecord getNeko(UUID uuid) {
        return nekos.get(uuid);
    }

    @Override
    public boolean removeOwner(UUID owner) {
        if(owners ==null)return false;
        NekoState removed= owners.remove(owner);
        return removed != null;
    }

    @Override
    public boolean removeOwnerAndState(UUID owner) {
        if(owners ==null)return false;
        var removed= owners.remove(owner);
        if(removed==null){
            return false;
        }
        else {
            if(owners.isEmpty()) beNonneko();
            return true;
        }
    }

    @Override
    public @NotNull Map<UUID,NekoRecord> getNekos() {
        return nekos;
    }



    @Override
    public boolean checkNeko(UUID uuid) {
        return nekos.containsKey(uuid);
    }

    @Override
    public boolean removeNeko(UUID neko) {
        return nekos.remove(neko)!=null;
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

    public void beNeko(){
        if(Config.addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase&&phrase==null||phrase.phrase.isEmpty()) phrase=defaultPhrase();
        if(owners ==null) owners =HashBiMap.create();
    }

    public void beNonneko(){
        owners =null;

    }

    public static PetPhrase defaultPhrase(){
        return new PetPhrase(Config.defaultPetPhrase,Config.defaultPetPhraseIgnoreEnglishText,Config.defaultPetPhraseIgnoreAfter);
    }

    // NOT deep copy
    public void copy(NekoStateObject clone){
        owners =clone.owners;
        nekos =clone.nekos;
        phrase=clone.phrase;

    }

    @Override
    public String toString() {
        return super.toString()+"{" +
                "nekos=" + nekos +
                ", owners=" + (owners==null?null:owners.keySet()) +
                ", phrase=" + phrase +
                '}';
    }
}
