class CtorConcreteDelegationSuper<T>() {
    checker.note("super");
}
@noanno
class C<T> extends CtorConcreteDelegationSuper<T> {
    checker.note("1");
    shared new X() extends CtorConcreteDelegationSuper<T>() {
        checker.note("X");
    }
    checker.note("2");
    shared new Y() extends X() {
        checker.note("Y");
    }
    checker.note("3");
    shared new Z() extends X() {
        checker.note("Z");
    }
    checker.note("4");
    shared new Alpha() extends Z() {
        checker.note("alpha");
    }
    
    checker.note("5");
     
}
@noanno
class ExtendsBasic<X> {
    checker.note("1");
    X? who;
    shared new For(X? who) {
        checker.note("For");
        this.who = who;
    }
    checker.note("2");
    shared new World()
            extends For(null) {
        checker.note("World");
    }
    checker.note("3");
}
shared void runCtorConcreteDelegation() {
    checker.reset();
    C<String>.X();
    checker.check("[super, 1, X, 2, 3, 4, 5]");
    
    checker.reset();
    C<String>.Y();
    checker.check("[super, 1, X, 2, Y, 3, 4, 5]");
    
    checker.reset();
    C<String>.Z();
    checker.check("[super, 1, X, 2, 3, Z, 4, 5]");
    
    checker.reset();
    C<String>.Alpha();
    checker.check("[super, 1, X, 2, 3, Z, 4, alpha, 5]");
    
    checker.reset();
    ExtendsBasic.For("tom");
    checker.check("[1, For, 2, 3]");
    
    checker.reset();
    ExtendsBasic.World();
    checker.check("[1, For, 2, World, 3]");
}