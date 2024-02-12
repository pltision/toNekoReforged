package yee.pltision.tonekoreforged.api;

import java.util.UUID;

/**
 * 记录一个猫猫或主人的UUID的信息。
 * 包含好感度等信息方便快速读取。
 *
 * 如果真的有需要的话，可以联系我将UUID换成任意的句柄喵~
 */
public interface NekoRecord {

    /**
     * @return 所指向对象的uuid
     */
    UUID getUUID();

    /**
     * @return *对*所指向对象的好感度
     */
    int getExp();

    /**
     * 设置对所指向对象的好感度
     */
    void setExp(int exp);

    default void copy(NekoRecord other){
        other.setExp(getExp());
    }

}
