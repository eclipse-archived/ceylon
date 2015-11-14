abstract class Shadowing1() {
    shared formal String? name;
    shared default Integer? count=0;
    shared Float? amount => 0.0;
    shared variable Object? obj = null;
    void method() {
        @error if (exists name) {}
        @error if (exists count) {}
        @error if (exists amount) {}
        @error if (exists obj) {}
    }
}

abstract class Shadowing2() {
    shared formal String? name;
    shared default Integer? count=0;
    shared Float? amount => 0.0;
    shared variable Object? obj = null;
    void method() {
        if (exists n=name) {}
        if (exists c=count) {}
        if (exists a=amount) {}
        if (exists o=obj) {}
    }
}

abstract class Shadowing3() {
    shared formal String? name;
    shared default Integer? count=0;
    shared Float? amount => 0.0;
    shared variable Object? obj = null;
    if (exists @error n=name) {}
    if (exists @error c=count) {}
    if (exists a=amount) {}
    if (exists o=obj) {}
}

abstract class Shadowing4() {
    shared formal String? name;
    shared default Integer? count=0;
    shared Float? amount => 0.0;
    shared variable Object? obj = null;
    @error if (exists name) {}
    @error if (exists count) {}
    @error if (exists amount) {}
    @error if (exists obj) {}
}

abstract class Using1() {
    shared formal String? name;
    shared default Integer? count=0;
    shared Float? amount => 0.0;
    shared variable Object? obj = null;
    @error print(name);
    @error print(count);
    print(amount);
    print(obj);
}

abstract class Using2() {
    shared formal String? name;
    shared default Integer? count=0;
    shared Float? amount => 0.0;
    shared variable Object? obj = null;
    print(obj);
    print(amount);
    @error print(count);
    @error print(name);
}
