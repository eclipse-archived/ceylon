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