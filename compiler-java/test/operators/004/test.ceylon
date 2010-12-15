doc "Test comparison operator"
void test(Process process) {
    process.writeLine(2 <=> 3);
    process.writeLine(3 <=> 3);
    process.writeLine(3 <=> 2);
}
