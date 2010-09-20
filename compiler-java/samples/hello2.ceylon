public void hello2(Process process) {
    process.writeLine("Hello, World!");
}
public void hello2(Process process, String name) {
    process.writeLine("Hello");
    process.writeLine(name);
}
