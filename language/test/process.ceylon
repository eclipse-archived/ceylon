void testProcess() {
    // basically just test if everything can be called without error
    Void args = process.arguments;
    if (is Void[] args) {
        for (arg in args) {
            check(is String arg, "process.arguments");
        }
    } else {
        fail("process.arguments is no sequence");
    }
    Void argPresent = process.namedArgumentPresent("");
    check(is Boolean argPresent, "process.namedArgumentPresent");
    check(!(exists process.namedArgumentValue("")), "process.namedArgumentValue");
    String? filesep = process.propertyValue("file.separator");
    if (exists filesep) {
        check((filesep=="/")||(filesep=="\\"), "process.propertyValue");
    } else {
        fail("process.propertyValue (null)");
    }
    check(process.newline.contains(`\n`), "process.newline");
    process.write("test write");
    process.writeLine(" and writeLine");
    process.writeError("test writeError");
    process.writeErrorLine(" and writeErrorLine");
    check(process.milliseconds > 0, "process.milliseconds");
    check(process.nanoseconds > 0, "process.milliseconds");
}
