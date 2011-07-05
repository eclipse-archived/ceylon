class InheritanceAndFormal() {
    interface Named {
        shared formal String name;
        shared String nick() { return name; }
    }

    abstract class Animal() {
        shared formal Boolean mammal;
    }

    //formal from interface and superclass not implemented
    @error class Human(Integer age) extends Animal() satisfies Named {
        shared Integer age = age;
    }

    //formal from interface not implemented
    @error class Toy() satisfies Named {
    }

    //formal from superclass not implemented
    @error class Lion(Integer age) extends Animal() {
        shared Integer age = age;
    }

    class Computer(String hostname) satisfies Named {
        shared actual String name = hostname;
    }

    class Horse() extends Animal() {
        shared actual Boolean mammal = true;
    }

    interface A {
        shared formal Boolean a;
        shared String b() { return "b"; }
        shared formal Boolean d;
    }

    interface B satisfies A {
        shared String c() { return "c"; }
    }

    abstract class C() {
        shared Boolean d = true;
    }

    @error class D() extends C() satisfies B {

    }

    class E() extends C() satisfies B {
        shared actual Boolean a = false;
    }

}