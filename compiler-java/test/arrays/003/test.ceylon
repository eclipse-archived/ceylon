public void test(Process p) {
    // This Sequence initializer is just a kludge until we have
    // varargs and named parameter declarations.
    Natural[] s = ArrayList.arrayListOf(1, 2, 3, 4);

    mutable Iterator<Natural> it = s.iterator();
    while (exists Natural i = it.head()) {
        p.write(""i" ");
        it := it.tail();
    }
    p.writeLine("");
}
