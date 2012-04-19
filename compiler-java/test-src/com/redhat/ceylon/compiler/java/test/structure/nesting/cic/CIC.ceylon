@nomodel
class C() {
    void m1(){}
    interface CI {
        class CIC() {
            void m2() {
                m1();
            }
        }
    }
}