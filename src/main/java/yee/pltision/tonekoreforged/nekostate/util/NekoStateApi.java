package yee.pltision.tonekoreforged.nekostate.util;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.event.NekoStateEvent;
import yee.pltision.tonekoreforged.nekostate.capability.NekoCapability;
import yee.pltision.tonekoreforged.nekostate.common.NekoRecord;
import yee.pltision.tonekoreforged.nekostate.common.NekoState;
import yee.pltision.tonekoreforged.nekostate.common.PetPhrase;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NekoStateApi {

    public static @Nullable Set<UUID> getOwners(Player player){
        NekoState state = player.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(player));
        return state.getOwners()==null?null:state.getOwners().keySet();
    }

    public static Set<UUID> getNekos(Player player){
        NekoState state = player.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(player));
        return state.getNekos().keySet();
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
        return getNekos(owner).contains(other);
    }

    public static boolean isNeko(Player player){
        return getOwners(player)!=null;
    }

    public static boolean connect(Player neko,Player owner){
        if(MinecraftForge.EVENT_BUS.post(new NekoStateEvent.TryConnectPlayersEvent(neko,owner))) return false;

        NekoState nekoState = neko.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(neko));
        NekoState ownerState = owner.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(owner));

        if(nekoState.addOwner(owner.getUUID(),ownerState) | ownerState.addNeko(neko.getUUID(),nekoState)){
            MinecraftForge.EVENT_BUS.post(new NekoStateEvent.ConnectedPlayersEvent(owner,neko));
            return true;
        }
        return false;
    }


    public static boolean removeNeko(Player owner, UUID neko, boolean removeState){
        var event=new NekoStateEvent.TryRemoveNekoEvent(owner,neko,removeState);
        if(MinecraftForge.EVENT_BUS.post(event)) return false;
        removeState=event.doRemoveNekoStateWhenPlayerHaveNoOwner();

        NekoState nekoState = NekoCapability.getOrCreateNekoState(neko);
        NekoState ownerState = owner.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(owner));

        if(nekoState.removeOwner(owner.getUUID(),removeState) | ownerState.removeNeko(neko)){
            MinecraftForge.EVENT_BUS.post(new NekoStateEvent.RemovedNekoEvent(owner,neko,nekoState.isNeko()));
            return true;
        }
        return false;
    }

    public static boolean removeOwner(Player neko, UUID owner, boolean removeState){
        var event=new NekoStateEvent.TryRemoveOwnerEvent(neko,owner,removeState);
        if(MinecraftForge.EVENT_BUS.post(event)) return false;
        removeState=event.doRemoveNekoStateWhenPlayerHaveNoOwner();

        NekoState nekoState = neko.getCapability(NekoCapability.NEKO_STATE).orElseThrow(()->new CannotGetCapabilityException(neko));
        NekoState ownerState = NekoCapability.getOrCreateNekoState(owner);

        /*try {
            System.out.println("阿米诺斯");
            System.out.println("neko: "+neko.getUUID()+" "+nekoState);
            System.out.println("owner: "+owner+" "+ownerState);
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }*/

        if(nekoState.removeOwner(owner,removeState) | ownerState.removeNeko(neko.getUUID())){
            MinecraftForge.EVENT_BUS.post(new NekoStateEvent.RemovedNekoEvent(neko,owner,nekoState.isNeko()));
            return true;
        }
        return false;
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
        NekoRecord nekoRecord = state.getNeko(b);
        if (nekoRecord == null){
            state=state.getOwner(b);
            if (state!=null) {
                state = NekoCapability.nekoStatePool.get(b);
                nekoRecord = state.getNeko(a.getUUID());
                if(nekoRecord!=null) return nekoRecord.getExp();
            }
        }
        else return nekoRecord.getExp();
        return Float.NaN;
    }

    /**
     * @return 如果失败返回true
     */
    public static boolean setExp(UUID a,UUID b,float exp){
        NekoState state = NekoCapability.getNekoState(a);
        if(state!=null){
            NekoRecord nekoRecord = state.getNeko(b);
            if (nekoRecord == null){
                state=state.getOwner(b);
                if (state!=null) {
                    nekoRecord = state.getNeko(a);
                    if(nekoRecord!=null) {
                        nekoRecord.setExp(exp);
                        return false;
                    }
                }
            }
            else{
                nekoRecord.setExp(exp);
                return false;
            }
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
