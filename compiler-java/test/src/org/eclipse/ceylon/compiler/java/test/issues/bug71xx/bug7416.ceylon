void bug7416() {
    print(Bug7416(1) < 5);
}

class Bug7416(Integer i) satisfies Comparable<Integer> {
    compare(Integer o) => i <=> o;
}