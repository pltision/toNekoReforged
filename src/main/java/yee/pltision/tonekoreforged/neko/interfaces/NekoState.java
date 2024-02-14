package yee.pltision.tonekoreforged.neko.interfaces;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public interface NekoState {
    /**
     * @return 猫猫的所有主人。当返回null时说明这不是一个猫猫。
     */
    @Nullable Set<?extends NekoRecord> getOwners();

    /**
     * 向猫猫添加主人。如果目标不是猫猫（即getOwners为null）则可能会新增一个集合将其设为猫猫。
     * @param owner 要添加的主人。
     * @return 如果成功添加（即原来不存在该uuid的主人）
     */
    boolean addOwner(UUID owner);

    /**
     * @param uuid 尝试获取的主人的uuid。
     * @return 若拥有一个此uuid的主人返回它的记录，否则返回Null。
     */
    @Nullable NekoRecord getOwner(UUID uuid);

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
     * @return 对象的所有猫猫。
     */
    @NotNull Set<UUID> getNekos();

    /**
     * 向对象添加猫猫。
     * @param neko 要添加的猫猫。
     * @return 如果成功添加（即原来不存在该uuid的猫猫）
     */
    boolean addNeko(UUID neko);

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
}
