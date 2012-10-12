String, Integer, Float triple(String s, Integer i, Float f) {
    return s, i, f;
}

void test() {
    @type["Tuple<String,Tuple<Integer,Tuple<Float,Unit>>>"] 
    value tup = triple("hello", 0, 0.0);
    @type["String"] value first = tup.first;
    @type["Integer"] value second = tup.rest.first;
    @type["Float"] value third = tup.rest.rest.first;
    @type["Unit"] value nuthin = tup.rest.rest.rest;
    
    String, String hibye = "hello", "goodbye";
    String, String fun() {
        return hibye;
    }
    @type["String"] value hi = hibye.first;
    @type["String"] value bye = hibye.rest.first;
}