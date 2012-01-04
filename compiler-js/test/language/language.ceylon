void test_largest() {
    print(largest(100,200));
    print(largest(200,100));
}

void test_smallest() {
    print(smallest(100,200));
    print(smallest(200,100));
}

void test_join() {
    value l1 = { "join", 1,2,3};
    value l2 = { 4,5,6 };
    value l3 = {7,8,9};
    print(join(l1, l2, l3));
}

void test_max() {
    value nums = { 2, 4, 6, 8, 7, 250, 5, 3, 1 };
    print(max(nums));
}

void test_min() {
    value nums = { 200, 400, 600, 800, 700, 500, 300, 150 };
    print(min(nums));
}

void test_zip() {
    value keys = { 1, 2, 3, 4, 5, 6 };
    value items = { "one", "two", "three", "four", "five" };
    print(zip(keys, items));
    print(zip(keys, { "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete" }));
}

void test_coalesce() {
    value nulls = { "one", null, "two", null, "three", null, "no nulls..." };
    print(coalesce(nulls));
    print(nulls.item(1)?"item 1 is null");
    print(coalesce(nulls).item(1)?"WTF coalesced item 1 is null");
}

void test_append() {
    print(append({"one", "two" , "three"}, "four"));
}

void test_singleton() {
    value theone = Singleton("the one and only singleton");
    print(theone.item(0)?"WTF Singleton must have one element!");
    print(exists theone.item(1) then "WTF Singleton must ONLY have one element!" else "OK, Singleton has only 1 element");
}

void test_entries() {
    value e = entries("a", "b", "c", "X", "Y", "Z", "1", "2", "3", "d", "e", "f");
    print(e);
    print(e.item(0)?"you should NOT be reading this");
}

//This is actually a test for the compiler. "exists" doesn't work yet.
void test_exists_nonempty() {
    String? yes = "yes";
    String? no = null;
    variable Integer[]? empties := Singleton(1);
    print(exists yes then "yes exists" else "WTF you should NOT be reading this");
    print(exists no then "WTF" else "no doesn't exist");
    print(nonempty empties then "nonempty works" else "nonempty broken");
    print(nonempty Empty then "nonempty is broken" else "like I said, nonempty works");
}

//Another test for the compiler.
void test_interpolate() {
    //print("String part " 1 " interpolation " 2 " works");
}

shared void test_language() {
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
    test_interpolate();
}
