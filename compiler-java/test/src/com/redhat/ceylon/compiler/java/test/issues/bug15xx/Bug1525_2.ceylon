class TestListener2() satisfies TestListener {
    void log() { print(""); }
    
    shared actual void mDefault() => log();
    shared void anotherShared() => log();
}