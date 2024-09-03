package yee.pltision.tonekoreforged.nekostate.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.nekostate.common.NekoRecord;
import yee.pltision.tonekoreforged.nekostate.common.NekoState;
import yee.pltision.tonekoreforged.nekostate.object.NekoStateObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber
public class NekoCapability implements ICapabilityProvider {
    public static final Capability<NekoState> NEKO_STATE = CapabilityManager.get(new CapabilityToken<>(){});
    public final LazyOptional<NekoState> optional;

    public NekoCapability(Player player){
        optional=LazyOptional.of(()->getOrCreateNekoState(player.getUUID()));
    }

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject()instanceof Player player){
            event.addCapability(ToNeko.location("neko_state"), new NekoCapability(player));
        }
    }

    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(NekoState.class);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == NEKO_STATE ? optional.cast():LazyOptional.empty();
    }

    public static Map<UUID, NekoState> nekoStatePool=new HashMap<>();

    public static NekoState getOrCreateNekoState(UUID uuid){
        return nekoStatePool.computeIfAbsent(uuid, k -> new NekoStateObject());
    }

    public static NekoState getNekoState(UUID uuid){
        return nekoStatePool.get(uuid);
    }

    public static final String NEKO_STATE_SUFFIX="_neko_state.dat";
    public static String TO_NEKO_PATH ="to_neko";

    public static File getNekoPath(MinecraftServer server){
        return new File(server.getWorldPath(LevelResource.PLAYER_DATA_DIR).toFile(), TO_NEKO_PATH);
    }

    @SubscribeEvent
    public static void serverStart(ServerAboutToStartEvent event){
        nekoStatePool=new HashMap<>();
        if(Config.dontSave) return;
        File toNekoPath= getNekoPath(event.getServer());
        if(toNekoPath.isDirectory()){
            for(File file: Objects.requireNonNull(toNekoPath.listFiles())){
                if(file.isFile()&&file.getName().endsWith(NEKO_STATE_SUFFIX)){
                    UUID uuid;
                    try{
                        uuid=UUID.fromString(file.getName().substring(0,file.getName().length()-NEKO_STATE_SUFFIX.length()));
                    }catch (IllegalArgumentException exception){
                        ToNeko.LOGGER.warn("[ToNeko] {} is end of \"{}\" but not start with an UUID!",file,NEKO_STATE_SUFFIX);
                        continue;
                    }
                    try{
                        nekoStatePool.put(uuid, SerializeUtil.nekoState(new NekoStateObject(),Objects.requireNonNull(NbtIo.read(file)), NekoCapability::getOrCreateNekoState));
                    }
                    catch (Exception exception){
                        ToNeko.LOGGER.error("[ToNeko] Exception when reading neko state in {}! {}",file,exception);
                    }
                }
                else ToNeko.LOGGER.info("[ToNeko] {} is not end of \"{}\", toNeko will not try to decode it like a neko state.",file,NEKO_STATE_SUFFIX);
            }
            //构建双向图
            for(Map.Entry<UUID,NekoState> entry:nekoStatePool.entrySet()){
                Map<UUID, NekoRecord> nekos=entry.getValue().getNekos();
                for(UUID ownerUUID:nekos.keySet()){
                    nekoStatePool.computeIfAbsent(ownerUUID, k -> new NekoStateObject()).addOwner(entry.getKey(),entry.getValue());
                }
            }
        }
        else{
            if(toNekoPath.exists())
                ToNeko.LOGGER.error("[ToNeko] {} is not a directory, maybe neko states cannot be save!",toNekoPath);
            else {
                ToNeko.LOGGER.info("[ToNeko] {} is not exists, maybe it just didn't been created.", toNekoPath);
            }
        }
    }

    @SubscribeEvent
    public static void serverStop(ServerStoppingEvent event){
        if(Config.dontSave) return;
//        ToNeko.LOGGER.info("[ToNeko] Server stop, try save neko states: {}",nekoStatePool);
        saveNekoStates(event.getServer());
        nekoStatePool=null;
    }

    @SubscribeEvent
    public static void saveOverworld(LevelEvent.Save event){
        try {
//            System.out.println("喵");
            if(Config.saveNekoStatesWhenSaveOverworld){
                MinecraftServer server=event.getLevel().getServer();
                if(server!=null&& !event.getLevel().isClientSide()&&event.getLevel()==server.overworld())
                    saveNekoStates(server);
            }
        }
        catch (Exception e){
            ToNeko.LOGGER.error("[ToNeko] Exception when save neko states when save overworld: "+e);
        }
    }

    public static void saveNekoStates(MinecraftServer server){
        File toNekoPath= getNekoPath(server);
        if(!toNekoPath.exists())
            if(!toNekoPath.mkdirs()) ToNeko.LOGGER.warn("[ToNeko] {} mkdirs() return false.",toNekoPath);
        for(Map.Entry<UUID,NekoState> entry: nekoStatePool.entrySet()){
            File file=new File(toNekoPath,entry.getKey().toString()+NEKO_STATE_SUFFIX);
//            ToNeko.LOGGER.info("[ToNeko] Saving: {}",file);
            try{
                NbtIo.write(SerializeUtil.nekoState(entry.getValue()),file);
            }
            catch (Exception exception){
                ToNeko.LOGGER.error("[ToNeko] Exception when writing neko state in {}! {}",file,exception);
            }
        }
    }

    /*@SubscribeEvent
    public static void save(PlayerEvent.SaveToFile event){
        if(Config.doSave){
            event.getEntity().getCapability(NEKO_STATE).ifPresent(cap-> {
                try {
                    NbtIo.write(SerializeUtil.nekoState(cap),event.getPlayerFile("to_neko.dat"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    @SubscribeEvent
    public static void load(PlayerEvent.LoadFromFile event){
        event.getEntity().getCapability(NEKO_STATE).addListener();
        if(Config.doSave){
            event.getEntity().getCapability(NEKO_STATE).ifPresent(cap-> {
                try {
                    SerializeUtil.nekoState(cap, Objects.requireNonNull(NbtIo.read(event.getPlayerFile("to_neko.dat"))));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NullPointerException e){
                    ToNeko.LOGGER.info("Try get neko state with UUID {} return null. Maybe data is not been created.", event.getPlayerFile("to_neko.dat"));
                }
            });
        }
    }

    @SubscribeEvent
    public static void clone(PlayerEvent.Clone event){
        event.getOriginal().getCapability(NEKO_STATE).ifPresent(old->
                        event.getEntity().getCapability(NEKO_STATE).ifPresent(cap-> SerializeUtil.nekoState(cap, SerializeUtil.nekoState(old))
                )
        );

    }*/
}
