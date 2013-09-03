void bug1270() {
    variable [Integer*] ints = [];
    ints = [1, *ints];
    print(ints);
    ints.sort((Integer x, Integer y) => x <=> y);
}