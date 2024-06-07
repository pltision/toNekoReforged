package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerPlayer;
import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.util.NekoStateApi;

import java.util.Set;
import java.util.UUID;

public class CommandTester {

    /**
     * 如果有指令权限可以强制添加的则用return，否则用throw
     */
    public static CommandSyntaxException canAddNeko(ServerPlayer test, ServerPlayer neko) throws CommandSyntaxException{
        Set<UUID> nekoOwners= NekoStateApi.getOwners(neko);

        if(NekoStateApi.containsNeko(test,neko.getUUID())) {
            throw CommandExceptions.GET_NEKO_ALREADY.create();
        }

        if(!Config.playerCanHaveMultipleOwners&&nekoOwners!=null&& !nekoOwners.isEmpty())   //另一个玩家是否已有主人
            return CommandExceptions.GET_NEKO_OTHER_ALREADY.create();

        if(!Config.nekoCanBeItsOwner&&test==neko) //不能成为自己的猫娘
            return CommandExceptions.CANNOT_BE_OWN_NEKO_OR_OWNER.create();

        if (NekoStateApi.isNeko(test)) {
            if(!Config.nekoCanHaveNeko&&test!=neko) //猫娘不能有自己以外的猫娘
                return CommandExceptions.GET_NEKO_NEKO_CANNOT_HAVE_NEKO.create();
        }

        if(!Config.playerCanHaveMultipleNekos&& !NekoStateApi.getNekos(test).isEmpty()) //不能有多个猫娘
            return CommandExceptions.GET_NEKO_CANNOT_MULTIPLE.create();

        System.out.println(NekoStateApi.containsNeko(test,neko.getUUID()));

        return null;
    }

    public static CommandSyntaxException canAddOwner(ServerPlayer test, ServerPlayer owner) throws CommandSyntaxException{
        Set<UUID> testOwners= NekoStateApi.getOwners(test);
        if(testOwners!=null) {
            if (testOwners.contains(owner.getUUID())) //检查是否已经是owner的猫猫
                throw CommandExceptions.GET_OWNER_ALREADY.create();

            if (!Config.playerCanHaveMultipleOwners && !testOwners.isEmpty()) //不能有多个主人
                return CommandExceptions.GET_OWNER_CANNOT_MULTIPLE.create();
        }

        if(!Config.playerCanHaveMultipleNekos&& !NekoStateApi.getNekos(owner).isEmpty())   //另一个玩家是否已有猫娘
            return CommandExceptions.GET_OWNER_OTHER_ALREADY.create();

        if(!Config.nekoCanBeItsOwner&&test==owner) //不能成为自己的猫娘
            return CommandExceptions.CANNOT_BE_OWN_NEKO_OR_OWNER.create();

        return null;
    }

    public static CommandSyntaxException canModifyPetPhrase(ServerPlayer test,ServerPlayer beModify){
        if(test.equals(beModify))
        {
            if((!Config.everyoneCanModifyTheirPetPhrase)&&!Config.nekoCanModifyTheirPetPhrase) return CommandExceptions.SET_PET_PHRASE_EVERYONE_CANNOT.create();

            if( (NekoStateApi.isNeko(beModify) && !Config.nekoCanModifyTheirPetPhrase) &&
                    !( Config.ownerCanModifyTheirNekoPetPhrase && NekoStateApi.containsNeko(test,beModify.getUUID()) ) )
                return CommandExceptions.SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO.create();

            if(!Config.everyoneCanModifyTheirPetPhrase)
                return CommandExceptions.SET_PET_PHRASE_ONLY_NEKO.create();
        }
        else{
            if(Config.ownerCanModifyTheirNekoPetPhrase){
                if(NekoStateApi.containsOwner(test,beModify.getUUID()))
                    return CommandExceptions.SET_PET_PHRASE_NOT_OWNER.create();
            }
            else return CommandExceptions.SET_PET_PHRASE_CANNOT_MODIFY_OTHER.create();
        }
        return null;
    }

    public static <T extends Throwable> void throwIfNotNull(T t) throws T{
        if(t!=null) throw t;
    }


}
