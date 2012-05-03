abstract class Abstract() {
    shared formal String name;
}

class Concrete() extends Abstract() {
    name = "Trompon";
    print(name);
}

@error class Broken() extends Abstract() {
    void method() {
        @error name = "Trompon";
    }
}

class BadlyTyped() extends Abstract() {
    @error name = 1;
}

class BadlyUsed() extends Abstract() {
    @error print(name);
    name = "Tompon";
}

class OK() extends Abstract() {
    name = "Trompon";
    print(name);
    value x = 0.0;
}

