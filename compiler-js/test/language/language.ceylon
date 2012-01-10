void expect(Equality actual, Equality expected, String text) {
    print(text + ": actual='" + actual.string + "', expected='"
            + expected.string + "' => "
            + ((actual==expected) then "ok" else "NOT OK"));
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
        for (Integer i in l) {
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
    for (Integer i in odds) {
      sum += i;
      for (Integer j in { 2, 4, 6 }) {
        sum += j;
      }
    }
    expect(sum, 45, "nested foreach");
    //key-value
    sum := 0;
    value _entries = { 1->10, 2->20, 3->30 };
    for (Integer idx -> Integer elem in _entries) {
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
    variable Integer sum := 0;
    for (Integer x in r1) {
        sum += x;
    }
    expect(sum, 15, "range iteration");
}

//Another test for the compiler.
void test_interpolate() {
    //print("String part " 1 " interpolation " 2 " works");
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
    test_ranges();
    //test_interpolate();
    print("--- End Language Module Tests ---");
}
