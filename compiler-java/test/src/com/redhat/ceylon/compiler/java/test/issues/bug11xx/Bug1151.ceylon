@noanno
shared interface Bug1151_A {
    shared default void methodA( Integer a = 1 ) {
    }
}
@noanno
shared class Bug1151_B() {
    shared default void methodB( Integer a = 1 ) {
    }
}
@noanno
shared class Bug1151_C() extends Bug1151_B() satisfies Bug1151_A {
    shared actual Integer methodA( Integer a ) {
        return a;
    }
    shared actual Integer methodB( Integer a ) {
        return a;
    }
}
void bug1151_callsite() {
    value c = Bug1151_C();
    Integer ia3 = c.methodA();
    assert(1 == ia3);
    
    Integer ib3 = c.methodB();
    assert(1 == ib3);
}
