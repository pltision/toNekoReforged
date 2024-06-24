package yee.pltision.tonekoreforged.config;

public class Lang {
    public static final ConfigLang
        PLAYER_NOT_NEKO=new ConfigLang("commands.toneko.player_not_neko", "This player is not a neko","这个玩家不是一只猫娘"),

        SEND_REQUEST_INFO=new ConfigLang("commands.toneko.request.send_request_info", "Send request to %s","已将请求发送至%s"),
        ACCEPT_REQUEST_BUTTON=new ConfigLang("commands.toneko.accept.accept_button", "[Accept]","[接受]"),
        DENY_REQUEST_BUTTON=new ConfigLang("commands.toneko.deny.deny_button", "[Deny]","[拒绝]"),
        REQUEST_COMMAND_INFO=new ConfigLang("commands.toneko.request.request_command_info", "Execute command: ","运行指令: %s"),
        SEND_REQUEST_COOLING=new ConfigLang("commands.toneko.request.cooling", "Your request is not accepted or expire, please wait a minute to try again","你的请求还未被接受或过期，请稍等一会再发送另一个请求"),


        LIST_NEKO_INFO=new ConfigLang("commands.toneko.list_neko.info", "Your nekos include: ","你的猫娘包括: "),

        GET_NEKO_ALREADY=new ConfigLang("commands.toneko.get_neko.already", "This player is already your neko","这个玩家已经是你的猫娘了"),
        GET_NEKO_INFO=new ConfigLang("commands.toneko.get_neko.info", "%s is your neko now","%s现在是你的猫娘了"),
        GET_NEKO_REQUEST=new ConfigLang("commands.toneko.get_neko.request_info", "%s want let you be their neko","%s想让你成为他的猫娘"),
        GET_NEKO_NEKO_CANNOT_HAVE_OTHER_NEKO=new ConfigLang("commands.toneko.get_neko.neko_cannot_have", "A neko cannot have other neko","猫娘不能拥有其他猫娘"),
        GET_NEKO_CANNOT_MULTIPLE =new ConfigLang("commands.toneko.get_neko.cannot_multiple", "You cannot have multiple neko","你不能拥有多个猫娘"),
        GET_NEKO_OTHER_ALREADY=new ConfigLang("commands.toneko.get_neko.other_already", "This player is already have a owner","这个玩家已经有一个主人了"),
        CANNOT_BE_OWN_NEKO_OR_OWNER=new ConfigLang("commands.toneko.cannot_be_own_neko_or_owner","Cannot be own neko or owner","不能成为自己的主人或猫娘"),

        REMOVE_REQUEST=new ConfigLang("commands.toneko.remove_request", "%s want remove your connect","%s想与你断开联系"),
        LIST_OWNER_INFO=new ConfigLang("commands.toneko.list_owner.info", "Your owners include: ","你的主人包括: "),

        GET_OWNER_ALREADY=new ConfigLang("commands.toneko.get_owner.already", "This player is already your owner","这个玩家已经是你的主人了"),
        GET_OWNER_INFO=new ConfigLang("commands.toneko.get_owner.info", "%s is your owner now","%s现在是你的主人了"),
        GET_OWNER_REQUEST=new ConfigLang("commands.toneko.get_owner.request_info", "%s want let you be their owner","%s想让你成为他的主人"),
        GET_OWNER_CANNOT_MULTIPLE =new ConfigLang("commands.toneko.get_owner.cannot_multiple", "You cannot have multiple owner","你不能拥有多个主人"),
        GET_OWNER_OTHER_ALREADY=new ConfigLang("commands.toneko.get_owner.other_already", "This player is already have a neko","这个玩家已经有一只猫娘了"),


        REMOVE_NEKO_NOT_FOUND=new ConfigLang("commands.toneko.remove_neko.not_found", "You have no neko whit this UUID or name","你没有这个UUID或名称的猫娘"),
        REMOVE_NEKO_INFO=new ConfigLang("commands.toneko.remove_neko.info", "%s is not your neko now","%s现在不是你的猫娘了"),

