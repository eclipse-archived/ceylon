shared void bug2124() {
    Object x = 'a':26;
    assert(!x is Tuple<Anything,Anything,Sequential<Anything>>);
    Object y = Exception();
    assert(y is Exception);
}