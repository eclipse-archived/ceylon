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

/*
 package ceylon.language.serialization;
 
 import com.redhat.ceylon.compiler.java.metadata.Ceylon;
 import com.redhat.ceylon.compiler.java.metadata.Class;
 
 @Ceylon(major=8, minor=0)
 @Class
 final class NativeDequeImpl implements NativeDeque {
    private final java.util.Deque deque = new java.util.ArrayDeque<>();
    public java.lang.Object pushFront(java.lang.Object element) {
        deque.addFirst(element);
        return null;
    }
    public java.lang.Object pushBack(java.lang.Object element) {
        deque.addLast(element);
        return null;
    }
    public java.lang.Object popFront() {
        return deque.pollFirst();
    }
    public boolean getEmpty() {
        return deque.isEmpty();
    }
    public String toString() {
        return deque.toString();
    }
 }
 */