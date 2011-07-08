@nomodel
class SuperReference(){
    shared default void m(SuperReferenceChild x) {
        x.test();
    }
    
    shared default void test() {
        return;
    }
}

@nomodel
class SuperReferenceChild() extends SuperReference() {
    shared actual void test() {
        super.test();
    }
}