class CtorDelegationClassRefsSuper {
    shared new Super(){
        checker.note("Super");
    }
}
class CtorDelegationClassRefs extends CtorDelegationClassRefsSuper {
    checker.note("1");
    abstract new Abstract() extends Super() {
        checker.note("Abstract");
    }
    checker.note("2");
    shared new Concrete() extends Abstract() {
        checker.note("Concrete");
    }
    checker.note("3");
    shared new Concrete2() extends Concrete() {
        checker.note("Concrete2");
    }
    checker.note("4");
}
void runCtorDelegationClassRefs() {
    checker.reset();
    variable CtorDelegationClassRefs() ref = CtorDelegationClassRefs.Concrete;
    checker.check("[]");
    ref();
    checker.check("[Super, 1, Abstract, 2, Concrete, 3, 4]");
    checker.reset();
    ref = CtorDelegationClassRefs.Concrete2;
    checker.check("[]");
    ref();
    checker.check("[Super, 1, Abstract, 2, Concrete, 3, Concrete2, 4]");
}