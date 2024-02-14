package yee.pltision.tonekoreforged.config;

public class Lang {
    public static final ConfigLang
            PLAYER_NOT_NEKO=new ConfigLang("commands.toneko.player_not_neko", "This player is not a neko","这个玩家不是一只猫娘"),

            LIST_NEKO_INFO=new ConfigLang("commands.toneko.list_neko.info", "Your nekos include: ","你的猫娘包括: "),
            GET_NEKO_ALREADY=new ConfigLang("commands.toneko.get_neko.already", "This player is already your neko","这个玩家已经是你的猫娘了"),
            GET_NEKO_INFO=new ConfigLang("commands.toneko.get_neko.info", " is your neko now","现在是你的猫娘了"),

            LIST_OWNER_INFO=new ConfigLang("commands.toneko.list_owner.info", "Your owners include: ","你的主人包括: "),
            GET_OWNER_ALREADY=new ConfigLang("commands.toneko.get_owner.already", "This player is already your owner","这个玩家已经是你的主人了"),
            GET_OWNER_INFO=new ConfigLang("commands.toneko.get_owner.info", " is your owner now","现在是你的主人了"),

            REMOVE_NEKO_NOT_FOUND=new ConfigLang("commands.toneko.remove_neko.not_found", "You have no neko whit this UUID or name","你没有这个UUID或名称的猫娘"),
            REMOVE_NEKO_INFO=new ConfigLang("commands.toneko.remove_neko.info", " is not your neko now","现在不是你的猫娘了"),

            REMOVE_OWNER_NOT_FOUND=new ConfigLang("commands.toneko.remove_owner.not_found", "You have no owner whit this UUID or name","你没有这个UUID或名称的主人"),
            REMOVE_OWNER_INFO=new ConfigLang("commands.toneko.remove_owner.info", " is not your owner now","现在不是你的主人了"),


            GET_EXP_NOT_FOUND=new ConfigLang("commands.toneko.get_exp.not_found", "You are not a neko or owner whit this player","你不是这个玩家的猫娘或主人"),
            GET_EXP_INFO=new ConfigLang("commands.toneko.get_exp.info", "Your exp is ","你们的好感经验值是"),

            SET_EXP_NOT_CONNECTED=new ConfigLang("commands.toneko.get_exp.not_found", "These tow players (or same player) is not connected","这两个（或同个）玩家没有建立关系"),
            SET_EXP_INFO=new ConfigLang("commands.toneko.get_exp.info", "Set exp to ","已将好感经验值设为");

    public static void intiClass(){}
}
