class Test() {
    void print(String s) {}
    @error Inner();
    @error method();
    @error print(attribute);
    String attribute { 
        Inner();
        method();
        @error print(attribute);
        return "hello"; 
    }
    class Inner() {
        Inner();
        method();
        print(attribute);
    }
    void method() {
        Inner();
        method();
        print(attribute);
    }
}

abstract class WithFormals() {
    shared formal String name;
    @error name = "Ceylon";
    shared formal Integer count;
    @error count => 0;
    shared formal variable Float price;
    @error print(price=0.0);
}