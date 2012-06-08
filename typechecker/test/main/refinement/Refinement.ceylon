import ceylon.language { pr = print }

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
            shared default variable Integer count := 0;
            shared default void print(String s) {}
            shared default String getHello() { return hello; }
            shared default class Z() {}
        }
        
        class Y() extends X() {
            @error shared actual Integer hello = 1;
            @error shared actual Integer count { return 1; }
            shared actual void print(@error Object o) {}
            @error shared actual Integer getHello() { return hello; }
            @error shared actual class Z() {}
        }
        
    }
    
    interface GoodTypes {
        
        class X() {
            shared default Object something = "Hello";
            shared default Integer count = 0;
            shared default void print(Object o) {}
            shared default Object getSomething() { return something; }
            shared default class Z() {}
        }
        
        class Y() extends X() {
            shared actual Integer something = 1;
            shared actual variable Integer count := 0;
            shared actual void print(Object o) {}
            shared actual Integer getSomething() { return something; }
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

    class BadSubclassWithParamMember4() 
            extends WithParamMember() {
        @error shared actual T[] seq<T>(T t) 
                given T satisfies String { return {t}; }
        @error shared actual class Inner<T>(T t) 
                extends super.Inner<T>(t) 
                given T satisfies String {}
}

}

class RefinedAsVariableSetterBase() {
    shared default String attr = "";
    shared variable default String vattr := "";
    shared default String bar {
        return "";
    }
    assign bar {}
}

class RefinedAsVariableSetterSub() extends RefinedAsVariableSetterBase() {
    shared actual String attr {return "";}
    assign attr {}
    shared actual String vattr {return "";}
    assign vattr {}
    shared actual String bar {
        return "";
    }
    assign bar {}    
}

abstract class AbstractRefined(){
    shared default String bar {
        return "";
    }
    assign bar {}
}

class ConcreteRefinement() extends AbstractRefined() {
    shared actual String bar {
        return "";
    }
    assign bar {}
}

class VariableSuper() {
    shared default variable Object i:=0;
    shared default Object j=0;
    shared default variable Object k:=0;
}

class VariableSub() extends VariableSuper() {
    @error shared actual variable Integer i:=1;
    shared actual variable Integer j:=1;
    @error shared actual variable Void k:=0;
}

abstract class WithRefinedMethod() {
    shared formal void method1(String s)(Integer i);
    shared formal void method2(String s)(Integer i);
    shared formal void method3(String s)(Integer i);
}

class WithRefiningMethod() extends WithRefinedMethod() {
    @error shared actual void method1(String s) {}
    @error shared actual void method2(String s)(Integer i)(Float f) {}
    shared actual void method3(String s)(@error String i) {}
}

class Qux() {
    shared default void print(String... strings) {}
    shared default void print2(Iterable<String> strings) {}
    shared default void print3(String... strings) {}
}

class QuxQux() extends Qux() {
    actual shared default 
    void print(Iterable<String> strings) {
        for (string in strings) {
            pr(string);
        }
    }
    shared actual void print2(String... strings) {
        for (string in strings) {
            pr(string);
        }
    }
    shared actual void print3(String... strings) {
        for (string in strings) {
            pr(string);
        }
    }
}

void testQux() {
    Qux().print2({"hello", "world"});
    QuxQux().print2("hello", "world");
    Qux().print("hello", "world");
    QuxQux().print({"hello", "world"});
}