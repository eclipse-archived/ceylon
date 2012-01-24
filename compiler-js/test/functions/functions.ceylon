shared void helloWorld() {
    print("hello world");
}

shared void hello(String name) {
    print("hello" + name);
}

shared void helloAll(String... names) {}

shared String toString(Object obj) {
    return obj.string;
}

shared Float add(Float x, Float y) {
    return x+y;
}

shared void repeat(Integer times, void f(Integer iter)) {
    f(0);
}

shared void test() {
    helloWorld();
    hello("test");
    helloAll("Gavin", "Enrique", "Ivo");
    toString(5);
    add(1.5, 2.5);
    //repeat(5, void p(Integer x) { print(x); });
}
