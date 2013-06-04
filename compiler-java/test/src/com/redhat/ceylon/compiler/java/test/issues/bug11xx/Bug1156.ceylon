@noanno
void bug1156(EventBus eb, Handler<Message<Object>> handler1, Handler<Message<String>> handler2) {
    eb.registerHandler("foo", handler1);
    eb.registerHandler("foo", handler2);
}