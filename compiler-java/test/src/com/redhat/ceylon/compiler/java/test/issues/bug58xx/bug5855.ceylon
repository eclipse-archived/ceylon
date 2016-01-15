import java.lang { Long }
import ceylon.language.meta { type }

shared void bug5855(){
    for (c in Array { Long(1) } of {Anything*}) {
        assert(is Long c);
    }
    for (c in Array { 'x' } of {Character*}) {
        Character c2 = c;
    }
}