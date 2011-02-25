class Unit2() {

    Unit1().hello;
    @error Unit1().goodbye;

    Unit1().Inner().hello;
    @error Unit1().Inner().goodbye;

    class Inner() {
        shared String hello = "Hello";
        String goodbye = "Goodbye";
        void method() {
            say(hello);
            say(goodbye);
        }
        void say(String s) {}
    }

    Inner().hello;
    @error Inner().goodbye;
        
}