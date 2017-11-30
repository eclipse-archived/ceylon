import compat120{c=run}

shared void run() {
    print("hello, world");
    assert(exists version = process.arguments[0]);
    c(version);
}