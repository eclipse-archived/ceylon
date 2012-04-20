@nomodel
class C<out T,in X>() given T satisfies String given X satisfies Boolean {
    T m1<U>(X? b, U? u = null) { throw; }
    interface CI {
        interface CII {
            T m2() {
                return m1(null);
            }
        }
    }
}