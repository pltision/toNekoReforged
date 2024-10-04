package yee.pltision.tonekoreforged.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;
import yee.pltision.tonekoreforged.collar.bauble.TeleporterState;

import java.util.EnumSet;

public class EnderLead extends Item {
    public static final String TAG_NAME="EnderLead";
    public static final String BINDING_UUID_NAME="binding";

    public EnderLead(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, Player player, @NotNull InteractionHand hand) {
        if(player.isShiftKeyDown()){
            InteractionResultHolder<ItemStack> superReturn= super.use(p_41432_, player, hand);
            if(superReturn.getResult()!=InteractionResult.PASS) {
                return superReturn;
            }
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack item, @NotNull  Player player, @NotNull  LivingEntity entity, @NotNull  InteractionHand hand) {
        CollarState state=ToNeko.getCollar(entity).getState();
        if(state!=null) //TODO 检测是否为主人
            for(CollarBaubleState bauble:state.baubles()){
                if(bauble instanceof TeleporterState){
                    CompoundTag tag= item.getOrCreateTagElement(TAG_NAME);
                    tag.putUUID(BINDING_UUID_NAME,entity.getUUID());
                    return InteractionResult.SUCCESS;
                }
            }
        return super.interactLivingEntity(item, player, entity, hand);
    }
    //data get entity @s Inventory[{id:"toneko:ender_lead"}]
    //TODO 标题栏进度条

    @Override
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return 20;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack item, @NotNull Level level, @NotNull LivingEntity player) {
        if(player instanceof ServerPlayer serverPlayer) {
            CompoundTag tag= item.getTagElement(TAG_NAME);
            if (tag != null) {
                Player neko = serverPlayer.server.getPlayerList().getPlayer(tag.getUUID(BINDING_UUID_NAME));
                if (neko != null) {
                    neko.teleportTo(serverPlayer.serverLevel(), player.getX(), player.getY(), player.getZ(), EnumSet.noneOf(RelativeMovement.class), player.getYRot(),player.getXRot());
                }
                //TODO: 检测传送器是否存在是否有足够能量是否为主人啥的
            }
        }
        return super.finishUsingItem(item, level, player);
    }
}
