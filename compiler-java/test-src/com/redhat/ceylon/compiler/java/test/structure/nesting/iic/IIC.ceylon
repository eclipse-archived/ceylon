@nomodel
interface I<T> {
    shared formal T m1<U>(U? u);
    interface II {
        class IIC() {
            T m2() {
                return m1(null);
            }
        }
    }
}