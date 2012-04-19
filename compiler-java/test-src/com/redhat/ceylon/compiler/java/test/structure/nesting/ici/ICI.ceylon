@nomodel
interface I {
    shared formal void m1();
    class IC() {
        interface ICI {
            void m2() {
                m1();
            }
        }
    }
}