package yee.pltision.tonekoreforged.neko.object;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;

import java.util.UUID;

public class NekoRecordObject implements NekoRecord {
    public final @NotNull UUID uuid;
    public float exp;
    public float factor=1;

    public NekoRecordObject(@NotNull UUID uuid,int exp) {
        this.uuid = uuid;
        this.exp=exp;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public float getExp() {
        return exp;
    }

    @Override
    public void setExp(float exp) {
        this.exp=exp;
    }

    @Override
    public float getFactor() {
        return factor;
    }

    @Override
    public void setFactor(float factor) {
        if(factor<=0){
            throw new IllegalArgumentException("factor<0");
        }
        this.factor=factor;
    }

    @Override
    public void modifyFactor(float factor){
        this.factor+=factor;
    }

    @Override
    public float growExp(float exp) {
        if(factor<=0){
            throw new IllegalArgumentException("factor<0");
        }
        float muled=exp>0 ? exp*factor: exp/factor;
        this.exp+=muled;
        return muled;
    }

    @Override
    public void tick(Player player) {}

    public CompoundTag serializeNBT() {
        CompoundTag tag= new CompoundTag();
        tag.putUUID("uuid",uuid);
        tag.putFloat("exp",exp);
        tag.putFloat("factor",factor);
        return tag;
    }


}
