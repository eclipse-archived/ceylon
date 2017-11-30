void run() {
    C().m();
    C().n();
    $type:"String" C().s();
}

class C() satisfies L5 {
    shared actual void m(Integer i) => print("");
    n(Integer i) => print("");
    s() => "";
}

interface L5 satisfies L4 {
    shared actual formal void m(Integer i);
    shared actual formal void n(Integer i);
}


interface L3 satisfies L2 {
    shared actual formal void m(Integer i);
    shared actual formal void n(Integer i);
}

interface L1 {
    shared formal void m(Integer i=1);
    shared formal void n(Integer i=0);
    shared formal String s();
}

interface L2 satisfies L1 {
    shared actual formal void m(Integer i);
    shared actual formal void n(Integer i);
}

interface L4 satisfies L3 {
    shared actual formal void m(Integer i);
    shared actual formal void n(Integer i);
}


void test1() {
    I&J ij1 = TBottom().thing;
    I&J ij2 = TBottom().thingy("");
    $type:"I&J" value ij3 = TBottom().thing;
    $type:"I&J" value ij4 = TBottom().thingy("");
}

class TBottom() satisfies TMid1&TMid2<J> {
    /*shared actual value */thing = object satisfies I&J{};
    /*shared actual value */thingy(String s) => object satisfies I&J{};
}

interface I {} interface J {}

interface TTop {
    shared formal Object thing;
    shared formal Object thingy(String s);
}

interface TMid1 satisfies TTop {
    shared actual formal I thing;
    shared actual formal I thingy(String s);
}

interface TMid2<T> 
        satisfies TTop 
        given T satisfies Object {
    shared actual formal T thing;
    shared actual formal T thingy(String s);
}

void test2() {
    I&J ij1 = TBottom().thing;
    I&J ij2 = TBottom().thingy("");
    $type:"I&J" value ij3 = TBottom().thing;
    $type:"I&J" value ij4 = TBottom().thingy("");
}
