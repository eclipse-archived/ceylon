import java.lang { JString = String }

@noanno
void bug1156(EventBus eb, 
             Handler<Message<Object>> handler1, 
             Handler<Message<String>> handler2,
             Handler<BoundedMessage<JString>> boundedHandler,
             Handler<DualBoundedMessage<JString,JString>> dualBoundedHandler,
             Handler<DualComplexBoundedMessage<JString,BoundedMessage<JString>>> dualComplexBoundedHandler) {
    eb.registerHandler("foo", handler1);
    // this doesn't work because it requires use-site variance
    //eb.registerHandler("foo", handler2);
    eb.boundedMessage(boundedHandler);
    eb.dualBoundedMessage(dualBoundedHandler);
    eb.dualComplexBoundedMessage(dualComplexBoundedHandler);
}
