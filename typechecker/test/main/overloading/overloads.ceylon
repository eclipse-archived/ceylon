import java.lang {
    overloaded
}
import ceylon.language.meta.model {
    Class
}

interface Runnable {}

object service {
    overloaded shared void execute(Runnable run) {}
    overloaded shared void execute(Anything run(String id)) {}
}

object service2 {
    $error shared void execute(Runnable run) {}
    $error shared void execute(Anything run(String id)) {}
}

void runme() {
    service.execute(object satisfies Runnable {});
    service.execute((id) => print("id: " + id));
    service.execute(print);
}


interface Interface {
    shared overloaded formal void run(String str);
    overloaded shared formal void run(String str1, String str2);
    shared overloaded formal void run(Integer int);
    shared overloaded formal void run(Integer int1, Integer int2);
    shared overloaded formal void run(Integer* int);
    $error overloaded shared formal void run(Integer? int);
    
    shared overloaded formal void wrong(Integer int);
    $error shared overloaded formal void wrong(Integer? int);
    shared overloaded formal void wrong(String string);
    shared overloaded formal void wrong(Object thing);
    $error shared overloaded formal void wrong(Anything thing);
}

void test(Interface i) {
    i.run("");
    i.run("", "");
    i.run(1);
    i.run(1, 2);
    i.run(1,2,3);
    i.run(null);
    
    i.wrong(1);
    i.wrong("");
    i.wrong(null);
    i.wrong(object {});
}

class X() {
    overloaded shared void execute(void run()) => run();
    overloaded shared T execute<T>(T run()) given T satisfies Object => run();
}

class X2() {
    $error shared void execute(void run()) => run();
    $error shared T execute<T>(T run()) given T satisfies Object => run();
}

void do() {
    X().execute(void () {});
}

interface Inter<T> {}

class Clazz() {
    shared overloaded void find<T>(Inter<T> c) {
        find(c);
    }
    shared overloaded void find<T>() {
        Inter<T> list = nothing of Inter<T>;
    }
    
    shared class Find<T>(Inter<T> c) {
        Find(c);
    }
    $error shared class Find<T>() {
        Inter<T> list = nothing of Inter<T>;
    }
}

interface Iface {
    shared overloaded formal void y();
    overloaded shared formal void y(String s);
    shared overloaded formal void y(Integer i);
}

class Clas() satisfies Iface {
    shared overloaded actual void y() {}
    shared overloaded actual void y(String s) {}
    shared overloaded actual void y(Integer i) {}
    $error shared overloaded actual void y(Float x) {}
    shared overloaded void y(Byte x) {}
    $error void y(Character x) {}
    $error String y = "";
}

class My() {
    $error shared void equals() {}
}

class Sender() {}

class Controller() {
    overloaded shared void fun(String() fun) {}
    overloaded shared void fun<U>(Class<U> c, Object* objs) {}
    
    fun(`Sender`);
}

interface Predicate<T> {}

class Stream<T>(T* elements){
    overloaded shared Stream<T> filter(Predicate<T> pred) => this;
    overloaded shared Stream<T> filter(Boolean fun(T t)) => this;
}

class Stream2<T>(T* elements){
    $error shared Stream<T> filter(Predicate<T> pred) => this;
    $error shared Stream<T> filter(Boolean fun(T t)) => this;
}

void testStream() {
    Stream<String> result1 = Stream("").filter((String t) => true);
    Stream<String> result2 = Stream("").filter((t) => true);
    Stream<String> result3 = Stream("").filter(object satisfies Predicate<String> {});
    Stream<String|Integer> result4 = Stream("", 1).filter((t) => true);
    Stream<String|Integer> result5 = Stream("", 1).filter((String|Integer t) => true);
    $error Stream("").filter((Integer t) => true);
    $error Stream("").filter((t) => t);
    $error Stream("").filter(object satisfies Predicate<Integer> {});
    $error Stream("", 1).filter((Integer t) => true);
}


native("jvm")
class Native() {
    overloaded shared void fun(String s) {}
    overloaded shared void fun(Integer u) {}
    
    overloaded shared void func(String|Integer si) {}
    $error overloaded shared void func(Basic b) {}
    $error overloaded shared void func(Nothing n) {}
    $error overloaded shared void func(Null n) {}
    $error overloaded shared void func(Float f = 1.9) {}
    overloaded shared void func(Object o, Float f, Integer i) {}
    overloaded shared void func(Object* o) {}
    
    overloaded shared void sing(Singleton<String> sing) {}
    $error overloaded shared void sing(Singleton<Integer>? sing) {}
}

native shared void run();

native("jvm")
shared void run() {
    Native().fun("hello");
    Native().fun(0);
}

native("js")
shared void run() {}

class StaticClass {
    shared overloaded static void x() { print("bye"); }
    shared overloaded static void x(String s) { print(s); }
    shared overloaded static void x(Object o) { print(o); }
    shared overloaded static void x(Integer i) => print(10 * i);
    shared new () {}
}

void testStaticClass() {
    StaticClass.x();
    StaticClass.x(StaticClass());
    StaticClass.x("hello");
    StaticClass.x(10);
}

class Thing {
    shared actual String string;
    shared overloaded new () {
        string = "nil";
    }
    shared overloaded new (Object oj) {
        string = oj.string;
    }
    shared overloaded new (String string) {
        this.string = string.uppercased;
    }
    shared overloaded new (Integer integer) {
        this.string = (integer^2).string;
    }
    $error shared overloaded new (String string) {
        this.string = string;
    }
}

class OtherThing {
    shared actual String string;
    shared overloaded new make() {
        string = "nil";
    }
    shared overloaded new make(String string) {
        this.string = string.uppercased;
    }
    shared overloaded new make(Integer integer) {
        this.string = (integer^2).string;
    }
    $error shared overloaded new make(String string) {
        this.string = string;
    }
}


class Wrong {
    $error shared new () {}
    $error shared new (Integer i) {}
    
    $error shared new make() {}
    $error shared new make(Integer i) {}
}

void tryit() {
    Thing x = Thing();
    Thing y = Thing("");
    Thing z = Thing(1);
    OtherThing ox = OtherThing.make();
    OtherThing oy = OtherThing.make("");
    OtherThing oz = OtherThing.make(1);
}
