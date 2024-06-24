package yee.pltision.tonekoreforged.neko.common;

import java.util.Set;

public class BracketPair{
    public final Set<Character> open,close;
    boolean random=false;
    boolean canBreak=false;

    public boolean canRandomUse() {
        return random;
    }
    public boolean canBreakContent(){
        return canBreak;
    }

    BracketPair(Set<Character> open, Set<Character> close) {
        this.open = open;
        this.close = close;
    }
    public BracketPair(String open, String close){
        this.open=stringToCharacterSet(open);
        this.close=stringToCharacterSet(close);
    }

    public static Set<Character> stringToCharacterSet(String str){
        return switch (str.length()) {
//            case 0 -> Set.of();
            case 1 -> Set.of(str.charAt(0));
            case 2 -> Set.of(str.charAt(0), str.charAt(1));
            default -> PetPhrase.stringToCharacterHashSet(str);
        };
    }

    public BracketPair random(){
        this.random=true;
        return this;
    }

    public BracketPair canBreak(){
        this.canBreak=true;
        return this;
    }
}
