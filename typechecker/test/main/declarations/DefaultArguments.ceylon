class DefiningClass() {
    String defaultName = "gavin";
    void method1(String name = defaultName) {}
    void method2(String name = this.defaultName) {}
    void method3(String name, String nickname = name) {}
}

class DefaultedAndSequenced() {
    void m(String s, Integer i = 1, Boolean... b = {true, false}) {}
    m("hello");
    m("hello", 2);
    m("hello", 2, {}...);
    m("hello", 2, true);
    m("hello", 2, true, false);
}