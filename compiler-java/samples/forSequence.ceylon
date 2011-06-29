doc "Test a sequence and a loop"
shared void forSequence(Process process) {
    for (Natural i in { 1, 2, 3 }) {
        process.writeLine("Hello World");
    }
}

