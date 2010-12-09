doc "Test method invocation"
class Test(Process p) {
    Process process = p;

    void run(String a, String b, String c) {
        process.writeLine("a = ");
        process.writeLine(a);
        process.writeLine("b = ");
        process.writeLine(b);
        process.writeLine("c = ");
        process.writeLine(c);
    }
}
