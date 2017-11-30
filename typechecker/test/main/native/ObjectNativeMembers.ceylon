shared object objectNativeMembers {
    native Integer testPrivate(Integer i);
    native("jvm") Integer testPrivate(Integer i) {
        return i;
    }
    native("js") Integer testPrivate(Integer i) {
        return i;
    }
    
    native Integer attrPrivate;
    native("jvm") Integer attrPrivate => 1;
    native("js") Integer attrPrivate => 2;
    
    native class ClassPrivate(Integer i) {
        native shared void test();
    }
    native("jvm") class ClassPrivate(Integer i) {
        native("jvm") shared void test() {
            throw Exception("ObjectNativeMembers-JVM");
        }
    }
    native("js") class ClassPrivate(Integer i) {
        native("js") shared void test() {
            throw Exception("ObjectNativeMembers-JS");
        }
    }
    
    native object objectPrivate {
        native shared Integer test(Integer i);
    }
    native("jvm") object objectPrivate {
        native("jvm") shared Integer test(Integer i) {
            return i;
        }
    }
    native("js") object objectPrivate {
        native("js") shared Integer test(Integer i) {
            return i;
        }
    }
    
    native shared object objectShared {
        native shared Integer test(Integer i);
    }
    native("jvm") shared object objectShared {
        native("jvm") shared Integer test(Integer i) {
            return i;
        }
    }
    native("js") shared object objectShared {
        native("js") shared Integer test(Integer i) {
            return i;
        }
    }
    
    shared void test() {
        ClassPrivate(objectShared.test(objectPrivate.test(testPrivate(attrPrivate)))).test();
    }
}

void testObjectNativeMembers() {
    value x = objectNativeMembers.test();
}
