package yee.pltision.tonekoreforged.neko.capability;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.*;
import yee.pltision.tonekoreforged.ToNeko;
import yee.pltision.tonekoreforged.neko.common.PetPhrase;
import yee.pltision.tonekoreforged.neko.object.NekoRecordObject;
import yee.pltision.tonekoreforged.neko.object.NekoStateObject;

import java.util.*;

public class StateSerializeUtil {
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

    public static CompoundTag nekoRecord(NekoRecordObject record){
        CompoundTag tag=new CompoundTag();
        tag.putUUID("uuid",record.uuid);
        tag.putFloat("exp",record.exp);
        return tag;
    }

    public static CompoundTag nekoState(NekoStateObject nekoState){
        CompoundTag tag=new CompoundTag();
        tag.put("petPhrase",petPhrase(nekoState.phrase));

        Set<NekoRecordObject> ownerRecords=nekoState.getOwners();
        if(ownerRecords!=null){
            ListTag recordList=new ListTag();
            for(NekoRecordObject record:nekoState.getOwners()){
                recordList.add(nekoRecord(record));
            }
            tag.put("owners",recordList);
            tag.putBoolean("isNeko",true);
        }
        else{
            tag.putBoolean("isNeko",false);
        }

        ListTag nekoList=new ListTag();
        for(UUID uuid:nekoState.getNekos()){
            nekoList.add(NbtUtils.createUUID(uuid));
        }
        tag.put("nekos",nekoList);

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

    public static void nekoState(NekoStateObject nekoStateObject,CompoundTag tag){

        nekoStateObject.setPetPhrase(petPhrase(tag.getCompound("petPhrase")));

        if(tag.getBoolean("isNeko")){
            try{
                nekoStateObject.beNeko();
                for(Tag ownerTag:tag.getList("owners",10)){
                    try {
                        NekoRecordObject recordObject = nekoRecord((CompoundTag) ownerTag);
                        nekoStateObject.ownerMap.put(recordObject.uuid, recordObject);
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
            nekoStateObject.beNotNeko();
        }

        try{
            for(Tag nekoTag:tag.getList("nekos",11)){
                nekoStateObject.addNeko(UUIDUtil.uuidFromIntArray(((IntArrayTag)nekoTag).getAsIntArray()));
            }
        }
        catch (Exception e){
            ToNeko.LOGGER.error(e.toString());
        }
    }
}
