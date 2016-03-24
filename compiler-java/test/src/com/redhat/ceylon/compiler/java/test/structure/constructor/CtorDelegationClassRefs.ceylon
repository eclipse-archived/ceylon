class CtorDelegationClassRefsSuper {
    shared new sup(){
        checker.note("Super");
    }
}
class CtorDelegationClassRefs extends CtorDelegationClassRefsSuper {
    checker.note("1");
    abstract new abstract() extends sup() {
        checker.note("Abstract");
    }
    checker.note("2");
    shared new concrete() extends abstract() {
        checker.note("Concrete");
    }
    checker.note("3");
    shared new concrete2() extends concrete() {
        checker.note("Concrete2");
    }
    checker.note("4");
}
void runCtorDelegationClassRefs() {
    checker.reset();
    variable CtorDelegationClassRefs() ref = CtorDelegationClassRefs.concrete;
    checker.check("[]");
    ref();
    checker.check("[Super, 1, Abstract, 2, Concrete, 3, 4]");
    checker.reset();
    ref = CtorDelegationClassRefs.concrete2;
    checker.check("[]");
    ref();
    checker.check("[Super, 1, Abstract, 2, Concrete, 3, Concrete2, 4]");
}