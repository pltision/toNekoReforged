package yee.pltision.tonekoreforged.neko.action;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.neko.util.NekoActionUtil;

@Mod.EventBusSubscriber
public class Events {

    @SubscribeEvent
    public static void modifyRecipe(PlayerEvent.ItemCraftedEvent event){
        if(NekoActionUtil.isCatStick(event.getCrafting())){
            ItemStack item=event.getCrafting();
            CompoundTag tag= item.getTag();
            if (tag != null) {
                CompoundTag display=tag.getCompound("display");
                display.putString("Name", Lang.CAT_STICK.config);
                item.setTag(tag);
            }
        }
    }

    @SubscribeEvent
    public static void modifyChatMessage(ServerChatEvent event){
//        event.setCanceled(true);
/*        event.getPlayer().getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            event.setMessage(cap.prefix().append(event.getMessage()));
        })*/
        event.getPlayer().getCapability(NekoCapability.NEKO_STATE).ifPresent(cap->{
            PetPhrase petPhrase=cap.getPetPhrase();
            if(petPhrase!=null){
                event.setMessage(Component.literal(petPhrase.addPhrase(event.getMessage().getString())));
            }
        });
    }
    @SubscribeEvent
    public static void hit(AttackEntityEvent event){
        if(event.getTarget()instanceof Player otherPlayer){
            catStick(event.getEntity().level(), event.getEntity(),otherPlayer,event.getEntity().getMainHandItem());
        }
//        System.out.println(event.getTarget()+" <> "+event.getEntity().getMainHandItem());
    }

    @SubscribeEvent
    public static void interact(PlayerInteractEvent.EntityInteract event){
        if(event.getTarget()instanceof ServerPlayer otherPlayer){
//            catStick(event.getLevel(),event.getEntity(),otherPlayer,event.getItemStack());

        }

    }

    public static final float LN21=(float) Math.log(20+1);
    public static void catStick(final Level level, final Player player, final Player otherPlayer, ItemStack item){
        otherPlayer.getCapability(NekoCapability.NEKO_STATE).ifPresent(other->{
            NekoRecord otherRecord=other.getOwner(player.getUUID());
            if(otherRecord!=null&& NekoActionUtil.isCatStick(item)){
                NekoActionUtil.growExpAndParticle(level,otherPlayer.getEyePosition(),otherRecord, 0.1f/( (float) Math.log(Math.max(2,otherRecord.getExp()*2+1))/LN21 ));
                catStickEffects(otherPlayer);
            }
        });
    }

        /**
         * 给被撅的玩家添加效果。
         * <ul>
         *      <li>缓慢IV</li>
         *      <li>挖掘疲劳III</li>
         *      <li>虚弱III</li>
         *      每个效果均为6秒，外加12秒的缓慢II
         * </ul>
         */
        public static void catStickEffects(final Player player){
//        player.addEffect(new MobEffectInstance(MobEffects.JUMP,5*20,128,true,true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,12*20,2,true,true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,6*20,4,true,true));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,6*20,3,true,true));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS,6*20,3,true,true));

    }
}
