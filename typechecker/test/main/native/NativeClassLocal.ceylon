void testNativeClassLocal() {
    native class NativeClassLocal() {
        native shared Integer test(Integer i);
        native shared Integer foo;
        native shared variable Integer bar;
    }
    native("jvm") class NativeClassLocal() {
        native("jvm") shared Integer test(Integer i) {
            throw Exception("NativeClassLocal-JVM");
        }
        native("jvm") shared Integer foo => 0;
        native("jvm") shared Integer bar => 0;
        native("jvm") assign bar { test(0); }
    }
    
    native("js") class NativeClassLocal() {
        native("js") shared Integer test(Integer i) {
            throw Exception("NativeClassLocal-JS");
        }
        native("js") shared Integer foo => 0;
        native("js") shared Integer bar => 0;
        native("js") assign bar {test(0); }
    }
    
    value x = NativeClassLocal().foo;
    value y = NativeClassLocal().bar;
    NativeClassLocal().bar = x;
}
