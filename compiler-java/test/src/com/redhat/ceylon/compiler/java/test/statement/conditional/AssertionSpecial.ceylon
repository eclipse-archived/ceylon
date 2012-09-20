void run() {
    String|Integer v1 = 5;
    assert(is Integer a1 = v1);
    print(-a1);
    String? v2 = "X";
    assert(exists a2 = v2);
    print(a2.size);
    Integer[] v3 = {1,2,3};
    assert(nonempty a3 = v3);
    print(a3.size);
}
