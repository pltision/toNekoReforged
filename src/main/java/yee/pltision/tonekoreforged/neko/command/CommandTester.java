package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.object.NekoRecordObject;
import yee.pltision.tonekoreforged.neko.util.NekoCommonUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static yee.pltision.tonekoreforged.neko.capability.NekoCapability.NEKO_STATE;
import static yee.pltision.tonekoreforged.neko.command.FailToGetCapabilityException.FAIL_TO_GET_CAPABILITY;

public class CommandTester {

    /**
     * 如果有指令权限可以强制添加的则用return，否则用throw
     */
    public static CommandSyntaxException canAddNeko(ServerPlayer test, ServerPlayer neko) throws CommandSyntaxException{
        Set<UUID> nekoOwners=NekoCommonUtils.getOwners(neko);
        if(!Config.playerCanHaveMultipleOwners&&nekoOwners!=null&& !nekoOwners.isEmpty())   //另一个玩家是否已有主人
            return CommandExceptions.GET_NEKO_OTHER_ALREADY.create();

        if(!Config.nekoCanBeItsOwner&&test==neko) //不能成为自己的猫娘
            return CommandExceptions.CANNOT_BE_OWN_NEKO_OR_OWNER.create();

        if (NekoCommonUtils.isNeko(test)) {
            if(!Config.nekoCanHaveNeko&&test!=neko) //猫娘不能有自己以外的猫娘
                return CommandExceptions.GET_NEKO_NEKO_CANNOT_HAVE_NEKO.create();
        }

        if(!Config.playerCanHaveMultipleNekos&& !NekoCommonUtils.getNekos(test).isEmpty()) //不能有多个猫娘
            return CommandExceptions.GET_NEKO_CANNOT_MULTIPLE.create();

        if(NekoCommonUtils.containsNeko(test,neko.getUUID())) //检查是否已经是
            throw CommandExceptions.GET_NEKO_ALREADY.create();

        return null;
    }

    public static CommandSyntaxException canAddOwner(ServerPlayer test, ServerPlayer owner) throws CommandSyntaxException{
        if(!Config.playerCanHaveMultipleNekos&& !NekoCommonUtils.getNekos(owner).isEmpty())   //另一个玩家是否已有猫娘
            return CommandExceptions.GET_OWNER_OTHER_ALREADY.create();

        if(!Config.nekoCanBeItsOwner&&test==owner) //不能成为自己的猫娘
            return CommandExceptions.CANNOT_BE_OWN_NEKO_OR_OWNER.create();

        Set<UUID> testOwners=NekoCommonUtils.getOwners(test);
        if(testOwners!=null) {
            if (!Config.playerCanHaveMultipleOwners && !testOwners.isEmpty()) //不能有多个主人
                return CommandExceptions.GET_OWNER_CANNOT_MULTIPLE.create();

            if (testOwners.contains(owner.getUUID())) //检查是否已经是owner的猫猫
                throw CommandExceptions.GET_OWNER_ALREADY.create();
        }

        return null;
    }


}
