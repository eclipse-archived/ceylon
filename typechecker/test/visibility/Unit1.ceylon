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
    
    void print(String s) {}
    
    print(Inner().hello);
    @error print(Inner().goodbye);
    
    @error print(Unit2().Inner().hello);
    @error print(Unit2().Inner().goodbye); 
    
}