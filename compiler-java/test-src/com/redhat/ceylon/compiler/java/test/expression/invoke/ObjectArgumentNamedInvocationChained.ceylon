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
            shared actual Boolean equals(Object other) {return false;}
            shared actual Integer hash = 0;
        }
    }.callFunction {
        object o extends Object() {
            shared actual String string = "Bar";
            shared actual Boolean equals(Object other) {return false;}
            shared actual Integer hash = 0;
        }
    };
}