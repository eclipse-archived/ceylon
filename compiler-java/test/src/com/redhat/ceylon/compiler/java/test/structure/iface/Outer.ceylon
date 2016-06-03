interface Outer {
    void m() {}
    Anything f() {
        return object {
            void y() {
                outer.m();
            }
        };
    }
    
}