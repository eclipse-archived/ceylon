@nomodel
class SequencedParameterInvocation(){
    void m(String a, Natural... i) {
    }
    
    void f() {
        m("foo", 1, 2, 3);
    }
}