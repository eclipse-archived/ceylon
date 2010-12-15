doc "Test mutable argument shizzle"
void test(Process process) {
    mutable Integer q = 15;
    a(q);
    process.writeLine(q);
}
