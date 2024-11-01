package yee.pltision.tonekoreforged.config;

import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.nekostate.common.PetPhrase;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue NO_DATA_MODE=BUILDER
            .comment("If true, mod will not read or save data. Only for test.")
            .comment("如果为true，模组不会读取或保存数据。仅调试用。")
            .define("noDataMode", false);
    private static final ForgeConfigSpec.BooleanValue SAVE_NEKO_STATES_WHEN_SAVE_OVERWORLD =BUILDER
            .comment("Save neko states when save overwold. If something wrong when save in server, try disable this.")
            .comment("在保存主世界时保存猫娘状态。如果在服务器中保存时遇到了什么错误可以试试禁用这个。")
            .define("saveNekoStatesWhenSaveOverworld", false);
    private static final ForgeConfigSpec.BooleanValue OUTPUT_LANGUAGE_FILE =BUILDER
            .comment("Output json language file to ./to_neko for make resource pack.")
            .comment("输出json的语言文件到 ./to_neko 以用于制作资源包。")
            .define("outputLanguageFile", false);

    private static final ForgeConfigSpec.ConfigValue<String> NEKO_RITE = BUILDER
            .comment("Using neko rite。 Neko rite can let a player be ones neko. It only can be \"disabled\" or \"default\" now.")
            .comment("正在使用的变猫仪式。变猫仪式可能被用于强制的让一个玩家变成另一个玩家的猫娘。现在只有\"disabled\"和\"default\"选项。")
            .define("nekoRite", "default");

    private static final ForgeConfigSpec.BooleanValue REMOVE_STATE_WHEN_REMOVED_ALL_OWNER = BUILDER
            .comment("If true, when command removed a neko's all owner, the neko will be not a neko.")
            .comment("如果为true，当使用命令移除了一只猫娘的所有主人后，它就不是猫娘了。")
            .define("command.removeStateWhenRemovedAllOwner", true);
    private static final ForgeConfigSpec.BooleanValue EVERYONE_CAN_MODIFY_THEIR_PET_PHRASE = BUILDER
            .comment("If true, everyone can modify their pet phrase, but if player is a neko it will follow nekoCanModifyTheirPetPhrase.")
            .comment("如果为true，所有人可以修改它们的口癖，但如果玩家是猫娘的话则遵循nekoCanModifyTheirPetPhrase。")
            .define("command.everyoneCanModifyTheirPetPhrase", true);
    private static final ForgeConfigSpec.BooleanValue NEKO_CAN_MODIFY_THEIR_PET_PHRASE = BUILDER
            .comment("If true, neko can modify their pet phrase.")
            .comment("如果为true，猫娘可以修改它的口癖。")
            .define("command.nekoCanModifyTheirPetPhrase", true);
    private static final ForgeConfigSpec.BooleanValue OWNER_CAN_MODIFY_THEIR_NEKO_PET_PHRASE = BUILDER
            .comment("If true, owner can modify their neko's pet phrase.")
            .comment("如果为true，主人可以修改其猫娘的口癖。")
            .define("command.ownerCanModifyTheirNekoPetPhrase", true);
    private static final ForgeConfigSpec.BooleanValue ADD_PET_PHRASE_WHEN_PLAYER_BE_NEKO_AND_IT_HAVE_NO_PHRASE = BUILDER
            .comment("If true, when player be a neko, and it has no pet phrase will append a default phrase.")
            .comment("如果为true，当玩家变成猫娘后且没有口癖时为其添加默认口癖。")
            .define("command.addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase", true);
    private static final ForgeConfigSpec.ConfigValue<Integer> MAX_ACCEPT_TIME = BUILDER
            .comment("Request can be accepted within these ticks.")
            .comment("请求可以在这个tick内被接受。")
            .define("command.maxAcceptTime", 1200);

    private static final ForgeConfigSpec.BooleanValue NEKO_CAN_BE_ITS_OWNER = BUILDER
            .comment("If true, neko can be its owner.")
            .comment("如果为true，猫娘可以成为它自己的主人。")
            .define("command.nekoCanBeItsOwner", true);
    private static final ForgeConfigSpec.BooleanValue NEKO_CAN_HAVE_NEKO = BUILDER
            .comment("If true, neko can have other nekos except itself.")
            .comment("如果为true，猫娘可以有除了他自己的其他猫娘。")
            .define("command.nekoCanHaveNeko", true);
    private static final ForgeConfigSpec.BooleanValue PLAYER_CAN_HAVE_MULTIPLE_NEKOS = BUILDER
            .comment("If true, a player can have multiple nekos.")
            .comment("如果为true，一个玩家可以有多个猫娘。")
            .define("command.playerCanHaveMultipleNekos", true);
    private static final ForgeConfigSpec.BooleanValue PLAYER_CAN_HAVE_MULTIPLE_OWNERS = BUILDER
            .comment("If true, a player can have multiple owners.")
            .comment("如果为true，一个玩家可以有多个主人。")
            .define("command.playerCanHaveMultipleOwners", true);

    private static final ForgeConfigSpec.BooleanValue ENABLE_GET_NEKO_OR_OWNER = BUILDER
            .comment("If true, player can use command to get their neko or owner.")
            .comment("如果为true，玩家可以通过命令获取猫娘或主人。")
            .define("command.enableGetNekoOrOwner", true);
    private static final ForgeConfigSpec.BooleanValue ENABLE_REMOVE_NEKO = BUILDER
            .comment("If true, owner can use command to remove their neko.")
            .comment("如果为true，玩家可以通过命令移除猫娘或主人。")
            .define("command.enableRemoveNeko", true);
    private static final ForgeConfigSpec.BooleanValue ENABLE_REMOVE_OWNER = BUILDER
            .comment("If true, neko can use command to remove their owner.")
            .comment("如果为true，玩家可以通过命令移除猫娘或主人。")
            .define("command.enableRemoveOwner", true);
    private static final ForgeConfigSpec.BooleanValue ADD_OR_REMOVE_NEED_REQUEST = BUILDER
            .comment("If true, player add or remove owner and neko need request.")
            .comment("如果为true，玩家通过命令获取或移除主人和猫娘需要请求。")
            .define("command.addOrRemoveNeedRequest", true);

    //根据择默认语言选择配置
    public static final boolean useChinese=Locale.getDefault().getLanguage().equals("zh");

    static String defaultPetPhrase(){return useChinese?"喵~":", nya~";}
    static boolean defaultPetPhraseIgnoreEnglishText(){return useChinese;}
    static int defaultPetPhraseIgnoreAfter(){return useChinese?0:2;}

    static {
        BUILDER.comment("Default pet phrase will change with your locale. If getLanguage() return \"zh\" it will use chinese, else use english.")
                .comment("默认的口癖会随着你的区域更改。如果getLanguage()返回\"zh\"它会使用中文，否则使用英文。").comment("");
    }

    private static final ForgeConfigSpec.ConfigValue<String> DEFAULT_PET_PHRASE = BUILDER
            .comment("The default pet phrase. In english it can be \", nya~\".")
            .comment("默认的口癖。中文可填写\"喵~\"。")
            .define("petPhrase.defaultPetPhrase", defaultPetPhrase());
    private static final ForgeConfigSpec.BooleanValue DEFAULT_PET_PHRASE_IGNORE_ENGLISH_TEXT = BUILDER
            .comment("If true, default pet phrase will not append to text that all character's value <= 255. In english it can be false.")
            .comment("如果为true，默认的口癖不会添加到所有字符的值<=255（可以认为都是英文）的文本中。中文可填写true。")
            .define("petPhrase.defaultPetPhraseIgnoreEnglishText", defaultPetPhraseIgnoreEnglishText());
    private static final ForgeConfigSpec.ConfigValue<String> PET_PHRASE_IGNORE_CHARACTER = BUILDER
            .comment("Pet phrase will insert before the last character that not in this string.")
            .comment("口癖会插入到最后一个不在这个字符串内的字符后面。")
            .define("petPhrase.ignoreCharacters", ",.!?~-，。？！、—～…");
    private static final ForgeConfigSpec.ConfigValue<Integer> DEFAULT_PET_PHRASE_IGNORE_AFTER = BUILDER
            .comment("Ignore the first of X characters. If use default pet phrase it can be 0.")
            .comment("When program try to append pet phrase to a text, it will ignore first X characters of pet phrase, like ',' and ' ' and ignore character in above string like '~' then we will leave \"nya\".")
            .comment("Program will use this word to test is the last world in the text is equals to that word you leave. If true, program will not append pet phrase to the text.")
            .comment("忽略前x个字符。中文可填写0。")
            .comment("当程序尝试将口癖加到文本中时，它会忽略前x字符（例如','和' '）以及上面的字符串中的字符（例如'~'），之后你会剩下\"nya\"。")
            .comment("程序会用你剩下的词与文本的最后一个词进行比较，如果它们相等，程序就不会修改原文本。")
            .define("petPhrase.defaultPetPhraseIgnoreAfter", defaultPetPhraseIgnoreAfter());

    private static final ForgeConfigSpec.BooleanValue ENABLE_ROB_SHEAR = BUILDER
            .comment("Enable Rob Shear enchantment.")
            .comment("启用剥取附魔。")
            .define("robShear.enable", true);
    private static final ForgeConfigSpec.BooleanValue ONLY_OWNER_CAN_SHEAR_NEKO = BUILDER
            .comment("If true, only one's owner can use Rob Shear enchantment to them.")
            .comment("如果为true，只有猫娘的主人可以对其使用剥取附魔。")
            .define("robShear.onlyOwnerCanShearNeko", true);
    private static final ForgeConfigSpec.BooleanValue DISPENSER_CAN_USE = BUILDER
            .comment("If true, dispenser can ues a shear with rob shear to player.")
            .comment("如果为true，发射器可以对玩家使用带有剥取附魔的剪刀。")
            .define("robShear.dispenserCanUseRobShear", true);

    private static final ForgeConfigSpec.ConfigValue<String> CURIOS_COLLAR_SLOT = BUILDER
            .comment("Collar slot in Curios.")
            .comment("项圈在Curios中的槽位。")
            .define("collar.curiosSlot", "collar");

    private static final ForgeConfigSpec.BooleanValue ENABLE_NAME_TAG_MODIFY = BUILDER
            .comment("If true, players can wear a collar with name tag and modify their display name.")
            .comment("如果为true，玩家可以戴上带有命名牌的项圈来修改他们在聊天栏显示的名字。")
            .define("collar.nameTag.enable",true);

    private static final ForgeConfigSpec.ConfigValue<Integer> NAME_TAG_SHOW_OWNER_LIMIT = BUILDER
            .comment("when players wear a non renamed name tag their will join their owner's name. If owner more then the number, names will not join.")
            .comment("玩家可以戴上带有未命名的命名牌的项圈时，名字中加入它们主人的名字不超过此值，若超过则不添加主人的名字。")
            .define("collar.nameTag.limit",3);
    private static final ForgeConfigSpec.BooleanValue ENABLE_NAME_TAG_COSTUME_PREFIX = BUILDER
            .comment("If true, players can wear a renamed name tag to add a prefix to their display name.")
            .comment("如果为true，玩家可以戴上命名的命名牌来让他们聊天栏显示的名字加上前缀。")
            .define("collar.nameTag.enableCostumePrefix",true);

    /*//NOT_TODO: 写括号处理啥的，还有给忽略字符追加括号（算了懒了，不做配置，反正末尾符号优化还不是不能配置
    Pair<BracketPair,ForgeConfigSpec> pair=BUILDER.configure(builder->{
        builder.tr
        return new BracketPair("","");
    });*/

    public static final Map<String,ForgeConfigSpec.ConfigValue<String>> CONFIG_LANG_MAP=ConfigLang.initDefaultLangMap();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean dontSave;
    public static boolean saveNekoStatesWhenSaveOverworld;

    public static boolean removeStateWhenRemovedAllOwner;
    public static boolean addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase;
    public static boolean everyoneCanModifyTheirPetPhrase;
    public static boolean nekoCanModifyTheirPetPhrase;
    public static boolean ownerCanModifyTheirNekoPetPhrase;
    public static int maxAcceptTime;

    public static boolean nekoCanBeItsOwner, nekoCanHaveNeko, playerCanHaveMultipleNekos, playerCanHaveMultipleOwners;
    public static boolean enableGetNekoOrOwner, enableRemoveNeko, enableRemoveOwner, addOrRemoveNeedRequest;

    public static String defaultPetPhrase;
    public static int defaultPetPhraseIgnoreAfter;
    public static boolean defaultPetPhraseIgnoreEnglishText;

    public static String configNekoRite;
    public static Supplier<Component> usingRite=Lang.DISABLED_NEKO_RITE::component;

    public static boolean enableRobShear;
    public static boolean onlyOwnerCanShearNeko;
    public static boolean dispenserCanUseRobShear;

    public static String curiosSlotType;

    public static final Supplier<Component> DEFAULT_RITE=Lang.DEFAULT_NEKO_RITE_GUILD::component;

    public static boolean enableNameTagModify;
    public static int nameTagShowOwnerLimit;
    public static boolean enableNameTagCostumePrefix;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        dontSave =NO_DATA_MODE.get();
        saveNekoStatesWhenSaveOverworld=SAVE_NEKO_STATES_WHEN_SAVE_OVERWORLD.get();

        removeStateWhenRemovedAllOwner = REMOVE_STATE_WHEN_REMOVED_ALL_OWNER.get();
        addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase = ADD_PET_PHRASE_WHEN_PLAYER_BE_NEKO_AND_IT_HAVE_NO_PHRASE.get();
        everyoneCanModifyTheirPetPhrase=EVERYONE_CAN_MODIFY_THEIR_PET_PHRASE.get();
        nekoCanModifyTheirPetPhrase = NEKO_CAN_MODIFY_THEIR_PET_PHRASE.get();
        ownerCanModifyTheirNekoPetPhrase= OWNER_CAN_MODIFY_THEIR_NEKO_PET_PHRASE.get();
        maxAcceptTime=MAX_ACCEPT_TIME.get();

        nekoCanBeItsOwner=NEKO_CAN_BE_ITS_OWNER.get();
        nekoCanHaveNeko=NEKO_CAN_HAVE_NEKO.get();
        playerCanHaveMultipleNekos=PLAYER_CAN_HAVE_MULTIPLE_NEKOS.get();
        playerCanHaveMultipleOwners=PLAYER_CAN_HAVE_MULTIPLE_OWNERS.get();

        enableGetNekoOrOwner=ENABLE_GET_NEKO_OR_OWNER.get();
        enableRemoveNeko=ENABLE_REMOVE_NEKO.get();
        enableRemoveOwner=ENABLE_REMOVE_OWNER.get();
        addOrRemoveNeedRequest=ADD_OR_REMOVE_NEED_REQUEST.get();


        defaultPetPhrase = DEFAULT_PET_PHRASE.get();
        defaultPetPhraseIgnoreEnglishText = DEFAULT_PET_PHRASE_IGNORE_ENGLISH_TEXT.get();
        defaultPetPhraseIgnoreAfter = DEFAULT_PET_PHRASE_IGNORE_AFTER.get();

        configNekoRite=NEKO_RITE.get();
        if(configNekoRite.equals("default")) usingRite=DEFAULT_RITE;

        PetPhrase.initStatics(PET_PHRASE_IGNORE_CHARACTER.get(),PetPhrase.BRACKETS);

        ConfigLang.initConfigLangInstants();

        if(OUTPUT_LANGUAGE_FILE.get())ConfigLang.outLang();

        enableRobShear=ENABLE_ROB_SHEAR.get();
        dispenserCanUseRobShear=DISPENSER_CAN_USE.get()&&enableRobShear;
        onlyOwnerCanShearNeko= ONLY_OWNER_CAN_SHEAR_NEKO.get();

        curiosSlotType= CURIOS_COLLAR_SLOT.get();

        enableNameTagModify=ENABLE_NAME_TAG_MODIFY.get();
        nameTagShowOwnerLimit=NAME_TAG_SHOW_OWNER_LIMIT.get();
        enableNameTagCostumePrefix=ENABLE_NAME_TAG_COSTUME_PREFIX.get();

    }

}
