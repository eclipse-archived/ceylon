@test
shared void testIterables() {
    value s1 = { 1, 2, 3, 4, 5 };
    value s2 = { "Hello", "World" };
    //Map
    check(s1.map((Integer i) => i*2).sequence() == { 2, 4, 6, 8, 10 }.sequence(), "Iterable.map 1");
    check(s2.map((String s) => s.reversed).sequence() == { "olleH", "dlroW" }.sequence(), "Iterable.map 2");
    check("hola".map((Character c) => c.uppercased).sequence() == {'H', 'O', 'L', 'A'}.sequence(), "String.map");

    //Filter
    check(s1.filter((Integer i) => i%2==0).sequence() == { 2, 4 }.sequence(), "Iterable.filter 1");
    check(s2.filter((String s) => "e" in s).sequence() == { "Hello" }.sequence(), "Iterable.filter 2");
    check(String("h o l a".filter((Character c) => c.letter)) == "hola", "String.filter");

    //Collect (like map, but it's already T[])
    check(s1.collect((Integer i) => i*2) == [2, 4, 6, 8, 10], "Iterable.map 1");
    check(s2.collect((String s) => s.reversed) == ["olleH", "dlroW"], "Iterable.map 2");
    check("hola".collect((Character c) => c.uppercased) == ['H', 'O', 'L', 'A'], "String.map");

    //Select
    check(s1.select((Integer i) => i%2==0) == [2, 4], "Iterable.select 1");
    check(s2.select((String s) => "e" in s) == ["Hello"], "Iterable.select 2");
    check("h o l a".select((Character c) => c.letter) == "hola".sequence(), "String.select");

    //Fold
    check(s1.fold(0, (Integer a, Integer b) => a+b) == 15, "Iterable.fold 1");
    check(s2.fold(1, (Integer a, String b) => a+b.size) == 11, "Iterable.fold 2");
    //Reduce
    check(s1.reduce((Integer a, Integer b) => a+b) == 15, "Iterable.reduce 1");
    check(s2.reduce<Integer>((Integer|String a, String b) {
      switch(a)
      case(is Integer) { return a+b.size; }
      case(is String) { return a.size+b.size; }
    }) == 10, "Iterable.reduce 2");

    //Find
    if (exists four = s1.find((Integer i) => i>3)) {
        check(four == 4, "Iterable.find 1");
    } else { fail("Iterable.find 1"); }
    if (exists s = s2.find((String s) => s.size>5)) {
        fail("Iterable.find 2");
    }
    if (exists s = s2.find((String s) => "r" in s)) {
        check(s == "World", "Iterable.find 3");
    } else { fail("Iterable.find 3"); }
    if (exists c = "hola!".find((Character c) => !c.letter)) {
        check(c == '!', "String.find");
    } else { fail("String.find"); }
    //FindLast
    if (exists four = s1.findLast((Integer i) => i>3)) {
        check(four == 5, "Iterable.findLast 1");
    } else { fail("Iterable.find 1"); }
    if (exists s = s2.findLast((String s) => s.size>5)) {
        fail("Iterable.findLast 2");
    }
    if (exists s = s2.findLast((String s) => "o" in s)) {
        check(s == "World", "Iterable.findLast 3");
    } else { fail("Iterable.findLast 3"); }
    if (exists c = "hola!".findLast((Character c) => c.letter)) {
        check(c == 'a', "String.findLast");
    } else { fail("String.findLast"); }

    check((1..10).map((Integer i) => i.float).sequence() == {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}.sequence(), "Range.map 1");
    check((1..10).filter((Integer i) => i>5).sequence() == {6, 7, 8, 9, 10}.sequence(), "Range.filter 1");
    check(((1..10).find((Integer i) => i>5) else -1)==6, "Range.find 1");
    check(((1..10).findLast((Integer i) => i>5) else -1)==10, "Range.findLast 1");
    check((1..10).fold(0, (Integer i, Integer j) => i+j)==55, "Range.fold 3");
    check((1..10).reduce((Integer i, Integer j) => i+j)==55, "Range.reduce 3");

    check({ 1, 3, 7, 10 }.map((Integer i) => i.float).sequence()=={1.0, 3.0, 7.0, 10.0}.sequence(), "map 2");
    check({ 1, 3, 7, 10 }.filter((Integer i) => i>5).sequence()=={7.0, 10.0}.sequence(), "filter 2");
    check(({ 1, 3, 7, 10 }.find((Integer i) => i>5) else -1)==7, "find 2");
    check(({ 1, 3, 7, 10 }.findLast((Integer i) => i>5) else -1)==10, "findLast 2");
    check({ 1, 3, 7, 10 }.fold(1, (Integer i, Integer j) => i*j)==210, "fold 4");
    check({ 1, 3, 7, 10 }.reduce((Integer i, Integer j) => i*j)==210, "reduce 4");
 
    //Empty optimized implementations
    object myEmpty satisfies {Integer*} {
        shared actual Iterator<Integer> iterator() => emptyIterator;
    }
    check(myEmpty.map((Integer i) => i).empty, "empty.map");
    check(myEmpty.filter((Integer i) => true).empty, "empty.filter");
    check(!myEmpty.find((Integer i) => i>5) exists, "find 3");
    check(!myEmpty.findLast((Integer i) => i>5) exists, "findLast 3");
    check(myEmpty.fold(0, (Integer i, Integer j) => i)==0, "empty.fold");
    check(!myEmpty.reduce((Integer i, Integer j) => i) exists, "empty.reduce");
    check(myEmpty.sort((Integer a, Integer b) => larger).sequence()=={}, "empty.sort");
    check(myEmpty.every((Integer x) => true), "empty.every");
    check(!myEmpty.any((Integer x) => true), "empty.any");
    check(myEmpty.skip(1).sequence()=={}, "empty.skip");
    check(myEmpty.take(1).sequence()=={}, "empty.take");

    Integer[] vacio = [];
    check(vacio.map((Integer i) => i).empty, "empty.map");
    check(vacio.filter((Integer i) => true).empty, "empty.filter");
    check(!vacio.find((Integer i) => i>5) exists, "find 3");
    check(!vacio.findLast((Integer i) => i>5) exists, "findLast 3");
    check(vacio.fold(0, (Integer i, Integer j) => i)==0, "empty.fold");
    check(!vacio.reduce((Integer i, Integer j) => i) exists, "empty.reduce");
    check(vacio.sort((Integer a, Integer b) => larger).sequence()=={}, "empty.sort");
    check(vacio.every((Integer x) => true), "empty.every");
    check(!vacio.any((Integer x) => true), "empty.any");
    check(vacio.skip(1).sequence()=={}, "empty.skip");
    check(vacio.take(1).sequence()=={}, "empty.take");
 
    //Singleton optimized implementations 
    check(Singleton(5).map((Integer i) => i.float).sequence()=={5.0}.sequence(), "Singleton.map");
    check(Singleton(5).filter((Integer i) => i>5).sequence()=={}, "Singleton.filter");
    check(!Singleton(5).find((Integer i) => i>5) exists, "Singleton.find");
    check(!Singleton(5).findLast((Integer i) => i>5) exists, "Singleton.findLast");
    check(Singleton(5).fold(0, (Integer i, Integer j) => i+j)==5, "Singleton.fold");
    check(Singleton(5).reduce((Integer i, Integer j) => i+j)==5, "Singleton.reduce");
    check(Singleton(5).sort((Integer x, Integer y) => x<=>y) == Singleton(5), "Singleton.sort");
    check(Singleton(1).any((Integer x) => x == 1), "Singleton.any");
    check(Singleton(1).every((Integer x) => x>0), "Singleton.every");
    check(Singleton(1).skip(0).sequence()=={1}.sequence(), "Singleton.skip [1]");
    check(Singleton(1).skip(1).sequence()=={}, "Singleton.skip [2]");
    check(Singleton(1).skip(9).sequence()=={}, "Singleton.skip [3]");
    check(Singleton(1).take(5).sequence()=={1}.sequence(), "Singleton.take");
    check(Singleton(1).by(1).sequence()=={1}.sequence(), "Singleton.by [1]");
    check(Singleton(1).by(5).sequence()=={1}.sequence(), "Singleton.by [2]");
    //Let's test by(0) with Singleton
    /*value endlessIter = Singleton(1).by(0).iterator;
    for (i in 1..1000) {
        if (is Finished endlessIter.next()) { fail("Singleton.by(0)"); }
    }*/

    //Any
    check( (1..10).any((Integer x) => x==9), "Iterable.any [1]");
    check( !(1..10).any((Integer x) => x%9==9), "Iterable.any [2]");
    check("hello world".any((Character c) => c.whitespace), "Iterable.any [3]");

    //Every
    check( (1..10).every((Integer x) => x<=10), "Iterable.every [1]");
    check( {2,4,6,8,10}.every((Integer x) => x%2==0), "Iterable.every [2]");
    check( "hello".every((Character c) => c.lowercase), "Iterable.every [3]");
    check( !"Hello".every((Character c) => c.lowercase), "Iterable.every [3]");

    //Sorted
    check({5,4,3,2,1}.sort((Integer x, Integer y) => x<=>y).sequence() == {1,2,3,4,5}.sequence(), "sort [1]");
    check({"tt","aaa","z"}.sort((String a, String b) => a<=>b).sequence() == {"aaa", "tt", "z"}.sequence(), "sort [2]");
    check("hola".sort((Character a, Character b) => a<=>b) == "ahlo".sequence(), "String.sort");

    //Skipping
    check({1,2,3,4,5}.skip(3).sequence()=={4,5}.sequence(), "skip [1]");
    check(!{1,2,3,4,5}.skip(9).sequence() nonempty, "skip [2]");
    check((1..10).skip(5).sequence()==6..10, "Range.skip [3]");
    check(!(1..5).skip(9).sequence() nonempty, "skip [4]");
    check((5..1).skip(2).sequence()==3..1, "Range.skip [5]");
    check("hola".skip(2)=="la", "String.skip");
    check({for(i in 1..10) i}.skip(8).sequence()=={9,10}.sequence(), "comprehension.skip");

    //Taking
    check({1,2,3,4,5}.take(3).sequence()=={1,2,3}.sequence(), "take [1]");
    check(!{1,2,3,4,5}.take(0).sequence() nonempty, "take [2]");
    check((1..10).take(5).sequence()==1..5, "Range.take [3] was ``(1..10).take(5)``");
    check(!(1..5).take(0).sequence() nonempty, "Range.take [4]");
    check((1..10).take(100).sequence()==1..10, "Range.take [5]");
    check({1,2,3,4,5}.take(100).sequence()=={1,2,3,4,5}.sequence(), "take [6]");
    check((5..1).take(3).sequence()==5..3, "Range.take [7] was ``(5..1).take(3)``");
    check("hola".take(2)=="ho", "String.take");
    check({for (i in 1..10) i}.take(2).sequence()=={1,2}.sequence(), "comprehension.take");

    //By
    check({1,2,3,4,5}.by(1).sequence()=={1,2,3,4,5}.sequence(), "by [1]");
    check({1,2,3,4,5}.by(2).sequence()=={1,3,5}.sequence(), "by [2]");
    check({1,2,3,4,5}.by(3).sequence()=={1,4}.sequence(), "by [3]");
    check({1,2,3,4,5}.by(4).sequence()=={1,5}.sequence(), "by [4]");
    check({1,2,3,4,5}.by(5).sequence()=={1,2,3,4,5}.by(9).sequence(), "by [5]");
    check("AaEeIiOoUu".by(2)=="AEIOU", "String.by [1]");
    check("1234567890".by(3)=="1470", "String.by [2]");
    check("1234567890".by(4)=="159", "String.by [3]");
    check("1234567890".by(5)=="16", "String.by [4]");
    check("1234567890".by(8)=="19", "String.by [5]");
    check("1234567890".by(11)=="1", "String.by [6]");
    check((1..10).by(2).sequence()=={1,3,5,7,9}.sequence(), "Range.by [1]");
    check((10..1).by(2).sequence()=={10,8,6,4,2}.sequence(), "Range.by [2]");
    check((1..10).by(6).sequence()=={1,7}.sequence(), "Range.by [3]");
    check((1..10).by(100).sequence()=={1}.sequence(), "Range.by [4]");
    check({for(i in 1..10) i}.by(4).sequence()=={1,5,9}.sequence(), "comprehension.by");

    //Count
    check((1..10).count((Integer x) => x%2==0)==5, "Range.count");
    check({1,2,3,4,5}.count((Integer x) => x%2==0)==2, "Sequence.count");
    check({for (i in 1..10) i}.count(greaterThan(7))==3, "Iterable.count (greaterThan)");
    check({for (i in 1..10) i}.count(lessThan(7))==6, "Iterable.count (lessThan)");
    check(Array{1,2,3,4,5}.count((Integer x) => x%2==1)==3, "Array.count");
    check("AbcdEfghIjklmnOp".count((Character c) => c.uppercase)==4, "String.count");
    check(Singleton(1).count(equalTo(1))==1, "Singleton.count (equalTo)");

    //coalesced
    check((1..10).coalesced == 1..10, "Range.coalesced");
    check({1,2,3,null,4,5}.coalesced.sequence()=={1,2,3,4,5}.sequence(), "Sequence.coalesced");
    check(String({for (c in "HoLa") c.uppercase then c else null}.coalesced.sequence())=="HL", "Iterable.coalesced");
    print(Array{1,2,3,null,5}.coalesced);
    print({1,2,3,null,5}.coalesced);
    check(Array{1,2,3,null,5}.coalesced.sequence()=={1,2,3,5}.sequence(), "Array.coalesced");
    check(Singleton("X").coalesced==Singleton("X"), "Singleton.coalesced [1]");
    check("ABC".coalesced=="ABC", "String.coalesced");
    check({}.coalesced=={}, "Empty.coalesced");
    //indexed
    for (k->v in (1..5).indexed) {
        check(k+1==v, "Range.indexed");
    }
    check({"a", "b", "c"}.indexed.sequence()=={0->"a", 1->"b", 2->"c"}.sequence(), "Sequence.indexed");
    check(Array{0, 1, 2}.indexed.sequence()=={0->0, 1->1, 2->2}.sequence(), "Array.indexed");
    check(Singleton("A").indexed.sequence()=={0->"A"}.sequence(), "Singleton.indexed");
    check({}.indexed=={}, "Empty.indexed");
    check({for (c in "abc") c}.indexed.sequence()=={0->'a', 1->'b', 2->'c'}.sequence(), "Iterable.indexed");
    check("abc".indexed.sequence()=={0->'a', 1->'b', 2->'c'}.sequence(), "String.indexed");
    check({1,null,2}.indexed.sequence() == {0->1, 2->2}.sequence(), "indexed with nulls");

    //last (defined in ContainerWithFirst but tested here)
    check((1..5000000000).last == 5000000000, "Range.last");
    check(Singleton(1).last == 1, "Singleton.last");
    check([1,2,3,4,5,6,7,8,9,10].last==10, "Sequence.last");
    if (exists l="The very last character".last) {
        check(l=='r', "String.last [1]");
    } else { fail("String.last [1]"); }
    if ("".last exists) {
        fail("String.last [2]");
    }
    if (exists l={for(i in 1..1000) if (i>500) i}.last) {
        check(l==1000, "Iterable.last");
    } else { fail("Iterable.last"); }

    //chain()
    check({1,2}.chain({"a", "b"}).sequence()=={1,2,"a","b"}.sequence(), "Sequence.chain");
    check(Singleton(1).chain({2,3}).sequence()=={1,2,3}.sequence(), "Singleton.chain");
    check((1..3).chain(Singleton(4)).sequence()=={1,2,3,4}.sequence(), "Range.chain");
    check("abc".chain({1,2}).sequence()=={'a', 'b', 'c', 1, 2}.sequence(), "String.chain");
    check("".chain(Singleton(1)).sequence()=={1}.sequence(), "\"\".chain");
    check({}.chain({1,2}).sequence()=={1,2}.sequence(), "Empty.chain");
    check(Array([]).chain({1,2}).sequence()==[1,2], "EmptyArray.chain");
    check(Array{1,2}.chain({3,4}).sequence()=={1,2,3,4}.sequence(), "NonemptyArray.chain");
    check(Singleton(1).chain(Singleton(2)).chain(Singleton("3")).sequence()=={1,2,"3"}.sequence(), "Singletons.chain");
    
    check({}.follow("a").sequence()=={"a"}.sequence(), "Sequence.follow(a) ``{}.follow("a")``");
    check({"b"}.follow("a").sequence()=={"a", "b"}.sequence(), "Sequence.follow(a), 2 ``{"b"}.follow("a")``");

    //group
    /*value grouped = (1..10).group((Integer i) => i%2==0 then "even" else "odd");
    check(grouped.size == 2, "Iterable.group 1");
    if (exists v=grouped["even"]) {
        check(v.size == 5, "Iterable.group 2");
        check(v.every((Integer i) => i%2==0), "Iterable.group 3");
    } else { fail("Iterable.group 2"); }
    check(grouped.defines("odd"), "Iterable.group 4");
    value gr2 = "aBcDeFg".group((Character c) => c.lowercase);
    check(gr2.size == 2, "Iterable.group 5");
    if (exists v=gr2[true]) {
        check(v.size == 4, "Iterable.group 6");
        check(v.every((Character i) => i.lowercase), "Iterable.group 7");
    } else { fail("Iterable.group 6"); }
    check(gr2.defines(false), "Iterable.group 8");*/

    check({for (i in 1..10) i }.shorterThan(11), "Iterable.shorterThan");
    check({for (i in 1..10) i }.longerThan(9), "Iterable.longerThan");

	// string
	value ia = {};
	value ib = { 1, 2, 3, 4, 5 };
	value ic = { 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5 };
	value ix = mapPairs(plus<Integer>, ia, ia);
	{Integer+} iy = mapPairs(plus<Integer>, ib, ib);
	{Integer+} iz = mapPairs(plus<Integer>, ic, ic);
	print(ix);
	print(iy);
    check(ix.string=="{}", "Iterable.string [1]");
    check(iy.string=="{ 2, 4, 6, 8, 10 }", "Iterable.string [2]");
    check(iz.string=="{ 2, 4, 6, 8, 10, 2, 4, 6, 8, 10, 2, 4, 6, 8, 10, 2, 4, 6, 8, 10, 2, 4, 6, 8, 10, 2, 4, 6, 8, 10, ... }", "Iterable.string [3]");
	
    //Iterable-related functions
    check({"aaa", "tt", "z"}.sort(byIncreasing((String s) => s.size)).sequence()=={"z","tt","aaa"}.sequence(), "sort(byIncreasing)");
    check({"z", "aaa", "tt"}.sort(byDecreasing((String s) => s.size)).sequence()=={"aaa","tt","z"}.sequence(), "sort(byDecreasing)");
    Iterable<String> combined = mapPairs((Character c, Integer i) => "comb ``c``+``i``",
                                               "hello", { 1,2,3,4 });
    check(combined.sequence().size==4, "combine [1]");
    check(combined.sequence() == { "comb h+1", "comb e+2", "comb l+3", "comb l+4" }.sequence(), "combine [2]");
    
    check((1..4).fold(0, plus<Integer>)==10, "fold with plus");
    check((1..4).fold(1, times<Integer>)==24, "fold with times");
    check((1..4).reduce(plus<Integer>)==10, "reduce with plus");
    check((1..4).reduce(times<Integer>)==24, "reduce with times");
    
    check({null, "foo", "bar", null}.defaultNullElements(0).sequence()=={0, "foo", "bar", 0}.sequence(), "defaultNullElements [1]");
    check({"foo", null, "bar"}.defaultNullElements("-").sequence()=={"foo", "-", "bar"}.sequence(), "defaultNullElements [2]");
    
    check((0..2).cycle(3).fold(0,plus<Integer>)==9, "cycle");

    //more tests for fold/reduce
    check("1234".fold(5,(Integer a, Character b)=>a+b.integer-48)==15, "String.fold");
    value reducedStringTest = "12345".reduce((Integer|Character a, Character b) {
      switch(a)
      case (is Integer) { return a+b.integer-48; }
      case (is Character) { return a.integer-48+b.integer-48; }
    });
    if (exists reducedStringTest) {
      check(reducedStringTest==15, "String.reduce");
    } else {
      fail("String.reduce returned null");
    }
    
    check({for (i in 1..4) i*i}.reverse()==[16,9,4,1], "iterable reverse");
    
    value itfun = iterable(1, (Integer i) => i<5 then i*2 else finished);
    check([*itfun]==[1,2,4,8], "iterable function ``itfun``");
    
    //check([*(0:5).paired]==[[0,1],[1,2],[2,3],[3,4],[4,5]], "range paired");

    // tests for the laziness-protecting string implementation
    //"simple, laziness-breaking implementation of [[Iterable.string]]"
    //String oldString({Anything*} self) {
    //    String commaList({Anything*} elements) =>
    //            ", ".join { for (element in elements)
    //                        element?.string else "<null>" };
    //    if (self.empty) {
    //        return "{}";
    //    }
    //    else {
    //        String list = commaList(self.take(30));
    //        return "{ `` self.longerThan(30)
    //                    then list + ", ..."
    //                    else list `` }";
    //    }
    //}
    //"counter for accesses to an iterable"
    //variable Integer count = 0;
    //T counting<T>(T t) {
    //    count++;
    //    return t;
    //}
    //for (i in 0..64) {
    //    value cur = { for (t in { "x", null }) counting(t) }.cycled.take(i);
    //    value lazinessProtecting = cur.string;
    //    value expectedCount = min { i, 31 };
    //    check(count == expectedCount, "Iterable.string, evaluate only ``count`` of ``i`` elements  (expecting ``expectedCount``)");
    //    value breakingLaziness = oldString(cur);
    //    // count is now higher than 2 * min { i, 31 }
    //    // because the old string implementation evaluates elements multiple times
    //    check(lazinessProtecting.replace("[]", "{}" /* take(0) returns empty */) == breakingLaziness, "Iterable.string, ``i`` elements");
    //    count = 0;
    //}
}
