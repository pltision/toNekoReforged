package yee.pltision.tonekoreforged.nekostate.common;

import io.netty.util.internal.UnstableApi;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * 记录一个猫猫的UUID等信息。
 * 包含好感度等信息方便快速读取。
 * 如果真的有需要的话，可以将UUID换成任意的句柄喵~
 */
@UnstableApi
public interface NekoRecord {

    /**
     * @return 猫猫的uuid
     */
    UUID getUUID();

    NekoState getState();

    /**
     * @return 猫猫的好感度
     */
    float getExp();

    /**
     * 设置猫猫的好感度
     */
    void setExp(float exp);

    /**
     * @return 好感度增长因数
     * 可用范围为0>x>inf，一般为0.5>x>1.5。x>1时更容易增加好感度，更难下降好感度，x<1则相反。
     * 程序可能会自动将因数向1靠拢。
     */
    float getFactor();

    /**
     * 设置好感度增长因数
     * @param factor 0>x>inf
     * @exception IllegalArgumentException 若factor小于0
     */
    void setFactor(float factor) throws IllegalArgumentException;

    /**
     * 让程序增加（或减少）因数
     */
    void modifyFactor(float factor);

    /**
     * 让程序增长（或减少）对猫猫的好感度
     * @return 实际增长的好感度
     */
    float growExp(float exp);

    /**
     * 用于处理因数等动态信息。
     */
    void tick(Player player);

}
