void test_singleton() {
    String[] singleton = Singleton("hello");
    check(singleton.size==1, "singleton size");
    check(!singleton.empty, "singleton empty");
    check(singleton.defines(0), "singleton defines");
    check(!singleton.defines(1), "singleton defines");
    check(singleton.string=="[hello]", "singleton string");
    check(singleton.reversed==singleton, "singleton reversed");
    check(singleton nonempty, "singleton nonempty");
    if (nonempty singleton) {
        check(singleton.first=="hello", "singleton first");
        check(singleton.lastIndex==0, "sequence last index");
        //check(!singleton.rest nonempty, "singleton rest empty");
        /*if (nonempty rest = singleton.rest) {
            fail("singleton rest empty");
        }*/
        for (element in singleton.reversed) {
        }
        Sequence<String> s = singleton.sequence;
    }
    else {
        fail("singleton nonempty");
    }
    variable value j=0;
    for (x in singleton) {
        check(x=="hello", "singleton iteration");
        j=j+1;
    }
    check(j==1, "singleton iteration");
    if (exists str=singleton[0]) {
        check(str=="hello", "singleton item");
    }
    else {
        fail("singleton item");
    }
    if (exists str=singleton.get(0)) {
        check(str=="hello", "singleton item");
    }
    else {
        fail("singleton item");
    }
    if (exists str=singleton[1]) {
        fail("singleton item");
    }
    if (exists str=singleton.get(1)) {
        fail("singleton item");
    }

    check(singleton.span(0, 1) nonempty, "nonempty singleton span(0,1)");
    check(singleton.span(0, 0) nonempty, "nonempty singleton span(0,0)");
    check(singleton.spanFrom(0) nonempty, "nonempty singleton spanFrom(0)");
    check(!singleton.spanFrom(1) nonempty, "nonempty singleton spanFrom(1)");
    check(singleton.spanTo(0) nonempty, "nonempty singleton spanTo(0)");
    check(singleton.spanTo(1) nonempty, "nonempty singleton spanTo(1)");
    check(singleton.segment(0, 1) nonempty, "nonempty singleton segment(0,1)");
    check(singleton.span(0, 3).string=="[hello]", "singleton span(0,3).string");
    check(singleton.segment(0, 3).string=="[hello]", "singleton segment(0,3).string");
    check(!singleton.span(1, 1) nonempty, "!nonempty singleton span(1,1)");
    check(!singleton.spanFrom(1) nonempty, "!nonempty singleton spanFrom(1)");
    check(!singleton.segment(1, 1) nonempty, "!nonempty singleton segment(1,1)");
    check(singleton.span(0, 0) nonempty, "nonempty singleton span(0,0)");
    check(singleton.span(0, 10) nonempty, "nonempty singleton span(0,10)");
    check(!singleton.segment(0, 0) nonempty, "!nonempty singleton segment(0,0)");
    check(!singleton.segment(0, -1) nonempty, "!nonempty singleton segment(0,-1)");

    check(singleton[0...] nonempty, "nonempty singleton[0...]");
    check(!singleton[1...] nonempty, "nonempty singleton[1...]");

    check(singleton.keys.contains(0), "singleton keys.contains(0)");
    check(!singleton.keys.contains(1), "!singleton keys.contains(1)");
    check(!singleton.keys.contains(2), "!singleton keys.contains(2)");
    
    // Disabled: does not pass typechecker on M3.1
    //value ss = Singleton("Trompon").span(0, 0);
    //switch(ss)
    //case (is Empty) {}
    //case (is Singleton<String>) {
    //    String first = ss.first;
    //}
}

