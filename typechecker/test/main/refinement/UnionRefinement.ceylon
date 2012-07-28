void unionRefinement() {
    value ints1 = { 1, 2 };
    print(ints1.first);
    Integer[] ints2 = { 1, 2 };
    print(ints2.first else "");
    Sequence<Integer>|Empty ints3 = { 1, 2 };
    print(ints3.first else "");
}