@noanno
void spreadIntoIterable() {
    List<String> list = ["a", "b", "c"];//Arrays.asList("hello", "world");
    variable value it = {*list};
    assert(it.size == 3);
    assert(!it.longerThan(4));
    assert(it.shorterThan(4));
    assert(!it.longerThan(3));
    assert(!it.shorterThan(3));
    assert(it.longerThan(2));
    assert(!it.shorterThan(2));
    assert(it.sequence() == list);
    
    it = {"a", *list};
    assert(it.size == 4);
    assert(!it.longerThan(5));
    assert(it.shorterThan(5));
    assert(!it.longerThan(4));
    assert(!it.shorterThan(4));
    assert(it.longerThan(3));
    assert(!it.shorterThan(3));
    assert(it.sequence() == ["a", "a", "b", "c"]);
    
    it = {"a", "b"};
    assert(it.size == 2);
    assert(!it.longerThan(3));
    assert(it.shorterThan(3));
    assert(!it.longerThan(2));
    assert(!it.shorterThan(2));
    assert(it.longerThan(1));
    assert(!it.shorterThan(1));
    assert(it.sequence() == ["a", "b"]);
    
    value it2 = {true, 1, -1, 1.0, -1.0, 'c', ""};
    
    value b = "b";
    it = {b, *list};
    
    variable value jj = 0;
    value it3 = {for (x in 1..3) x+jj++};
    print(it3);
    print(it3);
    jj = 0;
    value it4 = {jj, for (x in 1..3) x+jj++};
    print(it4);
    print(it4);
}