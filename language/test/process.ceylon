void testProcess() {
    // basically just test if everything can be called without error
    Void args = process.arguments;
    if (is Void[] args) {
        for (arg in args) {
            assert(is String arg, "process.arguments");
        }
    } else {
        fail("process.arguments is no sequence");
    }
    Void argPresent = process.namedArgumentPresent("");
    assert(is Boolean argPresent, "process.namedArgumentPresent");
    assert(!(exists process.namedArgumentValue("")), "process.namedArgumentValue");
    String? filesep = process.propertyValue("file.separator");
    if (exists filesep) {
        assert((filesep=="/")||(filesep=="\\"), "process.propertyValue");
    } else {
        fail("process.propertyValue (null)");
    }
    assert(process.newline.contains(`\n`), "process.newline");
    process.write("test write");
    process.writeLine(" and writeLine");
    process.writeError("test writeError");
    process.writeErrorLine(" and writeErrorLine");
    assert(process.milliseconds > 0, "process.milliseconds");
    assert(process.nanoseconds > 0, "process.milliseconds");
}