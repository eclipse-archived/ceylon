import functions { ... }
import check { check, results }

interface AmbiguousParent {
    shared formal String doSomething();
}
interface Ambiguous1 satisfies AmbiguousParent {
    shared actual default String doSomething() {
        return "ambiguous 1";
    }
}
interface Ambiguous2 satisfies AmbiguousParent {
    shared actual default String doSomething() {
        return "ambiguous 2";
    }
}

class QualifyAmbiguousSupertypes(Boolean one)
        satisfies Ambiguous1 & Ambiguous2 {
    shared actual String doSomething() {
        return one then Ambiguous1::doSomething() else Ambiguous2::doSomething();
    }
}

shared void test() {
    helloWorld();
    helloWorld{};
    hello("world");
    hello { name="world"; };
    helloAll("someone", "someone else");
    helloAll { "someone", "someone else" };
    String s1 = toString(99);
    String s2 = toString { obj=99; };    
    Float f1 = add(1.0, -1.0);
    Float f2 = add { x=1.0; y=-1.0; };
    void p(Integer i) {
        print(i);
    }
    repeat(10,p);
    testNamedArguments();
    //qualified "super" calls
    check(QualifyAmbiguousSupertypes(true).doSomething()=="ambiguous 1", "qualified super calls [1]");
    check(QualifyAmbiguousSupertypes(false).doSomething()=="ambiguous 2", "qualified super calls [2]");
    results();
}
