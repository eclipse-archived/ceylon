doc "Test member functions of Character literals"
void test(Process process) {
    process.writeLine(`c`.uppercase());
    process.writeLine(`E`.lowercase());
    process.writeLine(`y`.lowercase());
    process.writeLine(`L`.lowercase());
    process.writeLine(`o`.lowercase());
    process.writeLine(`N`.lowercase());
    process.writeLine("woo hoo!");
}
