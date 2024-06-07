package yee.pltision.tonekoreforged.neko.capability;

import com.google.common.base.Function;
import net.minecraft.nbt.*;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.neko.object.NekoRecordObject;
import yee.pltision.tonekoreforged.neko.object.NekoStateObject;

import java.util.*;

public class SerializeUtil {
    public static CompoundTag petPhrase(PetPhrase phrase){
        CompoundTag tag=new CompoundTag();
        if(phrase==null){
            tag.putBoolean("hasPhrase",false);
        }
        else{
            tag.putString("phrase",phrase.phrase);
            tag.putInt("ignoreAfter",phrase.ignoreAfter);
            tag.putBoolean("ignoreEnglish",phrase.ignoreEnglish);
            tag.putBoolean("hasPhrase",true);
        }
        return tag;
    }

    public static CompoundTag nekoRecord(NekoRecord record){
        CompoundTag tag=new CompoundTag();
        tag.putUUID("uuid",record.getUUID());
        tag.putFloat("exp",record.getExp());
        return tag;
    }

    public static CompoundTag nekoState(NekoState state){
        CompoundTag tag=new CompoundTag();
        tag.put("petPhrase",petPhrase(state.getPetPhrase()));

        Map<UUID, NekoRecord> ownerRecords=state.getNekos();
        ListTag recordList=new ListTag();
        for(NekoRecord record:ownerRecords.values()){
            recordList.add(nekoRecord(record));
        }
        tag.put("nekos",recordList);
//        tag.putBoolean("isNeko",true);

        //仅序列化单向图
        /*ListTag nekoList=new ListTag();
        for(UUID uuid:state.getNekos()){
            nekoList.add(NbtUtils.createUUID(uuid));
        }
        tag.put("nekos",nekoList);*/

        return tag;
    }

    public static PetPhrase petPhrase(CompoundTag tag){
        if(tag.getBoolean("hasPhrase"))
            return new PetPhrase(tag.getString("phrase"),tag.getBoolean("ignoreEnglish"),tag.getInt("ignoreAfter"));
        return null;
    }

    public static NekoRecordObject nekoRecord(CompoundTag tag, Function<UUID, NekoState> stateGetter){
        UUID uuid= tag.getUUID("uuid");
        return new NekoRecordObject(uuid,stateGetter.apply(uuid),tag.getInt("exp"));
    }

    public static NekoState nekoState(NekoState nekoState,CompoundTag tag, Function<UUID, NekoState> stateGetter){

        for(Tag ownerTag:tag.getList("nekos",10)){
            try {
                NekoRecordObject recordObject = nekoRecord((CompoundTag) ownerTag,stateGetter);
                nekoState.computeNekoState(recordObject.uuid,(k, o)-> recordObject);
            }
            catch (Exception e){
                ToNeko.LOGGER.error(e.toString());
            }
        }
        nekoState.setPetPhrase(petPhrase(tag.getCompound("petPhrase")));

        //仅序列化单向图
        /*try{
            for(Tag nekoTag:tag.getList("nekos",11)){
                nekoState.addNeko(UUIDUtil.uuidFromIntArray(((IntArrayTag)nekoTag).getAsIntArray()));
            }
        }
        catch (Exception e){
            ToNeko.LOGGER.error(e.toString());
        }*/

        return nekoState;
    }
}
