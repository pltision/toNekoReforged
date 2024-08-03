package yee.pltision.tonekoreforged.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DyingTranslateRecipe extends CustomRecipe {
    final ResourceLocation location;
    final Item clean;
    final Item dyed;


    public DyingTranslateRecipe(ResourceLocation location, Item clean, Item dyed) {
        super(location,CraftingBookCategory.EQUIPMENT);
        this.location = location;
        this.clean = clean;
        this.dyed = dyed;
    }


    /*@Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }*/

    static FluidStack BUCKET_OF_WATER=new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME);
    @Override
    public boolean matches(CraftingContainer container, Level p_44003_) {
        boolean hasClean=false;
        boolean hasDyed=false;
        boolean hasDye=false;
        boolean hasWater=false;
        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(clean)) {
                    if(hasClean) return false;
                    hasClean=true;
                    continue;
                }
                if (itemstack.is(dyed)) {
                    if(hasDyed) return false;
                    hasDyed=true;
                    continue;
                }
                LazyOptional<IFluidHandlerItem> cap=itemstack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
                if(cap.isPresent()){
                    if(cap.orElseThrow(()->new RuntimeException("LazyOptional is persent but still throw"))
                            .drain(BUCKET_OF_WATER, IFluidHandler.FluidAction.SIMULATE).getAmount()>=FluidType.BUCKET_VOLUME){
                        if(hasWater) return false;
                        hasWater=true;
                        continue;
                    }
                }
                if (itemstack.getItem()instanceof DyeItem) {
//                    if(hasDye) return false;
                    hasDye=true;
                    continue;
                }
                return false;
            }
        }
        return (hasDye&&hasClean)^(hasWater&&hasDyed);
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess access) {
        boolean type=false;
        ItemStack item=ItemStack.EMPTY;
        ItemStack water=ItemStack.EMPTY;
        List<DyeItem> dyes=new ArrayList<>(8);

        for(int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack itemstack = container.getItem(j);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(clean)) {
                    type=false;
                    item=itemstack;
                }
                if (itemstack.is(dyed)) {
                    item=itemstack;
                    type=true;
                }
                LazyOptional<IFluidHandler> cap=itemstack.getCapability(ForgeCapabilities.FLUID_HANDLER);
                if(cap.isPresent()){
                    if(cap.orElseThrow(()->new RuntimeException("LazyOptional is persent but still throw"))
                            .drain(BUCKET_OF_WATER, IFluidHandler.FluidAction.SIMULATE).getAmount()>=FluidType.BUCKET_VOLUME){
                        type=true;
                        water=itemstack;
                    }
                }
                if (itemstack.getItem()instanceof DyeItem dye) {
                    type=false;
                    dyes.add(dye);
                }
            }
        }
        ItemStack result;
        CompoundTag tag=item.getTag();
        if(type){
            result = new ItemStack(clean);
            if (tag != null) {
                tag=tag.copy();
                tag.getCompound(DyeableArmorItem.TAG_DISPLAY).remove(DyeableArmorItem.TAG_COLOR);
                result.setTag(item.getTag());
            }
//            item.shrink(1);
            /*LazyOptional<IFluidHandler> cap=water.getCapability(ForgeCapabilities.FLUID_HANDLER);
            if(cap.isPresent()){
                cap.orElseThrow(()->new RuntimeException("LazyOptional is persent but still throw"))
                        .drain(BUCKET_OF_WATER, IFluidHandler.FluidAction.EXECUTE).getAmount();
            }*/
        }
        else{
            result = new ItemStack(dyed);
//            item.shrink(1);
            result= DyeableLeatherItem.dyeArmor(result, dyes);
            if (tag != null) {
                result.setTag (tag.copy());
            }
        }
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return p_43999_*p_43999_>=2;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return location;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    public static class Serializer implements RecipeSerializer<DyingTranslateRecipe> {
        public static final Serializer INSTANCE=new Serializer();
        public @NotNull DyingTranslateRecipe fromJson(@NotNull ResourceLocation p_44290_, @NotNull JsonObject p_44291_) {

            String clean = GsonHelper.getAsString(p_44291_, "clean");
            String dyed = GsonHelper.getAsString(p_44291_, "dyed");

            return new DyingTranslateRecipe(p_44290_,
                    BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(clean)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + clean + "'")),
                    BuiltInRegistries.ITEM.getOptional(ResourceLocation.tryParse(dyed)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + dyed + "'")));
        }


        public DyingTranslateRecipe fromNetwork(@NotNull ResourceLocation p_44293_, FriendlyByteBuf p_44294_) {
            return new DyingTranslateRecipe(p_44293_,p_44294_.readById(BuiltInRegistries.ITEM),p_44294_.readById(BuiltInRegistries.ITEM));
        }

        public void toNetwork(FriendlyByteBuf p_44281_, DyingTranslateRecipe p_44282_) {
            p_44281_.writeId(BuiltInRegistries.ITEM,p_44282_.clean);
            p_44281_.writeId(BuiltInRegistries.ITEM,p_44282_.dyed);
        }
    }
}
