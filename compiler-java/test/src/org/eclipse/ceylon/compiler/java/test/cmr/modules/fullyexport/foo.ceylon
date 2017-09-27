import javax.inject { inject, Provider }

shared class Foo(){
    inject
    Boolean b = true;
}

shared void run(){
    Foo();
    Provider<Foo>? f = null;
    print(`interface Provider`);
}