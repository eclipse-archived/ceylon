import assert {...}
//Tests for multiple parameter lists
Comparison multiCompare()(Integer x, Integer y) {
    return x<=>y;
}
String multiFullname(String nombre)(String apat)(String amat) {
    return "" nombre " " apat " " amat "";
}
String multiDefaulted(String name="A")(String apat)(String amat) {
    return "" name " " apat " " amat "";
}
String multiSequenced(String... names)(Integer count) {
    value sb = StringBuilder();
    for (name in names) {
        sb.append(name).append(" ");
    }
    sb.append("count ").append(count.string);
    return sb.string;
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
    assert(multiDefaulted()("B")("C")=="A B C", "Multi defaulted 1");
    Callable<Callable<String,String>,String> md1=multiDefaulted();
    assert(md1("B")("C")=="A B C", "Multi defaulted 2");
    assert(md1("B")("Z")=="A B Z", "Multi defaulted 3");
    assert(md1("Z")("C")=="A Z C", "Multi defaulted 4");
    assert(md1("Y")("Z")=="A Y Z", "Multi defaulted 5");
    function md2(String x)=multiDefaulted()("B");
    assert(md2("C")=="A B C", "Multi defaulted 6");
    assert(md2("Z")=="A B Z", "Multi defaulted 7");
    assert(multiSequenced("A","B","C")(1)=="A B C count 1", "Multi sequenced 1");
    function ms1(Integer c)=multiSequenced("x","y","z");
    assert(ms1(5)=="x y z count 5", "Multi sequenced 2");
    assert(ms1(10)=="x y z count 10", "Multi sequenced 3");
}
