@nomodel
interface I<out T,in X> given T satisfies String given X satisfies Boolean {
    shared formal T m1<U>(X? b, U? u=null);
    class IC() {
        interface ICI {
            T m2() {
                return m1(null);
            }
        }
    }
}