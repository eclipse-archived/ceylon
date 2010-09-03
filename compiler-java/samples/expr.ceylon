doc "Simple arithmetic"
public void expr(ceylon.Process process) {
    // Unary arithmetic operations
    process.writeLine((-(2)).string());

    // Binary arithmetic operations
    process.writeLine((2+3).string());
    process.writeLine((2-3).string());
    process.writeLine((2*3).string());
    process.writeLine((2**3).string());
    process.writeLine((2/3).string());
    process.writeLine((2%3).string());

    // Unary bitwise operations
    //process.writeLine((~(2)).string());

    // Binary bitwise operations
    //process.writeLine((2&3).string());
    //process.writeLine((2|3).string());
    //process.writeLine((2^3).string());

    // Comparisons
    //process.writeLine((2==3).string());
    //process.writeLine((2===3).string());
    //process.writeLine((2!=3).string());
    process.writeLine((2<3).string());
    process.writeLine((2>3).string());
    process.writeLine((2<=3).string());
    process.writeLine((2>=3).string());
    process.writeLine((2<=>3).string());
}
