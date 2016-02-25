shared void hello() {
    print("Hello, ``process.arguments.first else "world"``");
}

shared void run() {
    hello();
}

shared void do() {
    hello();
}
