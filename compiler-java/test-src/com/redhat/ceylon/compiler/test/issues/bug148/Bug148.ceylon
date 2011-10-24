@nomodel
shared void bug148() {
    for (n in 0..50) {
        process.writeLine(fib(n).string);
    }
}
