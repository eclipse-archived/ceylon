public void test(Process p) {
    Sequence<Natural> s = ArrayList.arrayListOf(1, 2, 3, 4);

    mutable Iterator<Natural> it = s.iterator();
    while (exists Natural i = it.head()) {
        p.write(""i" ");
        it := it.tail();
    }
    p.writeLine("");
}
