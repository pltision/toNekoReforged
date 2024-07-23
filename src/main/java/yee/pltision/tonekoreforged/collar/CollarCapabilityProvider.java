package yee.pltision.tonekoreforged.collar;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.client.collar.CollarBaubleRenderer;
import yee.pltision.tonekoreforged.client.collar.CollarRenderHelper;
import yee.pltision.tonekoreforged.client.collar.CollarRenderer;
import yee.pltision.tonekoreforged.collar.bauble.CollarBaubleState;
import yee.pltision.tonekoreforged.client.collar.BellRenderer;

import java.util.List;

@Mod.EventBusSubscriber
public class CollarCapabilityProvider implements ICapabilityProvider {
    public static final Capability<CollarCapability> COLLAR_RECORD = CapabilityManager.get(new CapabilityToken<>(){});
    public final LazyOptional<CollarCapability> optional;

    public CollarCapabilityProvider(Player player){
        optional= LazyOptional.of(()->new CollarCapability() {
            final CollarState state=new CollarState() {
                final List<CollarBaubleState> baubles=List.of(new CollarBaubleState() {
                    @Override
                    public ItemStack asItem() {
                        return null;
                    }

                    @Override
                    public <T> CollarBaubleRenderer<T,?> getRenderer(T entity, CollarState collar) {
                        return (CollarBaubleRenderer<T,?>) BellRenderer.INSTANT;
                    }
                });
                @Override
                public List<CollarBaubleState> baubles() {
                    return baubles;
                }

                @Override
                public ItemStack asItem() {
                    return null;
                }

                @Override
                public <E> CollarRenderHelper<E, ?> getCollarRenderHelper() {
                    return (CollarRenderHelper<E, ?>) CollarRenderer.INSTANT;
                }

                @Override
                public Component getDisplayName() {
                    return null;
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
                    return null;
                }
            };
            @Override
            public CollarState getState() {
                return state;            }

            @Override
            public ItemStack getCollarSlot() {
                return null;
            }

            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void setCollarSlot(ItemStack item) {

            }
        });
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == COLLAR_RECORD ? optional.cast():LazyOptional.empty();
    }
    public static CollarCapability FALLBACK_CAPABILITY=new CollarCapability() {
        @Override
        public CollarState getState() {
            return null;
        }

        @Override
        public ItemStack getCollarSlot() {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void setCollarSlot(ItemStack item) {
        }
    };

    @SubscribeEvent
    public static void registryCapability(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject()instanceof Player player){
            event.addCapability(new ResourceLocation(ToNeko.MODID,"collar_state"), new CollarCapabilityProvider(player));
        }
    }

}
