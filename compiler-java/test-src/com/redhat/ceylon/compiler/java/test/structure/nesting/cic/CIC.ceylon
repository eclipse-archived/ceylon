@nomodel
class C<T>() {
    T m1<U>(U? u){throw;}
    interface CI {
        class CIC() {
            T m2() {
                return m1(null);
            }
        }
    }
}