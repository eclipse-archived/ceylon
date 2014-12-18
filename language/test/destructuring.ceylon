void tupleVar([Integer, Float, String] tuple) {
    value [i1, f1, s1] = tuple;
    assert(i1 == 0);
    assert(f1 == 1.0);
    assert(s1 == "foo");
    value [i2, Float f2, s2] = tuple;
    assert(i2 == 0);
    assert(f2 == 1.0);
    assert(s2 == "foo");
    value [Integer i3, Float f3, String s3] = tuple;
    assert(i3 == 0);
    assert(f3 == 1.0);
    assert(s3 == "foo");
}

void tupleLiteral() {
    value [i1, f1, s1] = [0, 1.0, "foo"];
    assert(i1 == 0);
    assert(f1 == 1.0);
    assert(s1 == "foo");
    value [i2, Float f2, s2] = [0, 1.0, "foo"];
    assert(i2 == 0);
    assert(f2 == 1.0);
    assert(s2 == "foo");
    value [Integer i3, Float f3, String s3] = [0, 1.0, "foo"];
    assert(i3 == 0);
    assert(f3 == 1.0);
    assert(s3 == "foo");
}

void tupleGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value [f1] = [FooSub<Integer>()];
    value [FooSub<Integer> f2] = [FooSub<Integer>()];
    value [Foo<Integer> f3] = [FooSub<Integer>()];
}

void entryVar(Integer->String entry) {
    value i1->s1 = entry;
    assert(i1 == 0);
    assert(s1 == "foo");
    value Integer i2->s2 = entry;
    assert(i2 == 0);
    assert(s2 == "foo");
    value Integer i3->String s3 = entry;
    assert(i3 == 0);
    assert(s3 == "foo");
}

void entryLiteral() {
    value i1->s1 = 0->"foo";
    assert(i1 == 0);
    assert(s1 == "foo");
    value Integer i2->s2 = 0->"foo";
    assert(i2 == 0);
    assert(s2 == "foo");
    value Integer i3->String s3 = 0->"foo";
    assert(i3 == 0);
    assert(s3 == "foo");
}

void entryGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value i1->f1 = 0->FooSub<Integer>();
    value Integer i2->FooSub<Integer> f2 = 0->FooSub<Integer>();
    value Integer i3->Foo<Integer> f3 = 0->FooSub<Integer>();
}

void destructuringLet([String, Float, Integer] tuple, String->Object entry) {
    value x1 = let ([s, f, i]=tuple) s.size + f*i;
    value y2 = let ([String s, Float f, Integer i]=tuple) s.size + f*i;
    value e1 = let (k->v=entry) k+v.string;
    value f2 = let (String k->Object v=entry) k+v.string;
}

void variadicDestructuring([String, String, String*] strings, 
        [Integer, Float, String] tup, 
        [Float+] floats) {
    value [x, y, *rest] = strings;
    value [i, *pair] = tup;
    value [Float ff, String ss] = pair;
    value [z, *zs] = floats;
    
}

void destructureTupleInEntry(String->[Float,Float] entry) {
    value s->[x, y] = entry;
    value z = let (s_->[x_, y_] = entry) x_*y_;
}

void destructureNestedTuple([String,[Integer,Float],String->String] tuple) {
    value [s, [i, f], k -> v] = tuple;
    value x = let ([s_, [i_,f_], k_ -> v_] = tuple) k_+v_;
}

void destructureInFor({[String, Float, String->String]*} iter) {
    for ([x, y, s1->s2] in iter) {
        String s = x;
        Float f = y;
        String->String e = s1->s2;
    }
    for ([String x, Float y, String s1 -> String s2] in iter) {
        String s = x;
        Float f = y;
        String->String e = s1->s2;
    }
}

void destructureInForComprehensions({[String, Float[], String->String]*} iter, {[String, Float[], String->String]?*} iter2) {
    value xs = { for ([x, y, s1->s2] in iter) [s1->s2, y, x] };
    value ys = { for ([String x, Float[] y, String s1 -> String s2] in iter) [s1->s2, y, x] };
    value xys = { for ([x1, y1, sk1->sv1] in iter) for ([x2, y2, sk2->sv2] in iter) [x1, y2, sk1->sv2] };
    // FIXME Still fails in the JVM backend
    //value xsif1 = { for (tup in iter2) if (exists [x, y, s1->s2]=tup) [s1->s2, y, x] };
    //value xsif2 = { for ([x, y, s1->s2] in iter) if (nonempty [y1, *restys] = y) [s1->s2, y1, x] };
    //value xsif3 = { for (tup in iter2) if (exists [x, y, s1->s2]=tup, nonempty [y1, *restys] = y) [s1->s2, y1, x] };
}

void destructureIf([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    if (exists [x, i] = maybePair) {
        Float c = x;
        Integer j = i;
    }
    if (nonempty [name, *rest] = names) {
        String n = name;
        String[] ns = rest;
    }
    if (exists k->v = maybeEntry) {
        String key = k;
        Object item = v;
    }
    if (exists [x, i] = maybePair, nonempty [name, *rest] = names) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
    }
    // FIXME Still fails in the JVM backend
    //if (exists [x, i] = maybePair, nonempty [name, *rest] = names) {
    //    Float c = x;
    //    Integer j = i;
    //    String n = name;
    //    String[] ns = rest;
    //} else {
    //    // Do nothing, just need an else block
    //}
}

void destructureAssert([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    //assert (exists [x, i] = maybePair);
    //Float c = x;
    //Integer j = i;
    //assert (exists k->v = maybeEntry);
    //String key = k;
    //Object item = v;
    //assert (nonempty [name, *rest] = names);
    //String n = name;
    //String[] ns = rest;
    //assert (exists [x2, i2] = maybePair, nonempty [name2, *rest2] = names);
    //Float c2 = x2;
    //Integer j2 = i2;
    //String n2 = name2;
    //String[] ns2 = rest2;
}

@test
shared void testDestructuring() {
    tupleVar([0, 1.0, "foo"]);
    tupleLiteral();
    tupleGeneric();
    entryVar(0->"foo");
    entryLiteral();
    entryGeneric();
    destructuringLet(["", 1.0, 2], ""->2);
    variadicDestructuring(["", "", "", ""], [1, 1.0, ""], [1.0, 2.0, 3.0]);
    destructureTupleInEntry(""->[1.0, 2.0]);
    destructureNestedTuple(["", [1, 2.0], ""->""]);
    destructureInFor({["", 2.0, ""->""], ["", 2.0, ""->""]});
    destructureInForComprehensions({["", [1.0, 2.0], ""->""], ["", [1.0, 2.0], ""->""]}, {["", [1.0, 2.0], ""->""], ["", [1.0, 2.0], ""->""], null});
    destructureIf([1.0, 2], ["", ""], ""->2);
    destructureAssert([1.0, 2], ["", ""], ""->2);
}
