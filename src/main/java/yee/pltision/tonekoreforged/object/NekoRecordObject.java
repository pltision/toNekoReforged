package yee.pltision.tonekoreforged.object;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.api.NekoRecord;

import java.util.UUID;

public class NekoRecordObject implements NekoRecord {
    public final @NotNull UUID uuid;
    public int exp;

    public NekoRecordObject(@NotNull UUID uuid) {
        this.uuid = uuid;
    }
    public NekoRecordObject(@NotNull UUID uuid,int exp) {
        this.uuid = uuid;
        this.exp=exp;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public int getExp() {
        return exp;
    }

    @Override
    public void setExp(int exp) {
        this.exp=exp;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag= new CompoundTag();
        tag.putUUID("uuid",uuid);
        tag.putInt("exp",exp);
        return tag;
    }


}
