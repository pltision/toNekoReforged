package yee.pltision.tonekoreforged.config;

public class Lang {
    public static final ConfigLang
            PLAYER_NOT_NEKO=new ConfigLang("commands.toneko.player_not_neko", "This player is not a neko","这个玩家不是一只猫娘"),

        SEND_REQUEST_INFO=new ConfigLang("commands.toneko.request.send_request_info", "Send request to ","已将请求发送至"),
        ACCEPT_REQUEST_BUTTON=new ConfigLang("commands.toneko.accept.accept_button", "[Accept]","[接受]"),
        DENY_REQUEST_BUTTON=new ConfigLang("commands.toneko.deny.deny_button", "[Deny]","[拒绝]"),
        REQUEST_COMMAND_INFO=new ConfigLang("commands.toneko.request.request_command_info", "Run command ","运行指令"),
        SEND_REQUEST_COOLING=new ConfigLang("commands.toneko.request.cooling", "Your request is not accepted or expire, please wait a minute to try again","你的请求未被接收，请稍等一会再发送另一个请求"),


        LIST_NEKO_INFO=new ConfigLang("commands.toneko.list_neko.info", "Your nekos include: ","你的猫娘包括: "),

        GET_NEKO_ALREADY=new ConfigLang("commands.toneko.get_neko.already", "This player is already your neko","这个玩家已经是你的猫娘了"),
        GET_NEKO_INFO=new ConfigLang("commands.toneko.get_neko.info", " is your neko now","现在是你的猫娘了"),
        GET_NEKO_REQUEST=new ConfigLang("commands.toneko.get_neko.request_info", " want let you be their neko","想让你称为他的猫娘"),
        REMOVE_REQUEST=new ConfigLang("commands.toneko.remove_request", " want remove your connect","想与你断开联系"),

        LIST_OWNER_INFO=new ConfigLang("commands.toneko.list_owner.info", "Your owners include: ","你的主人包括: "),
        GET_OWNER_ALREADY=new ConfigLang("commands.toneko.get_owner.already", "This player is already your owner","这个玩家已经是你的主人了"),
        GET_OWNER_INFO=new ConfigLang("commands.toneko.get_owner.info", " is your owner now","现在是你的主人了"),
        GET_OWNER_REQUEST=new ConfigLang("commands.toneko.get_owner.request_info", " want let you be their owner","想让你称为他的主人"),

        REMOVE_NEKO_NOT_FOUND=new ConfigLang("commands.toneko.remove_neko.not_found", "You have no neko whit this UUID or name","你没有这个UUID或名称的猫娘"),
        REMOVE_NEKO_INFO=new ConfigLang("commands.toneko.remove_neko.info", " is not your neko now","现在不是你的猫娘了"),

        REMOVE_OWNER_NOT_FOUND=new ConfigLang("commands.toneko.remove_owner.not_found", "You have no owner whit this UUID or name","你没有这个UUID或名称的主人"),
        REMOVE_OWNER_INFO=new ConfigLang("commands.toneko.remove_owner.info", " is not your owner now","现在不是你的主人了"),

        GET_EXP_NOT_FOUND=new ConfigLang("commands.toneko.get_exp.not_found", "You are not a neko or owner whit this player","你不是这个玩家的猫娘或主人"),
        GET_EXP_INFO=new ConfigLang("commands.toneko.get_exp.info", "Your exp is ","你们的好感经验值是"),

        SET_EXP_NOT_CONNECTED=new ConfigLang("commands.toneko.set_exp.not_connected", "These tow players (or same player) is not connected","这两个（或同个）玩家没有建立关系"),
        SET_EXP_INFO=new ConfigLang("commands.toneko.set_exp.info", "Set exp to ","已将好感经验值设为"),

        NEKO_PREFIX=new ConfigLang("tone_ko.neko_prefix", "[Neko] ","[猫娘] "),

        GET_PET_PHRASE_INFO=new ConfigLang("commands.toneko.get_pet_phrase.info", " has pet phrase with ","的口癖为"),

        SET_PET_PHRASE_INFO=new ConfigLang("commands.toneko.set_pet_phrase.info", " own pet phrase set to ","的口癖为"),
        SET_PET_PHRASE_ONLY_NEKO=new ConfigLang("commands.toneko.set_pet_phrase.only_neko", " own pet phrase be set to ","的口癖被设为了"),
        SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO=new ConfigLang("commands.toneko.set_pet_phrase.except_neko", "Neko cannot modify their pet phrase","猫娘不可以修改它们的口癖"),
        SET_PET_PHRASE_EVERYONE_CANNOT=new ConfigLang("commands.toneko.set_pet_phrase.everyone_cannot", "Cannot modify own pet phrase","不可以修改自己的口癖"),
        SET_PET_PHRASE_NOT_OWNER=new ConfigLang("commands.toneko.set_pet_phrase.not_owner", "Only owner can modify their neko's pet phrase","只有猫娘的主人可以修改猫娘的口癖"),
        SET_PET_PHRASE_CANNOT_MODIFY_OTHER=new ConfigLang("commands.toneko.set_pet_phrase.not_owner", "Cannot modify other's pet phrase","不可以修改其他人的口癖"),
        SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL=new ConfigLang("commands.toneko.set_pet_phrase.ignore_after_illegal","ignore_after is illegal, try smaller","ignore_after的值不合法，请尝试输入更小的值"),

        ACCEPT_FAIL =new ConfigLang("commands.toneko.accept.fail","Player have no request for you or request expired","该玩家没有对你的请求或请求已过期"),
        ACCEPT_INFO =new ConfigLang("commands.toneko.accept.info","You accept the request","你接受了请求"),
        DENY_INFO =new ConfigLang("commands.toneko.deny.info","You deny the request","你拒绝了请求"),

        CAT_STICK=new ConfigLang("to_neko.cat_stick","{\"type\":\"translatable\",\"translate\":\"to_neko.cat_stick\",\"fallback\":\"Cat Stick\",\"color\":\"blue\"}","{\"type\":\"translatable\",\"translate\":\"to_neko.cat_stick\",\"fallback\":\"撅猫棒\",\"color\":\"blue\"}");



    public static void inti(){}
}
