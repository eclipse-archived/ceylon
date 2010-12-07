doc "Test \"super\""
class SubClass() extends BaseClass() {
    void test(Process process) {
        process.writeLine("I am the subclass");
    }

    void run(Process process) {
        this.test(process);
        super.test(process);
    }
}
