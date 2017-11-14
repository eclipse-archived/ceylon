shared object objectNativeMembersWithImpl {
    native shared void testShared(Integer i) {
        throw Exception("ObjectNativeMembersWithImpl-JVM");
    }
    native("js") shared void testShared(Integer i) {
        throw Exception("ObjectNativeMembersWithImpl-JS");
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

void testObjectNativeMembersWithImpl() {
    value x = objectNativeMembersWithImpl.attrShared;
    value y = objectNativeMembersWithImpl.objectShared.test(x);
    objectNativeMembersWithImpl.testShared(y);
}
