import com.redhat.ceylon.launcher{
    Bootstrap
}
import ceylon.language.meta.model {
    Class
}
import ceylon.modules.api.runtime {
    AbstractRuntime
}

import com.redhat.ceylon.cmr.api {
    ModuleInfo
}
import com.redhat.ceylon.common {
    Versions
}
import com.redhat.ceylon.compiler.java.codegen {
    Operators
}
import com.redhat.ceylon.compiler.js.util {
    TypeUtils
}
import com.redhat.ceylon.compiler.typechecker {
    TypeCheckerBuilder
}
import com.redhat.ceylon.model.typechecker.model {
    ModelClass=Class
}
import ceylon.language.meta {
    modules
}
"Run the module `compat`."
shared void run() {
    print("Compiled with ``Versions.\iCEYLON_VERSION`` according to com.redhat.ceylon.common::Versions");
    assert("1.2.0 (A Series Of Unlikely Explanations)" == Versions.\iCEYLON_VERSION);
    print("Running on ``language.version`` (``language.versionName``) according to language.version");
    assert("1.2.1" == language.version);
    
    // Check module literals of direct dependencies
    assert("module compat120/1.0.0" == `module`.string);
    assert("module ceylon.language/1.2.1" == `module ceylon.language`.string);
    //XXX see ceylon-compiler#2428 print("\`module ceylon.runtime\` is `` `module ceylon.runtime` ``");
    assert("module com.redhat.ceylon.common/1.2.1" == `module com.redhat.ceylon.common`.string);
    //XXX see ceylon-compiler#2428print(`module com.redhat.ceylon.typechecker`); 
    //XXX WTF print(`module "com.redhat.ceylon.module-resolver"`); 
    assert("module com.redhat.ceylon.compiler.java/1.2.1" == `module com.redhat.ceylon.compiler.java`.string);
    assert("module com.redhat.ceylon.compiler.js/1.2.1" == `module com.redhat.ceylon.compiler.js`.string);
    //XXX see ceylon-compiler#2428 print(`module com.redhat.ceylon.model`); 
    //XXX see ceylon-compiler#2428print("\`module ceylon.bootstrap\` is `` `module ceylon.bootstrap` ``"); 
    
    print("\`module\`.dependencies");
    for (imp in `module`.dependencies) {
        print("  ``imp``");
        // All our dependencies should be version 1.2.1 as runtime 
        assert("1.2.1" == imp.version);
    }
    // check we can list all the loaded modules
    print("modules.list: ");
    for (mod in modules.list) {
        print("  ``mod``");
    }
    
    print("Now test metamodels of distribution modules");
    Class<String> lang = `String`;
    assert(lang.declaration.containingModule == `module ceylon.language`);
    Class<ModuleInfo> cmr = `ModuleInfo`;
    print("  ``cmr``");
    //print(cmr.declaration.containingModule);
    Class<ModelClass> model = `ModelClass`;
    print("  ``model``");
    Class<Operators> jvm = `Operators`;
    print("  ``jvm``");
    assert(jvm.declaration.containingModule == `module com.redhat.ceylon.compiler.java`);
    Class<TypeUtils> js = `TypeUtils`;
    print("  ``js``");
    assert(js.declaration.containingModule == `module com.redhat.ceylon.compiler.js`);
    Class<TypeCheckerBuilder> tc = `TypeCheckerBuilder`;
    print("  ``tc``");
    Class<Versions> common = `Versions`;
    print("  ``common``");
    Class<AbstractRuntime> runtime = `AbstractRuntime`;
    print("  ``runtime``");
    Class<Bootstrap> bootstrap = `Bootstrap`;
    print("  ``bootstrap``");
}