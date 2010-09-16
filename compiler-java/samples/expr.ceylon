doc "Simple arithmetic"
public void expr(Process process) {
    // Unary arithmetic operations
    process.writeLine(-(2));

    // Binary arithmetic operations
    process.writeLine(2+3);
    process.writeLine(3-2);
    process.writeLine(2*3);
    process.writeLine(2**3);
    process.writeLine(2/3);
    process.writeLine(2%3);

    // Unary bitwise operations
    //process.writeLine(~(2));

    // Binary bitwise operations
    //process.writeLine(2&3);
    //process.writeLine(2|3);
    //process.writeLine(2^3);

    // Comparisons
    //process.writeLine(2==3);
    //process.writeLine(2===3);
    //process.writeLine(2!=3);
    process.writeLine(2<3);
    process.writeLine(2>3);
    process.writeLine(2<=3);
    process.writeLine(2>=3);
    process.writeLine(2<=>3);
}
