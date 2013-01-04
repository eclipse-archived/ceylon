void unionRefinement() {
    value ints1 = [ 1, 2 ];
    print(ints1.first);
    Integer[] ints2 = [ 1, 2 ];
    print(ints2.first else "");
    Sequence<Integer>|Empty ints3 = [ 1, 2 ] of Sequence<Integer>|Empty;
    Empty|Sequence<Integer> ints4 = [ 1, 2 ] of Sequence<Integer>|Empty;
    print(ints3.first else "");
    print(ints4.first else "");
}
