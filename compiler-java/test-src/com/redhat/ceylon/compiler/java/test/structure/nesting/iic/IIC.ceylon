interface I {
    shared formal void m1();
    interface II {
        class IIC() {
            void m2() {
                m1();
            }
        }
    }
}