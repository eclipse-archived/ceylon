doc "Test attribute/local evaluation"
void test(Process process) {
    Integer i = 1;
    Integer j = TestClass(5).run(i + 3);
    process.writeLine(j);
}
