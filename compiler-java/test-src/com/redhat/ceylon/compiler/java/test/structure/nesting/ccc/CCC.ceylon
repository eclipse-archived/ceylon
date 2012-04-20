@nomodel
class C<T>() {
    T m1<U>(U? u) { throw; }
    class CC() {
        class CCC() {
            T m2() {
                return m1(null);
            }
        }
    }
}