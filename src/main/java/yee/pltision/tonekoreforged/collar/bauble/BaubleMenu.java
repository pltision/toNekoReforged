package yee.pltision.tonekoreforged.collar.bauble;

import org.jetbrains.annotations.Nullable;
import yee.pltision.tonekoreforged.collar.CollarState;

public interface BaubleMenu {
    /**
     *  @return 饰品所在的项圈，仅服务端可用。
     */
    @Nullable
    CollarState getCollarState() throws NullPointerException;
}
