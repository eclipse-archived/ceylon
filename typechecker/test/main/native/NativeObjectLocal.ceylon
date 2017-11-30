void testNativeObjectLocal() {
    native object nativeObjectLocal {
        native shared Integer test(Integer i);
        native shared Integer foo;
        native shared Integer bar;
        native assign bar;
    }
    
    native("jvm") object nativeObjectLocal {
        native("jvm") shared Integer test(Integer i) {
            throw Exception("NativeObjectLocal-JVM");
        }
        native("jvm") shared Integer foo => 0;
        native("jvm") shared Integer bar => 0;
        native("jvm") assign bar { test(0); }
    }
    
    native("js") object nativeObjectLocal {
        native("js") shared Integer test(Integer i) {
            throw Exception("NativeObjectLocal-JS");
        }
        native("js") shared Integer foo => 0;
        native("js") shared Integer bar => 0;
        native("js") assign bar {test(0); }
    }
    
    value x = nativeObjectLocal.foo;
    value y = nativeObjectLocal.bar;
    nativeObjectLocal.bar = x;
}
