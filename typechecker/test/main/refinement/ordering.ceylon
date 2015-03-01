void run() {
    C().m();
    C().n();
    @type:"String" C().s();
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
