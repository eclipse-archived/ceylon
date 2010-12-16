public void test(Process p) {
    Integer size = 10;

    ArrayList<Integer> a = ArrayList<Integer>();

    mutable Integer i := 0;
    while (i < size) {
        a[i] := i;
        i++;
    }

    while (--i >= 0) {
        p.writeLine(i);
    }

    p.writeLine("");

    mutable Iterator<Integer> it = a.iterator();
    while (exists Integer value = it.head()) {
        p.writeLine(value);
        it := it.tail();
    }
}
