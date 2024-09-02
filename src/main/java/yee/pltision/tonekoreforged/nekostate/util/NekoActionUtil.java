package yee.pltision.tonekoreforged.nekostate.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yee.pltision.tonekoreforged.nekostate.common.NekoRecord;

public class NekoActionUtil {

    /**
     * 用于检测是否为撅猫棍。
     * stick{ToNeko:{catStick:true},display:{Name:'{"type":"translatable","translate":"to_neko.cat_stick","fallback":"Cat Stick","color":"blue"}'},Enchantments:[{}]}
     */
    public static boolean isCatStick(ItemStack test){
        if(test.getItem()!= Items.STICK)return false;
        CompoundTag tag= test.getTagElement("ToNeko");
        return tag!=null&&tag.getBoolean("catStick");
    }

    public static void growExpAndParticle(Level level, Vec3 pos, NekoRecord record, float grow){
        float old= record.getExp();
        int add=((int)(old+ record.growExp(grow)))-(int) old;
        if(add!=0&&level instanceof ServerLevel serverLevel){
            for(ServerPlayer player:serverLevel.players()){
                player.serverLevel().sendParticles(player,ParticleTypes.HEART,false,pos.x,pos.y,pos.z,add,0.2,0.1,0.2,0);
            }
        }
    }


}
