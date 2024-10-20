package yee.pltision.tonekoreforged.collar.lead;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import yee.pltision.tonekoreforged.ToNekoCapabilityHelper;

public interface LeadItemHandel {
    default boolean canUseOn(LivingEntity entity){
        return ToNekoCapabilityHelper.getCollarState(entity)==null;
    }

    default boolean canDrop(LivingEntity entity, LivingEntity leader){
        return true;
    }

    default boolean doDrop(LivingEntity entity, LivingEntity leader) {
        return length(entity.position(), leader.position()) <= 10;
    }


   //mc的Vec3什么玩意，连个clone都没有
    static double length(Vec3 a,Vec3 b) {
        return Math.sqrt((a.x-b.x) * (a.x-b.x) + (a.y-b.y) * (a.y-b.y) + (a.z-b.z) * (a.z-b.z));
    }

}
