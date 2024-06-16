package yee.pltision.tonekoreforged.config;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.command.CommandExceptions;

import java.io.FileWriter;
import java.util.*;

/**
 * 这是一坨史
 */
public class ConfigLang {
    private static final ArrayList<String> KEYS =new ArrayList<>(), DEFAULTS =new ArrayList<>(), CHINESE=new ArrayList<>();
    private static final HashMap<String,ConfigLang> WAIT_FOR_INTI =new HashMap<>();

    final public String key,def,chinese;
    public String config;
//    private MutableComponent component;

    public ConfigLang(String key, String def, String chinese) {
        KEYS.add(this.key = key);
        DEFAULTS.add(config= this.def = def);
        CHINESE.add(this.chinese = chinese);
        WAIT_FOR_INTI.put(key,this);
//        component= Component.translatableWithFallback(key,def);
    }

    /**
     * 用于复制的Component（不然可能会被修改）
     */
    public MutableComponent component(Object...values) {
        return Component.translatableWithFallback(key,config,values);
    }

    public static Map<String,ForgeConfigSpec.ConfigValue<String>> initDefaultLangMap(){
        Lang.inti();
        outLang(WAIT_FOR_INTI.values());

        Map<String,ForgeConfigSpec.ConfigValue<String>> configValues=new HashMap<>();
        for(int i=0;i<KEYS.size();i++)
            configValues.put(KEYS.get(i),Config.BUILDER.define("lang." + KEYS.get(i).replace('.','>'), DEFAULTS.get(i)));

        initChineseLangMap();
        return configValues;
    }

    public static void initChineseLangMap(){
        Config.BUILDER.comment("这是一个中文的示例，将其替换掉lang就可以把默认翻译改成中文了。");
        for(int i=0;i<KEYS.size();i++)
            Config.BUILDER.define("chinese_example_lang." + KEYS.get(i).replace('.','>'), CHINESE.get(i));
    }
    static void initConfigLangInstants()
    {
        for (Map.Entry<String, ForgeConfigSpec.ConfigValue<String>> entry: Config.CONFIG_LANG_MAP.entrySet()) {
            WAIT_FOR_INTI.computeIfPresent(entry.getKey(),(k,o)->{
                o.config=entry.getValue().get();
                System.out.println(entry.getValue().get());
                return o;
            });
        }

        CommandExceptions.intiExceptions();
    }

    @Deprecated
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> langInti(){
        Lang.inti();

        ArrayList<String> buildList=new ArrayList<>(KEYS.size()),zhList=new ArrayList<>(KEYS.size());
        for(int i=0;i<KEYS.size();i++){
            buildList.add(KEYS.get(i)+":"+DEFAULTS.get(i));
            zhList.add(KEYS.get(i)+":"+CHINESE.get(i));
        }

//        outLang(WAIT_FOR_INTI.values());

        Config.BUILDER.comment("这是一个中文的示例，将其替换掉lang就可以把默认翻译改成中文了。").defineList("chinese_example_lang",zhList,t->t instanceof String);
        return Config.BUILDER.defineList("lang",buildList, t->t instanceof String);
    }

    @Deprecated
    static void load(List<? extends String> configLangs)
    {
//        List<ConfigLang> outConfigLangs=new ArrayList<>(configLangs.size());
        for (String configLang : configLangs) {
            try{
                int index = configLang.indexOf(':');
                String key=configLang.substring(0,index);
                String lang=configLang.substring(index+1);
                ConfigLang configLang1= WAIT_FOR_INTI.get(key);
//                configLang1.component=Component.translatableWithFallback(key,lang);
                configLang1.config=lang;
//                outConfigLangs.add(configLang1);
            }
            catch (Exception e){
                ToNeko.LOGGER.error(e.toString());
            }
        }
//        outLang(outConfigLangs);

        CommandExceptions.intiExceptions();
    }

    @SuppressWarnings("all")
    @Deprecated
    static void outLang(Collection<ConfigLang>  langs){
        try{
            FileWriter writer=new FileWriter("zh_cn.json");

            writer.append("{\n");
            for (ConfigLang lang : langs) {
                writer.append("\t\""+lang.key+"\": \""+ (lang.chinese.replace("\n","\\n")).replace("\"","\\\"") +"\",\n");
            }
            writer.append("}");
            writer.close();

            writer=new FileWriter("en_us.json");
            writer.append("{\n");
            for (ConfigLang lang : langs) {
                writer.append("\t\""+lang.key+"\": \""+ (lang.def.replace("\n","\\n")).replace("\"","\\\"") +"\",\n");
            }
            writer.append("}");
            writer.close();
        }
        catch (Exception e){
            ToNeko.LOGGER.error(e.toString());
        }
    }



}
