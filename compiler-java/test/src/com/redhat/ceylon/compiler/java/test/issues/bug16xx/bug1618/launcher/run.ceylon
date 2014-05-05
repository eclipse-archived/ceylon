import ceylon.language.meta {
    modules
}
import ceylon.language.meta.declaration {
    Module,
    ValueDeclaration
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
    value modClassLoader = mod.classLoader;
    value classToLoad = "``modName``.module_";
    modClassLoader.loadClass(classToLoad);
}

shared void run() {
    assert(exists mod = loadModule("com.redhat.ceylon.compiler.java.test.issues.bug16xx.bug1618.main", "1"));
    for (pkg in mod.members) {
        print(pkg.annotatedMembers<ValueDeclaration, SharedAnnotation>());
    }
}