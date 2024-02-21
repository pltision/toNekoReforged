package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import yee.pltision.tonekoreforged.config.Lang;

public class FailToGetCapabilityException implements CommandExceptionType {
    public static final FailToGetCapabilityException FAIL_TO_GET_CAPABILITY =new FailToGetCapabilityException();

    public CommandSyntaxException create(ServerPlayer player){
        return new CommandSyntaxException(FAIL_TO_GET_CAPABILITY, Lang.FAIL_TO_GET_CAPABILITY.component().append(player.toString()));
    }
}
