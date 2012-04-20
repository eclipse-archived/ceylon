@nomodel
interface I<T> {
    shared formal T m1<U>(U? u);
    class IC() {
        class ICC() {
            T m2() {
                return m1(null);
            }
        }
    }
}