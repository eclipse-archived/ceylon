interface I {
    shared formal void m1();
    interface II {
        interface III {
            void m2() {
                m1();
            }
        }
    }
}