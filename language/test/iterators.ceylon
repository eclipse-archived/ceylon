class Pair(String one, String two) satisfies Iterable<String> {
    shared actual object iterator satisfies Iterator<String> {
        variable Integer i:=0;
        shared actual String|Finished next() {
            i++;
            if (i==1) { return one; }
            if (i==2) { return two; }
            return exhausted;
        }
    }
    shared actual Boolean empty = false;
}

void iterators() {
    variable value i:=0;
    for (s in Pair("hello", "world")) {
        if (i==0) { assert(s=="hello", "iterator iteration"); }
        if (i==1) { assert(s=="world", "iterator iteration"); }
        i++;
    }
    assert(i==2, "iterator iteration");        
}
