package yee.pltision.tonekoreforged.neko.capability;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.*;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.common.NekoRecord;
import yee.pltision.tonekoreforged.neko.common.NekoState;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.neko.object.NekoRecordObject;

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

    public static CompoundTag nekoState(NekoState nekoState){
        CompoundTag tag=new CompoundTag();
        tag.put("petPhrase",petPhrase(nekoState.getPetPhrase()));

        Map<UUID, NekoRecord> ownerRecords=nekoState.getOwners();
        if(ownerRecords!=null){
            ListTag recordList=new ListTag();
            for(NekoRecord record:nekoState.getOwners().values()){
                recordList.add(nekoRecord(record));
            }
            tag.put("owners",recordList);
            tag.putBoolean("isNeko",true);
        }
        else{
            tag.putBoolean("isNeko",false);
        }

        //仅序列化单向图
        /*ListTag nekoList=new ListTag();
        for(UUID uuid:nekoState.getNekos()){
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

    public static NekoRecordObject nekoRecord(CompoundTag tag){
        return new NekoRecordObject(tag.getUUID("uuid"),tag.getInt("exp"));
    }

    public static NekoState nekoState(NekoState nekoState,CompoundTag tag){

        nekoState.setPetPhrase(petPhrase(tag.getCompound("petPhrase")));

        if(tag.getBoolean("isNeko")){
            try{
                nekoState.beNeko();
                for(Tag ownerTag:tag.getList("owners",10)){
                    try {
                        NekoRecordObject recordObject = nekoRecord((CompoundTag) ownerTag);
                        nekoState.computeNekoState(recordObject.uuid,(k,o)-> recordObject);
                    }
                    catch (Exception e){
                        ToNeko.LOGGER.error(e.toString());
                    }
                }
            }
            catch (Exception e){
                ToNeko.LOGGER.error(e.toString());
            }

        }
        else{
            nekoState.beNonneko();
        }

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
