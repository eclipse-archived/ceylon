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