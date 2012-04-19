@nomodel
class C() {
    void m1(){}
    interface CI {
        interface CII {
            void m2() {
                m1();
            }
        }
    }
}