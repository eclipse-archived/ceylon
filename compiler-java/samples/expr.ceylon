doc "Simple arithmetic"
public void expr(ceylon.Process process) {
    // Binary arithmetic operations
    process.writeLine((2+3).asString());
    process.writeLine((2-3).asString());
    process.writeLine((2*3).asString());
    process.writeLine((2**3).asString());
    process.writeLine((2/3).asString());
    process.writeLine((2%3).asString());

    // Binary bitwise operations
    process.writeLine((2&3).asString());
    process.writeLine((2|3).asString());
    process.writeLine((2^3).asString());

    // Comparisons
    process.writeLine((2==3).asString());
    process.writeLine((2===3).asString());
    process.writeLine((2!=3).asString());
    process.writeLine((2<3).asString());
    process.writeLine((2>3).asString());
    process.writeLine((2<=3).asString());
    process.writeLine((2>=3).asString());
    process.writeLine((2<=>3).asString());
}
