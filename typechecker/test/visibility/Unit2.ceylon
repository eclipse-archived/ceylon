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
        
}