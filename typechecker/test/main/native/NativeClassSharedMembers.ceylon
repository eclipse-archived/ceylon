shared class NativeClassSharedMembers() {
    native shared void testShared(Integer i);
    native("jvm") shared void testShared(Integer i) {
    }
    native("js") shared void testShared(Integer i) {
    }
    
    native shared Integer attrShared;
    native("jvm") shared Integer attrShared => 1;
    native("js") shared Integer attrShared => 2;
    
    native shared class ClassShared() {
        native shared Integer test(Integer i);
        native shared Integer foo;
        native shared Integer bar;
        native assign bar;
    }
    
    native("jvm") shared class ClassShared() {
        native("jvm") shared Integer test(Integer i) => 1;
        native("jvm") shared Integer foo => 0;
        native("jvm") shared Integer bar => 0;
        native("jvm") assign bar { test(0); }
    }
    
    native("js") shared class ClassShared() {
        native("js") shared Integer test(Integer i) => 2;
        native("js") shared Integer foo => 0;
        native("js") shared Integer bar => 0;
        native("js") assign bar {test(0); }
    }
    
    native shared interface InterfaceShared {
        native shared Integer test(Integer i);
        native shared Integer foo;
        native shared Integer bar;
        native assign bar;
    }
    
    native("jvm") shared interface InterfaceShared {
        native("jvm") shared Integer test(Integer i) {
            throw Exception("NativeClassSharedMembers-JVM");
        }
        native("jvm") shared Integer foo => 0;
        native("jvm") shared Integer bar => 0;
        native("jvm") assign bar { test(0); }
    }
    
    native("js") shared interface InterfaceShared {
        native("js") shared Integer test(Integer i) {
            throw Exception("NativeClassSharedMembers-JS");
        }
        native("js") shared Integer foo => 0;
        native("js") shared Integer bar => 0;
        native("js") assign bar {test(0); }
    }
    
    shared class InterfaceSharedImpl() satisfies InterfaceShared {
    }
}

void testNativeClassSharedMembers() {
    value x = NativeClassSharedMembers().attrShared;
    NativeClassSharedMembers().testShared(x);
    NativeClassSharedMembers().ClassShared().test(x);
    NativeClassSharedMembers().InterfaceSharedImpl().test(x);
}
