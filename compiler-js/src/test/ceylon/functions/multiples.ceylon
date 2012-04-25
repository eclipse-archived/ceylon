import assert {...}
//Tests for multiple parameter lists
Comparison multiCompare()(Integer x, Integer y) {
    return x<=>y;
}
String multiFullname(String nombre)(String apat)(String amat) {
    return "" nombre " " apat " " amat "";
}

void testMultipleParamLists() {
    print("Testing multiple parameter lists...");
    assert(multiCompare()(1,1)==equal,   "Multi compare 1");
    assert(multiCompare()(1,2)==smaller, "Multi compare 2");
    assert(multiCompare()(2,1)==larger,  "Multi compare 3");
    function comp(Integer a, Integer b)=multiCompare();
    assert(comp(1,1)==equal,   "Multi compare 4");
    assert(comp(1,2)==smaller, "Multi compare 5");
    assert(comp(2,1)==larger,  "Multi compare 6");
    assert(multiFullname("a")("b")("c") == "a b c", "Multi fullname 1");
    function apat(String c)=multiFullname("A")("B");
    assert(apat("C") == "A B C", "Multi fullname 2");
    function nombre(String name)=multiFullname("Name");
    assert(is Callable<Callable<String,String>,String> nombre("Z"), "Multi callable 1");
    assert(nombre("Z")("L")=="Name Z L", "Multi callable 2");
}
