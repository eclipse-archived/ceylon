interface I {
    shared formal void m1();
    class IC() {
        class ICC() {
            void m2() {
                m1();
            }
        }
    }
}