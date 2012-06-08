void testIterables() {
    value s1 = { 1, 2, 3, 4, 5 };
    value s2 = { "Hello", "World" };
    //Map
    assert(s1.map((Integer i) i*2).sequence == { 2, 4, 6, 8, 10 }, "Iterable.map 1");
    assert(s2.map((String s) s.reversed).sequence == { "olleH", "dlroW" }, "Iterable.map 2");
    //Filter
    assert(s1.filter((Integer i) i%2==0).sequence == { 2, 4 }, "Iterable.filter 1");
    assert(s2.filter((String s) "e" in s).sequence == { "Hello" }, "Iterable.filter 2");
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
}
