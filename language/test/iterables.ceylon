void testIterables() {
    value s1 = { 1, 2, 3, 4, 5 };
    value s2 = { "Hello", "World" };
    //Map
    assert(s1.map((Integer i) i*2).sequence == { 2, 4, 6, 8, 10 }, "Iterable.map 1");
    assert(s2.map((String s) s.reversed).sequence == { "olleH", "dlroW" }, "Iterable.map 2");
    assert("hola".map((Character c) c.uppercased).sequence == {`H`, `O`, `L`, `A`}, "String.map");

    //Filter
    assert(s1.filter((Integer i) i%2==0).sequence == { 2, 4 }, "Iterable.filter 1");
    assert(s2.filter((String s) "e" in s).sequence == { "Hello" }, "Iterable.filter 2");
    assert("h o l a".filter((Character c) c.letter) == "hola", "String.filter");

    //Fold
    assert(s1.fold(0, (Integer a, Integer b) a+b) == 15, "Iterable.fold 1");
    assert(s2.fold(1, (Integer a, String b) a+b.size) == 11, "Iterable.fold 2");
    //Find
    if (exists four = s1.find((Integer i) i>3)) {
        assert(four == 4, "Iterable.find 1");
    } else { fail("Iterable.find 1"); }
    if (exists s = s2.find((String s) s.size>5)) {
        fail("Iterable.find 2");
    }
    if (exists s = s2.find((String s) "r" in s)) {
        assert(s == "World", "Iterable.find 3");
    } else { fail("Iterable.find 3"); }
    if (exists c = "hola!".find((Character c) !c.letter)) {
        assert(c == `!`, "String.find");
    } else { fail("String.find"); }
    //FindLast
    if (exists four = s1.findLast((Integer i) i>3)) {
        assert(four == 5, "Iterable.findLast 1");
    } else { fail("Iterable.find 1"); }
    if (exists s = s2.findLast((String s) s.size>5)) {
        fail("Iterable.findLast 2");
    }
    if (exists s = s2.findLast((String s) "o" in s)) {
        assert(s == "World", "Iterable.findLast 3");
    } else { fail("Iterable.findLast 3"); }
    if (exists c = "hola!".findLast((Character c) c.letter)) {
        assert(c == `a`, "String.findLast");
    } else { fail("String.findLast"); }

    assert({ (1..10).map((Integer i) i.float)... }=={1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, "map 1");
    assert({ (1..10).filter((Integer i) i>5)... }=={6, 7, 8, 9, 10}, "filter 1");
    assert(((1..10).find((Integer i) i>5) else -1)==6, "find 1");
    assert(((1..10).findLast((Integer i) i>5) else -1)==10, "findLast 1");
    assert((1..10).fold(0, (Integer i, Integer j) i+j)==55, "fold 1");

    assert({ 1, 3, 7, 10 }.map((Integer i) i.float).sequence=={1.0, 3.0, 7.0, 10.0}, "map 2");
    assert({ 1, 3, 7, 10 }.filter((Integer i) i>5).sequence=={7.0, 10.0}, "filter 2");
    assert(({ 1, 3, 7, 10 }.find((Integer i) i>5) else -1)==7, "find 2");
    assert(({ 1, 3, 7, 10 }.findLast((Integer i) i>5) else -1)==10, "findLast 2");
    assert({ 1, 3, 7, 10 }.fold(1, (Integer i, Integer j) i*j)==210, "fold 3");
 
    //Empty optimized implementations
    Integer[] vacio = {};
    assert(vacio.map((Integer i) i).empty, "empty.map");
    assert(vacio.filter((Integer i) true).empty, "empty.filter");
    assert(!exists vacio.find((Integer i) i>5), "find 3");
    assert(!exists vacio.findLast((Integer i) i>5), "findLast 3");
    assert(vacio.fold(0, (Integer i, Integer j) i)==0, "empty.fold");
    assert(vacio.sorted((Integer a, Integer b) larger).sequence=={}, "empty.sorted");
    assert(!vacio.every((Integer x) true), "empty.every");
    assert(!vacio.any((Integer x) true), "empty.any");
    assert(vacio.skipping(1).sequence=={}, "empty.skipping");
    assert(vacio.taking(1).sequence=={}, "empty.taking");
 
    //Singleton optimized implementations 
    assert(Singleton(5).map((Integer i) i.float).sequence=={5.0}, "Singleton.map");
    assert(Singleton(5).filter((Integer i) i>5).sequence=={}, "Singleton.filter");
    assert(!exists Singleton(5).find((Integer i) i>5), "Singleton.find");
    assert(!exists Singleton(5).findLast((Integer i) i>5), "Singleton.findLast");
    assert(Singleton(5).fold(0, (Integer i, Integer j) i+j)==5, "Singleton.fold");
    assert(Singleton(5).sorted((Integer x, Integer y) x<=>y) == Singleton(5), "Singleton.sorted");
    assert(Singleton(1).any((Integer x) x == 1), "Singleton.any");
    assert(Singleton(1).every((Integer x) x>0), "Singleton.every");
    assert(Singleton(1).skipping(0).sequence=={1}, "Singleton.skipping [1]");
    assert(Singleton(1).skipping(1).sequence=={}, "Singleton.skipping [2]");
    assert(Singleton(1).skipping(9).sequence=={}, "Singleton.skipping [3]");
    assert(Singleton(1).taking(5).sequence=={1}, "Singleton.taking");
    assert(Singleton(1).by(1).sequence=={1}, "Singleton.by [1]");
    assert(Singleton(1).by(5).sequence=={1}, "Singleton.by [2]");
    //Let's test by(0) with Singleton
    /*value endlessIter = Singleton(1).by(0).iterator;
    for (i in 1..1000) {
        if (is Finished endlessIter.next()) { fail("Singleton.by(0)"); }
    }*/

    //Any
    assert( (1..10).any((Integer x) x==9), "Iterable.any [1]");
    assert( !(1..10).any((Integer x) x%9==9), "Iterable.any [2]");
    assert("hello world".any((Character c) c.whitespace), "Iterable.any [3]");

    //Every
    assert( (1..10).every((Integer x) x<=10), "Iterable.every [1]");
    assert( {2,4,6,8,10}.every((Integer x) x%2==0), "Iterable.every [2]");
    assert( "hello".every((Character c) c.lowercase), "Iterable.every [3]");
    assert( !"Hello".every((Character c) c.lowercase), "Iterable.every [3]");

    //Sorted
    assert({5,4,3,2,1}.sorted((Integer x, Integer y) x<=>y).sequence == {1,2,3,4,5}, "sorted [1]");
    assert({"tt","aaa","z"}.sorted((String a, String b) a<=>b).sequence == {"aaa", "tt", "z"}, "sorted [2]");
    assert("hola".sorted((Character a, Character b) a<=>b) == "ahlo", "String.sorted");

    //Skipping
    assert({1,2,3,4,5}.skipping(3).sequence=={4,5}, "skipping [1]");
    assert(!nonempty {1,2,3,4,5}.skipping(9).sequence, "skipping [2]");
    assert((1..10).skipping(5)==6..10, "Range.skipping [3]");
    assert(!nonempty (1..5).skipping(9).sequence, "skipping [4]");
    assert((5..1).skipping(2)==3..1, "Range.skipping [5]");
    assert("hola".skipping(2)=="la", "String.skipping");
    assert(elements(for(i in 1..10) i).skipping(8).sequence=={9,10}, "comprehension.skipping");

    //Taking
    assert({1,2,3,4,5}.taking(3).sequence=={1,2,3}, "taking [1]");
    assert(!nonempty {1,2,3,4,5}.taking(0).sequence, "taking [2]");
    assert((1..10).taking(5)==1..5, "Range.taking [3]");
    assert(!nonempty (1..5).taking(0).sequence, "Range.taking [4]");
    assert((1..10).taking(100)==1..10, "Range.taking [5]");
    assert({1,2,3,4,5}.taking(100).sequence=={1,2,3,4,5}, "taking [6]");
    assert((5..1).taking(3)==5..3, "Range.taking [7]");
    assert("hola".taking(2)=="ho", "String.taking");
    assert(elements(for (i in 1..10) i).taking(2).sequence=={1,2}, "comprehension.taking");

    //By
    assert({1,2,3,4,5}.by(1).sequence=={1,2,3,4,5}, "by [1]");
    assert({1,2,3,4,5}.by(2).sequence=={1,3,5}, "by [2]");
    assert({1,2,3,4,5}.by(3).sequence=={1,4}, "by [3]");
    assert({1,2,3,4,5}.by(4).sequence=={1,5}, "by [4]");
    assert({1,2,3,4,5}.by(5).sequence=={1,2,3,4,5}.by(9).sequence, "by [5]");
    assert("AaEeIiOoUu".by(2)=="AEIOU", "String.by [1]");
    assert("1234567890".by(3)=="1470", "String.by [2]");
    assert("1234567890".by(4)=="159", "String.by [3]");
    assert("1234567890".by(5)=="16", "String.by [4]");
    assert("1234567890".by(8)=="19", "String.by [5]");
    assert("1234567890".by(11)=="1", "String.by [6]");
    assert((1..10).by(2).sequence=={1,3,5,7,9}, "Range.by [1]");
    assert((10..1).by(2).sequence=={10,8,6,4,2}, "Range.by [2]");
    assert((1..10).by(6).sequence=={1,7}, "Range.by [3]");
    assert((1..10).by(100).sequence=={1}, "Range.by [4]");
    assert(elements(for(i in 1..10) i).by(4).sequence=={1,5,9}, "comprehension.by");

    //Count
    assert((1..10).count((Integer x) x%2==0)==5, "Range.count");
    assert({1,2,3,4,5}.count((Integer x) x%2==0)==2, "Sequence.count");
    assert(elements(for (i in 1..10) i).count(greaterThan(7))==3, "Iterable.count (greaterThan)");
    assert(elements(for (i in 1..10) i).count(lessThan(7))==6, "Iterable.count (lessThan)");
    assert(array(1,2,3,4,5).count((Integer x) x%2==1)==3, "Array.count");
    assert("AbcdEfghIjklmnOp".count((Character c) c.uppercase)==4, "String.count");
    assert(Singleton(1).count(equalTo(1))==1, "Singleton.count (equalTo)");

    //coalesced
    assert((1..10).coalesced == 1..10, "Range.coalesced");
    assert({1,2,3,null,4,5}.coalesced.sequence=={1,2,3,4,5}, "Sequence.coalesced");
    assert(string(elements(for (c in "HoLa") c.uppercase then c else null).coalesced...)=="HL", "Iterable.coalesced");
    assert(array(1,2,3,null,5).coalesced.sequence=={1,2,3,5}, "Array.coalesced");
    assert(Singleton("X").coalesced==Singleton("X"), "Singleton.coalesced [1]");
    assert("ABC".coalesced=="ABC", "String.coalesced");
    assert({}.coalesced=={}, "Empty.coalesced");

    //indexed
    for (k->v in (1..5).indexed) {
        assert(k+1==v, "Range.indexed");
    }
    assert({"a", "b", "c"}.indexed.sequence=={0->"a", 1->"b", 2->"c"}, "Sequence.indexed");
    assert(array(0, 1, 2).indexed.sequence=={0->0, 1->1, 2->2}, "Array.indexed");
    assert(Singleton("A").indexed.sequence=={0->"A"}, "Singleton.indexed");
    assert({}.indexed=={}, "Empty.indexed");
    assert(elements(for (c in "abc") c).indexed.sequence=={0->`a`, 1->`b`, 2->`c`}, "Iterable.indexed");

    //last (defined in ContainerWithFirst but tested here)
    assert((1..5000000000).last == 5000000000, "Range.last");
    assert(Singleton(1).last == 1, "Singleton.last");
    value oneToTen=array(1,2,3,4,5,6,7,8,9,10);
    if (exists l=oneToTen.last) {
        assert(l==10, "Array.last");
    } else { fail("Array.last"); }
    assert({1,2,3,4,5,6,7,8,9,10}.last==10, "Sequence.last");
    if (exists l="The very last character".last) {
        assert(l==`r`, "String.last [1]");
    } else { fail("String.last [1]"); }
    if (exists "".last) {
        fail("String.last [2]");
    }
    if (exists l=elements(for(i in 1..1000) i).last) {
        assert(l==1000, "Iterable.last");
    } else { fail("Iterable.last"); }

    //Iterable-related functions
    assert({"aaa", "tt", "z"}.sorted(byIncreasing((String s) s.size)).sequence=={"z","tt","aaa"}, "sorted(byIncreasing)");
    assert({"z", "aaa", "tt"}.sorted(byDecreasing((String s) s.size)).sequence=={"aaa","tt","z"}, "sorted(byDecreasing)");
}
