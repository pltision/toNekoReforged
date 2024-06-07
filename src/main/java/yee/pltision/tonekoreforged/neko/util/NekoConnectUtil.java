package yee.pltision.tonekoreforged.neko.util;

import net.minecraft.server.level.ServerPlayer;

public class NekoConnectUtil {
    /**
     * @return 如果成功添加了。（失败的话说明neko已经是player的猫猫了
     */
    public static boolean getNeko(ServerPlayer player, ServerPlayer neko){
        return NekoStateApi.connect(neko,player);
    }

    public static boolean getOwner(ServerPlayer player, ServerPlayer owner) {
        return NekoStateApi.connect(player,owner);
    }


}
