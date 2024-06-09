package yee.pltision.tonekoreforged.neko.capability;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.config.Lang;
import yee.pltision.tonekoreforged.neko.command.CommandTester;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.neko.util.NekoActionUtil;
import yee.pltision.tonekoreforged.neko.util.NekoConnectUtil;
import yee.pltision.tonekoreforged.neko.util.NekoStateApi;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
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
        PetPhrase petPhrase= NekoStateApi.getPetPhrase(event.getPlayer());
        if(petPhrase!=null){
            event.setMessage(Component.literal(petPhrase.addPhrase(event.getMessage().getString())));
        }
    }
    @SubscribeEvent
    public static void hit(AttackEntityEvent event){
        if(event.getTarget()instanceof Player otherPlayer){
            catStick(event.getEntity().level(), event.getEntity(),otherPlayer,event.getEntity().getMainHandItem());
        }
    }

    @SubscribeEvent
    public static void interact(PlayerInteractEvent.EntityInteract event){
        if(event.getTarget()instanceof ServerPlayer otherPlayer){

        }
    }

    public static final float LN21=(float) Math.log(20+1);
    public static void catStick(final Level level, final Player player, final Player otherPlayer, ItemStack item){
        if(isEnchantmentFish(player.getOffhandItem())){
            player.getOffhandItem().shrink(1);
            if(Config.usingRite==Config.DEFAULT_RITE &&checkDayTime(level.dayTime())){
                for (Entity entity : level.getEntities(player, new AABB(player.getX() - 4, player.getY() - 4, player.getZ() - 4, player.getX() + 4, player.getY() + 4, player.getZ() + 4), entity -> entity instanceof Cat)) {
                    if (

                            level.getBlockState(entity.blockPosition()).getBlock() instanceof EnchantmentTableBlock
                                    && entity instanceof Cat cat
                                    && cat.isOwnedBy(player)
                    ) {
                        if (player instanceof ServerPlayer && otherPlayer instanceof ServerPlayer) {
                            CommandSyntaxException e;
                            try {
                                e = CommandTester.canAddNeko((ServerPlayer) player, (ServerPlayer) otherPlayer);
                            } catch (CommandSyntaxException ex) {
                                e = ex;
                            }
                            if (e == null) {
                                cat.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 20, 2));
                                if (NekoConnectUtil.getNeko((ServerPlayer) player, (ServerPlayer) otherPlayer)) {
                                    player.sendSystemMessage(Component.empty().append(otherPlayer.getName()).append(Lang.GET_NEKO_INFO.component()));
                                    otherPlayer.sendSystemMessage(Component.empty().append(player.getName()).append(Lang.GET_OWNER_INFO.component()));
                                }
                            } else {
                                player.sendSystemMessage(Component.literal(e.getMessage()).setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                            }
                        }
                        break;
                    }
                }
            }
        }
        else {
            player.getCapability(NekoCapability.NEKO_STATE).ifPresent(state -> {
                NekoRecord nekoRecord = state.getNeko(player.getUUID());
                if (nekoRecord != null && NekoActionUtil.isCatStick(item)) {
                    NekoActionUtil.growExpAndParticle(level, otherPlayer.getEyePosition(), nekoRecord, 0.1f / ((float) Math.log(Math.max(2, nekoRecord.getExp() * 2 + 1)) / LN21));
                    catStickEffects(otherPlayer);
                }
            });
        }
    }
    public static boolean checkDayTime(long time){
        return (time%24000)>=17840&&(time%24000)<=18160;
    }

    public static boolean isEnchantmentFish(ItemStack item){
        return item.is(Items.COOKED_COD)&&!item.getEnchantmentTags().isEmpty();
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
