void specFunction() {
    String f0(String e)();
    f0(String e)() => "hello";
    String f1(String e)();
    f1(String e) => () e;
    String f2(String e)();
    f2 = (String e)() e;
    
    Integer g0();
    @error g0() = 1;
    Integer g1();
    @error g1 => () 1;
}