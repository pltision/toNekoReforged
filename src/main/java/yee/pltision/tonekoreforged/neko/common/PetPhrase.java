package yee.pltision.tonekoreforged.neko.common;

import yee.pltision.tonekoreforged.config.Config;
import yee.pltision.tonekoreforged.neko.object.NekoStateObject;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class PetPhrase implements Cloneable {

    //有可能需要将它和后缀一起合并到PetPhrase里面
    public static Set<Character> IGNORE_CHARACTERS;

    public static Map<Character,BracketPair> OPEN_BRACKETS;
    public static Map<Character,BracketPair> CLOSE_BRACKETS;

    public static List<BracketPair> BRACKETS=Arrays.asList(
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
            new BracketPair("\"“","\"”").random());

    public static void initStatics(String ignoreCharacters,List<BracketPair> brackets){
        IGNORE_CHARACTERS=PetPhrase.stringToCharacterHashSet(ignoreCharacters);

        OPEN_BRACKETS=new HashMap<>();
        CLOSE_BRACKETS=new HashMap<>();
        for(BracketPair pair:brackets){
            for(Character c:pair.open)
                OPEN_BRACKETS.put(c,pair);
            for(Character c:pair.close)
                CLOSE_BRACKETS.put(c,pair);
//            IGNORE_CHARACTERS.addAll(pair.open);
//            IGNORE_CHARACTERS.addAll(pair.close);
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
//        int insertPhraseIndex= getLastIndexOfNotIgnoreCharacter(text,filterLastBracket(text))+1;//获取口癖的插入位置
        int insertPhraseIndex= filterLastBracket(text);//获取口癖的插入位置

        if(insertPhraseIndex==0) return text;

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
        class SearchNode{
            final BracketPair bracket;
            final int index;
//            boolean notEmpty;
            SearchNode(BracketPair bracket,int index) {
                this.bracket=bracket;
                this.index = index;
            }
        }

        int index=text.length();

        BracketPair bracket;
        boolean allBracketsCanBreak=true;    //用于检验是否要破坏括号的flag，如果可破坏括号在不可破坏括号内就不破坏
        Stack<SearchNode> stack=new Stack<>();
        while (true){
            index--;
            if(index<0) break;

            bracket=CLOSE_BRACKETS.get(text.charAt(index));
            if (bracket == null)
            {
                if(!stack.isEmpty() && stack.peek().bracket.open.contains(text.charAt(index))) { //结束一个括号
                    stack.pop();
                    if(stack.isEmpty()) allBracketsCanBreak=true; //复位，不知道放在这是不是最优解喵
                }
                //等一下如果stack为空那么allBracketsCanBreak就一定为true，所以给它放这不就好了
                if(allBracketsCanBreak) {
                    if ( !(IGNORE_CHARACTERS.contains(text.charAt(index))||OPEN_BRACKETS.containsKey(text.charAt(index))) )
                        return index + 1;    //放在括号里边
                }
            }
            else {
                stack.push(new SearchNode(bracket, index));
                allBracketsCanBreak&=bracket.canBreakContent();
            }
        }
        return stack.isEmpty()?0:stack.peek().index;
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
        } while (check >= 0 && IGNORE_CHARACTERS.contains(text.charAt(check)));
        return check;
    }
    /*public static int filterIgnoreCharacterAndBracket(String text,int end){
        boolean ignoreOpen=text.length()==end;
        int check=end;
        do {
            check--;
        } while (check >= 0 && (
                    IGNORE_CHARACTERS.contains(text.charAt(check))
                ||  (ignoreOpen&&OPEN_BRACKETS.containsKey(text.charAt(check)))
                ||  CLOSE_BRACKETS.containsKey(text.charAt(check))
        ) );
        return check;
    }*/

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

    @Deprecated
    @SuppressWarnings("all")
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        initStatics(",.!?~-，。？！、—～…",BRACKETS);
        PetPhrase petPhrase= new PetPhrase("喵~",false,0);
        while (true){
            String text= scanner.nextLine();
            if(text!=null) System.out.println(petPhrase.addPhrase(text));
        }
    }
}

