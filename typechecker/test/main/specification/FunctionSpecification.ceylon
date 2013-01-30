void specFunction() {
    String f0(String e)();
    f0(String e)() => "hello";
    String f1(String e)();
    f1(String e) => () => e;
    String f2(String e)();
    f2 = (String e)() => e;
    
    Integer g0();
    @error g0() = 1;
    Integer g1();
    @error g1 => () => 1;
    
    Integer x;
    if (true) {
        x=>100;
    }
    else {
        x=>100^100;
    }
    print(x);

    Integer y();
    if (true) {
        y()=>100;
    }
    else {
        y()=>100^100;
    }
    print(y());
    
    Integer z;
    if (true) {
        z=>100;
    }
    @error print(z);

    Integer w();
    if (true) {
    }
    else {
        w()=>100^100;
    }
    @error print(w());
    
}