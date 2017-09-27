@noanno
interface NoFinalMethodsIface {
    shared String ib => "";
    assign ib {}
    shared void im(String p = "") {}
    
    shared class IClass() {
        
    }
}

@noanno
class NoFinalMethods() satisfies NoFinalMethodsIface {
    shared variable String a = "";
    shared String b => a;
    assign b {}
    shared void m(String p = "") {}
    
    shared class Class() {
        
    }
}