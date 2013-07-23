class InheritanceConsistency() {
    interface Named {
        shared formal String name;
        shared String nick() { return name; }
    }

    abstract class Animal() extends Basic() {
        shared formal Boolean mammal;
    }

    //formal from interface and superclass not implemented
    @error class Human(age) extends Animal() satisfies Named {
        shared Integer age;
    }

    //formal from interface not implemented
    @error class Toy() satisfies Named {
    }

    //formal from superclass not implemented
    @error class Lion(age) extends Animal() {
        shared Integer age;
    }

    class Computer(String hostname) satisfies Named {
        shared actual String name = hostname;
    }

    class Horse() extends Animal() {
        shared actual Boolean mammal = true;
    }

    //formal members non implemented
    interface A {
        shared formal Boolean a;
        shared String b() { return "b"; }
        shared formal Boolean d;
    }

    interface B satisfies A {
        shared String c() { return "c"; }
    }

    abstract class C() {
    }

    //does not implement A.a and A.d
    @error class D() extends C() satisfies B {

    }

    //does not implement A.d (C.d is not an implementation of A.d)
    @error class E() extends C() satisfies B {
        shared actual Boolean a = false;
    }

    //avoid cycles in hierarchy
    @error class CycleA() extends CycleB() {}
    class CycleB() extends CycleA() {}

    @error interface CycleC satisfies CycleE {}
    interface CycleD satisfies CycleC {}
    interface CycleE satisfies CycleD {}
    class CycleF() satisfies CycleE {}

    //avoid parallel member definition in a class hierarchy
    interface ActualA {
        shared default String hello { return "Hi"; }
    }

    interface ActualB {
        shared default String hello { return "Bonjour"; }
    }

    @error //may not inherit two declarations with the same name that do not share a common supertype
    class ActualC() satisfies ActualA & ActualB {
    }

    interface ActualD {
        shared formal String hello;
    }
    interface ActualE satisfies ActualD {
        shared actual default String hello { return "Hi"; }
    }

    interface ActualF satisfies ActualD {
        shared actual default String hello { return "Bonjour"; }
    }

    @error //inherit two declarations with the same name (from same common ancestor but do not override)
    class ActualG() satisfies ActualE & ActualF {
    }

    class ActualH() satisfies ActualE & ActualF {
        shared actual String hello { return "Wazza"; }
    }
}

interface Inter<T> {
    shared formal T get();
}

void accept<T>(Inter<T> inter) 
        given T satisfies Object {
    print(inter.get());
}

void testValueArgs() {
    accept {
        object inter satisfies Inter<String> {
            shared actual String get() {
                return "hello";
            }
        }
    };
    accept {
        @error object inter satisfies Inter<String> {}
    };
}
