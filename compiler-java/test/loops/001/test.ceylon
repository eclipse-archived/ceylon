public void test(Process p) {
    Natural size = 12;

    ArrayList<Integer> a = ArrayList<Integer>();

    mutable Natural n := 0;
    while (n < size) {
        a[n] := fib(n);
        n++;
    }

    for (Integer i in a) {
        p.writeLine(i);
    }
}
