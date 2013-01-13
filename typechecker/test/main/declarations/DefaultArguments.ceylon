class DefiningClass() {
    String defaultName = "gavin";
    void method1(String name = defaultName) {}
    void method2(String name = this.defaultName) {}
    void method3(String name, String nickname = name) {}
}

class DefaultedAndSequenced() {
    void m(String s, Integer i = 1, @error Boolean* b = [true, false]) {}
    m("hello");
    m("hello", 2);
    m("hello", 2, *{});
    m("hello", 2, true);
    m("hello", 2, true, false);
}

interface Barface {
    shared formal Integer prop;
    shared formal void f1(Integer n = 5);
    shared formal void f2(Integer n, String s = "test");
    shared formal void f3(Integer n = 5, Integer m = n);
    shared formal void f4(Integer n = 5, Integer m = n + 1);
}

class FunctionalParameterDefaults(
    Integer f(Integer i)=>i,
    void k(Float f)=>print(f), 
    @error Integer h(Float f)=>f, 
    @error void g()=>"hello") {
}