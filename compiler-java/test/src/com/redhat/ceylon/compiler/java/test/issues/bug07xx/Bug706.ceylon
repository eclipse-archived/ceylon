shared void bug706() {
    class C() {}
    object obj extends C() {}        
    Void x = obj;
    if (is Identifiable x) {}
}