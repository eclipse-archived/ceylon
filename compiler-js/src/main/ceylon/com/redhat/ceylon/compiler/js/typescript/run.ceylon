import tsc {
    ...
}
import ceylon.json {
    JsonObject,
    JsonArray
}

suppressWarnings ("expressionTypeNothing")
shared void run() {
    if (process.arguments.size < 3) {
        process.writeErrorLine("Usage: load-typescript MODULE-NAME MODULE-VERSION FILE...");
        process.exit(64);
    }
    assert (exists moduleName = process.arguments[0]);
    assert (exists moduleVersion = process.arguments[1]);
    assert (nonempty files = process.arguments[2...]);
    
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
        dynamic {
            eval("(function(program,js){program.emit(undefined,function(fileName,data){js.write(data);});})")(program, js);
            // TODO diagnostics, see https://github.com/Microsoft/TypeScript/wiki/Using-the-Compiler-API#a-minimal-compiler
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
