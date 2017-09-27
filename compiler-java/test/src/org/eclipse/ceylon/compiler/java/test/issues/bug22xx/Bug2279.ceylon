@noanno
class Bug2279<T>() {
    Object bar = object {
        class Baz() {
            void qux(Object that) {
                assert (is Baz that);
            }
        }
    };
}
