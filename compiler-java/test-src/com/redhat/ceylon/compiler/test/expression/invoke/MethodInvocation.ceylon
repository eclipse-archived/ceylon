@nomodel
class MethodInvocation(){
    shared void m() {
        n();
        o(1);
        p();
    }
    
    void n() {
        return;
    }
    
    Natural o(Natural oarg) {
        return oarg;
    }
    
    shared void p() {
        return;
    }
}