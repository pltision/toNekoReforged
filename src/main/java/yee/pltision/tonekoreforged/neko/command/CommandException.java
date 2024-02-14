package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import yee.pltision.tonekoreforged.config.ConfigLang;
import yee.pltision.tonekoreforged.config.Lang;

public class CommandException {
    public static SimpleCommandExceptionType
            PLAYER_NOT_NEKO,
            GET_NEKO_ALREADY,GET_OWNER_ALREADY,
            REMOVE_NEKO_NOT_FOUND,REMOVE_OWNER_NOT_FOUND,
            GET_EXP_NOT_FOUND, SET_EXP_NOT_CONNECTED;


    public static void intiExceptions(){
        PLAYER_NOT_NEKO=create(Lang.PLAYER_NOT_NEKO);

        GET_NEKO_ALREADY=create(Lang.GET_NEKO_ALREADY);
        GET_OWNER_ALREADY=create(Lang.GET_OWNER_ALREADY);
        REMOVE_NEKO_NOT_FOUND=create(Lang.REMOVE_NEKO_NOT_FOUND);
        REMOVE_OWNER_NOT_FOUND=create(Lang.REMOVE_OWNER_NOT_FOUND);
        GET_EXP_NOT_FOUND=create(Lang.GET_EXP_NOT_FOUND);
        SET_EXP_NOT_CONNECTED =create(Lang.SET_EXP_NOT_CONNECTED);
    }
    public static SimpleCommandExceptionType create(ConfigLang lang){
        return new SimpleCommandExceptionType(lang.component());
    }
}
