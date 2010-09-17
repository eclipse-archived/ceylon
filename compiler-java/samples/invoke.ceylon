doc "The classic Hello World program"
public void invoke(ceylon.Process process) {
    Statements s = Statements(1);

    process.writeLine("Hello, World!");
    process.writeLine(s);
}
