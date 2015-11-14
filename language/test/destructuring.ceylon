void tupleVar([Integer, Float, String] tuple) {
    value [i1, f1, s1] = tuple;
    check(i1 == 0, "destr tuple 1");
    check(f1 == 1.0, "destr tuple 2");
    check(s1 == "foo", "destr tuple 3");
    value [i2, Float f2, s2] = tuple;
    check(i2 == 0, "destr tuple 4");
    check(f2 == 1.0, "destr tuple 5");
    check(s2 == "foo", "destr tuple 6");
    value [Integer i3, Float f3, String s3] = tuple;
    check(i3 == 0, "destr tuple 7");
    check(f3 == 1.0, "destr tuple 8");
    check(s3 == "foo", "destr tuple 9");
}

void tupleLiteral() {
    value [i1, f1, s1] = [0, 1.0, "foo"];
    check(i1 == 0, "destr tuple lit 1");
    check(f1 == 1.0, "destr tuple lit 2");
    check(s1 == "foo", "destr tuple lit 3");
    value [i2, Float f2, s2] = [0, 1.0, "foo"];
    check(i2 == 0, "destr tuple lit 4");
    check(f2 == 1.0, "destr tuple lit 5");
    check(s2 == "foo", "destr tuple lit 6");
    value [Integer i3, Float f3, String s3] = [0, 1.0, "foo"];
    check(i3 == 0, "destr tuple lit 7");
    check(f3 == 1.0, "destr tuple lit 8");
    check(s3 == "foo", "destr tuple lit 9");
}

void tupleGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value [f1] = [FooSub<Integer>()];
    value [FooSub<Integer> f2] = [FooSub<Integer>()];
    value [Foo<Integer> f3] = [FooSub<Integer>()];
    check(f3 is FooSub<Integer>, "destr tuple gens");
}

void entryVar(Integer->String entry) {
    value i1->s1 = entry;
    check(i1 == 0, "destr entry 1");
    check(s1 == "foo", "destr entry 2");
    value Integer i2->s2 = entry;
    check(i2 == 0, "destr entry 3");
    check(s2 == "foo", "destr entry 4");
    value Integer i3->String s3 = entry;
    check(i3 == 0, "destr entry 5");
    check(s3 == "foo", "destr entry 6");
}

void entryLiteral() {
    value i1->s1 = 0->"foo";
    check(i1 == 0, "destr entry lit 1");
    check(s1 == "foo", "destr entry lit 2");
    value Integer i2->s2 = 0->"foo";
    check(i2 == 0, "destr entry lit 3");
    check(s2 == "foo", "destr entry lit 4");
    value Integer i3->String s3 = 0->"foo";
    check(i3 == 0, "destr entry lit 5");
    check(s3 == "foo", "destr entry lit 6");
}

void entryGeneric() {
    class Foo<T>() {}
    class FooSub<T>() extends Foo<T>() {}
    value i1->f1 = 0->FooSub<Integer>();
    value Integer i2->FooSub<Integer> f2 = 0->FooSub<Integer>();
    value Integer i3->Foo<Integer> f3 = 0->FooSub<Integer>();
    check(f3 is FooSub<Integer>, "destr entry gens");
}

void destructuringLet([String, Float, Integer] tuple, String->Object entry) {
    value x1 = let ([s, f, i]=tuple) s.size + f*i;
    value y2 = let ([String s, Float f, Integer i]=tuple) s.size + f*i;
    value e1 = let (k->v=entry) k+v.string;
    value f2 = let (String k->Object v=entry) k+v.string;
    check(x1==tuple[0].size+tuple[1]*tuple[2], "destr let 1");
    check(y2==x1, "destr let 2");
    check(e1==entry.key+entry.item.string, "destr let 3");
    check(f2==e1, "destr let 4");
}

void variadicDestructuring([String, String, String*] strings, 
        [Integer, Float, String] tup, 
        [Float+] floats) {
    value [x, y, *rest] = strings;
    value [i, *pair] = tup;
    value [Float ff, String ss] = pair;
    value [z, *zs] = floats;
    check(x==strings[0], "destr variadic 1");
    check(y==strings[1], "destr variadic 2");
    check(rest==strings.rest.rest, "destr variadic 3");
    check(i==tup[0], "destr variadic 4");
    check(pair==tup.rest, "destr variadic 5");
    check(ff==tup[1], "destr variadic 6");
    check(ss==tup[2], "destr variadic 7");
    check(z==floats[0], "destr variadic 8");
    check(zs==floats.rest, "destr variadic 9");
}

