class SuperReference(){
    shared void m() {
        SuperReferenceChild x = SuperReferenceChild();
        x.test();
    }
    
    shared default void test() {
        return;
    }
}

class SuperReferenceChild() extends SuperReference() {
    shared actual void test() {
        super.test();
    }
}
