import ceylon.language {
    Map // explicit import to override tsc’s Map
}
import tsc {
    ...
}
import ceylon.collection {
    ArrayList,
    HashMap
}
import ceylon.json {
    JsonObject,
    JsonArray
}

shared variable String moduleName = "";
shared variable String moduleVersion = "";
shared variable [String+] files = [""];

"Parses the given [[arguments]] into options (`--foo=bar`, `--foo bar`) and positional arguments.
 Returns a tuple of options and positional arguments, or a string error message if the arguments can’t be parsed.
 If the special flags `--help` or `--version` are encountered, they are also returned as a string."
shared [Map<String, String>, List<String>]|String parseArguments(variable String[] arguments) {
    value options = HashMap<String, String>();
    value positionalArguments = ArrayList<String>(arguments.size);
    while (nonempty [first, *rest] = arguments) {
        arguments = rest;
        if (first in { "--help", "--version" }) {
            return first;
        }
        if (first.startsWith("--")) {
            String optionName;
            String optionValue;
            if (exists index->eq = first.locate('='.equals)) {
                optionName = first[2..(index-1)];
                optionValue = first[(index+1)...];
            } else {
                optionName = first[2...];
                if (nonempty [val, *rest2] = arguments) {
                    optionValue = val;
                    arguments = rest2;
                } else {
                    return "Missing value for option '``optionName``'";
                }
            }
            options.put(optionName, optionValue);
        } else {
            positionalArguments.add(first);
        }
    }
    return [options, positionalArguments];
}

"Processes the given options and positional arguments (usually from [[parseArguments]]).
 Returns [[null]] on success, otherwise a string error message."
shared String? processArguments([Map<String, String>, List<String>] arguments) {
    value [options, positionalArguments] = arguments;
    for (optionName->optionValue in options) {
        switch (optionName)
        case ("out") {
            return "Option 'out' not yet implemented";
        }
        else {
            return "Unknown option '``optionName``'";
        }
    }
    if (exists name = positionalArguments[0]) {
        moduleName = name;
    } else {
        return "Module name argument missing";
    }
    if (exists version = positionalArguments[1]) {
        moduleVersion = version;
    } else {
        return "Module version argument missing";
    }
    if (nonempty remaining = positionalArguments[2...].sequence()) {
        files = remaining;
    } else {
        return "File arguments missing";
    }
    return null;
}

"A usage information string."
shared String usage = "Usage: load-typescript MODULE-NAME MODULE-VERSION FILE...";

suppressWarnings ("expressionTypeNothing")
shared void run() {
    variable String|[Map<String, String>, List<String>]|Null result = parseArguments(process.arguments);
    if (is [Map<String, String>, List<String>] args = result) {
        result = processArguments(args);
    }
    if (is String error = result) {
        switch (error)
        case ("--help") {
            print(usage);
            return;
        }
        case ("--version") {
            print("`` `module`.name ``/`` `module`.version ``");
            // TODO copyright / license
        }
        else {
            process.writeErrorLine("Error: ``error``.");
            process.writeErrorLine(usage);
            process.exit(64);
        }
    } else {
        start();
    }
}

"Main function.
 [[moduleName]], [[moduleVersion]] and [[files]] must be initialized."
shared void start() {
    "Must be initialized"
    assert (moduleName != "",
        moduleVersion != "",
        files != [""]);
    try (js = Sha1Writer("modules/``moduleName``/``moduleVersion``/``moduleName``-``moduleVersion``.js"),
        model = Sha1Writer("modules/``moduleName``/``moduleVersion``/``moduleName``-``moduleVersion``-model.js")) {
        
        CompilerOptions options;
        dynamic { options = eval("({module: 3})"); } // UMD; TODO don’t hardcode enum constant
        value host = createCompilerHost(options);
        Program program;
        dynamic {
            // TODO write in Ceylon
            program = eval("(function(createProgram, options, host){return createProgram([``",".join(files.map((s) => "'" + s + "'"))``], options, host)})")(createProgram, options, host);
        }
        // prepare model object
        value packageObject = JsonObject {
            "$pkg-pa" -> packAnnotations { shared = true; }
        };
        value ccmm = JsonObject {
            moduleNameKey->moduleName,
            moduleVersionKey->moduleVersion,
            moduleBinaryVersionKey->binaryVersion,
            moduleDependenciesKey -> JsonArray {
                "ceylon.language/``languageVersion``"
                // TODO dependency handling
            },
            moduleName->packageObject
        };
        // emit program; TODO write in Ceylon
        EmitResult result;
        dynamic {
            result = eval("(function(program,js){
                               let result = program.emit(undefined,function(fileName,data){js.write(data);});
                               result.emittedFiles = result.emittedFiles || [];
                               result.sourceMaps = result.sourceMaps || [];
                               return result;
                           })")(program, js);
            // TODO diagnostics, see https://github.com/Microsoft/TypeScript/wiki/Using-the-Compiler-API#a-minimal-compiler
        }
        value diagnostics = array(getPreEmitDiagnostics(program)).sequence().append(array(result.diagnostics).sequence());
        for (diagnostic in diagnostics) {
            //value lineAndCharacter = getLineAndCharacterOfPosition(diagnostic.file, diagnostic.start);
            value message = flattenDiagnosticMessageText(diagnostic.messageText, "\n");
            process.writeErrorLine(message);
        }
        // write JS header
        js.writeLine("(function(define) { define(function(require, ex$, module) {
                      
                      var _CTM$;function $CCMM$(){if (_CTM$===undefined)_CTM$=require('``moduleName``/``moduleVersion``/``moduleName``-``moduleVersion``-model').$CCMM$;return _CTM$;}
                      ex$.$CCMM$=$CCMM$;
                      var m$1=require('ceylon/language/``languageVersion``/ceylon.language-``languageVersion``');
                      m$1.$addmod$(m$1,'ceylon.language/``languageVersion``');
                      m$1.$addmod$(ex$,'``moduleName``/``moduleVersion``');
                      ex$.$mod$ans$=[];
                      ex$.$pkg$ans$``moduleName``=function(){return[m$1.shared()];};");
        // visit: populate model object and add additional values to ex$ object
        for (sourceFile in program.getSourceFiles()) {
            if (sourceFile.fileName.endsWith("/lib.d.ts")) {
                // skip lib file
                // TODO need to figure out where tsc gets the lib file from, and whether this is supposed to be necessary
                continue;
            }
            forEachChild(sourceFile, visitDeclaration(packageObject, js, program));
        }
        // write JS footer
        js.writeLine("});
                      }(typeof define==='function' && define.amd ? define : function (factory) {
                      if (typeof exports!=='undefined') { factory(require, exports, module);
                      } else { throw 'no module loader'; }
                      }));");
        // write model
        model.writeLine("(function(define) { define(function(require, ex$, module) {
                         ex$.$CCMM$=``ccmm``;
                         });
                         }(typeof define==='function' && define.amd ? define : function (factory) {
                         if (typeof exports!=='undefined') { factory(require, exports, module);
                         } else { throw 'no module loader'; }
                         }));");
    } catch (Throwable t) {
        t.printStackTrace();
    }
}
