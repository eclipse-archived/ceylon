class ObtainableImpl() satisfies Obtainable {
    shared actual void obtain() {}
    shared actual void release(Throwable? error) {}
}