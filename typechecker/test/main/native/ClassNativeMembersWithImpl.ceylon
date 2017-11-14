shared class ClassNativeMembersWithImpl() {
    native shared void testShared(Integer i) {
        throw Exception("ClassNativeMembersWithImpl-JVM");
    }
    native("js") shared void testShared(Integer i) {
        throw Exception("ClassNativeMembersWithImpl-JS");
    }

    native shared Integer attrShared => 1;
    native("js") shared Integer attrShared => 2;

    native shared object objectShared {
        native shared Integer test(Integer i) {
            return i;
        }
    }
    native("jvm") shared object objectShared {}
    native("js") shared object objectShared {
        native("js") shared Integer test(Integer i) {
            return i;
        }
    }
}

void testClassNativeMembersWithImpl() {
    value klz = ClassNativeMembersWithImpl();
    value x = klz.attrShared;
    value y = klz.objectShared.test(x);
    klz.testShared(y);
}
