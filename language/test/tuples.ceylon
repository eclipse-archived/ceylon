void tuples() {
    <Integer,String,Singleton<Character>> returnTuple(Integer a, String b, Singleton<Character> c) {
        return (a,b,c);
    }
    value t = returnTuple(1, "2", Singleton(`3`));
    alias Triplet => <Integer,String,Singleton<Character>>;
    Triplet? subt = t;
    check(is Triplet subt, "tuples");
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
    check(t[0..1] == {1,"2"}, "tuple[0..1] " t[0..1] "...");
    check(t[1..2] == {"2",{`3`}}, "tuple[1..2] " t[1..2] "...");
    check(t[2..2] == {{`3`}}, "tuple[2..2] " t[2..2] "...");
    check(t[2..1] == {{`3`},"2"}, "tuple[2..1] " t[2..1] "...");
    check(t[1..0] == {"2",1}, "tuple[1..0] " t[1..0] "...");
    check(t[0:2] == {1,"2"}, "tuple[0:2] " t[0:2] "...");
    check(t[1:2] == {"2",{`3`}}, "tuple[1:2] " t[1:2] "...");
    check(t[2:1] == {{`3`}}, "tuple[2:1] " t[2:1] "...");
    //Check inherited methods work
    check(1 in t, "tuple contains");
    check(exists t.find((Object x) is Integer x), "tuple find");
    check(t.any((Object x) is String x), "tuple any");
    check(t.every((Object x) true), "tuple every");
    check(t.count((Object x) is String x) == 1, "tuple count");
}
