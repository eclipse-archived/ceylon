@nomodel
interface I<T> {
    shared formal T m1<U>(U? u);
    interface II {
        interface III {
            T m2() {
                return m1(null);
            }
        }
    }
}