void testIterables() {
    value s1 = { 1, 2, 3, 4, 5 };
    value s2 = { "Hello", "World" };
    //Map
    check(s1.map((Integer i) i*2).sequence == { 2, 4, 6, 8, 10 }, "Iterable.map 1");
    check(s2.map((String s) s.reversed).sequence == { "olleH", "dlroW" }, "Iterable.map 2");
    check("hola".map((Character c) c.uppercased).sequence == {`H`, `O`, `L`, `A`}, "String.map");

    //Filter
    check(s1.filter((Integer i) i%2==0).sequence == { 2, 4 }, "Iterable.filter 1");
    check(s2.filter((String s) "e" in s).sequence == { "Hello" }, "Iterable.filter 2");
    check("h o l a".filter((Character c) c.letter) == "hola", "String.filter");

    //Collect (like map, but it's already T[])
    check(s1.collect((Integer i) i*2) == { 2, 4, 6, 8, 10 }, "Iterable.map 1");
    check(s2.collect((String s) s.reversed) == { "olleH", "dlroW" }, "Iterable.map 2");
    check("hola".collect((Character c) c.uppercased) == {`H`, `O`, `L`, `A`}, "String.map");

    //Select
    check(s1.select((Integer i) i%2==0) == { 2, 4 }, "Iterable.filter 1");
    check(s2.select((String s) "e" in s) == { "Hello" }, "Iterable.filter 2");
    check("h o l a".select((Character c) c.letter) == "hola", "String.filter");

    //Fold
    check(s1.fold(0, (Integer a, Integer b) a+b) == 15, "Iterable.fold 1");
    check(s2.fold(1, (Integer a, String b) a+b.size) == 11, "Iterable.fold 2");
    //Find
    if (exists four = s1.find((Integer i) i>3)) {
        check(four == 4, "Iterable.find 1");
    } else { fail("Iterable.find 1"); }
    if (exists s = s2.find((String s) s.size>5)) {
        fail("Iterable.find 2");
    }
    if (exists s = s2.find((String s) "r" in s)) {
        check(s == "World", "Iterable.find 3");
    } else { fail("Iterable.find 3"); }
    if (exists c = "hola!".find((Character c) !c.letter)) {
        check(c == `!`, "String.find");
    } else { fail("String.find"); }
    //FindLast
    if (exists four = s1.findLast((Integer i) i>3)) {
        check(four == 5, "Iterable.findLast 1");
    } else { fail("Iterable.find 1"); }
    if (exists s = s2.findLast((String s) s.size>5)) {
        fail("Iterable.findLast 2");
    }
    if (exists s = s2.findLast((String s) "o" in s)) {
        check(s == "World", "Iterable.findLast 3");
    } else { fail("Iterable.findLast 3"); }
    if (exists c = "hola!".findLast((Character c) c.letter)) {
        check(c == `a`, "String.findLast");
    } else { fail("String.findLast"); }

    check((1..10).map((Integer i) i.float).sequence == {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, "map 1");
    check((1..10).filter((Integer i) i>5).sequence == {6, 7, 8, 9, 10}, "filter 1");
    check(((1..10).find((Integer i) i>5) else -1)==6, "find 1");
    check(((1..10).findLast((Integer i) i>5) else -1)==10, "findLast 1");
    check((1..10).fold(0, (Integer i, Integer j) i+j)==55, "fold 1");

    check({ 1, 3, 7, 10 }.map((Integer i) i.float).sequence=={1.0, 3.0, 7.0, 10.0}, "map 2");
    check({ 1, 3, 7, 10 }.filter((Integer i) i>5).sequence=={7.0, 10.0}, "filter 2");
    check(({ 1, 3, 7, 10 }.find((Integer i) i>5) else -1)==7, "find 2");
    check(({ 1, 3, 7, 10 }.findLast((Integer i) i>5) else -1)==10, "findLast 2");
    check({ 1, 3, 7, 10 }.fold(1, (Integer i, Integer j) i*j)==210, "fold 3");
 
    //Empty optimized implementations
    Integer[] vacio = {};
    check(vacio.map((Integer i) i).empty, "empty.map");
    check(vacio.filter((Integer i) true).empty, "empty.filter");
    check(!vacio.find((Integer i) i>5) exists, "find 3");
    check(!vacio.findLast((Integer i) i>5) exists, "findLast 3");
    check(vacio.fold(0, (Integer i, Integer j) i)==0, "empty.fold");
    check(vacio.sort((Integer a, Integer b) larger).sequence=={}, "empty.sort");
    check(!vacio.every((Integer x) true), "empty.every");
    check(!vacio.any((Integer x) true), "empty.any");
    check(vacio.skipping(1).sequence=={}, "empty.skipping");
    check(vacio.taking(1).sequence=={}, "empty.taking");
 
    //Singleton optimized implementations 
    check(Singleton(5).map((Integer i) i.float).sequence=={5.0}, "Singleton.map");
    check(Singleton(5).filter((Integer i) i>5).sequence=={}, "Singleton.filter");
    check(!Singleton(5).find((Integer i) i>5) exists, "Singleton.find");
    check(!Singleton(5).findLast((Integer i) i>5) exists, "Singleton.findLast");
    check(Singleton(5).fold(0, (Integer i, Integer j) i+j)==5, "Singleton.fold");
    check(Singleton(5).sort((Integer x, Integer y) x<=>y) == Singleton(5), "Singleton.sort");
    check(Singleton(1).any((Integer x) x == 1), "Singleton.any");
    check(Singleton(1).every((Integer x) x>0), "Singleton.every");
    check(Singleton(1).skipping(0).sequence=={1}, "Singleton.skipping [1]");
    check(Singleton(1).skipping(1).sequence=={}, "Singleton.skipping [2]");
    check(Singleton(1).skipping(9).sequence=={}, "Singleton.skipping [3]");
    check(Singleton(1).taking(5).sequence=={1}, "Singleton.taking");
    check(Singleton(1).by(1).sequence=={1}, "Singleton.by [1]");
    check(Singleton(1).by(5).sequence=={1}, "Singleton.by [2]");
    //Let's test by(0) with Singleton
    /*value endlessIter = Singleton(1).by(0).iterator;
    for (i in 1..1000) {
        if (is Finished endlessIter.next()) { fail("Singleton.by(0)"); }
    }*/

    //Any
    check( (1..10).any((Integer x) x==9), "Iterable.any [1]");
    check( !(1..10).any((Integer x) x%9==9), "Iterable.any [2]");
    check("hello world".any((Character c) c.whitespace), "Iterable.any [3]");

    //Every
    check( (1..10).every((Integer x) x<=10), "Iterable.every [1]");
    check( {2,4,6,8,10}.every((Integer x) x%2==0), "Iterable.every [2]");
    check( "hello".every((Character c) c.lowercase), "Iterable.every [3]");
    check( !"Hello".every((Character c) c.lowercase), "Iterable.every [3]");

    //Sorted
    check({5,4,3,2,1}.sort((Integer x, Integer y) x<=>y).sequence == {1,2,3,4,5}, "sort [1]");
    check({"tt","aaa","z"}.sort((String a, String b) a<=>b).sequence == {"aaa", "tt", "z"}, "sort [2]");
    check("hola".sort((Character a, Character b) a<=>b) == "ahlo", "String.sort");

    //Skipping
    check({1,2,3,4,5}.skipping(3).sequence=={4,5}, "skipping [1]");
    check(!{1,2,3,4,5}.skipping(9).sequence nonempty, "skipping [2]");
    check((1..10).skipping(5)==6..10, "Range.skipping [3]");
    check(!(1..5).skipping(9).sequence nonempty, "skipping [4]");
    check((5..1).skipping(2)==3..1, "Range.skipping [5]");
    check("hola".skipping(2)=="la", "String.skipping");
    check({for(i in 1..10) i}.skipping(8).sequence=={9,10}, "comprehension.skipping");

    //Taking
    check({1,2,3,4,5}.taking(3).sequence=={1,2,3}, "taking [1]");
    check(!{1,2,3,4,5}.taking(0).sequence nonempty, "taking [2]");
    check((1..10).taking(5)==1..5, "Range.taking [3]");
    check(!(1..5).taking(0).sequence nonempty, "Range.taking [4]");
    check((1..10).taking(100)==1..10, "Range.taking [5]");
    check({1,2,3,4,5}.taking(100).sequence=={1,2,3,4,5}, "taking [6]");
    check((5..1).taking(3)==5..3, "Range.taking [7]");
    check("hola".taking(2)=="ho", "String.taking");
    check({for (i in 1..10) i}.taking(2).sequence=={1,2}, "comprehension.taking");

    //By
    check({1,2,3,4,5}.by(1).sequence=={1,2,3,4,5}, "by [1]");
    check({1,2,3,4,5}.by(2).sequence=={1,3,5}, "by [2]");
    check({1,2,3,4,5}.by(3).sequence=={1,4}, "by [3]");
    check({1,2,3,4,5}.by(4).sequence=={1,5}, "by [4]");
    check({1,2,3,4,5}.by(5).sequence=={1,2,3,4,5}.by(9).sequence, "by [5]");
    check("AaEeIiOoUu".by(2)=="AEIOU", "String.by [1]");
    check("1234567890".by(3)=="1470", "String.by [2]");
    check("1234567890".by(4)=="159", "String.by [3]");
    check("1234567890".by(5)=="16", "String.by [4]");
    check("1234567890".by(8)=="19", "String.by [5]");
    check("1234567890".by(11)=="1", "String.by [6]");
    check((1..10).by(2).sequence=={1,3,5,7,9}, "Range.by [1]");
    check((10..1).by(2).sequence=={10,8,6,4,2}, "Range.by [2]");
    check((1..10).by(6).sequence=={1,7}, "Range.by [3]");
    check((1..10).by(100).sequence=={1}, "Range.by [4]");
    check({for(i in 1..10) i}.by(4).sequence=={1,5,9}, "comprehension.by");

    //Count
    check((1..10).count((Integer x) x%2==0)==5, "Range.count");
    check({1,2,3,4,5}.count((Integer x) x%2==0)==2, "Sequence.count");
    check({for (i in 1..10) i}.count(greaterThan(7))==3, "Iterable.count (greaterThan)");
    check({for (i in 1..10) i}.count(lessThan(7))==6, "Iterable.count (lessThan)");
    check(array(1,2,3,4,5).count((Integer x) x%2==1)==3, "Array.count");
    check("AbcdEfghIjklmnOp".count((Character c) c.uppercase)==4, "String.count");
    check(Singleton(1).count(equalTo(1))==1, "Singleton.count (equalTo)");

    //coalesced
    check((1..10).coalesced == 1..10, "Range.coalesced");
    check({1,2,3,null,4,5}.coalesced.sequence=={1,2,3,4,5}, "Sequence.coalesced");
    check(string({for (c in "HoLa") c.uppercase then c else null}.coalesced.sequence...)=="HL", "Iterable.coalesced");
    check(array(1,2,3,null,5).coalesced.sequence=={1,2,3,5}, "Array.coalesced");
    check(Singleton("X").coalesced==Singleton("X"), "Singleton.coalesced [1]");
    check("ABC".coalesced=="ABC", "String.coalesced");
    check({}.coalesced=={}, "Empty.coalesced");
    //indexed
    for (k->v in (1..5).indexed) {
        check(k+1==v, "Range.indexed");
    }
    check({"a", "b", "c"}.indexed.sequence=={0->"a", 1->"b", 2->"c"}, "Sequence.indexed");
    check(array(0, 1, 2).indexed.sequence=={0->0, 1->1, 2->2}, "Array.indexed");
    check(Singleton("A").indexed.sequence=={0->"A"}, "Singleton.indexed");
    check({}.indexed=={}, "Empty.indexed");
    check({for (c in "abc") c}.indexed.sequence=={0->`a`, 1->`b`, 2->`c`}, "Iterable.indexed");
    check("abc".indexed.sequence=={0->`a`, 1->`b`, 2->`c`}, "String.indexed");
    check({1,null,2}.indexed.sequence == {0->1, 2->2}, "indexed with nulls");

    //last (defined in ContainerWithFirst but tested here)
    check((1..5000000000).last == 5000000000, "Range.last");
    check(Singleton(1).last == 1, "Singleton.last");
    check({1,2,3,4,5,6,7,8,9,10}.last==10, "Sequence.last");
    if (exists l="The very last character".last) {
        check(l==`r`, "String.last [1]");
    } else { fail("String.last [1]"); }
    if ("".last exists) {
        fail("String.last [2]");
    }
    if (exists l={for(i in 1..1000) i}.last) {
        check(l==1000, "Iterable.last");
    } else { fail("Iterable.last"); }

    //chain()
    check({1,2}.chain({"a", "b"}).sequence=={1,2,"a","b"}, "Sequence.chain");
    check(Singleton(1).chain({2,3}).sequence=={1,2,3}, "Singleton.chain");
    check((1..3).chain(Singleton(4)).sequence=={1,2,3,4}, "Range.chain");
    check("abc".chain({1,2}).sequence=={`a`, `b`, `c`, 1, 2}, "String.chain");
    check("".chain(Singleton(1)).sequence=={1}, "\"\".chain");
    check({}.chain({1,2})=={1,2}, "Empty.chain");
    check(array().chain({1,2})=={1,2}, "EmptyArray.chain");
    check(array(1,2).chain({3,4}).sequence=={1,2,3,4}, "NonemptyArray.chain");
    check(Singleton(1).chain(Singleton(2)).chain(Singleton("3")).sequence=={1,2,"3"}, "Singletons.chain");

    //group
    value grouped = (1..10).group((Integer i) i%2==0 then "even" else "odd");
    check(grouped.size == 2, "Iterable.group 1");
    if (exists v=grouped["even"]) {
        check(v.size == 5, "Iterable.group 2");
        check(v.every((Integer i) i%2==0), "Iterable.group 3");
    } else { fail("Iterable.group 2"); }
    check(grouped.defines("odd"), "Iterable.group 4");
    value gr2 = "aBcDeFg".group((Character c) c.lowercase);
    check(gr2.size == 2, "Iterable.group 5");
    if (exists v=gr2[true]) {
        check(v.size == 4, "Iterable.group 6");
        check(v.every((Character i) i.lowercase), "Iterable.group 7");
    } else { fail("Iterable.group 6"); }
    check(gr2.defines(false), "Iterable.group 8");

    //Iterable-related functions
    check({"aaa", "tt", "z"}.sort(byIncreasing((String s) s.size)).sequence=={"z","tt","aaa"}, "sort(byIncreasing)");
    check({"z", "aaa", "tt"}.sort(byDecreasing((String s) s.size)).sequence=={"aaa","tt","z"}, "sort(byDecreasing)");
    Iterable<String> combined = combine<String,Character,Integer>((Character c, Integer i) "comb " c "+" i "",
        "hello", { 1,2,3,4 });
    check(combined.sequence.size==4, "combine [1]");
    check(combined.sequence == { "comb h+1", "comb e+2", "comb l+3", "comb l+4" }, "combine [2]");
}
