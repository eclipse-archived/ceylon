class ObjectArgumentNamedInvocationChained() {

    shared ObjectArgumentNamedInvocationChained callFunction(Object o) {
        print(o.string);
        return this;
    }

}
shared void objectArgumentNamedInvocationChained() {
    ObjectArgumentNamedInvocationChained().callFunction {
        object o extends Object() {
            shared actual String string = "Foo";
        }
    }.callFunction {
        object o extends Object() {
            shared actual String string = "Bar";
        }
    };
}