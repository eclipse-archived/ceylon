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
    @error shared formal void run(Integer int);
    @error shared formal void run(Integer int1, Integer int2);
    @error shared formal void run(Integer* int);
    @error shared formal void run(Integer? int);
    
    shared formal void wrong(Integer int);
    @error shared formal void wrong(Integer? int);
    @error shared formal void wrong(String string);
    @error shared formal void wrong(Object thing);
    @error shared formal void wrong(Anything thing);
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
