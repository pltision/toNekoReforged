package yee.pltision.tonekoreforged.collar;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.enchentment.RobShearEnchantment;
import yee.pltision.tonekoreforged.nekostate.util.NekoStateApi;

@Mod.EventBusSubscriber
public class CollarStateEventListeners {

    @SubscribeEvent
    public static void livingTick(LivingEvent.LivingTickEvent event){
        ToNeko.getCollar(event.getEntity()).entityTick(event.getEntity());
    }

    @SubscribeEvent
    public static void interact(PlayerInteractEvent.EntityInteract event){
        ItemStack item=event.getItemStack();
        if(event.getTarget()instanceof LivingEntity target){
            /*if(item.is(Items.LEAD)&&event.getTarget()instanceof Mob other){
            CollarState state=ToNeko.getCollarState(other);
            if(state!=null&& !other.isLeashed()){

            }
        }*/
            if(event.getEntity()instanceof ServerPlayer serverPlayer) {
                if (ToNeko.ROB_SHEAR.get().canEnchant(item)&&target instanceof Player) {
                    if (EnchantmentHelper.getEnchantmentLevel(ToNeko.ROB_SHEAR.get(), event.getEntity()) > 0) {
                        //运用了&&和||的短路🤓
                        if(
                                NekoStateApi.containsNeko(serverPlayer,target.getUUID())&&
                                ( serverPlayer.isShiftKeyDown() && RobShearEnchantment.tryShearEntityCollar(serverPlayer.serverLevel(), serverPlayer, item, target) )
                                || RobShearEnchantment.tryShearEntity(event.getLevel(), serverPlayer, item, target)
                        ){
                            event.setCancellationResult(InteractionResult.SUCCESS);
                            event.setCanceled(true);
                        }
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public static void dropItems(LivingDropsEvent event){
        LivingEntity entity=event.getEntity();

        if (entity instanceof Player && entity.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY))
            return;

        CollarSlotHandler handler=ToNeko.getCollar(entity);
        ItemEntity itemEntity= handler.dropWhenDeath(entity);
        if(itemEntity!=null)
            event.getDrops().add(itemEntity);

    }


}
