public void test(Process p) {
    for (Natural n in 0..11) {
        p.writeLine(fib(n));
    }
}
