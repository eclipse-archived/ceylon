interface NativeDeque {
    shared formal void pushFront(Anything element);
    shared formal void pushBack(Anything element);
    shared formal Anything popFront();
    shared formal Boolean empty;
}
