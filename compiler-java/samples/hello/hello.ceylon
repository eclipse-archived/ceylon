shared void hello() {
    for (n in 0..50) {
        process.writeLine(fib(n).string);
    }
}
