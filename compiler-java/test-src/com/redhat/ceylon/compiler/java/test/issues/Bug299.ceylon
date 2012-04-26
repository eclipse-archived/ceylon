@nomodel
class Bug299() {
    class Inner(String s = "a") {
        void m(String s = "b") {
        }
    }
    void m() {
        class Inner(String s = "a") {
            void m(String s = "b") {
            }
        }
    }
}