class Pair(String one, String two) satisfies Iterable<String> {
    shared actual object iterator satisfies Iterator<String> {
        shared actual String head = one;
        shared actual object tail satisfies Iterator<String> {
            shared actual String head = two;
            shared actual Iterator<String>? tail = null;
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
