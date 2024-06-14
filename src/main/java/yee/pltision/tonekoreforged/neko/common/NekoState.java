package yee.pltision.tonekoreforged.neko.common;

import io.netty.util.internal.UnstableApi;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.object.NekoRecordObject;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;

/**
 * 底层接口，不要调用！
 */
@ApiStatus.Internal
public interface NekoState {
    /**
     * 将玩家变成猫猫
     */
    void beNeko();

    /**
     * 移除所有主人并移除猫猫状态
     */
    void beNonneko();

    /**
     * 判断玩家是否是猫猫
     */
    default boolean isNeko(){
        return getOwners()==null;
    }

    /**
     * @return 猫猫的所有主人。当返回null时说明这不是一个猫猫。
     */
    @Nullable Map<UUID, NekoState> getOwners();

    /**
     * @return 对象的所有猫猫。
     */
    @NotNull Map<UUID,NekoRecord> getNekos();

    /**
     * 向猫猫添加主人。如果目标不是猫猫（即getOwners为null）则可能会新增一个集合将其设为猫猫。
     * @param owner 要添加的主人。
     * @return 如果成功添加（即原来不存在该uuid的主人）
     */
    @Deprecated
    default boolean addOwner(UUID owner){
        return addOwner(owner, NekoCapability.getOrCreateNekoState(owner));
    }
    boolean addOwner(UUID neko,NekoState state);


    /**
     * 向对象添加猫猫。
     * @param neko 要添加的猫猫。
     * @return 如果成功添加（即原来不存在该uuid的猫猫）
     */
    @Deprecated
    default boolean addNeko(UUID neko){
        return addNeko(neko, NekoCapability.getOrCreateNekoState(neko));
    }

    boolean addNeko(UUID neko,NekoState state);

    void computeNekoState(UUID uuid, BiFunction<? super UUID, ? super NekoRecord, ? extends NekoRecord> function);

    @Nullable NekoState getOwner(UUID uuid);
    @Nullable NekoRecord getNeko(UUID uuid);

    /**
     * 移除一个主人。
     * @param owner 要移除的主人。
     * @return 如果成功移除了。
     */
    boolean removeOwner(UUID owner);

    /**
     * 移除对象的一个主人，若为成功移除且为最后一个主人则移除集合。
     * @param owner 要移除的主人。
     * @return 如果成功移除了。
     */
    boolean removeOwnerAndState(UUID owner);

    /**
     * 移除一个主人。
     * @param owner 要移除的主人。
     * @param removeState 是否要移除集合
     * @return 如果成功移除了。
     */
    default boolean removeOwner(UUID owner,boolean removeState){
        return removeState? removeOwnerAndState(owner):removeOwner(owner);
    }


    /**
     * @param uuid 检查是否有此uuid的猫猫。
     */
    boolean checkNeko(UUID uuid);

    /**
     * 移除对象的一只猫猫。
     * @param neko 要移除的猫猫。
     * @return 如果成功移除了。
     */
    boolean removeNeko(UUID neko);

    /**
     * 用于处理因数等动态信息。
     */
    void tick(Player player);

    /**
     * 获取后缀。
     */
    @Nullable PetPhrase getPetPhrase();

    /**
     * 设置玩家的口癖。
     */
    void setPetPhrase(@Nullable PetPhrase petPhrase);
}
