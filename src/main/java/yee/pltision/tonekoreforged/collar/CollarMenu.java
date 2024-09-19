package yee.pltision.tonekoreforged.collar;

import yee.pltision.tonekoreforged.collar.bauble.BaublesAccessor;

public interface CollarMenu {
    /**
     *  给客户端使用，服务端请直接使用CollarState喵
     */
    BaublesAccessor createBaubleAccessor();
}