void destructureTupleInEntry(String->[Float,Float] entry) {
    value s->[x, y] = entry;
    check(s==entry.key, "tuple in entry 1");
    check(x==entry.item[0], "tuple in entry 2");
    check(y==entry.item[1], "tuple in entry 3");
    value z = let (s_->[x_, y_] = entry) x_*y_;
    check(z == entry.item[0]*entry.item[1], "tuple in entry 4");
}

void destructureNestedTuple([String,[Integer,Float],String->String] tuple) {
    value [s, [i, f], k -> v] = tuple;
    check(s==tuple[0], "nested tuple 1");
    check(i==tuple[1][0], "nested tuple 2");
    check(f==tuple[1][1], "nested tuple 3");
    check(k==tuple[2].key, "nested tuple 4");
    check(v==tuple[2].item, "nested tuple 5");
    value x = let ([s_, [i_,f_], k_ -> v_] = tuple) k_+v_;
    check(x == k+v, "nested tuple 6");
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
    if (!is Finished iter0=iter.iterator().next(),
        !is Finished xs0=xs.iterator().next()) {
        check(xs0[0]==iter0[2], "destr comps 1");
        check(xs0[1]==iter0[1], "destr comps 2");
        check(xs0[2]==iter0[0], "destr comps 3");
    } else {
        fail("destr comps 1");
    }
    value ys = { for ([String x, Float[] y, String s1 -> String s2] in iter) [s1->s2, y, x] };
    if (!is Finished iter0=iter.iterator().next(),
        !is Finished ys0=ys.iterator().next()) {
        check(ys0[0]==iter0[2], "destr comps 4");
        check(ys0[1]==iter0[1], "destr comps 5");
        check(ys0[2]==iter0[0], "destr comps 6");
    } else {
        fail("destr comps 4");
    }
    value xys = { for ([x1, y1, sk1->sv1] in iter) for ([x2, y2, sk2->sv2] in iter) [x1, y2, sk1->sv2] };
    if (!is Finished iter0=iter.iterator().next(),
        !is Finished xys0=xys.iterator().next()) {
        check(iter0==xys0, "destr comps 7");
    } else {
        fail("destr comps 7");
    }
    value xsif1 = { for (tup in iter2) if (exists [x, y, s1->s2]=tup) [s1->s2, y, x] };
    value xsif2 = { for ([x, y, s1->s2] in iter) if (nonempty [y1, *restys] = y) [s1->s2, y1, x] };
    value xsif3 = { for (tup in iter2) if (exists [x, y, s1->s2]=tup, nonempty [y1, *restys] = y) [s1->s2, y1, x] };
}

void destructureIf([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    if (exists [x, i] = maybePair) {
        Float c = x;
        Integer j = i;
        check(c==1.0, "destr if 1");
        check(j==2, "destr if 2");
    }
    if (nonempty [name, *rest] = names) {
        String n = name;
        String[] ns = rest;
        check(n=="Tako", "destr if 3");
        check(ns==["Enrique"], "destr if 4");
    }
    if (exists k->v = maybeEntry) {
        String key = k;
        Object item = v;
        check(key=="K", "destr if 5");
        check(item is Singleton<String>, "destr if 6");
    }
    if (exists [x, i] = maybePair, nonempty [name, *rest] = names) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
        check(c==1.0, "destr if 1");
        check(j==2, "destr if 2");
        check(n=="Tako", "destr if 3");
        check(ns==["Enrique"], "destr if 4");
    }
    if (exists [x, i] = maybePair, nonempty [name, *rest] = names) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
    } else {
        // Do nothing, just need an else block
    }
}

void destructureAssert([Float, Integer]? maybePair, String[] names, <String->Object>? maybeEntry) {
    assert (exists [x, i] = maybePair);
    Float c = x;
    Integer j = i;
    check(c==1.0, "destr assert 1");
    check(j==2, "destr assert 2");
    assert (exists k->v = maybeEntry);
    String key = k;
    Object item = v;
    check(key=="K", "destr assert 3");
    check(item is Singleton<String>, "destr assert 4");
    assert (nonempty [name, *rest] = names);
    String n = name;
    String[] ns = rest;
    check(n=="Tako", "destr assert 5");
    check(ns==["Enrique"], "destr assert 6");
    assert (exists [x2, i2] = maybePair, nonempty [name2, *rest2] = names);
    Float c2 = x2;
    Integer j2 = i2;
    String n2 = name2;
    String[] ns2 = rest2;
    check(c2==1.0 && j2==2, "destr assert 7");
    check(n2=="Tako" && ns2==["Enrique"], "destr assert 8");
}

