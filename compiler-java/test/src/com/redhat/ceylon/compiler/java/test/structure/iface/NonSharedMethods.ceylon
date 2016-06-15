interface NonSharedMethods {
    String f() => "hello";
    
    shared default String x() {
        // capture of f by an inner class
        object o {
            shared void x() {
                f();
            }
        }
        return f();
    }
}
interface NonSharedMethodsSub satisfies NonSharedMethods {
    String f() => "bye";
    
    shared actual String x() {
        
        // capture of f by an inner class
        object o {
            shared void x() {
                f();
            }
        }
        return super.x() + f();
    }
}
class NonSharedMethodsClass() satisfies NonSharedMethodsSub {}
shared void nonSharedMethods() {
    assert("hellobye" == NonSharedMethodsClass().x());
}