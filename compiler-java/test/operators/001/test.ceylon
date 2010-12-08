doc "Test operators"
void test(Process process) {
    process.writeLine("prefix increment");
    mutable Natural a = 1;
    process.writeLine(a);
    Natural b = ++a;
    process.writeLine(a);
    process.writeLine(b);

    process.writeLine("postfix increment");
    Natural c = a++;
    process.writeLine(a);
    process.writeLine(c);

    process.writeLine("prefix decrement");
    mutable Natural d = 57;
    process.writeLine(d);
    Natural e = --d;
    process.writeLine(d);
    process.writeLine(e);

    process.writeLine("postfix decrement");
    Natural f = d--;
    process.writeLine(d);
    process.writeLine(f);
}
