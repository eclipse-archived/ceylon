class Test() {
    @error Inner();
    @error method();
    @error attribute;
    String attribute { 
        Inner();
        method();
        attribute;
        return "hello"; 
    }
    class Inner() {
        Inner();
        method();
        attribute;
    }
    void method() {
        Inner();
        method();
        attribute;
    }
}