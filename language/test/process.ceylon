@test
shared void testProcess() {
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
    process.write("write");
    process.writeLine(" and writeLine");
    process.flush();
    process.writeError("writeError");
    process.writeErrorLine(" and writeErrorLine");
    process.flushError();
    
    //language object
    check(language.version=="1.0.0", "language.version");
    check(language.majorVersion==1, "language.majorVersion");
    check(language.minorVersion==0, "language.minorVersion");
    check(language.releaseVersion==0, "language.releaseVersion");
    check(!language.versionName.empty, "language.versionName");
    check(language.majorVersionBinary==6, "language.majorVersionBinary");
    check(language.minorVersionBinary==0, "language.minorVersionBinary");
    print("Ceylon language version ``language.version`` major ``language.majorVersion`` " +
        "minor ``language.minorVersion`` release ``language.releaseVersion`` " +
        "\'``language.versionName``\' major bin ``language.majorVersionBinary`` minor bin " +
        "``language.minorVersionBinary``");
    check(process.string == "process", "process.string");
    check(language.string == "language", "language.string");
}

void testSystem() {
    // basically just test if everything can be called without error
    print("system[milliseconds:``system.milliseconds``ms,nanoseconds:``system.nanoseconds``ns," +
           "timezoneOffset:``system.timezoneOffset``ms, locale: ``system.locale``]");
    check(system.milliseconds > 0, "machine.milliseconds");
    check(system.nanoseconds > 0, "machine.milliseconds");
    Integer timezoneOffset = system.timezoneOffset;
    check(timezoneOffset >= -(12 * 3600 * 1000), "timezoneOffset min value");
    check(timezoneOffset <= (14 * 3600 * 1000), "timezoneOffset max value");
    check(system.locale.size > 0, "system.locale");
}

void testMachine() {
    // basically just test if everything can be called without error
    print("machine[name: ``machine.name``, version: ``machine.version``]");
    check(machine.name.size > 0, "machine.name");
    check(machine.version.size > 0, "machine.version");
    if (machine.name == "jvm") {
        check(machine.integerSize == 64, "jvm machine.integerSize");
    } else if (machine.name in {"node.js", "Browser"}) {
        check(machine.integerSize == 53, "js machine.integerSize");
    } else {
        fail("UNKNOWN BACKEND ``machine.name`` - please add tests for this backend");
    }
    if (machine.integerSize == 64) {
        check(machine.minIntegerValue == -9223372036854775808, "jvm machine.minIntegerValue");
        check(machine.maxIntegerValue == 9223372036854775807, "jvm machine.maxIntegerValue");
    } else if (machine.integerSize == 53) {
        check(machine.minIntegerValue == -9007199254740991, "js machine.minIntegerValue");
        check(machine.maxIntegerValue == 9007199254740989, "js machine.maxIntegerValue");
    } else {
        fail("UNKNOWN INTEGER SIZE `` 0.size `` - please add number tests for this platform");
    }
}

void testOperatingSystem() {
    // basically just test if everything can be called without error
    print("operatingSystem[name:``operatingSystem.name``, version:``operatingSystem.version``]");
    check(operatingSystem.name.size > 0, "operatingSystem.name");
    check(operatingSystem.version.size > 0, "operatingSystem.version");
    check(operatingSystem.newline.contains('\n'), "operatingSystem.newline");
    String pathSeparator = operatingSystem.pathSeparator;
    check((pathSeparator=="/") || (pathSeparator=="\\"), "operatingSystem.pathSeparator");
}
