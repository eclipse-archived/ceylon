class ObjectArgumentNamedInvocation() {

    shared ObjectArgumentNamedInvocation callFunction(Object o) {
        print(o.string);
        return this;
    }

}
shared void objectArgumentNamedInvocation() {
    ObjectArgumentNamedInvocation().callFunction {
        object o extends Object() {
            shared actual String string = "Foo";
        }
    }.callFunction {
        object o extends Object() {
            shared actual String string = "Bar";
        }
    };
}