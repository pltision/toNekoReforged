package yee.pltision.tonekoreforged.neko.util;


import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StateApi {

    public static @Nullable Set<UUID> getOwners(Player player){
        NekoState state = player.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(player));
        return state.getOwners()==null?null:state.getOwners().keySet();
    }

    public static Set<UUID> getNekos(Player player){
        NekoState state = player.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(player));
        return state.getNekos();
    }

    public static @Nullable Set<Player> getPlayerOwners(Level level, Player player){
        Set<UUID> uuids= getOwners(player);
        if(uuids==null) return null;
        Set<Player>owners=new HashSet<>();
        for(UUID uuid:uuids){
            Player owner=level.getPlayerByUUID(uuid);
            if(owner!=null) owners.add(player);
        }
        return owners;
    }
    public static Set<Player> getPlayerNekos(Level level, Player player){
        Set<UUID> uuids= getNekos(player);
        Set<Player>nekos=new HashSet<>();
        for(UUID uuid:uuids){
            Player owner=level.getPlayerByUUID(uuid);
            if(owner!=null) nekos.add(player);
        }
        return nekos;
    }

    public static boolean containsOwner(Player neko,UUID other){
        Set<UUID> owners=getOwners(neko);
        return owners != null && owners.contains(other);
    }

    public static boolean containsNeko(Player owner,UUID other){
        System.out.println(getNekos(owner));
        return getNekos(owner).contains(other);
    }

    public static boolean isNeko(Player player){
        return getOwners(player)!=null;
    }

    public static boolean connect(Player neko,Player owner){
        NekoState nekoState = neko.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(neko));
        NekoState ownerState = owner.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(owner));
        return nekoState.addOwner(owner.getUUID()) | ownerState.addNeko(neko.getUUID());
    }


    public static boolean removeNeko(Player owner, UUID neko, boolean removeState){
        NekoState nekoState = NekoCapability.getOrCreateNekoState(neko);
        NekoState ownerState = owner.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(owner));
        return ( removeState?nekoState.removeOwnerAndState(neko):nekoState.removeOwner(neko) ) | ownerState.removeNeko(owner.getUUID());
    }

    public static boolean removeOwner(Player neko, UUID owner, boolean removeState){
        NekoState nekoState = neko.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(neko));
        NekoState ownerState = NekoCapability.getOrCreateNekoState(owner);
        return ( removeState?nekoState.removeOwnerAndState(neko.getUUID()):nekoState.removeOwner(neko.getUUID()) ) | ownerState.removeNeko(owner);
    }

    public static PetPhrase getPetPhrase(Player player){
        NekoState state = player.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(player));
        return state.getPetPhrase();
    }

    public static void setPetPhrase(Player player,PetPhrase petPhrase){
        NekoState state = player.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(player));
        state.setPetPhrase(petPhrase);
    }

    /**
     * @return 如果找到记录则返回经验，否则返回nan
     */
    public static float getExp(Player a,UUID b) {
        NekoState state = a.getCapability(NekoCapability.NEKO_STATE).orElseThrow(() -> new CannotGetCapabilityException(a));
        NekoRecord nekoRecord = state.getOwner(b);
        if (nekoRecord == null){
            if (state.getNekos().contains(b)) {
                state = NekoCapability.nekoStatePool.get(b);
                nekoRecord = state.getOwner(a.getUUID());
                if(nekoRecord!=null) return nekoRecord.getExp();
            }
        }
        else return nekoRecord.getExp();
        return Float.NaN;
    }

    /**
     * @return 如果失败返回false
     */
    public static boolean setExp(Player a,UUID b,float exp){
        NekoState state = a.getCapability(NekoCapability.NEKO_STATE).orElseThrow(() -> new CannotGetCapabilityException(a));
        NekoRecord nekoRecord = state.getOwner(b);
        if (nekoRecord == null){
            if (state.getNekos().contains(b)) {
                NekoState bState = NekoCapability.getOrCreateNekoState(b);
                if (bState != null) {
                    nekoRecord = state.getOwner(a.getUUID());
                    if(nekoRecord!=null){
                        nekoRecord.setExp(exp);
                        return false;
                    }
                }
            }
        }
        else{
            nekoRecord.setExp(exp);
            return false;
        }
        return true;

    }


    static class CannotGetCapabilityException extends RuntimeException{
        Player player;
        CannotGetCapabilityException(Player player){
            super("Cannot get neko state capability in player: "+player);
            this.player=player;
        }
    }
}
