import com.redhat.ceylon.testjs { run, Foo }

native
shared void test();

native("js")
shared void test() {
    print(run);
    print(Foo);
}

native("jvm")
shared void test() {
    throw Exception("otherref-JVM");
}
