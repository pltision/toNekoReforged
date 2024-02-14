package yee.pltision.tonekoreforged.config;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.command.CommandException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 这是一坨史
 */
@Mod.EventBusSubscriber(modid = ToNeko.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigLang {
    private static final ArrayList<String> KEYS =new ArrayList<>(), DEFAULTS =new ArrayList<>(), CHINESE=new ArrayList<>();
    private static final HashMap<String,ConfigLang> WAIT_FOR_INTI =new HashMap<>();

    final public String key,def,chinese;
    private MutableComponent component;

    public ConfigLang(String key, String def, String chinese) {
        KEYS.add(this.key = key);
        DEFAULTS.add(this.def = def);
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





    //主打一个随心所欲写答辩
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        Lang.intiClass();

        ArrayList<String> buildList=new ArrayList<>(KEYS.size()),zhList=new ArrayList<>(KEYS.size());
        for(int i=0;i<KEYS.size();i++){
            buildList.add(KEYS.get(i)+":"+DEFAULTS.get(i));
            zhList.add(KEYS.get(i)+":"+CHINESE.get(i));
        }
        Config.BUILDER.comment("这是一个中文的示例，将其替换掉lang就可以把默认翻译改成中文了。").defineList("chinese_example_lang",zhList,v->true);
        ForgeConfigSpec.ConfigValue<List<? extends String>> configLangs= Config.BUILDER.defineList("lang",buildList, v->true);

        for (String configLang : configLangs.get()) {
            try{
                int index = configLang.indexOf(':');
                String key=configLang.substring(0,index);
                String lang=configLang.substring(index+1);
                WAIT_FOR_INTI.get(key).component=Component.translatableWithFallback(key,lang);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        CommandException.intiExceptions();
    }


    @SubscribeEvent
    static void commonSetup(FMLCommonSetupEvent event){

    }


}
