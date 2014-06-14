import check { check }

//Tests for qualified supertype invocations
interface AmbiguousParent {
    shared formal String doSomething();
    shared formal Integer whatever;
    shared default String somethingElse(Integer x) {
        return "something ``x`` else";
    }
}
interface Ambiguous1 satisfies AmbiguousParent {
    shared actual default String doSomething() {
        return "ambiguous 1";
    }
    shared actual default Integer whatever { return 1; }
    shared actual default String somethingElse(Integer x) {
        if (x%2==0) {
            return (super of AmbiguousParent).somethingElse(x);
        }
        return "Ambiguous1 something ``x`` else";
    }
}
interface Ambiguous2 satisfies AmbiguousParent {
    shared actual default String doSomething() {
        return "ambiguous 2";
    }
    shared actual default Integer whatever { return 2; }
    shared actual default String somethingElse(Integer x) {
        return "Ambiguous2 ``x`` something else";
    }
}

class QualifyAmbiguousSupertypes(Boolean one)
        satisfies Ambiguous1 & Ambiguous2 {
    shared actual String doSomething() {
        return one then (super of Ambiguous1).doSomething() else (super of Ambiguous2).doSomething();
    }
    shared actual Integer whatever {
        if (one) {
            return (super of Ambiguous1).whatever;
        }
        return (super of Ambiguous2).whatever;
    }
    shared actual String somethingElse(Integer x) {
        return one then (super of Ambiguous1).somethingElse(x) else (super of Ambiguous2).somethingElse(x);
    }
}

class QualifiedA() {
  shared default variable Integer a=0;
}
class QualifiedB() extends QualifiedA() {
  shared actual variable Integer a=0;
  shared void f() {
    (super of QualifiedA).a++;
  }
  shared Integer g() {
    return ++(super of QualifiedA).a;
  }
  shared Integer supera { return (super of QualifiedA).a; }
}

class TestList() satisfies List<String> {
    shared actual List<String> clone() => {};
    shared actual String? getFromFirst(Integer index) { return null; }
    shared actual Integer? lastIndex = null;
    shared actual List<String> rest = [];
    shared actual List<String> reversed = {};
    shared actual List<String> segment(Integer from, Integer length) { return {}; }
    shared actual List<String> span(Integer from, Integer to) { return {}; }
    shared actual List<String> spanTo(Integer to) { return {}; }
    shared actual List<String> spanFrom(Integer from) { return {}; }
    shared actual Iterator<String> iterator() => emptyIterator;
    shared actual Boolean equals(Object that) { return (super of List<String>).equals(that); }
    shared actual Integer hash { return (super of List<String>).hash; }
}

void testQualified() {
    value q1 = QualifyAmbiguousSupertypes(true);
    value q2 = QualifyAmbiguousSupertypes(false);
    check(q1.doSomething()=="ambiguous 1", "qualified super calls [1]");
    check(q2.doSomething()=="ambiguous 2", "qualified super calls [2]");
    check(q1.whatever==1, "qualified super attrib [1]");
    check(q2.whatever==2, "qualified super attrib [2]");
    check(q1.somethingElse(5)=="Ambiguous1 something 5 else", "qualified super method [1]");
    check(q1.somethingElse(6)=="something 6 else", "qualified super method [2]");
    check(q2.somethingElse(5)=="Ambiguous2 5 something else", "qualified super method [3]");
    check(q2.somethingElse(6)=="Ambiguous2 6 something else", "qualified super method [4]");
    value qb = QualifiedB();
    check(qb.a == qb.supera, "Qualified attribute [1]");
    qb.f();
    check(++qb.a == qb.supera, "Qualified attribute [2]");
    check(++qb.a == qb.g(), "Qualified attribute [3]");
    value tl = TestList();
    check(tl.hash=={}.hash, "super of List.hash");
    check(tl=={}, "super of List.equals");
}
