shared interface TestListener {
    shared default void mDefault() => l();
    shared void mShared() => l();
    void l(){}
}