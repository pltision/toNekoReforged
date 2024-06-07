package yee.pltision.tonekoreforged.neko.object;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.object.additions.NekoAdditions;

import java.util.UUID;

public class NekoRecordObject extends NekoAdditions implements NekoRecord {
    public final UUID uuid;
    public final NekoState state;
    public float exp;
    public float factor=1;

    public NekoRecordObject(UUID uuid, NekoState state, int exp) {
        this.uuid = uuid;
        this.state = state;
        this.exp=exp;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public NekoState getState() {
        return state;
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

}
