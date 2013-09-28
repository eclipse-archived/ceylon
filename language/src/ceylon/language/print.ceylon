"Print a line to the standard output of the virtual machine 
 process, printing the given value\'s `string`, or `<null>` 
 if the value is `null`.
 
 This method is a shortcut for:
 
     process.writeLine(line?.string else \"<null>\")
 
 and is intended mainly for debugging purposes."
// FIXME: see https://github.com/ceylon/ceylon-spec/issues/694
//see (`process.writeLine`)
by ("Gavin")
shared void print(Anything val) =>
        process.writeLine(stringify(val));

"Print multiple values to the standard output of the virtual 
 machine process as a single line of text, separated by a
 given character sequence."
by ("Gavin")
shared void printAll({Anything*} values,
        "A character sequence to use to separate the values"
        String separator=", ") {
    if (exists first = values.first) {
        process.write(stringify(first));
        for (val in values.rest) {
            process.write(separator);
            process.write(stringify(val));
        }
    }
    process.write(operatingSystem.newline);
}

String stringify(Anything val) => val?.string else "<null>";
