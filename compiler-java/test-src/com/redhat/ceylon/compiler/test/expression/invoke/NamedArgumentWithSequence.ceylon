@nomodel
class NamedArgumentWithSequence() {
    void m(Integer a, Integer... b) {
    
    }
    void m2(Exception a, Exception... b) {
    
    }
    void m3(NamedArgumentWithSequence a, NamedArgumentWithSequence... b) {
    
    }
    void invoke() {
        m{a=+1; +2, +3, +4};
        Exception e = Exception("", null);
        m2{a=e; e, e, e};
        m3{a=this; this, this, this};
    }
}