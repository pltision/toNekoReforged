package yee.pltision.tonekoreforged.neko.util;


import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.command.FailToGetCapabilityException;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.object.NekoStateObject;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class NekoCommonUtils {
    public static Logger LOGGER= LogUtils.getLogger();

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
        return getNekos(owner).contains(other);
    }

    public static boolean isNeko(Player player){
        return getOwners(player)!=null;
    }

    static class CannotGetCapabilityException extends RuntimeException{
        Player player;
        CannotGetCapabilityException(Player player){
            super("Cannot get neko state capability in player: "+player);
            this.player=player;
        }
    }
}
