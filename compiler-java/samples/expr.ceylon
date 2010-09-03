doc "Simple arithmetic"
public void expr(ceylon.Process process) {
    // Unary arithmetic operations
    process.writeLine($(-(2)));

    // Binary arithmetic operations
    process.writeLine($(2+3));
    process.writeLine($(2-3));
    process.writeLine($(2*3));
    process.writeLine($(2**3));
    process.writeLine($(2/3));
    process.writeLine($(2%3));

    // Unary bitwise operations
    //process.writeLine($(~(2)));

    // Binary bitwise operations
    //process.writeLine($(2&3));
    //process.writeLine($(2|3));
    //process.writeLine($(2^3));

    // Comparisons
    //process.writeLine($(2==3));
    //process.writeLine($(2===3));
    //process.writeLine($(2!=3));
    process.writeLine($(2<3));
    process.writeLine($(2>3));
    process.writeLine($(2<=3));
    process.writeLine($(2>=3));
    process.writeLine($(2<=>3));
}
