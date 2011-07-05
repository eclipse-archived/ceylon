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

    class Horse() satisfies Named {
        shared Boolean mammal = true;
    }
}