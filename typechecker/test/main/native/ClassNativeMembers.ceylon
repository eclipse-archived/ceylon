shared class ClassNativeMembers() {
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
        native shared Integer test();
    }
    native("jvm") class ClassPrivate(Integer i) {
        native("jvm") shared Integer test() => i;
    }
    native("js") class ClassPrivate(Integer i) {
        native("js") shared Integer test() => i;
    }

    native shared class ClassShared() {
        native shared Integer test();
    }
    native("jvm") shared class ClassShared() {
        native("jvm") shared Integer test() {
            throw Exception("ClassNativeMembers-JVM");
        }
    }
    native("js") shared class ClassShared() {
        native("js") shared Integer test() {
            throw Exception("ClassNativeMembers-JS");
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

void testClassNativeMembers() {
    ClassNativeMembers().test();
    value y = ClassNativeMembers().ClassShared().test();
}
