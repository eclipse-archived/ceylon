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
import com.redhat.ceylon.compiler.typechecker.tree {
    TreeUtil
}
import com.redhat.ceylon.model.typechecker.model {
    ModelClass=Class
}
import ceylon.language.meta {
    modules
}

shared void run() {
    print("Running on ``language.version`` according to language.version");
    print("Compiled against com.redhat.ceylon.common/``Versions.\iCEYLON_VERSION`` according to com.redhat.ceylon.common::Versions");
    // Check module literals of direct dependencies
    print("module com.redhat.ceylon.compiler.java.test.compat.target/1.0.0" == `module`.string);
    print(`module ceylon.language`.string);
    assert("module ceylon.language/``language.version``" == `module ceylon.language`.string);
    //XXX see ceylon-compiler#2428 print("\`module ceylon.runtime\` is `` `module ceylon.runtime` ``");
    print(`module com.redhat.ceylon.common`.string);
    assert("module com.redhat.ceylon.common/``language.version``" == `module com.redhat.ceylon.common`.string);
    //XXX see ceylon-compiler#2428print(`module com.redhat.ceylon.typechecker`); 
    //XXX WTF print(`module "com.redhat.ceylon.module-resolver"`); 
    print(`module com.redhat.ceylon.compiler.java`.string);
    assert("module com.redhat.ceylon.compiler.java/``language.version``" == `module com.redhat.ceylon.compiler.java`.string);
    print(`module com.redhat.ceylon.compiler.js`.string);
    assert("module com.redhat.ceylon.compiler.js/``language.version``" == `module com.redhat.ceylon.compiler.js`.string);
    //XXX see ceylon-compiler#2428 print(`module com.redhat.ceylon.model`); 
    //XXX see ceylon-compiler#2428print("\`module ceylon.bootstrap\` is `` `module ceylon.bootstrap` ``"); 
    
    print("\`module\`.dependencies");
    for (imp in `module`.dependencies) {
        print("  ``imp``");
        // All our dependencies should be version 1.2.1 as runtime 
        assert(language.version == imp.version);
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
    Class<TreeUtil> tc = `TreeUtil`;
    print("  ``tc``");
    Class<Versions> common = `Versions`;
    print("  ``common``");
    Class<AbstractRuntime> runtime = `AbstractRuntime`;
    print("  ``runtime``");
    Class<Bootstrap> bootstrap = `Bootstrap`;
    print("  ``bootstrap``");
}