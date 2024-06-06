package yee.pltision.tonekoreforged.neko.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.capability.NekoCapability;
import yee.pltision.tonekoreforged.neko.command.CommandExceptions;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.object.NekoRecordObject;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NekoConnectUtil {
    /**
     * @return 如果成功添加了。（失败的话说明neko已经是player的猫猫了
     */
    public static boolean getNeko(ServerPlayer player, ServerPlayer neko){
        return StateApi.connect(neko,player);
    }

    public static boolean getOwner(ServerPlayer player, ServerPlayer owner) {
        return StateApi.connect(player,owner);
    }


}
