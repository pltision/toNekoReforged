package yee.pltision.tonekoreforged.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;
import yee.pltision.tonekoreforged.collar.bauble.TeleporterState;

import java.util.EnumSet;
import java.util.List;

public class EnderLeadItem extends Item {
    public static final String TAG_NAME="EnderLead";
    public static final String BINDING_UUID ="binding";
    public static final String BINDING_NAME ="bindingName";

    public EnderLeadItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41432_, @NotNull Player player, @NotNull InteractionHand hand) {
        if(player instanceof ServerPlayer serverPlayer)
            checkEntityAndSetTag(player.getItemInHand(hand), serverPlayer, hand);

//        player.startUsingItem(hand);

        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public void checkEntityAndSetTag(ItemStack item,ServerPlayer player,InteractionHand hand){
        CompoundTag tag= item.getTagElement(TAG_NAME);
        if (tag != null) {
            Player neko = player.server.getPlayerList().getPlayer(tag.getUUID(BINDING_UUID));
            if (neko != null&&neko.isAlive()) {
                //TODO: 检测是否为主人啥的
                tag.putString(BINDING_NAME,Component.Serializer.toJson(neko.getName()));
                TeleporterState enderBolt=ToNekoCapabilityHelper.getBaubleOnEntity(neko,TeleporterState.class, TeleporterState::canTeleport);
                if(enderBolt!=null){
                    player.startUsingItem(hand);
                }
                else message(player, componentWithEntity(this.getDescriptionId() + ".invalid",neko.getName()));
            }
            else message(player, Component.translatable(this.getDescriptionId() + ".entityNotFound").withStyle(EXCEPTION_STYLE));
        }
        else message(player, Component.translatable(this.getDescriptionId() + ".noBinding").withStyle(TITLE_STYLE));
    }

    static MutableComponent componentWithEntity(String translatable, Component name){
        return Component.translatable(translatable,Component.empty().append(name).withStyle(NAME_STYLE)).withStyle(TITLE_STYLE);
    }
    static MutableComponent componentWithEntityException(String translatable, Component name){
        return Component.translatable(translatable,Component.empty().append(name).withStyle(NAME_STYLE)).withStyle(EXCEPTION_STYLE);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack item, @NotNull  Player player, @NotNull  LivingEntity entity, @NotNull  InteractionHand hand) {
        //TODO 检测是否为主人
        TeleporterState enderBolt=ToNekoCapabilityHelper.getBaubleOnEntity(entity,TeleporterState.class);
        CompoundTag tag= item.getTagElement(TAG_NAME);
        if(tag==null||!tag.hasUUID(BINDING_UUID)) {
            if (enderBolt != null) {
                tag=item.getOrCreateTagElement(TAG_NAME);
                tag.putUUID(BINDING_UUID, entity.getUUID());
                tag.putString(BINDING_NAME, Component.Serializer.toJson(entity.getName()));
                message(player, componentWithEntity(this.getDescriptionId() + ".successBinding", entity.getName()));
                if (player.isCreative()) {
                    player.setItemInHand(hand, item);
                }
                return InteractionResult.CONSUME_PARTIAL;
            } else
                message(player, componentWithEntityException(this.getDescriptionId() + ".noTeleporter", entity.getName()));
        }
        return super.interactLivingEntity(item, player, entity, hand);
    }
    //data get entity @s Inventory[{id:"toneko:ender_lead"}]

    //https://www.desmos.com/calculator/btz0oqckqt
    //c=to x=tick t=time
    //c * ( (x/t)^2 )
    public static double flat(int to,double tick,double time){
        double c=tick/time;
        return (to*((c*c*c*c))+to*(c))/2;
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack item, int tick) {
        CompoundTag tag= item.getTagElement(TAG_NAME);
        if (tag != null) {
            Component name=Component.Serializer.fromJson(tag.getString(BINDING_NAME));
            MutableComponent component=componentWithEntity(this.getDescriptionId() + ".teleporting",name==null?Component.literal(tag.getUUID(BINDING_UUID).toString()):name);
            component.append(Component.literal("[").withStyle(ChatFormatting.GRAY));
            final int count=20;
            int flat= (int) flat(count,getUseDuration(item)-tick,getUseDuration(item));
            for(int i=0;i<count;i++){
                component.append(Component.literal("|").withStyle(Style.EMPTY.withColor(i<=flat?ChatFormatting.LIGHT_PURPLE:ChatFormatting.DARK_GRAY)/*.withBold(true)*/));
            }
            component.append(Component.literal("]").withStyle(ChatFormatting.GRAY));

            message(entity, component);
        }

        super.onUseTick(level, entity, item, tick);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack p_41454_) {
        return 20;
    }

    static final Style TITLE_STYLE=Style.EMPTY.withColor(ChatFormatting.BLUE);
    static final Style NAME_STYLE=Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);
    static final Style EXCEPTION_STYLE=Style.EMPTY.withColor(ChatFormatting.RED);

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack item, @NotNull Level level, @NotNull LivingEntity player) {
        if(player instanceof ServerPlayer serverPlayer) {
            CompoundTag tag= item.getTagElement(TAG_NAME);
            if (tag != null) {
                Player neko = serverPlayer.server.getPlayerList().getPlayer(tag.getUUID(BINDING_UUID));
                if (neko != null&&neko.isAlive()) {
                    //TODO: 检测是否为主人啥的
                    TeleporterState enderBolt=ToNekoCapabilityHelper.getBaubleOnEntity(neko,TeleporterState.class, TeleporterState::canTeleport);
                    if(enderBolt!=null){
                        message(player, componentWithEntity(this.getDescriptionId() + ".teleported",neko.getName()));
                        enderBolt.consume();
                        neko.teleportTo(serverPlayer.serverLevel(), player.getX(), player.getY(), player.getZ(), EnumSet.noneOf(RelativeMovement.class), player.getYRot(),player.getXRot());
                        serverPlayer.getCooldowns().addCooldown(this, 20);
                    }
                    else{
                        message(player, componentWithEntity(this.getDescriptionId() + ".invalid",neko.getName()));
                    }
                }
            }
        }
        return super.finishUsingItem(item, level, player);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if(count!=0)message(entity, Component.translatable(this.getDescriptionId() + ".canceled").withStyle(TITLE_STYLE));
        super.onStopUsing(stack, entity, count);
    }

    static void message(LivingEntity p_40957_, Component p_40958_) {
        if(p_40957_ instanceof ServerPlayer)
            ((ServerPlayer)p_40957_).sendSystemMessage(p_40958_, true);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack item, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(item, level, components, tooltipFlag);

        CompoundTag tag= item.getTagElement(TAG_NAME);
        if(tag!=null&&tag.hasUUID(BINDING_UUID)) {
            MutableComponent component=Component.translatable(this.getDescriptionId() + ".bindingLore");
            if(tag.getTagType(BINDING_NAME)== Tag.TAG_STRING){
                Component name;
                name=Component.Serializer.fromJson(tag.getString(BINDING_NAME));
                if(name!=null)
                    component.append(Component.empty().append(name).withStyle(NAME_STYLE)).append(Component.literal(" ").withStyle(ChatFormatting.GRAY));
            }
            component.append(Component.literal(tag.getUUID(BINDING_UUID).toString()).withStyle(ChatFormatting.GRAY));
            components.add(component.withStyle(TITLE_STYLE));
        }
        else components.add(Component.translatable(this.getDescriptionId() + ".noBindingLore").withStyle(ChatFormatting.GRAY));
    }
}
