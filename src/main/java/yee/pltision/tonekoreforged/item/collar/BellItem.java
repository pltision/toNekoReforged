package yee.pltision.tonekoreforged.item.collar;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.BellRenderer;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.AbstractCollarBaubleState;
import yee.pltision.tonekoreforged.collar.bauble.BaublesAccessor;
import yee.pltision.tonekoreforged.collar.bauble.BorderBaubleSlotAccessor;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;

public class BellItem extends Item implements CollarBaubleItem{
    public BellItem(Properties p_41383_) {
        super(p_41383_);
    }

    public static final int DING_TICK_WALK=20,DING_TICK_RUN =7,DING_TICK_JUMP=6,DING_RANDOM_WALK=5,DING_RANDOM_RUN=3;

    public CollarBaubleState asCollarBaubleState(ItemStack item) {
        return new AbstractCollarBaubleState(item) {
            int soundNextTick;
            int slot;

            @OnlyIn(Dist.CLIENT)
            @Override
            public <T> CollarBaubleRenderer<T, ?> getRenderer(T entity, CollarState collar) {
                return BellRenderer.INSTANT.cast();
            }

            @Override
            public void entityTick(LivingEntity entity) {
                if(entity.level().isClientSide&&entity.walkAnimation.speed()>0.5f) {
                    if( Math.abs(entity.tickCount-soundNextTick) > 40 ){
                        soundNextTick=entity.getRandom().nextInt(40);
                    }
                    if(entity.tickCount>=soundNextTick&&!entity.isUnderWater()){
                        int addTime;
                        if(
                                entity.walkAnimation.speed()>0.9f
                                &&((!entity.onGround())||(entity instanceof Player player&&player.isSprinting()))
                        ){
                            if(entity.onGround())
                                addTime= DING_TICK_RUN+entity.getRandom().nextInt(DING_RANDOM_RUN);
                            else
                                addTime=DING_TICK_JUMP;
                        }
                        else{
                            addTime= DING_TICK_WALK+entity.getRandom().nextInt(DING_RANDOM_WALK);
                        }
                        if(slot!=0)
                            addTime*=2+entity.getRandom().nextInt(1);

                        soundNextTick=entity.tickCount+addTime;
                        entity.playSound(ToNeko.BELL_SOUND.get(), entity.walkAnimation.speed()*entity.walkAnimation.speed()*entity.walkAnimation.speed()*(entity==Minecraft.getInstance().player?1/3f:1f )*(0.875f+entity.getRandom().nextFloat()*0.125f), 1.0F);
                    }
                }
            }

            @Override
            public void initEntity(@Nullable LivingEntity entity, CollarState state, int slot) {
                this.slot=slot;
            }

            @Override
            public boolean mayEquip(BaublesAccessor baublesAccessor, Object slotAccessor, int slot) {
                return slotAccessor instanceof BorderBaubleSlotAccessor accessor?
                        accessor.fontSideSlots().contains(slot)||accessor.backSideSlots().contains(slot)
                        : super.mayEquip(baublesAccessor, slotAccessor,slot);
            }
        };
    }
    /*@Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player p_41433_, @NotNull InteractionHand p_41434_) {
        return CollarBaubleItem.super.use(p_41432_, p_41433_, p_41434_);
    }*/

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return CollarBaubleItem.super.initCapabilities(stack, nbt);
    }
}
