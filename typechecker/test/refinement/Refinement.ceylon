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

        //formal member not implemented in concrete class
        @error
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

        //formal member not implemented in concrete class
        @error
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
            shared actual void print(@error Object o) {}
            @error shared actual Natural getHello() { return hello; }
            @error shared actual class Z() {}
        }
        
    }
    
    interface GoodTypes {
        
        class X() {
            shared default Object something = "Hello";
            shared default Natural count = 0;
            shared default void print(Object o) {}
            shared default Object getSomething() { return something; }
            shared default class Z() {}
        }
        
        class Y() extends X() {
            shared actual Natural something = 1;
            shared actual variable Natural count := 0;
            shared actual void print(Object o) {}
            shared actual Natural getSomething() { return something; }
            shared actual class Z() extends super.Z() {}
        }
        
        void check() {
            X.Z z1 = Y().Z();
            Y.Z z2 = Y().Z();
            @error Y.Z z3 = X().Z();
        }
        
    }
    
    interface Duplicate {
        
        interface X {
            shared String hello { return "hello"; }
        }
        
        interface Y {
            shared String hello { return "hi"; }
        }
        
        @error class Z() satisfies X & Y {}
    }
    
    abstract class WithParamMember() {
        shared formal T[] seq<T>(T t);
        shared default class Inner<T>(T t) {}
    }
    
    class SubclassWithParamMember1() 
            extends WithParamMember() {
        shared actual T[] seq<T>(T t) { return {t}; }
        shared actual class Inner<T>(T t) 
                extends super.Inner<T>(t) {}
    }
    
    class SubclassWithParamMember2() 
            extends WithParamMember() {
        shared actual Sequence<T> seq<T>(T t) { return {t}; }
    }

    class BadSubclassWithParamMember1() 
            extends WithParamMember() {
        @error shared actual T[] seq<T,U>(T t) { return {t}; }
        @error shared actual class Inner<T,U>(T t) 
                extends super.Inner<T>(t) {}
    }

    class BadSubclassWithParamMember2() 
            extends WithParamMember() {
        @error shared actual String[] seq(@error String s) { return {s}; }
        @error shared actual class Inner(@error String s) 
                extends super.Inner<String>(s) {}
    }

    class BadSubclassWithParamMember3() 
            extends WithParamMember() {
        @error shared actual Void[] seq<T>(T t) { return {t}; }
        @error shared actual class Inner<T>(T t) 
                extends super.Inner<String>("hello") {}
    }

}