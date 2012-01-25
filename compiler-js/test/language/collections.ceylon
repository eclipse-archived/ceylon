void test_foreach() {
    value list = { 1 ,2 ,3 ,4 ,5 };
    variable Integer sum := 0;
    for (Integer i in list) {
        sum += i;
    }
    expect(sum, 15, "simple foreach");
    Boolean hasEvens(Sequence<Integer> l) {
        variable Boolean found := false;
        for (i in l) {
            if (i % 2 == 0) {
                print("Found an even number");
                found := true;
                break;
            }
        } else {
            print("No even numbers");
        }
        return found;
    }
    value odds = { 1, 3, 5 };
    expect(hasEvens(list), true, "for/else");
    expect(hasEvens(odds), false, "for/else");
    //nested
    sum := 0;
    for (i in odds) {
      sum += i;
      for (Integer j in { 2, 4, 6 }) {
        sum += j;
      }
    }
    expect(sum, 45, "nested foreach");
    //key-value
    sum := 0;
    value _entries = { 1->10, 2->20, 3->30 };
    for (idx -> elem in _entries) {
      sum += idx;
      sum += elem;
    }
    expect(sum, 66, "key-value foreach");
    //with iterator
    sum := 0;
    variable Boolean did_else := false;
    for (idx -> elem in entries(2,4,6)) {
        sum += idx + elem;
    } else {
        did_else := true;
    }
    expect(sum, 15, "foreach with iterator");
    expect(did_else, true, "for/else with iterator");
    for (idx -> elem in entries(2,4,6)) {
        if (idx == 0) { break; }
    } else {
        sum := 0;
    }
    expect(sum, 15, "for/else with iterator");
}

void test_ranges() {
    expect((1..10).string, "1..10", "range.string");
    value r1= 1..5;
    expect(r1.size, 5, "range.size");
    value r2 = 7..4;
    expect(r2.size, 4, "range.size");
    value r3 = -10..-5;
    expect(r3.size, 6, "range.size");
    value r4 = 123..123;
    expect(r4.size, 1, "range.size");
    expect(r1.includes(3), true, "range.includes");
    expect(r1.includes(6), false, "range.includes");
    expect(r2.includes(5), true, "range.includes");
    expect(r2.includes(3), false, "range.includes");
    expect(r4.first, r4.last, "first == last");
    expect(r1 == r2, false, "range.equals");
    expect(r1 == 1..5, true, "range.equals");
    value r1r = r1.rest;
    value r2r = r2.rest;
    expect(r1r.size, r1.size-1, "range.rest");
    expect(r2r.size, r2.size-1, "range.rest");
    expect(r1r.first?0, r1.item(1)?1, "range.rest");
    expect(r2r.first?0, r2.item(1)?1, "range.rest");
    expect(nonempty r4.rest, false, "range.rest");
    expect(r1.lastIndex, 4, "range.lastIndex");
    expect(r2.lastIndex, 3, "range.lastIndex");
    expect(r1.by(2).string, "{ 1, 3, 5 }", "range.by");
    expect(r1.by(3).string, "{ 1, 4 }", "range.by");
    expect(r2.by(2).string, "{ 7, 5 }", "range.by");
    expect(r2.by(3).string, "{ 7, 4 }", "range.by");
    expect(r4.by(10).string, "123..123", "range.by");
    expect(r1.segment(2,2).string, "3..4", "range.segment");
    expect(nonempty r1.segment(1,0), false, "range.segment");
    expect(r1.segment(1,-1).empty, true, "range.segment");
    expect(r1.segment(3,1).string, "4..4", "range.segment");
    expect(r1.segment(0,1).string, "1..1", "range.segment");
    expect(r1.span(1, 3).string, "2..4", "range.span");
    expect(r1.span(3, 1).string, "4..2", "range.span");
    expect(r1.span(2, 2).string, "3..3", "range.span");
    expect(r1.span(3, null).string, "4..5", "range.span");
    expect(r1.span(3, 1000).string, "4..5", "range.span");
    expect(r1.span(0,0).string, "1..1", "range.span");
    expect(!nonempty (1..2).span(4,5), true, "range.span (out of bounds)");
    expect(r1.definesEvery(1,2,3), true, "range.definesEvery");
    expect(r1.definesEvery(4,5,6,7), false, "range.definesEvery");
    expect(r1.definesAny(1,2,3), true, "range.definesAny");
    expect(r1.definesAny(7,6,5), false, "range.definesAny");
    expect(r1.definesAny(6,5,4), true, "range.definesAny");
    variable Integer sum := 0;
    for (Integer x in r1) {
        sum += x;
    }
    expect(sum, 15, "range iteration");
}

void test_arraysequence() {
    value seq1 = { 1, 2, 3, 4, 5};
    value seq2 = { "a", "b", "c", "d", "e" };

    //equals
    //expect(seq1 == seq2, false, "seq.equals");
    //expect(seq1,{1,2,3,4,5}, "seq.equals");
}

void test_iterators() {
    value seq = { 1, 2, 3, 4, 5 };
    value range = 95..100;
    value sing = Singleton(10);
    value iter1 = seq.iterator;
    value iter2 = range.iterator;
    value iter3 = sing.iterator;
    expect(iter1.next(), 1, "seq.iter");
    iter1.next(); iter1.next(); iter1.next();
    expect(iter1.next(), 5, "seq.iter");
    expect(iter1.next(), exhausted, "seq.iter");
    expect(iter1.next(), exhausted, "seq.iter");
    expect(iter2.next(), 95, "range.iter");
    iter2.next(); iter2.next(); iter2.next(); iter2.next();
    expect(iter2.next(), 100, "range.iter");
    expect(iter2.next(), exhausted, "range.iter");
    expect(iter2.next(), exhausted, "range.iter");
    expect(iter3.next(), 10, "singleton.iter");
    expect(iter3.next(), exhausted, "singleton.iter");
    expect(iter3.next(), exhausted, "singleton.iter");
}
