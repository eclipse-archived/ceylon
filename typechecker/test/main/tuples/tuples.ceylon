<String, Integer, Float> triple(String s, Integer i, Float f) {
    return (s, i, f);
}

Float add(<Float,Float> floats=(1.0, 2.0)) {
    return floats.first+floats.rest.first;
}

void test() {
    @type["Tuple<String|Integer|Float,String,Tuple<Integer|Float,Integer,Tuple<Float,Float,Empty>>>"] 
    value tup = triple("hello", 0, 0.0);
    
    @type["String"] value first = tup.first;
    @type["Integer"] value second = tup.rest.first;
    @type["Float"] value third = tup.rest.rest.first;
    @type["Empty"] value nuthin = tup.rest.rest.rest;
    @type["Nothing"] value fourth = tup.rest.rest.rest.first;
    
    @type["Nothing"] value before1 = tup[-1];
    @type["String"] value first1 = tup[0];
    @type["Integer"] value second1 = tup[1];
    @type["Float"] value third1 = tup[2];
    @type["Nothing"] value fourth1 = tup[3];
    @type["Nothing"] value fifth1 = tup[4];
    
    <String, String> hibye = ("hello", "goodbye");
    <String, String> fun() {
        return hibye;
    }
    @type["String"] value hi = hibye.first;
    @type["String"] value bye = hibye.rest.first;
    add((1.0, 2.0));
    Sequence<String> strings = hibye;
    @type["Tuple<String,String,Empty|Sequence<String>>"]
    <String, String...> hibye1 = hibye;
    @type["Empty|Sequence<String>"]
    <String...> hibye2 = hibye;
    <String, Integer, Object...> trip = triple("", 0, 0.0);
    value ints = {1,2,3};
    <String,Integer,Integer...> vartup = ("hello", 4, ints...);
    @type["Nothing"] value v0 = vartup[-1];
    @type["String"] value v1 = vartup[0];
    @type["Integer"] value v2 = vartup[1];
    @type["Nothing|Integer"] value v3 = vartup[2];
    @type["Nothing|Integer"] value v4 = vartup[3];
    @type["Nothing|Integer"] value v5 = vartup[4];
    <> emp1 = {};
    <> emp2 = ();
    Tuple<String,String,Tuple<String,String,<>>> unsugared = Tuple("hello",Tuple("goodbye",()));
    <String,String> sugared = unsugared;
}