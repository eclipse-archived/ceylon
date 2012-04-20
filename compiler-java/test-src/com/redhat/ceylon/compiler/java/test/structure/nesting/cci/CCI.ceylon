@nomodel
class C<T>() {
    T m1<U>(U? u){throw;}
    class CC() {
        interface CCI {
            T m2() {
                return m1(null);
            }
        }
    }
}