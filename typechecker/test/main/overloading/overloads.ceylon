import ceylon.language.meta.model {
    Class
}

interface Runnable {}

object service {
    shared void execute(Runnable run) {}
    @error shared void execute(Anything run(String id)) {}
}


void run() {
    service.execute(object satisfies Runnable {});
    service.execute((id) => print("id: " + id));
    service.execute(print);
}


interface Interface {
    shared formal void run(String str);
    @error shared formal void run(String str1, String str2);
    shared formal void run(Integer int);
    shared formal void run(Integer int1, Integer int2);
    shared formal void run(Integer* int);
    shared formal void run(Integer? int);
    
    shared formal void wrong(Integer int);
    @error shared formal void wrong(Integer? int);
    shared formal void wrong(String string);
    shared formal void wrong(Object thing);
    shared formal void wrong(Anything thing);
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
    shared void execute(void run()) => run();
    @error shared T execute<T>(T run()) given T satisfies Object => run();
}

void do() {
    X().execute(void () {});
}

interface Inter<T> {}

class Clazz() {
    shared void find<T>(Inter<T> c) {
        find(c);
    }
    @error shared void find<T>() {
        Inter<T> list = nothing of Inter<T>;
    }
    
    shared class Find<T>(Inter<T> c) {
        Find(c);
    }
    @error shared class Find<T>() {
        Inter<T> list = nothing of Inter<T>;
    }
}

interface Iface {
    shared formal void y();
    @error shared formal void y(String s);
    shared formal void y(Integer i);
}

class Clas() satisfies Iface {
    shared actual void y() {}
    shared actual void y(String s) {}
    shared actual void y(Integer i) {}
    @error shared actual void y(Float x) {}
    @error shared void y(Byte x) {}
    @error void y(Character x) {}
    @error String y = "";
}

class My() {
    @error shared void equals() {}
}

class Sender() {}

class Controller() {
    shared void fun(String() fun) {}
    @error shared void fun<U>(Class<U> c, Object* objs) {}
    
    fun(`Sender`);
}

