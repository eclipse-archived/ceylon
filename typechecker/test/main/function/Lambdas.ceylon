class Lambdas() {
    
    /*void hi() = () print("Hi!");
    void hello(String name) = (String name) print("Hello, " name "!");
    
    hi();
    hello("Gavin");
    
    Float square(Float x) = (Float y) y**2;
    Float cube(Float x) = (Float y) return y**3;*/
    
    void repeat(Integer times, void perform(Integer it)) {
        for (Integer it in 0..times-1) {
            perform(it);
        }
    }
    
    repeat(10, (Integer n) print("Hello"));
    repeat(10, void (Integer n) print("Hello"));
    repeat(10, function (Integer n) print("Hello"));

    @error repeat(10, (Float n) print("Hello"));
    
    function foo(Float bar(Float x)(Integer y)) {
        return bar(1.0)(0);
    }
    
    @type["Float"] foo((Float x)(Integer y) x+y);
    @type["Float"] foo(function (Float x)(Integer y) x+y);
    
    @error foo(void (Float x)(Integer y) print((x+y).string));
    @error foo((Float x)(Float y) x+y);
    
    function baz(Float bar(Float qux(Float x))) {
        return bar((Float x) x);
    }
    
    @type["Float"] baz((Float qux(Float x)) qux(1.0));
    @type["Float"] baz(function (Float qux(Float x)) qux(1.0));

    @error baz((Float qux(Float x)) qux(1.0).integer);
    @error baz((Float qux(Integer x)) qux(1));
    @error baz((Integer qux(Float x)) qux(1.0).float);
}