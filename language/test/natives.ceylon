shared class NativeBase(Integer a) {
    shared String base() => "Base ``a``";
}


native shared class NativeHeader(variable Integer b, Integer c) 
        extends NativeBase(b) {
    native shared String meth() => "Native header method ``nbm()``";
    native shared String attr => "Native header attribute ``nba``";
    native shared String test2();
    shared String nat() => "Non-native header method ``b++`` ``c``";
    shared Integer val => b;
    native String nbm();
    native String nba;
}

native("jvm") shared class NativeHeader(variable Integer b, Integer c)
        extends NativeBase(b) {
    native("jvm") String nbm() => "``b++``:``c``";
    native("jvm") String nba => "``b++``:``c``";
    String jvmimpl() => "Non-shared JVM impl method ``b++``";
    native("jvm") shared String test2() {
        value orig=b;
        value r="``base()`` ``meth()`` ``attr`` ``nat()`` ``jvmimpl()``";
        check(b==orig+4, "Native JVM class variable attrib expected ``orig+4`` got ``b``");
        return r;
    }
}

native("js") shared class NativeHeader(variable Integer b, Integer c)
        extends NativeBase(b) {
    native("js") String nbm() => "``b++``,``c``";
    native("js") String nba => "``b++``,``c``";
    String jsimpl() => "Non-shared JS impl method ``b++``";
    native("js") shared String test2() {
        value orig=b;
        value r="``jsimpl()`` ``meth()`` ``base()`` ``nat()`` ``attr``";
        check(b==orig+4, "Native JS class variable attrib expected ``orig+4`` got ``b``");
        return r;
    }
}

native shared object baseNativeObject extends NativeBase(2) {
  shared String base2() => "Method in base object: ``natm()`` and ``natv``";
  native Integer natm();
  native Integer natv;
}

shared native("jvm") object baseNativeObject extends NativeBase(3) {
  native("jvm") Integer natm() => 2;
  native("jvm") Integer natv => 4;
}
shared native("js") object baseNativeObject extends NativeBase(4) {
  native("js") Integer natm() => 3;
  native("js") Integer natv => 6;
}

@test
shared void testNativeClassesAndObjects() {
    try {
        value c=NativeHeader(2,2);
        value s=c.test2();
        c.test2();
        c.test2();
        check(c.val==14, "Native class expected 14 got ``c.val``");
        String expect;
        if (runtime.name=="jvm") {
            expect="Base 2 Native header method 2:2 Native header attribute 3:2 Non-native header method 4 2 Non-shared JVM impl method 5";
        } else {
            expect="Non-shared JS impl method 2 Native header method 3,2 Base 2 Non-native header method 4 2 Native header attribute 5,2";
        }
        check(s==expect, "Native class expected '``expect``' got '``s``'");
    } catch (Throwable ex) {
        fail("Something is wrong with native header/implementation (classes)");
        ex.printStackTrace();
    }
    //objects
    try {
        if (runtime.name=="jvm") {
            check(baseNativeObject.base()=="Base 3", "Native object 1");
            check(baseNativeObject.base2()=="Method in base object: 2 and 4");
        } else {
            check(baseNativeObject.base()=="Base 4", "Native object 1");
            check(baseNativeObject.base2()=="Method in base object: 3 and 6");
        }
    } catch (Throwable ex) {
        fail("Something is wrong with native header/implementation (objects)");
        ex.printStackTrace();
    }
}
