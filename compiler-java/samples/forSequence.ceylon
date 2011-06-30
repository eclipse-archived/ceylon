doc "Test a sequence and a loop"
shared void forSequence(Process process) {
    for (Natural i -> String s in {1->"a", 2->"b"}) {
        process.writeLine("Hello World");
    }
}