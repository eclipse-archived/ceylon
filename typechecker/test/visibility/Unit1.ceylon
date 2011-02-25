class Unit1() {
    
    shared String hello = "Hello";
    String goodbye = "Goodbye";
    
    shared class Inner() {
        shared String hello = "Hello";
        String goodbye = "Goodbye";
        void method() {
            say(outer.hello);
            say(outer.goodbye);
            say(hello);
            say(goodbye);
        }
        void say(String s) {}
    }
    
    Inner().hello;
    @error Inner().goodbye;
    
    @error Unit2().Inner().hello;
    @error Unit2().Inner().goodbye; 
    
}