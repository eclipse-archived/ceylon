@nomodel
class C<T>() {
    T m1<U>(U? u){throw;}
    interface CI {
        interface CII {
            T m2() {
                return m1(null);
            }
        }
    }
}