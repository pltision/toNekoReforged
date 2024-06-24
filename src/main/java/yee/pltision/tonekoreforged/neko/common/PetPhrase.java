package yee.pltision.tonekoreforged.neko.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PetPhrase implements Cloneable {

    //有可能需要将它和后缀一起合并到PetPhrase里面
    public static Set<Character> IGNORE_CHARACTER;

    public static final Map<Character,BracketPair> OPEN_BRACKETS,CLOSE_BRACKETS;
    static {
        BracketPair[] pairs=new BracketPair[]{
                new BracketPair("(（","）)").canBreak(),
                new BracketPair("[","]"),
                new BracketPair("【","】"),
                new BracketPair("{","}"),
                new BracketPair("《","》"),
                new BracketPair("〈","〉"),
                new BracketPair("『","』"),
                new BracketPair("「","」"),
                new BracketPair("⌈","⌉"),
                new BracketPair("⌊","⌋"),
                new BracketPair("〔","〕"),
                new BracketPair("⟨","⟩"),
                new BracketPair("\"“","\"”").random(),
        };

        OPEN_BRACKETS=new HashMap<>();
        CLOSE_BRACKETS=new HashMap<>();
        for(BracketPair pair:pairs){
            for(Character c:pair.open)
                OPEN_BRACKETS.put(c,pair);
            for(Character c:pair.close)
                CLOSE_BRACKETS.put(c,pair);
        }
    }

    public final String phrase;

    public final int ignoreCharacterStart,ignoreAfter;
    public final boolean ignoreEnglish;

    public static Set<Character> stringToCharacterHashSet(String str){
        HashSet<Character>characters= new HashSet<>();
        for (char c : str.toCharArray())
            characters.add(c);
        return  characters;
    }

    public PetPhrase(String phrase, boolean ignoreEnglish,int ignoreAfter)throws IllegalArgumentException{
        this.phrase=phrase;
        ignoreCharacterStart= getLastIndexOfNotIgnoreCharacter(phrase)+1;
        this.ignoreEnglish=ignoreEnglish;
        if(ignoreAfter>ignoreCharacterStart) throw new IllegalArgumentException("ignoreAfter>=phrase.length()-ignoreCharacterStart");
        this.ignoreAfter=ignoreAfter;
    }

    public String addPhrase(String text){
        if(ignoreEnglish&&isTextEnglish(text))return text;
        int insertPhraseIndex= getLastIndexOfNotIgnoreCharacter(text,filterLastBracket(text))+1;//获取口癖的插入位置

//        if(insertPhraseIndex==0&&IGNORE_CHARACTER.contains(text.charAt(insertPhraseIndex))/*只有一个字符时也是0哎*/) return text;   //TODO: 全是忽略字符时不添加口癖，我想想咋做

        if(insertPhraseIndex>=ignoreCharacterStart-ignoreAfter/*有效长度*/ &&
                text.substring(insertPhraseIndex-(ignoreCharacterStart-ignoreAfter/*有效长度*/),insertPhraseIndex)
                        .equals(phrase.substring(ignoreAfter,ignoreCharacterStart)) ) //如果text最后的有效部分与口癖的有效部分匹配 "ab".substring(2,2)合法
            return text;
//        System.out.println("\""+text.substring(insertPhraseIndex-(ignoreCharacterStart-ignoreAfter),insertPhraseIndex)+"\" \""+phrase.substring(ignoreAfter,ignoreCharacterStart)+"\"");

        if(insertPhraseIndex==text.length()) //如果没有忽略的
            return text.substring(0,insertPhraseIndex)+phrase;
        else return text.substring(0,insertPhraseIndex)+phrase.substring(0,ignoreCharacterStart)+text.substring(insertPhraseIndex);
    }

    public static int filterLastBracket(String text){
        int index=text.length()-1;
        BracketPair bracket=CLOSE_BRACKETS.get(text.charAt(index));
        if(bracket==null) return text.length();//不是括号直接返回
        if(bracket.canBreakContent())return index;//如果可以破坏括号内容直接返回
        index--;
        for(;index>=0;index--){
            if(bracket.open.contains(text.charAt(index)))return index;//找到开括号位置
        }
        return text.length();//没找到收括号就不管
    }

    /**
     * @return 最后一个不是忽略字符的字符的索引。可能返回-1
     */
    public static int getLastIndexOfNotIgnoreCharacter(String text){
        return getLastIndexOfNotIgnoreCharacter(text,text.length());
    }

    public static int getLastIndexOfNotIgnoreCharacter(String text,int end){
        int check=end;
        do {
            check--;
        } while (check >= 0 && IGNORE_CHARACTER.contains(text.charAt(check)));
        return check;
    }

    /**
     * @return 文本的所有字符是否都小于255
     */
    public static boolean isTextEnglish(String text){
        for(char c:text.toCharArray())
            if(c>>>8!=0) return false;
        return true;
    }


    @Override
    public String toString() {
        return "PetPhrase{" +
                "phrase='" + phrase + '\'' +
                ", ignoreCharacterStart=" + ignoreCharacterStart +
                ", ignoreAfter=" + ignoreAfter +
                ", ignoreEnglish=" + ignoreEnglish +
                '}';
    }

    @Override
    public PetPhrase clone() {
        try {
            return (PetPhrase) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

