@nomodel
class C<out T,in X>() given T satisfies String given X satisfies Boolean {
    T m1<U>(X? b, U? u = null) { throw; }
    class CC() {
        interface CCI {
            T m2() {
                return m1(null);
            }
        }
    }
}