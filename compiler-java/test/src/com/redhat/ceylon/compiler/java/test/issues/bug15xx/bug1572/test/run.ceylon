import ceylon.language.meta {
    modules
}
import ceylon.language.meta.declaration {
    Module,
    ClassDeclaration
}

import org.jboss.modules {
    JBossModule=Module {
        ceylonModuleLoader=callerModuleLoader
    },
    ModuleIdentifier {
        createModuleIdentifier=create
    }
}

Module? loadModule(String name, String version) {
    loadModuleInClassPath(name, version);
    return modules.find(name, version);
}

void loadModuleInClassPath(String modName, String modVersion) {
    value modIdentifier = createModuleIdentifier(modName, modVersion);
    value mod = ceylonModuleLoader.loadModule(modIdentifier);
}

shared void run() {
    assert(exists mod = loadModule("com.redhat.ceylon.compiler.java.test.issues.bug15xx.bug1572.mod", "1"));
    for (pkg in mod.members) {
        for (klass in pkg.annotatedMembers<ClassDeclaration, SharedAnnotation>()){
            print(klass.annotatedMemberDeclarations<ClassDeclaration, SharedAnnotation>());
        }
    }
}