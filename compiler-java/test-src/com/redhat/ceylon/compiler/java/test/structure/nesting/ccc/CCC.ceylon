@nomodel
class C() {
    void m1(){}
    class CC() {
        class CCC() {
            void m2() {
                m1();
            }
        }
    }
}