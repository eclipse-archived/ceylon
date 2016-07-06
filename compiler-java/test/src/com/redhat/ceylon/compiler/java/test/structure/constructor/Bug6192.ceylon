shared void bug6192() {
    variable Anything o = null;
    class C {
        //value x => o; // workaround
        o = "a";
        shared new () {
            o = "b";
        }
    }
    C();
    print(o);
    assert(o exists);
}