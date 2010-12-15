doc "Test optionalization of non-optional things"
void test(Process process) {
    String? result = c("Hello World");
    if (exists result) {
        process.writeLine(result);
    }
    else {
        process.writeLine("FAIL");
    }
}
