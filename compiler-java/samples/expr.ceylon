doc "Simple arithmetic"
public void expr(ceylon.Process process) {
    process.writeLine((2+3).asString());
    process.writeLine((2-3).asString());
    process.writeLine((2*3).asString());
    process.writeLine((2**3).asString());
    process.writeLine((2/3).asString());
    process.writeLine((2%3).asString());
    process.writeLine((2&3).asString());
    process.writeLine((2|3).asString());
    process.writeLine((2^3).asString());
}
