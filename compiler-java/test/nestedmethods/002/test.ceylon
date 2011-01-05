public void test(Process p) {
    Natural fib(Natural n) {
        if (n <= 1) {
            return n;
        }
        return fib(n - 1) + fib(n - 2);
    }

    p.writeLine(fib(0));
    p.writeLine(fib(1));
    p.writeLine(fib(2));
    p.writeLine(fib(3));
    p.writeLine(fib(4));
    p.writeLine(fib(5));
    p.writeLine(fib(6));
    p.writeLine(fib(7));
    p.writeLine(fib(8));
    p.writeLine(fib(9));
    p.writeLine(fib(10));
    p.writeLine(fib(11));
    p.writeLine(fib(12));
}
