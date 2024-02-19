package yee.pltision.tonekoreforged.config;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.command.CommandExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这是一坨史
 */
public class ConfigLang {
    private static final ArrayList<String> KEYS =new ArrayList<>(), DEFAULTS =new ArrayList<>(), CHINESE=new ArrayList<>();
    private static final HashMap<String,ConfigLang> WAIT_FOR_INTI =new HashMap<>();

    final public String key,def,chinese;
    public String config;
    private MutableComponent component;

    public ConfigLang(String key, String def, String chinese) {
        KEYS.add(this.key = key);
        DEFAULTS.add(config= this.def = def);
        CHINESE.add(this.chinese = chinese);
        WAIT_FOR_INTI.put(key,this);
        component= Component.translatableWithFallback(key,def);
    }

    /**
     * 用于复制的Component（不然可能会被修改）
     */
    public MutableComponent component() {
        return Component.empty().append(component);
    }


    public static ForgeConfigSpec.ConfigValue<List<? extends String>> langInti(){
        Lang.inti();

        ArrayList<String> buildList=new ArrayList<>(KEYS.size()),zhList=new ArrayList<>(KEYS.size());
        for(int i=0;i<KEYS.size();i++){
            buildList.add(KEYS.get(i)+":"+DEFAULTS.get(i));
            zhList.add(KEYS.get(i)+":"+CHINESE.get(i));
        }
        Config.BUILDER.comment("这是一个中文的示例，将其替换掉lang就可以把默认翻译改成中文了。").defineList("chinese_example_lang",zhList,t->t instanceof String);
        return Config.BUILDER.defineList("lang",buildList, t->t instanceof String);
    }

    static void load(List<? extends String> configLangs)
    {
        for (String configLang : configLangs) {
            try{
                int index = configLang.indexOf(':');
                String key=configLang.substring(0,index);
                String lang=configLang.substring(index+1);
                ConfigLang configLang1= WAIT_FOR_INTI.get(key);
                configLang1.component=Component.translatableWithFallback(key,lang);
                configLang1.config=lang;
            }
            catch (Exception e){
                ToNeko.LOGGER.error(e.toString());
            }
        }

        CommandExceptions.intiExceptions();
    }


    @SubscribeEvent
    static void commonSetup(FMLCommonSetupEvent event){

    }


}
