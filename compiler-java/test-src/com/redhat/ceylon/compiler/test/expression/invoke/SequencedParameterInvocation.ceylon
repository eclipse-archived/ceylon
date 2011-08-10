@nomodel
class SequencedParameterInvocation(){
    void m(String a, Natural... i) {
    }
    
    void m2(String a, SequencedParameterInvocation... i) {
    }
    
    void f() {
        m("foo", 1, 2, 3);
        m2("foo", this, this, this);
    }
}