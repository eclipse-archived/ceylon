import java.lang{JIterable=Iterable}
import java.util{ArrayList}

String[] javaIterableInSpreadArgumentFunc(Iterable<String> s) {
    return s.sequence();
}
Anything[] bug6912(Anything* args) {
    return args;
}
void javaIterableInSpreadArgument() {
    value al = ArrayList<String>();
    al.add("a");al.add("b");al.add("c");
    JIterable<String> it = al;
    
    // named args spread (as sequenced arg)
    assert(javaIterableInSpreadArgumentFunc{*it} == ["a", "b", "c"]);
    assert(javaIterableInSpreadArgumentFunc{"z", *it} == ["z", "a", "b", "c"]);
    
    // iterable instantiation
    assert({*it}.sequence() == ["a", "b", "c"]);
    assert({"z", *it}.sequence() == ["z", "a", "b", "c"]);
    
    // tuple instantiation
    assert([*it] == ["a", "b", "c"]);
    assert(["z", *it] == ["z", "a", "b", "c"]);
    
    assert(bug6912(*al)==["a","b","c"]);
}