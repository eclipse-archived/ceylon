doc "Test operators"
void test(Process process) {
    // Prefix increment
    mutable Natural a = 1;
    process.writeLine(a);
    Natural b = ++a;
    process.writeLine(a);
    process.writeLine(b);

    // Postfix increment
    Natural c = a++;
    process.writeLine(a);
    process.writeLine(c);
}
