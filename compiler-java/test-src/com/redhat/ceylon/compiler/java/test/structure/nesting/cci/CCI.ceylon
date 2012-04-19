@nomodel
class C() {
    void m1(){}
    class CC() {
        interface CCI {
            void m2() {
                m1();
            }
        }
    }
}