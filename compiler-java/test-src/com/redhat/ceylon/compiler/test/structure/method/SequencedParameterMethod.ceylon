@nomodel
class SequencedParameterMethod() {
    void f(String a, Integer... ns) {
        for (Integer n in ns) {
        }
    }
    shared void f2(String a, Integer... ns) {
        for (Integer n in ns) {
        }
    }
}