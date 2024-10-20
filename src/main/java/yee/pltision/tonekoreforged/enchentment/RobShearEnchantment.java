package yee.pltision.tonekoreforged.enchentment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.enchantment.BindingCurseEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;
import yee.pltision.tonekoreforged.collar.CollarSlotHandler;
import yee.pltision.tonekoreforged.config.Config;

public class RobShearEnchantment extends Enchantment {
    public RobShearEnchantment(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot[] p_44678_) {
        super(p_44676_, p_44677_, p_44678_);
    }

    @Override
    public boolean canEnchant(@NotNull ItemStack p_44689_) {
        return Config.dispenserCanUseRobShear&&( p_44689_.getItem() instanceof ShearsItem||p_44689_.getItem() instanceof EnchantedBookItem );
    }

    @Override
    public boolean canApplyAtEnchantingTable(@NotNull ItemStack stack) {
        return canEnchant(stack);
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    public int getMinCost(int p_44616_) {
        return 30;
    }

    public int getMaxCost(int p_44619_) {
        return 50;
    }

    static EquipmentSlot[] ARMOR_SLOTS=new EquipmentSlot[]{EquipmentSlot.FEET,EquipmentSlot.LEGS,EquipmentSlot.CHEST,EquipmentSlot.HEAD};
    public static boolean tryShearEntity(Level level, @Nullable ServerPlayer player, ItemStack shears, LivingEntity target){
        EquipmentSlot last=null;
        boolean binding = false;

        EquipmentSlot targetSlot=ARMOR_SLOTS[level.random.nextInt(4)];

        for(EquipmentSlot slot:ARMOR_SLOTS){
            ItemStack item=target.getItemBySlot(slot);
            if(!item.isEmpty()){
                checkBinding:{
                    for(Enchantment enchantment:item.getAllEnchantments().keySet()){
                        if(enchantment instanceof BindingCurseEnchantment) {
                            if (last == null) {
                                last = slot;
                                binding=true;
                            }
                            break checkBinding;
                        }
                    }
                    binding=false;
                    last=slot;
                }
            }
            if(last==targetSlot&&last!=null) break;
        }

        if(last==null)return false;

        if(!binding){
            if(player==null)
                createItem(level,target,target.getItemBySlot(last));
            else {
                if(!player.getInventory().add(target.getItemBySlot(last)))
//                    createItem(level,player,target.getItemBySlot(last));
                    player.spawnAtLocation(target.getItemBySlot(last));
            }
            target.setItemSlot(last,ItemStack.EMPTY);
            level.playSound(null, target, SoundEvents.SHEEP_SHEAR, player==null? SoundSource.BLOCKS:SoundSource.PLAYERS, 1.0F, 1.0F);
            shears.hurt(5,level.random,player);
        }
        return true;
    }

    public static boolean tryShearEntityCollar(ServerLevel level, @Nullable ServerPlayer player, ItemStack shears, LivingEntity target){
        CollarSlotHandler handler= ToNekoCapabilityHelper.getCollar(target);
        if(handler.canTake(player, target)){
            ItemStack item=handler.getCollarItem();
            if(player==null)
                createItem(level,target,item);
            else {
                if(!player.getInventory().add(item))
                    player.spawnAtLocation(item);
            }
            handler.setCollarSlotAndSend(target,ItemStack.EMPTY);
            level.playSound(null, target, SoundEvents.SHEEP_SHEAR, player==null? SoundSource.BLOCKS:SoundSource.PLAYERS, 1.0F, 1.0F);
            shears.hurt(5,level.random,player);
            return true;
        }
        return false;
    }

    static void createItem(Level level,LivingEntity drop,ItemStack item){
        ItemEntity itementity = drop.spawnAtLocation(item);
        if (itementity != null) {
            itementity.setDeltaMovement(itementity.getDeltaMovement().add(((level.random.nextFloat() - level.random.nextFloat()) * 0.1F), (level.random.nextFloat() * 0.05F), ((level.random.nextFloat() - level.random.nextFloat()) * 0.1F)));
        }
    }
    public static boolean tryShearLivingEntity(ServerLevel p_123583_, BlockPos p_123584_,ItemStack item) {
        for(Player player : p_123583_.getEntitiesOfClass(Player.class, new AABB(p_123584_), EntitySelector.NO_SPECTATORS)) {
            if(tryShearEntity(p_123583_,null, item, player)||tryShearEntityCollar(p_123583_,null,item,player))
                return true;
        }

        return false;
    }


}
