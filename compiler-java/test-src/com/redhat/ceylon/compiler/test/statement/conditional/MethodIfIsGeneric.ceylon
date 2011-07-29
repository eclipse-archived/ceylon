@nomodel
interface Base<T> {
    shared formal void baseMethod();
}
@nomodel
interface A<T> satisfies Base<T> {
    shared formal void aMethod();
}
@nomodel
interface B<T> satisfies Base<T> {
    shared formal void bMethod();
}

@nomodel
class MethodIfIsGeneric() {
    shared void m(Object x) {
        if (is Base<Integer> x) {
            x.baseMethod();
        }

        if (is A<Integer>|B<Integer> x) {
            x.baseMethod();
        }
    }
}