        REMOVED_NEKO_STATE_INFO=new ConfigLang("commands.toneko.removed_neko_state", "You are not a neko now","你现在不是猫娘了"),

        REMOVE_OWNER_NOT_FOUND=new ConfigLang("commands.toneko.remove_owner.not_found", "You have no owner whit this UUID or name","你没有这个UUID或名称的主人"),
        REMOVE_OWNER_INFO=new ConfigLang("commands.toneko.remove_owner.info", "%s is not your owner now","%s现在不是你的主人了"),

        GET_EXP_NOT_FOUND=new ConfigLang("commands.toneko.get_exp.not_found", "You are not a neko or owner whit this player","你不是这个玩家的猫娘或主人"),
        GET_EXP_INFO=new ConfigLang("commands.toneko.get_exp.info", "Your exp is %s","你们的好感经验值是%s"),

        SET_EXP_NOT_CONNECTED=new ConfigLang("commands.toneko.set_exp.not_connected", "These tow players (or same player) is not connected","这两个（或同个）玩家没有建立关系"),
        SET_EXP_INFO=new ConfigLang("commands.toneko.set_exp.info", "Set exp to %s","已将好感经验值设为%s"),

//        NEKO_PREFIX=new ConfigLang("to_neko.neko_prefix", "[Neko] ","[猫娘] "),

        GET_PET_PHRASE_INFO=new ConfigLang("commands.toneko.get_pet_phrase.info", "%s has pet phrase with %s","%s的口癖为%s"),

        CLEARED_PET_PHRASE_INFO =new ConfigLang("commands.toneko.set_pet_phrase.clear", "Cleared pet phrase for %s","清除了%s的口癖"),
        SET_PET_PHRASE_INFO =new ConfigLang("commands.toneko.set_pet_phrase.info", "Set %s own pet phrase to %s","已将%s的口癖设为%s"),
        SET_PET_PHRASE_ONLY_NEKO=new ConfigLang("commands.toneko.set_pet_phrase.only_neko", "Only can modify neko's pet phrase","只能修改猫娘的口癖"),
        SET_PET_PHRASE_EVERYONE_EXCEPT_NEKO=new ConfigLang("commands.toneko.set_pet_phrase.except_neko", "Neko cannot modify their pet phrase","猫娘不可以修改它们的口癖"),
        SET_PET_PHRASE_EVERYONE_CANNOT=new ConfigLang("commands.toneko.set_pet_phrase.everyone_cannot", "Cannot modify own pet phrase","不可以修改自己的口癖"),
        SET_PET_PHRASE_NOT_OWNER=new ConfigLang("commands.toneko.set_pet_phrase.not_owner", "Only owner can modify their neko's pet phrase","只有猫娘的主人可以修改猫娘的口癖"),
        SET_PET_PHRASE_CANNOT_MODIFY_OTHER=new ConfigLang("commands.toneko.set_pet_phrase.cannot_modify_other", "Cannot modify other's pet phrase","不可以修改其他人的口癖"),
        SET_PET_PHRASE_AFTER_IGNORE_ILLEGAL=new ConfigLang("commands.toneko.set_pet_phrase.ignore_after_illegal","ignore_after is illegal, try smaller","ignore_after的值不合法，请尝试输入更小的值"),

        ACCEPT_FAIL =new ConfigLang("commands.toneko.accept.fail","Player have no request for you or request expired","该玩家没有对你的请求或请求已过期"),
        ACCEPT_INFO =new ConfigLang("commands.toneko.accept.info","You accept the request","你接受了请求"),
        DENY_INFO =new ConfigLang("commands.toneko.deny.info","You deny the request","你拒绝了请求"),

        CAT_STICK=new ConfigLang("to_neko.cat_stick","{\"type\":\"translatable\",\"translate\":\"to_neko.cat_stick\",\"fallback\":\"Cat Stick\",\"color\":\"blue\"}","{\"type\":\"translatable\",\"translate\":\"to_neko.cat_stick\",\"fallback\":\"撅猫棒\",\"color\":\"blue\"}"),