void test_nullsingleton() {
    Anything[] singleton = Singleton(null);
    check(singleton.size==1, "nullsingleton size");
    check(!singleton.empty, "nullsingleton empty");
    check(singleton.defines(0), "nullsingleton defines");
    check(!singleton.defines(1), "nullsingleton defines");
    check(singleton.string=="[null]", "nullsingleton string");
    // Because Singleton(null) != Singleton(null) since null hasn't equals() 
    //check(singleton.reversed==singleton, "nullsingleton reversed");
    check(singleton nonempty, "nullsingleton nonempty");
    if (nonempty singleton) {
        check(!(singleton.first exists), "nullsingleton first");
        check(singleton.lastIndex==0, "sequence last index");
        //check(!singleton.rest nonempty, "singleton rest empty");
        /*if (nonempty rest = singleton.rest) {
            fail("singleton rest empty");
        }*/
        for (element in singleton.reversed) {
        }
        Sequence<Anything> s = singleton.sequence;
    }
    else {
        fail("singleton nonempty");
    }
    variable value j=0;
    for (x in singleton) {
        check(!(x exists), "nullsingleton iteration");
        j=j+1;
    }
    check(j==1, "nullsingleton iteration");
    if (exists str=singleton[0]) {
        fail("nullsingleton item");
    }
    
    if (exists str=singleton.get(0)) {
        fail("nullsingleton item");
    }
    else {
    }
    if (exists str=singleton[1]) {
        fail("nullsingleton item");
    }
    if (exists str=singleton.get(1)) {
        fail("nullsingleton item");
    }

    check(singleton.span(0, 1) nonempty, "nonempty nullsingleton span(0,1)");
    check(singleton.span(0, 0) nonempty, "nonempty nullsingleton span(0,0)");
    check(singleton.spanFrom(0) nonempty, "nonempty nullsingleton spanFrom(0)");
    check(!singleton.spanFrom(1) nonempty, "nonempty nullsingleton spanFrom(1)");
    check(singleton.spanTo(0) nonempty, "nonempty nullsingleton spanTo(0)");
    check(singleton.spanTo(1) nonempty, "nonempty nullsingleton spanTo(1)");
    check(singleton.segment(0, 1) nonempty, "nonempty nullsingleton segment(0,1)");
    check(singleton.span(0, 3).string=="[null]", "nullsingleton span(0,3).string");
    check(singleton.segment(0, 3).string=="[null]", "nullsingleton segment(0,3).string");
    check(!singleton.span(1, 1) nonempty, "!nonempty nullsingleton span(1,1)");
    check(!singleton.spanFrom(1) nonempty, "!nonempty nullsingleton spanFrom(1)");
    check(!singleton.segment(1, 1) nonempty, "!nonempty nullsingleton segment(1,1)");
    check(singleton.span(0, 0) nonempty, "nonempty nullsingleton span(0,0)");
    check(singleton.span(0, 10) nonempty, "nonempty nullsingleton span(0,10)");
    check(!singleton.segment(0, 0) nonempty, "!nonempty nullsingleton segment(0,0)");
    check(!singleton.segment(0, -1) nonempty, "!nonempty nullsingleton segment(0,-1)");

    check(singleton.keys.contains(0), "nullsingleton keys.contains(0)");
    check(!singleton.keys.contains(1), "!nullsingleton keys.contains(1)");
    check(!singleton.keys.contains(2), "!nullsingleton keys.contains(2)");
    
    // Disabled: does not pass typechecker on M3.1
    //value ss = Singleton("Trompon").span(0, 0);
    //switch(ss)
    //case (is Empty) {}
    //case (is Singleton<String>) {
    //    String first = ss.first;
    //}
}

void test_concatenate() {
    value l1 = { "concatenate", 1,2,3};
    value l2 = { 4,5,6 };
    value l3 = {7,8,9};
    value concatenated = concatenate(l1, l2, l3);
    check(concatenated.size==l1.size+l2.size+l3.size, "concatenate [1]");
    check(concatenate("aa", "bb", "cc").sequence=={'a', 'a', 'b', 'b', 'c', 'c'}, "concatenate [2]");
}

