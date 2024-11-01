package yee.pltision.tonekoreforged.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.collar.CollarCapabilityProvider;
import yee.pltision.tonekoreforged.collar.CollarState;
import yee.pltision.tonekoreforged.collar.bauble.BaublesAccessor;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleHandel;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;
import yee.pltision.tonekoreforged.collar.bauble.NameFormatCollarBaubleState;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.nekostate.util.NekoStateApi;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
public class NameTageCapabilityAdditionProvider implements ICapabilityProvider {

    final ItemStack item;

    LazyOptional<CollarBaubleHandel> optional=LazyOptional.of(()->new CollarBaubleHandel() {
        final NameFormatCollarBaubleState state=new NameFormatCollarBaubleState() {
            @Override
            public ItemStack asItem() {
                return item;
            }

            @Override
            public void initEntity(@Nullable LivingEntity entity, CollarState state, int slot) {
                if(Config.enableNameTagModify)
                    NameFormatCollarBaubleState.super.initEntity(entity, state, slot);
            }

            @Override
            public void unEquip(LivingEntity entity, CollarState state, int slot) {
                if(Config.enableNameTagModify)
                    NameFormatCollarBaubleState.super.unEquip(entity, state, slot);
            }

            @Override
            public Component format(LivingEntity entity, Component entityName, Component displayName) {
                if(!Config.enableNameTagModify)
                    return displayName;

                if(item.hasCustomHoverName()){
                    return Component.empty()
                            .append(Component.empty().append(item.getHoverName()).withStyle(Style.EMPTY.withItalic(true)))
                            .append(displayName);
                }
                if(Config.enableNameTagCostumePrefix&&entity instanceof ServerPlayer serverPlayer){
                    withNoOwner:
                    {
                        Set<UUID> owners = NekoStateApi.getOwners(serverPlayer);
                        if(owners==null) break withNoOwner;

                        int maxShowNames= Config.nameTagShowOwnerLimit;
                        ArrayList<Component> ownerNames=new ArrayList<>(maxShowNames);
                        for(UUID ownerUuid:owners)
                        {
                            if(ownerNames.size()==maxShowNames)
                                break withNoOwner;

                            {
                                ServerPlayer owner=serverPlayer.server.getPlayerList().getPlayer(ownerUuid);
                                if(owner!=null&&owner!=serverPlayer) {
                                    ownerNames.add(owner.getName());
                                    continue;
                                }
                            }

                            {
                                GameProfileCache cache= serverPlayer.server.getProfileCache();
                                if(cache!=null){
                                    Optional<GameProfile> owner=cache.get(ownerUuid);
                                    owner.ifPresent(gameProfile -> ownerNames.add(Component.literal(gameProfile.getName())));
                                }
                            }
                        }
                        if(ownerNames.isEmpty())
                            break withNoOwner;
                        Object[] nameArray=ownerNames.toArray(new Object[ownerNames.size()+1]);
                        nameArray[ownerNames.size()]=displayName;
                        return Component.translatable("nameFormat.toneko.nameFormatNekoWith"+ownerNames.size()+"Owners",nameArray);
                    }
                }
                return Component.translatable("nameFormat.toneko.nameFormatNeko",displayName);
            }

            @Override
            public boolean mayEquip(BaublesAccessor baublesAccessor, Object slotAccessor, int slot) {
                return Config.enableNameTagModify && NameFormatCollarBaubleState.super.mayEquip(baublesAccessor, slotAccessor, slot);
            }
        };
        @Override
        public CollarBaubleState getBaubleState() {
            return state;
        }

    });

    public NameTageCapabilityAdditionProvider(ItemStack item) {
        this.item = item;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap== CollarCapabilityProvider.COLLAR_BAUBLE_HANDEL_ITEM?optional.cast():LazyOptional.empty();
    }

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<ItemStack> event){
        if(event.getObject().is(Items.NAME_TAG)) {
            event.addCapability(ToNeko.location("collar_bauble_name_tag"), new NameTageCapabilityAdditionProvider(event.getObject()));
        }
    }

}
