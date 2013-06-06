package com.redhat.ceylon.compiler.java.test.issues.bug11xx;

interface EventBus {
    EventBus registerHandler(String address, Handler<? extends Message> handler);
    void boundedMessage(Handler<? extends BoundedMessage> handler);
    void dualBoundedMessage(Handler<? extends DualBoundedMessage> handler);
    void dualComplexBoundedMessage(Handler<? extends DualComplexBoundedMessage> handler);
    void r(Raw r);
}

interface Message<T> {}
interface BoundedMessage<T extends String> {}
interface DualBoundedMessage<T extends String, T2 extends T> {}
interface DualComplexBoundedMessage<T extends String, T2 extends BoundedMessage<T>> {}

interface Handler<E> {
    void handle(E event);
}

interface Raw<T extends Raw<T>>{
}