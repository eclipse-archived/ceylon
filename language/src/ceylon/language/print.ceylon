/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Print a line to the standard output of the virtual machine 
 process, printing the given value\'s `string`, or `<null>` 
 if the value is `null`.
 
 This function is a shortcut for:
 
     process.writeLine(line?.string else \"<null>\")
 
 and is intended mainly for debugging purposes."
see (function process.writeLine)
by ("Gavin")
tagged("Environment")
shared void print(Anything val) 
        => process.writeLine(stringify(val));

"Print multiple values to the standard output of the virtual 
 machine process as a single line of text, separated by a
 given character sequence."
by ("Gavin")
see (function process.write)
tagged("Environment")
shared void printAll({Anything*} values,
        "A character sequence to use to separate the values"
        String separator=", ") {
    variable value first = true;
    values.each((element) {
        if (first) {
            first = false;
        }
        else {
            process.write(separator);
        }
        process.write(stringify(element));
    });
    process.write(operatingSystem.newline);
}

String stringify(Anything val) => val?.string else "<null>";
