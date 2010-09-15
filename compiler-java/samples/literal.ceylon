doc "Members of literals"
public void literal(Process process) {
    process.writeLine(1.5.natural());
    process.writeLine((5).natural()); // XXX should work without parens!
}
