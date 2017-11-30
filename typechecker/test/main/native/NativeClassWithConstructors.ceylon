native
shared class NativeClassWithConstructors {
    shared native new (Integer x, Integer y) {}
    shared native new base(Integer x, Integer y) {}
}

native("jvm")
shared class NativeClassWithConstructors {
    native("jvm") shared new (Integer x, Integer y) {}
    native("jvm") shared new base(Integer x, Integer y) {}
}

native("js")
shared class NativeClassWithConstructors {
    native("js") shared new (Integer x, Integer y) {}
    native("js") shared new base(Integer x, Integer y) {}
}


//native
//shared class NativeClassWithConstructors2 {
//    shared native new (Integer x, Integer y);  // SYNTAX ERROR
//    shared native new base(Integer x, Integer y);  // SYNTAX ERROR
//}
//
//native("jvm")
//shared class NativeClassWithConstructors2 {
//    native("jvm") shared new (Integer x, Integer y) {}
//    native("jvm") shared new base(Integer x, Integer y) {}
//}
//
//native("js")
//shared class NativeClassWithConstructors2 {
//    native("js") shared new (Integer x, Integer y) {}
//    native("js") shared new base(Integer x, Integer y) {}
//}


native
shared class NativeClassWithConstructors3 {
    shared native new (Integer x, Integer y) {}
    shared native new base(Integer x, Integer y) {}
}

native("jvm")
shared class NativeClassWithConstructors3 {
    native("jvm") new jvm(Integer x, Integer y) {}
}

native("js")
shared class NativeClassWithConstructors3 {
    native("js") new js(Integer x, Integer y) {}
}


void testNativeClassWithConstructors() {
    value klza = NativeClassWithConstructors(1, 2);
    value klzb = NativeClassWithConstructors.base(1, 2);
    value klz3a = NativeClassWithConstructors3(1, 2);
    value klz3b = NativeClassWithConstructors3.base(1, 2);
    throw Exception("NativeClassWithConstructors-JVM");
}
