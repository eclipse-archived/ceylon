doc "Simple arithmetic"
public void expr(ceylon.Process process) {
    // Binary arithmetic operations
    process.writeLine((2+3).string());
    process.writeLine((2-3).string());
    process.writeLine((2*3).string());
    process.writeLine((2**3).string());
    process.writeLine((2/3).string());
    process.writeLine((2%3).string());

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
