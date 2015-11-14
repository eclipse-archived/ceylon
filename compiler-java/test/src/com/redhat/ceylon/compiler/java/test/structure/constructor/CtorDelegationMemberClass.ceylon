
class CtorDelegationMemberClass() {
    shared class Super() {
        checker.note("Super");
    }
    class ConcreteDelegating extends Super {
        checker.note("ConcreteDelegating.1");
        shared new a() extends Super() {
            checker.note("ConcreteDelegating.A");
        }
        checker.note("ConcreteDelegating.2");
        shared new b() extends a() {
            checker.note("ConcreteDelegating.B");
        }
        checker.note("ConcreteDelegating.3");
    }
    class AbstractDelegating extends Super {
        checker.note("AbstractDelegating.1");
        abstract new a() extends Super() {
            checker.note("AbstractDelegating.A");
        }
        checker.note("AbstractDelegating.2");
        shared new b() extends a() {
            checker.note("AbstractDelegating.B");
        }
        checker.note("AbstractDelegating.3");
    }
    shared void runNonShared() {
        checker.reset();
        ConcreteDelegating.a();
        checker.check("[Super, ConcreteDelegating.1, ConcreteDelegating.A, ConcreteDelegating.2, ConcreteDelegating.3]");
        checker.reset();
        ConcreteDelegating.b();
        checker.check("[Super, ConcreteDelegating.1, ConcreteDelegating.A, ConcreteDelegating.2, ConcreteDelegating.B, ConcreteDelegating.3]");
        checker.reset();
        AbstractDelegating.b();
        checker.check("[Super, AbstractDelegating.1, AbstractDelegating.A, AbstractDelegating.2, AbstractDelegating.B, AbstractDelegating.3]");
    }
    shared class SharedConcreteDelegating extends Super {
        checker.note("SharedConcreteDelegating.1");
        shared new a() extends Super() {
            checker.note("SharedConcreteDelegating.A");
        }
        checker.note("SharedConcreteDelegating.2");
        shared new b() extends a() {
            checker.note("SharedConcreteDelegating.B");
        }
        checker.note("SharedConcreteDelegating.3");
    }
    shared class SharedAbstractDelegating extends Super {
        checker.note("SharedAbstractDelegating.1");
        abstract new a() extends Super() {
            checker.note("SharedAbstractDelegating.A");
        }
        checker.note("SharedAbstractDelegating.2");
        shared new b() extends a() {
            checker.note("SharedAbstractDelegating.B");
        }
        checker.note("SharedAbstractDelegating.3");
    }
}

shared void runCtorDelegationMemberClass() {
    value instance = CtorDelegationMemberClass();
    instance.runNonShared();
    checker.reset();
    instance.SharedConcreteDelegating.a();
    checker.check("[Super, SharedConcreteDelegating.1, SharedConcreteDelegating.A, SharedConcreteDelegating.2, SharedConcreteDelegating.3]");
    checker.reset();
    instance.SharedConcreteDelegating.b();
    checker.check("[Super, SharedConcreteDelegating.1, SharedConcreteDelegating.A, SharedConcreteDelegating.2, SharedConcreteDelegating.B, SharedConcreteDelegating.3]");
    checker.reset();
    instance.SharedAbstractDelegating.b();
    checker.check("[Super, SharedAbstractDelegating.1, SharedAbstractDelegating.A, SharedAbstractDelegating.2, SharedAbstractDelegating.B, SharedAbstractDelegating.3]");
}