@nomodel
class NamedArgumentWithSequence() {
    void m(Integer a, Integer... b) {
    
    }
    void m2() {
        m{a=+1; +2, +3, +4};
    }
}