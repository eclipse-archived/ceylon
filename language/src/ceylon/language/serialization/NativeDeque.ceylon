import java.util{ArrayDeque, Deque}

"A queue with a native implementation"
native class NativeDeque() {
    shared native void pushFront(Anything element);
    shared native void pushBack(Anything element);
    shared native Anything popFront();
    shared native Boolean empty;
    shared actual native String string;
}

native("jvm") class NativeDeque() {
    Deque<Anything> deque = ArrayDeque<Anything>();
    shared native("jvm") void pushFront(Anything element) {
        deque.addFirst(element);
    }
    shared native("jvm") void pushBack(Anything element) {
        deque.addLast(element);
    }
    shared native("jvm") Anything popFront() {
        return deque.pollFirst();
    }
    shared native("jvm") Boolean empty {
        return deque.empty;
    }
    shared actual native("jvm") String string {
        return deque.string;
    }
}
