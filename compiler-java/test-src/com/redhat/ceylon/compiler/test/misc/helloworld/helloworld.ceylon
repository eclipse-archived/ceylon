doc "The classic Hello World program"
shared void helloworld(Process process) {
    for (Natural i -> String s in {1->"a", 2->"b"}) {
        process.writeLine("Hello World");
    }
}