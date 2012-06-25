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

  assert({ (1..10).map((Integer i) i.float)... }=={1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}, "map 1");
  assert({ (1..10).filter((Integer i) i>5)... }=={6, 7, 8, 9, 10}, "filter 1");
  assert(((1..10).find((Integer i) i>5) else -1)==6, "find 1");
  assert((1..10).fold(0, (Integer i, Integer j) i+j)==55, "fold 1");
  
  assert({ 1, 3, 7, 10 }.map((Integer i) i.float).sequence=={1.0, 3.0, 7.0, 10.0}, "map 2");
  assert({ 1, 3, 7, 10 }.filter((Integer i) i>5).sequence=={7.0, 10.0}, "filter 2");
  assert(({ 1, 3, 7, 10 }.find((Integer i) i>5) else -1)==7, "find 2");
  assert({ 1, 3, 7, 10 }.fold(1, (Integer i, Integer j) i*j)==210, "fold 3");
  
  assert({}.map((Bottom i) i).empty, "map 3");
  assert({}.filter((Bottom i) true).empty, "filter 3");
  //assert(!exists {}.find((Bottom i) i>5), "find 3");
  assert({}.fold(0, (Integer i, Bottom j) i)==0, "fold 3");
  
  assert(Singleton(5).map((Integer i) i.float).sequence=={5.0}, "map 4");
  assert(Singleton(5).filter((Integer i) i>5).sequence=={}, "filter 4");
  assert(!exists Singleton(5).find((Integer i) i>5), "find 4");
  assert(Singleton(5).fold(0, (Integer i, Integer j) i+j)==5, "fold 4");

  assert({5,4,3,2,1}.sorted((Integer x, Integer y) x<=>y).sequence == {1,2,3,4,5}, "sorted [1]");
  assert({"tt","aaa","z"}.sorted((String a, String b) a<=>b).sequence == {"aaa", "tt", "z"}, "sorted [2]");

  //Iterable-related functions
  assert({"aaa", "tt", "z"}.sorted(byIncreasing((String s) s.size)).sequence=={"z","tt","aaa"}, "sorted(byIncreasing)");
  assert({"z", "aaa", "tt"}.sorted(byDecreasing((String s) s.size)).sequence=={"aaa","tt","z"}, "sorted(byDecreasing)");
}
