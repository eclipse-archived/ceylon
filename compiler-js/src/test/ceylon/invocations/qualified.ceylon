import check { check }

//Tests for qualified supertype invocations
interface AmbiguousParent {
    shared formal String doSomething();
    shared formal Integer whatever;
}
interface Ambiguous1 satisfies AmbiguousParent {
    shared actual default String doSomething() {
        return "ambiguous 1";
    }
    shared actual default Integer whatever { return 1; }
}
interface Ambiguous2 satisfies AmbiguousParent {
    shared actual default String doSomething() {
        return "ambiguous 2";
    }
    shared actual default Integer whatever { return 2; }
}

class QualifyAmbiguousSupertypes(Boolean one)
        satisfies Ambiguous1 & Ambiguous2 {
    shared actual String doSomething() {
        return one then Ambiguous1::doSomething() else Ambiguous2::doSomething();
    }
    shared actual Integer whatever {
        if (one) {
            return Ambiguous1::whatever;
        }
        return Ambiguous2::whatever;
    }
}

void testQualified() {
    value q1 = QualifyAmbiguousSupertypes(true);
    value q2 = QualifyAmbiguousSupertypes(false);
    check(q1.doSomething()=="ambiguous 1", "qualified super calls [1]");
    check(q2.doSomething()=="ambiguous 2", "qualified super calls [2]");
    check(q1.whatever==1, "qualified super attrib [1]");
    check(q2.whatever==2, "qualified super attrib [2]");
}