void destructureWhile() {
    variable [Float, Integer]? maybePair = [1.0, 2];
    while (exists [x, i] = maybePair) {
        Float c = x;
        Integer j = i;
        check(c==1.0, "destr while 1");
        check(j==2, "destr while 2");
        maybePair = null;
    }
    variable <String->Object>? maybeEntry = "K"->Singleton("V");
    while (exists k->v = maybeEntry) {
        String key = k;
        Object item = v;
        check(key=="K", "destr while 3");
        check(item is Singleton<String>, "destr while 4");
        maybeEntry = null;
    }
    variable String[] names = ["Tako", "Enrique"];
    while (nonempty [name, *rest] = names) {
        String n = name;
        String[] ns = rest;
        check(n=="Tako", "destr while 5");
        check(ns==["Enrique"], "destr while 6");
        names = [];
    }
    variable [Float, Integer]? maybePair2 = [1.0, 2];
    variable String[] names2 = ["Tako", "Enrique"];
    if (exists [x, i] = maybePair2, nonempty [name, *rest] = names2) {
        Float c = x;
        Integer j = i;
        String n = name;
        String[] ns = rest;
        check(c==1.0, "destr while 7");
        check(j==2, "destr while 8");
        check(n=="Tako", "destr while 9");
        check(ns==["Enrique"], "destr while 10");
        maybePair = null;
    }
}

void simpleDestructuring() {
    value t1=[1, "2", 3.0];
    value [a,b,c]=t1;
    check(a==1, "simple destr 0.1");
    check(b=="2", "simple destr 0.2");
    check(c==3.0, "simple destr 0.3");
    value d->e = "K"->1;
    check(d=="K", "simple destr 0.4");
    check(e==1, "simple destr 0.5");
    value [f,g,[h,i,j]]=[9,10,t1];
    check(f==9, "simple destr 0.6");
    check(g==10, "simple destr 0.7");
    check(h==a, "simple destr 0.8");
    check(i==b, "simple destr 0.9");
    check(j==c, "simple destr 0.10");
    value [m,n,o->p] = [c,d,e->f];
    check(m==c, "simple destr 0.11");
    check(n==d, "simple destr 0.12");
    check(o==e, "simple destr 0.13");
    check(p==f, "simple destr 0.14");
    value [q,*r] = t1;
    check(q==a, "simple destr 0.15");
    check(r==["2",3.0], "simple destr 0.16");
    value t2=[[1,2],[3,4],[5,6]];
    value t3=[1->2,3->4,5->6];
    for ([s,t] in t2) {
        check(s%2==1 && t%2==0, "simple destructuring 1");
    }
    for (u->v in t3) {
        check(u%2==1 && v%2==0, "simple destructuring 2");
    }
    check([ for ([w,x] in t2) w+x ] == [3,7,11], "simple destructuring 3");
    check([ for ([w,x] in t2) if (w%2==1) w+x ] == [3,7,11], "simple destructuring 4");
    check((let (y->z="K"->1, aa=y.lowercased,ab=z*5) [aa,ab]) == ["k",5], "simple destructuring 5");
    Entry<Integer,String>? ac = 1->"2";
    if (exists ad->ae = ac) {
        check(ad==1 && ae=="2", "simple destructuring 6");
    }
    [Integer,String]? af = [1, "2"];
    if (exists [ag,ah] = af) {
        check(ag==1 && ah=="2", "simple destructuring 7");
    }
    [String*] ai = ["1","2","3"];
    if (nonempty [aj, *ak] = ai) {
        check(aj=="1", "simple destructuring 8");
        check(ak==["2","3"], "simple destructuring 9");
    }
}

@test
shared void testDestructuring() {
    tupleVar([0, 1.0, "foo"]);
    tupleLiteral();
    tupleGeneric();
    entryVar(0->"foo");
    entryLiteral();
    entryGeneric();
    destructureNestedTuple(["1",[2,3.0],"4"->"5"]);
    destructureTupleInEntry("1"->[2.0,3.0]);
    destructuringLet(["1",2.0,3], "4"->Singleton("5"));
    variadicDestructuring(["a","b","c","d","e"], [1,2.0,"3"],[1.0,2.0,3.0]);
    destructureInForComprehensions({["1",[2.0,3.0,4.0],"5"->"6"]}, {["1",[2.0,3.0,4.0],"5"->"6"],null});
    destructureInFor{["1",2.0,"3"->"4"],["5",6.0,"7"->"8"]};
    destructureIf([1.0,2], ["Tako","Enrique"], "K"->Singleton("V"));
    destructureAssert([1.0,2], ["Tako","Enrique"], "K"->Singleton("V"));
    destructureWhile();
    simpleDestructuring();
    //js479
    class Bug479(){
        value a=[function()=>1,void(){}];
        value [b,c]=a;
        check(b()==1, "JS#479");
    }
    Bug479();
}
