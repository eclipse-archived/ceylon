@nomodel
class MethodWithVarargs() {
    shared void f1(Integer... ns) {
        for (Integer n in ns) { }
    }
    shared void f2(Integer i, Integer... ns) {
        for (Integer n in ns) { }
    }
}