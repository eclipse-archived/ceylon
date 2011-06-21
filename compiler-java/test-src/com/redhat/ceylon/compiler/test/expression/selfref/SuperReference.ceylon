class SuperReference(){
    shared void m() {
        SuperReferenceChild x = SuperReferenceChild();
        x.test();
    }
    
    shared void test() {
        return;
    }
}

class SuperReferenceChild() extends SuperReference() {
    shared void test() {
        super.test();
    }
}
