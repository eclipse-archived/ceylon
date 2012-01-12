void expect(Equality actual, Equality expected, String text) {
    print(text + ": actual='" + actual.string + "', expected='"
            + expected.string + "' => "
            + ((actual==expected) then "ok" else "FAIL"));
}

void test_largest() {
    expect(largest(100,200), 200, "largest");
    expect(largest(200,100), 200, "largest");
}

void test_smallest() {
    expect(smallest(100,200), 100, "smallest");
    expect(smallest(200,100), 100, "smallest");
}

void test_join() {
    value l1 = { "join", 1,2,3};
    value l2 = { 4,5,6 };
    value l3 = {7,8,9};
    value joint = join(l1, l2, l3);
    print(joint);
    expect(joint.size, l1.size+l2.size+l3.size, "join");
}

void test_max() {
    value nums = { 2, 4, 6, 8, 7, 250, 5, 3, 1 };
    expect(max(nums), 250, "max");
}

void test_min() {
    value nums = { 200, 400, 600, 800, 700, 500, 300, 150 };
    expect(min(nums), 150, "min");
}

void test_zip() {
    value keys = { 1, 2, 3, 4, 5, 6 };
    value items = { "one", "two", "three", "four", "five" };
    value z1 = zip(keys, items);
    value z2 = zip(keys, { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete" });
    print(z1);
    print(z2);
    expect(z1.size, 5, "zip");
    expect(z2.size, 6, "zip");
}

void test_coalesce() {
    value nulls = { "one", null, "two", null, "three", null, "no nulls..." };
    print(coalesce(nulls));
    expect(nulls.item(1)?"null", "null", "coalesce");
    expect(coalesce(nulls).item(1)?"null", "two", "coalesce");
}

void test_append() {
    expect(append({"one", "two" , "three"}, "four").size, 4, "append");
}

void test_singleton() {
    value theone = Singleton("the one and only singleton");
    expect(theone.size, 1, "singleton");
    expect(theone.item(0)?"null", "the one and only singleton", "singleton");
    expect(theone.item(1)?"null", "null", "singleton");
}

void test_entries() {
    value e = entries("a", "b", "c", "X", "Y", "Z", "1", "2", "3", "d", "e", "f");
    value _e = Entry(-1, "null");
    print(e);
    expect((e.item(2)?_e).key, 2, "entries");
    expect((e.item(2)?_e).item, "c", "entries");
}

//This is actually a test for the compiler. "exists" doesn't work yet.
void test_exists_nonempty() {
    String? yes = "yes";
    String? no = null;
    variable Integer[]? empties := Singleton(1);
    print(exists yes then "yes exists" else "WTF you should NOT be reading this");
    print(exists no then "WTF" else "no doesn't exist");
    print(nonempty empties then "nonempty works" else "nonempty broken");
    print(nonempty {} then "nonempty is broken" else "like I said, nonempty works");
}

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
    expect(r1.by(2).string, "1,3,5", "range.by");
    expect(r1.by(3).string, "1,4", "range.by");
    expect(r2.by(2).string, "7,5", "range.by");
    expect(r2.by(3).string, "7,4", "range.by");
    expect(r4.by(10).string, "123..123", "range.by");
    expect(r1.segment(2,2).string, "3..4", "range.segment");
    expect(nonempty r1.segment(1,0), false, "range.segment");
    expect(r1.segment(3,1).string, "4..4", "range.segment");
    expect(r1.segment(0,1).string, "1..1", "range.segment");
    expect(r1.span(1, 3).string, "2..4", "range.span");
    expect(r1.span(3, 1).string, "4..2", "range.span");
    expect(r1.span(2, 2).string, "3..3", "range.span");
    expect(r1.span(3, null).string, "4..5", "range.span");
    expect(r1.span(3, 1000).string, "4..5", "range.span");
    expect(r1.span(0,0).string, "1..1", "range.span");
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
    expect(seq1.size, 5, "seq.size");
    expect(seq1.first, 1, "seq.first");
    expect(seq2.last, "e", "seq.last");
    //segment
    expect(seq1.segment(2,2).string, "3,4", "seq.segment");
    expect(nonempty seq1.segment(3,0), false, "seq.segment");
    expect(seq1.segment(3,3).string, "4,5", "seq.segment");
    expect(seq1.segment(0,1).string, "1", "seq.segment");
    //empty
    expect(seq1.empty, false, "seq.empty");
    expect(seq1.segment(1,0).empty, true, "seq.empty");
    expect(seq2.lastIndex, 4, "seq.lastIndex");
    //defines
    expect(seq2.defines(3), true, "seq.defines");
    expect(seq2.defines(5), false, "seq.defines");
    expect(seq1.defines(4), true, "seq.defines");
    expect(seq1.definesEvery(1,2,3), true, "seq.definesEvery");
    expect(seq1.definesEvery(4,5,6), false, "seq.definesEvery");
    expect(seq1.definesAny(6,7,8), false, "seq.definesAny");
    expect(seq1.definesAny(6,5,4), true, "seq.definesAny");
    //span
    expect(seq1.span(1, 3).string, "2,3,4", "seq.span");
    expect(seq1.span(3, 1).string, "4,3,2", "seq.span");
    expect(seq1.span(2, 2).string, "3", "seq.span");
    expect(seq1.span(3, null).string, "4,5", "seq.span");
    expect(seq1.span(3, 1000).string, "4,5", "seq.span");
    expect(seq1.span(0,0).string, "1", "seq.span");
    //rest
    expect(seq1.rest.string, "2,3,4,5", "seq.rest");
    expect({1}.rest.string, "", "seq.rest");
    expect(nonempty {1}.rest, false, "seq.rest");
    //items
    expect(seq1.items(1,3,2).string, "2,4,3", "seq.items");
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
    expect(iter1.next(), finished, "seq.iter");
    expect(iter1.next(), finished, "seq.iter");
    expect(iter2.next(), 95, "range.iter");
    iter2.next(); iter2.next(); iter2.next(); iter2.next();
    expect(iter2.next(), 100, "range.iter");
    expect(iter2.next(), finished, "range.iter");
    expect(iter2.next(), finished, "range.iter");
    expect(iter3.next(), 10, "singleton.iter");
    expect(iter3.next(), finished, "singleton.iter");
    expect(iter3.next(), finished, "singleton.iter");
}

//Another test for the compiler.
void test_interpolate() {
    //print("String part " 1 " interpolation " 2 " works");
}

void testCharacter() {
    Character c1 = `A`;
    //Character c2 = `𝄞`;
    //Character c3 = `Ũ`;
    expect(c1.string, "A", "Character.string");
    //expect(c2.string, "𝄞", "Character.string");
    //expect(c3.string, "Ũ", "Character.string");
    //expect(`Ä`.lowercased, `ä`, "Character.lowercased");
    //expect(`x`.lowercased, `x`, "Character.lowercased");
    //expect(`ö`.uppercased, `Ö`, "Character.uppercased");
    //expect(`#`.uppercased, `#`, "Character.uppercased");
}

void testString() {
    expect("".empty, true, "String.empty");
    expect("x".empty, false, "String.empty");
    expect("".size, 0, "String.size");
    String s1 = "abc";
    String s2 = "ä€Ũ\t";
    String s3 = "A𝄞`ŨÖ";
    expect(s1.size, 3, "String.size");
    expect(s2.size, 4, "String.size");
    expect(s3.size, 5, "String.size");
    expect((s1+s2).size, 7, "String.size");
    expect((s1+s3).size, 8, "String.size");
    
    expect("".shorterThan(0), false, "String.shorterThan");
    expect("".shorterThan(1), true, "String.shorterThan");
    expect("abc".shorterThan(3), false, "String.shorterThan");
    expect("abc".shorterThan(4), true, "String.shorterThan");
    expect("".longerThan(0), false, "String.longerThan");
    expect("x".longerThan(0), true, "String.longerThan");
    expect("abc".longerThan(3), false, "String.longerThan");
    expect("abc".longerThan(2), true, "String.longerThan");
}

shared void test() {
    print("--- Start Language Module Tests ---");
    test_largest();
    test_smallest();
    test_max();
    test_min();
    test_join();
    test_zip();
    test_coalesce();
    test_append();
    test_singleton();
    test_entries();
    test_exists_nonempty();
    test_foreach();
    test_arraysequence();
    test_iterators();
    test_ranges();
    //test_interpolate();
    testCharacter();
    testString();
    print("--- End Language Module Tests ---");
}
