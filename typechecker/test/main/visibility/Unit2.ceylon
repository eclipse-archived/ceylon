class Unit2() {

    void print(String s) {}
    
    print(Unit1().hello);
    @error print(Unit1().goodbye);

    print(Unit1().Inner().hello);
    @error print(Unit1().Inner().goodbye);

    class Inner() {
        shared String hello = "Hello";
        String goodbye = "Goodbye";
        void method() {
            say(hello);
            say(goodbye);
        }
        void say(String s) {}
    }

    print(Inner().hello);
    @error print(Inner().goodbye);
        
    @error class Subclass1() extends Unit3() {}
    @error class Subclass2() extends Unit4() {}
    
    @error Unit3 u3 = Unit3();
    @error Unit4 u4 = Unit4();
    
}