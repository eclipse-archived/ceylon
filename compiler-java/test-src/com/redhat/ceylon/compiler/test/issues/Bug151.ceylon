@nomodel
shared void bug151() {
    for (n in 0..50) {
        process.writeLine(fib(n).string);
    }
}
