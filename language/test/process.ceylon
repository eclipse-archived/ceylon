void testProcess() {
    // basically just test if everything can be called without error
    Anything args = process.arguments;
    if (is Anything[] args) {
        for (arg in args) {
            check(arg is String, "process.arguments");
        }
    } else {
        fail("process.arguments is no sequence");
    }
    Anything argPresent = process.namedArgumentPresent("");
    check(argPresent is Boolean, "process.namedArgumentPresent");
    check(!process.namedArgumentValue("") exists, "process.namedArgumentValue");
    String? filesep = process.propertyValue("file.separator");
    if (exists filesep) {
        check((filesep=="/")||(filesep=="\\"), "process.propertyValue");
    } else {
        fail("process.propertyValue (null)");
    }
    check(process.newline.contains(`\n`), "process.newline");
    process.write("write");
    process.writeLine(" and writeLine");
    process.writeError("writeError");
    process.writeErrorLine(" and writeErrorLine");
    print("Process VM " process.vm " version " process.vmVersion " on " process.os " v" process.osVersion "");
    check(process.milliseconds > 0, "process.milliseconds");
    check(process.nanoseconds > 0, "process.milliseconds");

    //language object
    check(language.version=="0.5", "language.version");
    check(language.majorVersion==0, "language.majorVersion");
    check(language.minorVersion==5, "language.minorVersion");
    check(language.releaseVersion==0, "language.releaseVersion");
    check(!language.versionName.empty, "language.versionName");
    check(language.majorVersionBinary==3, "language.majorVersionBinary");
    check(language.minorVersionBinary==0, "language.minorVersionBinary");
    print("Ceylon language version " language.version " major " language.majorVersion
        " minor " language.minorVersion " release " language.releaseVersion
        " '" language.versionName "' major bin " language.majorVersionBinary " minor bin "
        language.minorVersionBinary "");
    check(process.string == "process", "process.string");
    check(language.string == "language", "language.string");
}
