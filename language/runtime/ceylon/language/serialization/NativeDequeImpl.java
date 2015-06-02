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
