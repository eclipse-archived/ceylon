class CtorConcreteDelegationSuper<T>() {
    checker.note("super");
}
@noanno
class C<T> extends CtorConcreteDelegationSuper<T> {
    checker.note("1");
    shared new x() extends CtorConcreteDelegationSuper<T>() {
        checker.note("X");
    }
    checker.note("2");
    shared new y() extends x() {
        checker.note("Y");
    }
    checker.note("3");
    shared new z() extends x() {
        checker.note("Z");
    }
    checker.note("4");
    shared new alpha() extends z() {
        checker.note("alpha");
    }
    
    checker.note("5");
     
}
@noanno
class ExtendsBasic<X> {
    checker.note("1");
    X? who;
    shared new pour(X? who) {
        checker.note("For");
        this.who = who;
    }
    checker.note("2");
    shared new monde()
            extends pour(null) {
        checker.note("World");
    }
    checker.note("3");
}
@noanno
class Hello {
    String who;
    shared new pour(String who) {
        this.who = who;
    }
    shared new monde()
            extends pour("world") {}
    print("Hello " + who);
}
shared void runCtorConcreteDelegation() {
    checker.reset();
    C<String>.x();
    checker.check("[super, 1, X, 2, 3, 4, 5]");
    
    checker.reset();
    C<String>.y();
    checker.check("[super, 1, X, 2, Y, 3, 4, 5]");
    
    checker.reset();
    C<String>.z();
    checker.check("[super, 1, X, 2, 3, Z, 4, 5]");
    
    checker.reset();
    C<String>.alpha();
    checker.check("[super, 1, X, 2, 3, Z, 4, alpha, 5]");
    
    checker.reset();
    ExtendsBasic.pour("tom");
    checker.check("[1, For, 2, 3]");
    
    checker.reset();
    ExtendsBasic.monde();
    checker.check("[1, For, 2, World, 3]");
}