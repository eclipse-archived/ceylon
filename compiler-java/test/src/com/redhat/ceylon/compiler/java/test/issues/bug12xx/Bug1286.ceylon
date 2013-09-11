@noanno
String bug1286_getterSetter {
    String getterSetter {
        Consumer<Object> times1 = nothing;
        Consumer<String> objects1 = times1;
        interface Consumer<in E> {
            shared void add(E e) {}
        }
        return "";
    }
    assign getterSetter {
        Consumer<Object> times1 = nothing;
        Consumer<String> objects1 = times1;
        interface Consumer<in E> {
            shared void add(E e) {}
        }
    }
    return "";
}
@noanno
assign bug1286_getterSetter {
    object objectDefinition extends Object() {
        shared actual Integer hash = 0;
        shared actual Boolean equals(Object other) => false;
        Consumer<Object> times1 = nothing;
        Consumer<String> objects1 = times1;
        interface Consumer<in E> {
            shared void add(E e) {}
        }
    }
}
@noanno
object bug1286_objectDefinition extends Object() {
    shared actual Integer hash = 0;
    shared actual Boolean equals(Object other) => false;
    void m(String setterArgument, Anything methodArgument(), Object objectArgument) {}
    m{
        value setterArgument {
            Consumer<Object> times1 = nothing;
            Consumer<String> objects1 = times1;
            interface Consumer<in E> {
                shared void add(E e) {}
            } 
            return ""; 
        }
        void methodArgument() {
            Consumer<Object> times1 = nothing;
            Consumer<String> objects1 = times1;
            interface Consumer<in E> {
                shared void add(E e) {}
            }
        }
        object objectArgument extends Object() {
            shared actual Integer hash = 0;
            shared actual Boolean equals(Object other) => false;
            Consumer<Object> times1 = nothing;
            Consumer<String> objects1 = times1;
            interface Consumer<in E> {
                shared void add(E e) {}
            }
        }
    };
}
@noanno
Object bug1286_method(Object objectArgument) {
    return bug1286_method{
        objectArgument = bug1286_method{
            object objectArgument extends Object() {
                shared actual Integer hash = 0;
                shared actual Boolean equals(Object other) => false;
                Consumer<Object> times1 = nothing;
                Consumer<String> objects1 = times1;
                interface Consumer<in E> {
                    shared void add(E e) {}
                }
            }
        };
    };
}
@noanno
class Bug1286_initializer(Object objectArgument) {
    bug1286_method{
        objectArgument = bug1286_method{
            object objectArgument extends Object() {
                shared actual Integer hash = 0;
                shared actual Boolean equals(Object other) => false;
                Consumer<Object> times1 = nothing;
                Consumer<String> objects1 = times1;
                interface Consumer<in E> {
                    shared void add(E e) {}
                }
            }
        };
    };
}

