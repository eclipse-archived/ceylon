class Refinement() {

    interface Good {
    
        abstract class X() {
            shared formal String hello;
        }
        
        abstract class Y() extends X() {
            shared actual default String hello = "Hello";
        }
        
        abstract class Z() extends Y() {
            shared actual String hello = "Hi";
        }
    
    }
    
    interface Bad {
        
        class X() {
            @error shared formal String hello;
        }
        
        abstract class Y() extends X() {
            @error shared String hello = "Hello";
            @error shared actual String goodbye = "Goodbye";
        }
        
        abstract class Z() extends Y() {
            @error shared actual String hello = "Hi";
        }
        
        class W() extends X() {
            @error actual String hello = "Hello";
            @error default String goodbye = "Goodbye";
            @error formal String hiAgain;
        }
    
    }
    
    interface BadTypes {
        
        class X() {
            shared default String hello = "Hello";
            shared default variable Natural count := 0;
            shared default void print(String s) {}
            shared default String getHello() { return hello; }
            shared default class Z() {}
        }
        
        class Y() extends X() {
            @error shared actual Natural hello = 1;
            @error shared actual Natural count { return 1; }
            @error shared actual void print(Object o) {}
            @error shared actual Natural getHello() { return hello; }
            @error shared actual class Z() {}
        }
        
    }
    
    interface GoodTypes {
        
        class X() {
            shared default Object something = "Hello";
            shared default void print(Object o) {}
            shared default Object getSomething() { return something; }
            shared default class Z() {}
        }
        
        class Y() extends X() {
            shared actual Natural something = 1;
            shared actual void print(Object o) {}
            shared actual Natural getSomething() { return something; }
            shared actual class Z() extends super.Z() {}
        }
        
    }
    
}