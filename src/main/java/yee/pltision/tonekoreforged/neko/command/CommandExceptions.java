package yee.pltision.tonekoreforged.neko.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import yee.pltision.tonekoreforged.config.ConfigLang;
import yee.pltision.tonekoreforged.config.Lang;

public class CommandExceptions {
    public static SimpleCommandExceptionType
            PLAYER_NOT_NEKO,
            GET_NEKO_ALREADY,GET_OWNER_ALREADY,
            REMOVE_NEKO_NOT_FOUND,REMOVE_OWNER_NOT_FOUND,
            GET_EXP_NOT_FOUND, SET_EXP_NOT_CONNECTED,
            SET_PET_PHRASE_ONLY_NEKO,SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO,SET_PET_PHRASE_EVERYONE_CANNOT,SET_PET_PHRASE_NOT_OWNER,SET_PET_PHRASE_CANNOT_MODIFY_OTHER,SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL,
            ACCEPT_FAIL,
            SEND_REQUEST_COOLING;


    public static void intiExceptions(){
        PLAYER_NOT_NEKO=create(Lang.PLAYER_NOT_NEKO);

        GET_NEKO_ALREADY=create(Lang.GET_NEKO_ALREADY);
        GET_OWNER_ALREADY=create(Lang.GET_OWNER_ALREADY);
        REMOVE_NEKO_NOT_FOUND=create(Lang.REMOVE_NEKO_NOT_FOUND);
        REMOVE_OWNER_NOT_FOUND=create(Lang.REMOVE_OWNER_NOT_FOUND);
        GET_EXP_NOT_FOUND=create(Lang.GET_EXP_NOT_FOUND);
        SET_EXP_NOT_CONNECTED =create(Lang.SET_EXP_NOT_CONNECTED);

        SET_PET_PHRASE_ONLY_NEKO=create(Lang.SET_PET_PHRASE_ONLY_NEKO);
        SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO=create(Lang.SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO);
        SET_PET_PHRASE_EVERYONE_CANNOT=create(Lang.SET_PET_PHRASE_EVERYONE_CANNOT);
        SET_PET_PHRASE_NOT_OWNER=create(Lang.SET_PET_PHRASE_NOT_OWNER);
        SET_PET_PHRASE_CANNOT_MODIFY_OTHER=create(Lang.SET_PET_PHRASE_CANNOT_MODIFY_OTHER);
        SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL=create(Lang.SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL);

        ACCEPT_FAIL=create(Lang.ACCEPT_FAIL);
        SEND_REQUEST_COOLING=create(Lang.SEND_REQUEST_COOLING);
    }
    public static SimpleCommandExceptionType create(ConfigLang lang){
        return new SimpleCommandExceptionType(lang.component());
    }
}
