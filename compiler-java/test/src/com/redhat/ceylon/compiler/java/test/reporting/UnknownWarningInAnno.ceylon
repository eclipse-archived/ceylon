suppressWarnings("blahblah")
class UnknownWarningInAnno(){
    
    suppressWarnings("blahblah", "unknownWarning")
    shared class C() {
        suppressWarnings("blahblah")
        shared void m() {
            
        }
    }
    
}