        FAIL_TO_GET_CAPABILITY=new ConfigLang("commands.toneko.fail_to_get_capability","Fail to get capability! Player: ","无法获取能力！玩家："),
        HELP_GUIDE=new ConfigLang("commands.toneko.help", """
Get help
/toneko help

Get your all nekos
/toneko getNeko
Set <player> to your neko or send request
/toneko getNeko <player>

Get your all owners
/toneko getOwner
Set <player> to your owner or send request
/toneko getOwner <player>

Remove a neko with a name or UUID with <player> or send request
/toneko removeNeko <player>
Remove a owner with a name or UUID with <player> or send request
/toneko removeOwner <player>

Get the pet phrase of the executor or <player>
/toneko petPhrase [<player>]

Set the pet phrase for <player>
When <ignore_english> is true, if all character in message <=255 will not append pet phrase to message, false for default
Ignore <ignore_after> character to the pet phrase after
/toneko petPhrase <player> <phrase> [<ignore_english>] [<ignore_after>]

Clear pet phrase for <player>
/toneko petPhrase <player> ""

Accept request from <player>
/toneko accept <player>

Deny request from <player>
/toneko deny <player>

Check is enable neko rite and see how to use
/toneko nekoRite
""",
"""
查看帮助
/toneko help

获取玩家的全部猫娘
/toneko getNeko
将 <player> 设为执行者的猫娘或发送请求
/toneko getNeko <player>

获取玩家的全部主人
/toneko getOwner
将<player>设为执行者的主人或发送请求
/toneko getOwner <player>

为执行者移除名称或 UUID 为 <player> 的猫娘或发送请求
/toneko removeNeko <player>
为执行者移除名称或 UUID 为 <player> 的主人或发送请求
/toneko removeOwner <player>

获取执行者或 <player> 的口癖
/toneko petPhrase [<player>]

设置 <player> 的口癖
当 <ignore_english> 为 true 时，在对话中若所有字符的值 <=255 则不会添加口癖。若不填写则默认为 false
<ignore_after>为检查是否添加口癖时可以忽略口癖前的几个字符
/toneko petPhrase <player> <phrase> [<ignore_english>] [<ignore_after>]

移除玩家的口癖
/toneko petPhrase <player> ""

获取执行者与 <player> 之间的好感经验值
/toneko getExp <player>
设置 <player1> 与 <player2> 之间的好感经验值，需要命令权限
/toneko setExp <player1> <player2> <value>

同意 <player> 发出的请求
/toneko accept <player>

拒绝 <player> 发出的请求
/toneko deny <player>

查看是否启用以及如何使用变猫仪式
/toneko nekoRite

若你有指令权限的话，还可以使用 execute 来指定执行者喵~
"""),
        DISABLED_NEKO_RITE=new ConfigLang("command.toneko.disabled_rite","The neko rite is been disabled or config is wrong","变猫仪式并未启用或配置文件错误"),
        DEFAULT_NEKO_RITE_GUILD=new ConfigLang("command.toneko.default_rite",
"""
Use lapis lazuli, enchanted_book and cooked cod to craft an enchanted cod.
Then in the mid night (17840<=DayTime<=18160), let your cat sit on the Enchantment Table, put the enchanted cod on your off hand and use Cat Stick to attack another player.
If every things fine, your cat will get 15 second Wither effect in level 3, and the another player will be your neko.
If the state with another player is not right, you will get some message Whether the rite is successful or not, your cod will be shrink.
""", """
使用青金石、任意附魔书和熟鳕鱼合成一条附魔鳕鱼。然后在午夜（17840<=DayTime<=18160）时，将你驯服的猫放在附魔台方块上，副手手持鳕鱼，主手手持撅猫棒攻击附魔台附近的玩家。
若条件成立，被攻击的玩家将会变成你的猫娘，猫将受到15秒的凋零III效果。若检测另一个玩家的猫娘关系时有异常，你将会收到报错喵~
此外，无论仪式是否完成，鳕鱼都会消耗喵。
""");

    public static void inti(){}
}
