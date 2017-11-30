class StaticClass {
    shared static String hello = "hi";
    shared static void noop(String s) {}
    shared static class Class() {}
    new () {}
}

interface StaticInterface {
    //shared static String hello => "hi";
    //shared static void noop(String s) {}
    shared static class Class() {}
}

@test
shared void testStatic() {
    StaticClass.noop("hello");
    //StaticInterface.noop("hello");
    //StaticClass.noop(StaticInterface.hello);
    //StaticInterface.noop(StaticClass.hello);
    StaticClass.Class c1 = StaticClass.Class();
    StaticInterface.Class c2 = StaticInterface.Class();
}