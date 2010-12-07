class SubClass() {
    void test(Process process) {
        process.writeLine("I am the subclass");
    }

    void run(Process process) {
        test(process);
    }
}
