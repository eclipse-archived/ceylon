package com.redhat.ceylon.compiler.java.test.issues.bug11xx;

interface EventBus {
    EventBus registerHandler(String address, Handler<? extends Message> handler);
}

interface Message<T> {
    
}

interface Handler<E> {
    void handle(E event);
}