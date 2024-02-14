package yee.pltision.tonekoreforged.object;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.interfaces.NekoRecord;

import java.util.UUID;

public class NekoRecordObject implements NekoRecord {
    public final @NotNull UUID uuid;
    public int exp;
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
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
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
    public int growExp(int exp) {
        if(factor<=0){
            throw new IllegalArgumentException("factor<0");
        }
        int muled=(int)(exp>0?exp*factor:exp/factor);
        this.exp+=muled;
        return muled;
    }

    @Override
    public void tick(Player player) {}

    public CompoundTag serializeNBT() {
        CompoundTag tag= new CompoundTag();
        tag.putUUID("uuid",uuid);
        tag.putInt("exp",exp);
        return tag;
    }


}
