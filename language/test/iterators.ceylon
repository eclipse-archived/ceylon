class Pair(String one, String two) satisfies Iterable<String> {
    shared actual object iterator satisfies Iterator<String> {
        variable Integer i:=0;
        shared actual String|Finished next() {
            i++;
            if (i==1) { return one; }
            if (i==2) { return two; }
            return exhausted;
        }
    }
    shared actual Boolean empty = false;
}

void test_foreach() {
    value list = { 1 ,2 ,3 ,4 ,5 };
    variable Integer sum := 0;
    for (Integer i in list) {
        sum += i;
    }
    assert(sum==15, "simple foreach");
    Boolean hasEvens(Sequence<Integer> l) {
        variable Boolean found := false;
        for (i in l) {
            if (i % 2 == 0) {
                found := true;
                break;
            }
        } else {
            print("No even numbers");
        }
        return found;
    }
    value odds = { 1, 3, 5 };
    assert(hasEvens(list), "for/else 1");
    assert(!hasEvens(odds), "for/else 2");
    assert(hasEvens({1,3,5,2}),"for/else 3");
    assert(hasEvens({1,3,2,5}),"for/else 4");
    //nested
    sum := 0;
    for (i in odds) {
      sum += i;
      for (Integer j in { 2, 4, 6 }) {
        sum += j;
      }
    }
    assert(sum==45, "nested foreach");
    //key-value
    sum := 0;
    value _entries = { 1->10, 2->20, 3->30 };
    for (idx -> elem in _entries) {
      sum += idx;
      sum += elem;
    }
    assert(sum==66, "key-value foreach");
    //with iterator
    sum := 0;
    variable Boolean did_else := false;
    for (idx -> elem in entries(2,4,6)) {
        sum += idx + elem;
    } else {
        did_else := true;
    }
    assert(sum==15, "foreach with iterator");
    assert(did_else, "for/else with iterator");
    for (idx -> elem in entries(2,4,6)) {
        if (idx == 0) { break; }
    } else {
        sum := 0;
    }
    assert(sum==15, "for/else with iterator");
}

void iterators() {
    variable value i:=0;
    for (s in Pair("hello", "world")) {
        if (i==0) { assert(s=="hello", "iterator iteration"); }
        if (i==1) { assert(s=="world", "iterator iteration"); }
        i++;
    }
    assert(i==2, "iterator iteration");

    //more tests, from ceylon-js
    value seq = { 1, 2, 3, 4, 5 };
    value range = 95..100;
    value sing = Singleton(10);
    value iter1 = seq.iterator;
    value iter2 = range.iterator;
    value iter3 = sing.iterator;
    assert(iter1.next()==1, "seq.iter");
    iter1.next(); iter1.next(); iter1.next();
    assert(iter1.next()==5, "seq.iter");
    assert(iter1.next()==exhausted, "seq.iter");
    assert(iter1.next()==exhausted, "seq.iter");
    assert(iter2.next()==95, "range.iter");
    iter2.next(); iter2.next(); iter2.next(); iter2.next();
    assert(iter2.next()==100, "range.iter");
    assert(iter2.next()==exhausted, "range.iter");
    assert(iter2.next()==exhausted, "range.iter");
    assert(iter3.next()==10, "singleton.iter");
    assert(iter3.next()==exhausted, "singleton.iter");
    assert(iter3.next()==exhausted, "singleton.iter");

    print("Testing for/else loops");
    test_foreach();

    //Test ChainedIterator
    value chained = ChainedIterator({1},{2});
    if (is Integer ii=chained.next()) {
        assert(ii==1, "ChainedIterator [1]");
    } else { fail("ChainedIterator [1]");}
    if (is Integer ii=chained.next()) {
        assert(ii==2, "ChainedIterator [2]");
    } else { fail("ChainedIterator [2]");}
    assert(is Finished chained.next(), "ChainedIterator [3]");
}
