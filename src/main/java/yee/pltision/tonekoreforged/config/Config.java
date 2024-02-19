package yee.pltision.tonekoreforged.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;

import java.util.HashSet;
import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

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

    private static final ForgeConfigSpec.ConfigValue<String> DEFAULT_PET_PHRASE = BUILDER
            .comment("The default pet phrase.")
            .comment("默认的口癖。中文可填写\"喵~\"。")
            .define("petPhrase.defaultPetPhrase", ", nya~");
    private static final ForgeConfigSpec.BooleanValue DEFAULT_PET_PHRASE_IGNORE_ENGLISH_TEXT = BUILDER
            .comment("If true, default pet phrase will not append to text that all character's value <= 255.")
            .comment("如果为true，默认的口癖不会添加到所有字符的值<=255（可以认为都是英文）的文本中。中文可填写true。")
            .define("petPhrase.defaultPetPhraseIgnoreEnglishText", false);
    private static final ForgeConfigSpec.ConfigValue<String> PET_PHRASE_IGNORE_CHARACTER = BUILDER
            .comment("Pet phrase will insert before the last character that not in this string.")
            .comment("口癖会插入到最后一个不在这个字符串内的字符后面。")
            .define("petPhrase.ignoreCharacters", ",.!?~-，。？！、—～…");

    private static final ForgeConfigSpec.ConfigValue<Integer> PET_PHRASE_IGNORE_AFTER = BUILDER
            .comment("Ignore the first of X characters.")
            .comment("When program try to append pet phrase to a text, it will ignore first X characters of pet phrase, like ',' and ' ' and ignore character in above string like '~' then we will leave \"nya\".")
            .comment("Program will use this word to test is the last world in the text is equals to that word you leave. If true, program will not append pet phrase to the text.")
            .comment("忽略前x个字符。中文可填写0。")
            .comment("当程序尝试将口癖加到文本中时，它会忽略前x字符（例如','和' '）以及上面的字符串中的字符（例如'~'），之后你会剩下\"nya\"。")
            .comment("程序会用你剩下的词与文本的最后一个词进行比较，如果它们相等，程序就不会修改原文本。")
            .define("petPhrase.petPhraseIgnoreAfter", 2);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CONFIG_LANG = ConfigLang.langInti();

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean removeStateWhenRemovedAllOwner;
    public static boolean addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase;
    public static boolean everyoneCanModifyTheirPetPhrase;
    public static boolean nekoCanModifyTheirPetPhrase;
    public static boolean ownerCanModifyTheirNekoPetPhrase;
    public static int maxAcceptTime;

    public static boolean enableGetNekoOrOwner, enableRemoveNeko, enableRemoveOwner, addOrRemoveNeedRequest;

    public static String defaultPetPhrase;
    public static int petPhraseIgnoreAfter;
    public static boolean defaultPetPhraseIgnoreEnglishText;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        removeStateWhenRemovedAllOwner = REMOVE_STATE_WHEN_REMOVED_ALL_OWNER.get();
        addPetPhraseWhenPlayerBeNekoAndItHaveNoPhrase = ADD_PET_PHRASE_WHEN_PLAYER_BE_NEKO_AND_IT_HAVE_NO_PHRASE.get();
        everyoneCanModifyTheirPetPhrase=EVERYONE_CAN_MODIFY_THEIR_PET_PHRASE.get();
        nekoCanModifyTheirPetPhrase = NEKO_CAN_MODIFY_THEIR_PET_PHRASE.get();
        ownerCanModifyTheirNekoPetPhrase= OWNER_CAN_MODIFY_THEIR_NEKO_PET_PHRASE.get();
        maxAcceptTime=MAX_ACCEPT_TIME.get();

        enableGetNekoOrOwner=ENABLE_GET_NEKO_OR_OWNER.get();
        enableRemoveNeko=ENABLE_REMOVE_NEKO.get();
        enableRemoveOwner=ENABLE_REMOVE_OWNER.get();
        addOrRemoveNeedRequest=ADD_OR_REMOVE_NEED_REQUEST.get();


        defaultPetPhrase = DEFAULT_PET_PHRASE.get();
        defaultPetPhraseIgnoreEnglishText = DEFAULT_PET_PHRASE_IGNORE_ENGLISH_TEXT.get();
        petPhraseIgnoreAfter = PET_PHRASE_IGNORE_AFTER.get();

        PetPhrase.IGNORE_CHARACTER = new HashSet<>();
        for (char c : PET_PHRASE_IGNORE_CHARACTER.get().toCharArray())
            PetPhrase.IGNORE_CHARACTER.add(c);

        ConfigLang.load(CONFIG_LANG.get());
    }
}
