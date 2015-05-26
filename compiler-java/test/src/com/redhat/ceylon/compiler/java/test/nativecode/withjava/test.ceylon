shared native class NativeClass(String s) {}

shared native("js") class NativeClass(String s) {}

shared void test() {
    NativeClass("Hi from Java native implementation !"); 
    throw Exception("withjava-JVM");
}