void test_zip() {
    value keys = { 1, 2, 3, 4, 5, 6 };
    value items = { "one", "two", "three", "four", "five" };
    value z1 = zipEntries(keys, items);
    value z2 = zipEntries(keys, { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete" });
    check(z1.size==5, "zip 1:`` z1 ``");
    check(z2.size==6, "zip 2:`` z2 ``");
}

//This is actually a test for the compiler. "exists" doesn't work yet.
void test_exists_nonempty() {
    String? yes = "yes";
    String? no = null;
    variable Integer[]? empties = Singleton(1);
    value t1 = yes exists then "yes exists" else "WTF";
    check(t1 == "yes exists", "exists 1");
    value t2 = no exists then "WTF" else "no doesn\'t exist";
    check(t2 == "no doesn\'t exist", "exists 2");
    value t3 = empties nonempty then "nonempty works" else "nonempty broken";
    check(t3 == "nonempty works", "nonempty 1");
    Integer[] _t4 = {};
    value t4 = _t4 nonempty then "nonempty is broken" else "works";
    check(t4 == "works", "nonempty 2");
}

void test_max_min() {
    Integer mx1 = max([1, 2, 3]);
    check(mx1==3, "max nonempty seq");
    // fails because of https://github.com/ceylon/ceylon-compiler/issues/953
    //Null mx2 = max({});
    Integer? mx3 = max(concatenate({},{1, 2, 3}));
    check((mx3 else 10)==3, "max concatenated seq");
    Integer? mx4 = max({1, 2, 3}.filter((Integer i) => i>0));
    check((mx4 else 10)==3, "max filtered seq");
    
    Integer mn1 = min([1, 2, 3]);
    check(mn1==1, "min nonempty seq");
    // fails because of https://github.com/ceylon/ceylon-compiler/issues/953
    //Null mn2 = min({});
    Integer? mn3 = min(concatenate({},{1, 2, 3}));
    check((mn3 else 10)==1, "min concatenated seq");
    Integer? mn4 = min({1, 2, 3}.filter((Integer i) => i>0));
    check((mn4 else 10)==1, "min filtered seq");
}

shared void arraySequence() {
    value abc = ArraySequence({"a", "b", "c"});
    check(3==abc.size, "abc.size");
    check(!abc.empty, "abc.empty");
    check(2==abc.lastIndex, "abc.lastIndex");
    
    check("a"==abc.first, "abc.first");
    check({"b", "c"}==abc.rest, "abc.rest");
    check({"c"}==abc.rest.rest, "abc.rest.rest");
    check({}==abc.rest.rest.rest, "abc.rest.rest.rest");
    
    check(abc.defines(0), "abc.defines(0)");
    check(abc.defines(1), "abc.defines(1)");
    check(abc.defines(2), "abc.defines(2)");
    check(!abc.defines(3), "abc.defines(3)");
    
    check(abc[0]=="a", "abc[0]");
    check(abc[1] exists, "abc[1]");
    check(abc[2] exists, "abc[2]");
    check(!(abc[3] exists), "abc[3]");
    
    check(abc.reversed.string=="[c, b, a]", "abc.reversed ``abc.reversed``");
    check(abc.reversed.reversed==abc, "abc.reversed.reversed");
    
    check(abc.span(-1,-1)=={}, "abc.span(-1,-1)");
    check(abc.span(-1, 0)=={"a"}, "abc.span(-1,0)");
    check(abc.span(-1, 1)=={"a", "b"} , "abc.span(-1,1)");
    check(abc.span(-1, 2)==abc, "abc.span(-1,2)");
    check(abc.span(-1, 3)==abc, "abc.span(-1,3)");
    
    check(abc.span(0,-1)=={"a"}, "abc.span(0,-1)");
    check(abc.span(0, 0)=={"a"}, "abc.span(0,0)");
    check(abc.span(0, 1)=={"a", "b"} , "abc.span(0,1)");
    check(abc.span(0, 2)==abc, "abc.span(0,2)");
    check(abc.span(0, 3)==abc, "abc.span(0,3)");
    
    check(abc.span(1,-1)=={"b", "a"}, "abc.span(1,-1)");
    check(abc.span(1, 0)=={"b", "a"}, "abc.span(1,0)");
    check(abc.span(1, 1)=={"b"} , "abc.span(1,1)");
    check(abc.span(1, 2)=={"b", "c"}, "abc.span(1,2)");
    check(abc.span(1, 3)=={"b", "c"}, "abc.span(1,3)");
    
    check(abc.span(2,-1)=={"c", "b", "a"}, "abc.span(2,-1)");
    check(abc.span(2, 0)=={"c", "b", "a"}, "abc.span(2,0)");
    check(abc.span(2, 1)=={"c", "b"} , "abc.span(2,1)");
    check(abc.span(2, 2)=={"c"}, "abc.span(2,2)");
    check(abc.span(2, 3)=={"c"}, "abc.span(2,3)");
    
    check(abc.span(3,-1)=={"c", "b", "a"}, "abc.span(3,-1) ``abc.span(3,-1)``");
    check(abc.span(3, 0)=={"c", "b", "a"}, "abc.span(3,0)");
    check(abc.span(3, 1)=={"c", "b"} , "abc.span(3,1)");
    check(abc.span(3, 2)=={"c"}, "abc.span(3,1)");
    check(abc.span(3, 3)=={}, "abc.span(3,3)");
    
    check(abc.spanFrom(-1)==abc, "abc.spanFrom(-1)");
    check(abc.spanFrom(0)==abc, "abc.spanFrom(0)");
    check(abc.spanFrom(1)=={"b", "c"} , "abc.spanFrom(1)");
    check(abc.spanFrom(2)=={"c"}, "abc.spanFrom(2)");
    check(abc.spanFrom(3)=={}, "abc.spanFrom(3)");
    
    check(abc.spanTo(-1)=={}, "abc.spanTo(-1)");
    check(abc.spanTo(0)=={"a"}, "abc.spanTo(0)");
    check(abc.spanTo(1)=={"a", "b"} , "abc.spanTo(1)");
    check(abc.spanTo(2)==abc, "abc.spanTo(2)");
    check(abc.spanTo(3)==abc, "abc.spanTo(3)");
    
    
    check(abc.segment(-1,-1)=={}, "abc.segment(-1,-1)");
    check(abc.segment(-1, 0)=={}, "abc.segment(-1,0)");
    check(abc.segment(-1, 1)=={} , "abc.segment(-1,1)");
    check(abc.segment(-1, 2)=={"a"}, "abc.segment(-1,2)");
    check(abc.segment(-1, 3)=={"a", "b"}, "abc.segment(-1,3)");
    
    check(abc.segment(0,-1)=={}, "abc.segment(0,-1)");
    check(abc.segment(0, 0)=={}, "abc.segment(0,0)");
    check(abc.segment(0, 1)=={"a"} , "abc.segment(0,1)");
    check(abc.segment(0, 2)=={"a", "b"}, "abc.segment(0,2)");
    check(abc.segment(0, 3)==abc, "abc.segment(0,3)");
    
    check(abc.segment(1,-1)=={}, "abc.segment(1,-1)");
    check(abc.segment(1, 0)=={}, "abc.segment(1,0)");
    check(abc.segment(1, 1)=={"b"} , "abc.segment(1,1)");
    check(abc.segment(1, 2)=={"b", "c"}, "abc.segment(1,2)");
    check(abc.segment(1, 3)=={"b", "c"}, "abc.segment(1,3)");
    
    check(abc.segment(2,-1)=={}, "abc.segment(2,-1)");
    check(abc.segment(2, 0)=={}, "abc.segment(2,0)");
    check(abc.segment(2, 1)=={"c"} , "abc.segment(2,1)");
    check(abc.segment(2, 2)=={"c"}, "abc.segment(2,2)");
    check(abc.segment(2, 3)=={"c"}, "abc.segment(2,3)");
    
    check(abc.segment(3,-1)=={}, "abc.segment(3,-1)");
    check(abc.segment(3, 0)=={}, "abc.segment(3,0)");
    check(abc.segment(3, 1)=={} , "abc.segment(3,1)");
    check(abc.segment(3, 2)=={}, "abc.segment(3,1)");
    check(abc.segment(3, 3)=={}, "abc.segment(3,3)");
}

@test
shared void sequences() {
    arraySequence();
    value builder = SequenceBuilder<String>();
    value empty = builder.sequence;
    check(empty.size==0, "empty sequence");
    check(!empty nonempty, "empty sequence");
    if (empty nonempty) {
        fail("empty sequence");
    }
    check(!empty.span(1, 2) nonempty, "empty.span(1,2)");
    check(!empty.spanFrom(0) nonempty, "empty.spanFrom(0)");
    check(!empty.spanTo(0) nonempty, "empty.spanTo(0)");
    check(!empty.spanFrom(1) nonempty, "empty.spanFrom(1)");
    check(!empty.spanTo(1) nonempty, "empty.spanTo(1)");
    check(!empty.segment(1, 2) nonempty, "empty sequence segment");
    check(empty.string=="[]", "empty.string");
    check(empty.reversed==empty, "empty reversed");
    check(empty.sequence==empty, "empty.sequence");

    builder.append("hello");
    builder.append("world");
    value result = builder.sequence;
    check(result.size==2, "sequence size");
    check(result nonempty, "nonempty sequence");
    if (nonempty result) {
        check(result.lastIndex==1, "sequence last index");
    }
    else {
        fail("sequence nonempty");
    }
    if (exists first = result.first) {
        check(first=="hello", "sequence first");
    }
    else {
        fail("sequence first");
    }
    check(result.sequence==result, "sequence.sequence");
    if (exists last = result.last) {
        check(last=="world", "sequence last");
    }
    else {
        fail("sequence last");
    }
    check(result.string=="[hello, world]", "sequence.string 1");

    //span
    check(result.span(-2,-1).string=="[]", "sequence.span(-2,-1).string");
    check(result.span(-2,0).string=="[hello]", "sequence.span(-2,0).string");
    check(result.span(1,1).string=="[world]", "sequence.span(1,1).string");
    check(result.span(1,2).string=="[world]", "sequence.span(1,2).string");
    check(result.spanFrom(1).string=="[world]", "sequence.spanFrom(1).string");
    check(result.spanFrom(0).string=="[hello, world]", "sequence.spanFrom(0).string");
    check(result.spanTo(1).string=="[hello, world]", "sequence.spanTo(1).string");
    check(result.spanTo(0).string=="[hello]", "sequence.spanTo(0).string");
    check(result.span(0,3).string=="[hello, world]", "sequence.span(0,3).string");
    //check(result.span(1,0).string=="{ world, hello }", "sequence reverse span.string");
    check(result.span(1,1) nonempty, "nonempty sequence.span(1,1)");
    check(result.span(0,0) nonempty, "nonempty sequence.span(0,0)");

    //segment
    check(result.segment(1,1).string=="[world]", "sequence.segment(1,1).string");
    check(result.segment(0,3).string=="[hello, world]", "sequence.segment(0,3).string");
    check(result.segment(1,1) nonempty, "nonempty sequence.segment(1,1)");
    check(!result.segment(0,0) nonempty, "!nonempty sequence.segment(0,0)");
    //check(!result.segment(1,-1) nonempty, "!nonempty sequence.segment(1,-1)");
    
    check(result.reversed=={"world", "hello"}, "sequence.reversed");

    if (exists str = result[0]) {
        check(str=="hello", "sequence item");
    }
    else {
        fail("sequence item");
    }
    if (exists str = result[1]) {
        check(str=="world", "sequence item");
    }
    else {
        fail("sequence item");
    }
    if (exists str = result[2]) {
        fail("sequence item");
    }

    check(result.keys.contains(0), "sequence keys 0");
    check(result.keys.contains(1), "sequence keys 1");
    check(!result.keys.contains(2), "sequence keys 2");
    check(result.defines(0)&&result.defines(1)&&!result.defines(2),
           "sequence defines");
    check(result.definesEvery {0,1}, "sequence definesEvery 0,1");
    check(!result.definesEvery {1,2}, "sequence definesEvery 1,2");
    check(result.definesAny {1,2}, "sequence definesAny 1,2");
    check(!result.definesAny {2,3}, "sequence definesAny 2,3");
    check(result.items {0,1,2,3}.string=="[hello, world, <null>, <null>]", "sequence.items 1");
    check(result.items {1,0}.string=="[world, hello]", "sequence.items 2");
    check(result.items {5,6,7}.string=="[<null>, <null>, <null>]", "sequence.items 3");

    if (nonempty result) {
        value rest = result.rest;
        check(rest.size==1, "rest size");
        check(rest.keys.contains(0), "rest keys 1");
        check(!rest.keys.contains(1), "rest keys 2");
        if (exists str = rest[0]) {
            check(str=="world", "rest item");
        }
        else {
            fail("rest item");
        }
        if (exists str = rest[1]) {
            fail("rest item");
        }
        check(rest nonempty, "empty rest");
        if (nonempty rest) {
            check(rest.first=="world", "rest first");
            if (nonempty rr = rest.rest) {
                fail("rest rest");
            }
            check(!rest.rest nonempty, "empty rest");
        }
        else {
            fail("rest nonempty");
        }
    }

    if (nonempty result) {
        value appender = SequenceAppender(result);
        appender.append("goodbye");
        value more = appender.sequence;
        check(more.size==3, "sequence size");
        check(more.first=="hello", "sequence first");
        check(more.string=="[hello, world, goodbye]", "sequence.string 2");
        appender.appendAll({"everyone", "good luck!"});
        //appender.append("everyone");
        //appender.append("good luck!");
        value evenMore = appender.sequence;
        check(evenMore.size==5, "sequence size");
        check(evenMore.string=="[hello, world, goodbye, everyone, good luck!]", "sequence.string 3");
    }

    value seq = [ 1, 2, 3, 4 ];
    check(seq.size==4, "sequence size");
    check(seq.string=="[1, 2, 3, 4]", "sequence.string 4: " + seq.string);
    check(seq.reversed=={4, 3, 2, 1}, "sequence reversed");
    check(seq.first==1, "sequence first");
    check(seq.rest.string=="[2, 3, 4]", "sequence.rest.string " + seq.rest.string);
    variable value i=0;
    for (s in seq) {
        if (exists it=seq[i]) {
            check(it==s, "sequence iteration");
        }
        else {
            fail("sequence iteration");
        }
        i=i+1;
    }
    check(i==4, "sequence iteration");

    value union = SequenceBuilder<String|Float>();
    union.append("x");
    union.append(5.1);
    union.appendAll({"y", -1.2});
    value useq = union.sequence;
    check(useq.size==4, "union sequence builder");
    check(useq.string=="[x, 5.1, y, -1.2]", "union sequence builder.string");
    variable value s=0;
    variable value f=0;
    for (e in useq) {
        if (e is String) {
            s=s+1;
        }
        if (e is Float) {
            f=f+1;
        }
    }
    check(s==2&&f==2, "union sequence iteration");

    test_singleton();
    test_nullsingleton();

    Sequential<String?> nulls = [ null, "hello", null, "world" ];
    if (exists n0 = nulls[0]) { fail("sequence with nulls"); }
    if (exists n1 = nulls[1]) {} else { fail("sequence with nulls"); }
    check(nulls.string=="[<null>, hello, <null>, world]", "sequence with nulls.string " + nulls.string);
    variable value nonnull=0;
    for (o in nulls) {
        if (exists o) { nonnull++; }
    }
    check(nonnull==2, "iterate sequence with nulls");

    value coalesced = coalesce(nulls).sequence;
    check(coalesced.size==2, "coalesce size");
    check(coalesced.string=="[hello, world]", "coalesce.string");
    check(coalesced.keys.contains(0), "coalesced keys");
    check(coalesced.keys.contains(1), "coalesced keys");
    check(!coalesced.keys.contains(2), "coalesced keys");
    check(coalesced.defines(0)&&coalesced.defines(1)&&!coalesced.defines(2),
           "coalesce defines");
    check(coalesced nonempty, "nonempty coalesced");
    //value coal2 = coalesce { for (c in "hElLo") null }.sequence;
    //check(!coal2 nonempty, "nonempty coalesced2");
    //check(coal2.size == 0, "coalesced2.size");
    //check(!'h' in coal2, "coalesced2.contains");
    value entriesBuilder = SequenceBuilder<Integer->String>();
    entriesBuilder.append(1->"hello");
    entriesBuilder.append(2->"world");
    value entrySequence = entriesBuilder.sequence;
    check(entrySequence.string=="[1->hello, 2->world]", "entries sequence.string");
    variable value cntr=0;
    for (nat->str in entrySequence) {
        cntr++;
        check(nat==1||nat==2, "entry key iteration");
        check(str=="hello"||str=="world", "entry key iteration");
    }
    check(cntr==2, "entry iteration");

    for (name->initial in { "Gavin"->'G', "Tom"->'T' }) {
        check(name.initial(1)==initial.string, "entry iteration");
    }

    value sequenceEntries = entries { "X1", "X2", "X3" };
    check(sequenceEntries.sequence.size==3, "entries size");
    check(sequenceEntries.sequence nonempty, "nonempty entries");
    if (exists primero=sequenceEntries.first) {
        check(primero==Entry(0, "X1"), "entries first");
    }
    else {
        fail("entries first");
    }
    for (nat->str in sequenceEntries) {
        check("X"+(nat+1).string==str, "entries iteration");
    }

    //More sequence-related functions
    test_concatenate();
    test_zip();
    test_exists_nonempty();
    test_max_min();
    
    check(emptyOrSingleton(1) nonempty, "emptyOrSingleton [1]");
    check(!emptyOrSingleton(null) nonempty, "emptyOrSingleton [2]");
    
    check(["hello"].withTrailing("world").first=="hello", "sequence with trailing");
    check(["world"].withLeading("hello").first=="hello", "sequence with trailing");
    
    //collect
    check({ 1, 2, 3, 4, 5 }.collect((Integer i) => i*2) == { 2, 4, 6, 8, 10 }, "Sequence<Integer>.collect");
    check("hola".collect((Character c) => c.uppercased) == {'H', 'O', 'L', 'A'}, "Sequence<String>.collect");
    
    check([1,2,3,4,5].longerThan(4), "Sequence.longerThan");
    check(![1,2,3].longerThan(3), "Sequence.longerThan");
    check([1,2,3,4,5].shorterThan(6), "Sequence.shorterThan");
    check(![1,2,3].shorterThan(3), "Sequence.shorterThan");
}
