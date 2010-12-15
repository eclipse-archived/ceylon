void test(Process process) {
    process.writeLine("what");
    test(process, "time");
    test(2.4, process);
    test(5, process);
}

void test(Process process, String line) {
    process.writeLine(line);
}

void test(Float dummy, Process process) {
    process.writeLine("is");
}

void test(Natural index, Process process) {
    if (index == 5) {
        process.writeLine("it?");
    }
}
