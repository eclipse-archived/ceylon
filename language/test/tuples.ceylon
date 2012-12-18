void tuples() {
    [Integer,String,Singleton<Character>] returnTuple(Integer a, String b, Singleton<Character> c) {
        return [a,b,c];
    }
    value t = returnTuple(1, "2", Singleton(`3`));
    alias Triplet => [Integer,String,Singleton<Character>];
    Triplet? subt = t;
    check(subt is Triplet, "tuples");
    check(t.lastIndex == 2, "tuple lastIndex");
    Integer v1 = t[0];
    String v2 = t[1];
    Singleton<Object> v3 = t[2];
    Integer t1 = t.first;
    check(t1 == 1, "tuples first");
    String t2 = t.rest.first;
    check(t2 == "2", "tuple rest.first");
    Singleton<Character> t3 = t.rest.rest.first;
    check(t3.first == `3`, "tuple rest.rest.first");
    check(t.reversed.sequence == {Singleton(`3`),"2",1}, "tuple reversed " t.reversed.sequence "...");
    check(t[-2..-1] == {}, "tuple[-2..-1] " t[-2..-1] "...");
    check(t[-2..0] == {1}, "tuple[-2..0] " t[-2..0] "...");
    check(t[0..1] == {1,"2"}, "tuple[0..1] " t[0..1] "...");
    check(t[1..2] == {"2",{`3`}}, "tuple[1..2] " t[1..2] "...");
    check(t[2..2] == {{`3`}}, "tuple[2..2] " t[2..2] "...");
    check(t[2..3] == {{`3`}}, "tuple[2..3] " t[2..3] "...");
    check(t[2..1] == {{`3`},"2"}, "tuple[2..1] " t[2..1] "...");
    check(t[1..0] == {"2",1}, "tuple[1..0] " t[1..0] "...");
    check(t[-2..6] == {1,"2",{`3`}}, "tuple[-2..6] " t[-2..6] "...");
    check(t[5..6] == {}, "tuple[5..6] " t[5..6] "...");
    
    check(t[0:2] == {1,"2"}, "tuple[0:2] " t[0:2] "...");
    check(t[1:2] == {"2",{`3`}}, "tuple[1:2] " t[1:2] "...");
    check(t[2:1] == {{`3`}}, "tuple[2:1] " t[2:1] "...");
    
    check(t[-1...] == {1,"2",{`3`}}, "tuple[-1...] " t[-1...] "...");
    check(t[0...] == {1,"2",{`3`}}, "tuple[0...] " t[0...] "...");
    check(t[1...] == {"2",{`3`}}, "tuple[1...] " t[1...] "...");
    check(t[2...] == {{`3`}}, "tuple[2...] " t[2...] "...");
    check(t[3...] == {}, "tuple[3...] " t[3...] "...");
    check(t[...-1] == {}, "tuple[...-1] " t[...-1] "...");
    check(t[...0] == {1}, "tuple[...0] " t[...0] "...");
    check(t[...1] == {1, "2"}, "tuple[...1] " t[...1] "...");
    check(t[...2] == {1, "2", {`3`}}, "tuple[...2] " t[...2] "...");
    check(t[...3] == {1, "2", {`3`}}, "tuple[...3] " t[...3] "...");
    //Check inherited methods work
    check(1 in t, "tuple contains");
    check(t.find((Object x) x is Integer) exists, "tuple find");
    check(t.any((Object x) x is String), "tuple any");
    check(t.every((Object x) true), "tuple every");
    check(t.count((Object x) x is String) == 1, "tuple count");
}
