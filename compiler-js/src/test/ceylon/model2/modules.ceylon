import ceylon.language.model { modules, annotations }
import ceylon.language.model.declaration {
  Import, FunctionDeclaration, ClassOrInterfaceDeclaration
}
import check { check,fail }

void modulesTests() {
    print("Testing modules");
    check(modules.default exists, "Default module found");
    check(modules.list nonempty, "Loaded modules");
    check(modules.find("check", "0.1") exists, "Loaded module search");
    if (exists funmod = modules.find("functions", "0.1")) {
        check(funmod.dependencies.size == 2, "functions/0.1 should have 2 deps");
        check(funmod.dependencies.find((Import imp) => imp.name=="ceylon.language") exists,
            "functions/0.1 should depend on ceylon.language");
        check(funmod.dependencies.find((Import imp) => imp.name=="check") exists,
            "functions/0.1 should depend on ceylon.language");
        check(funmod.members.size == 1, "functions/0.1 has 1 package");
        check(funmod.annotations<Authors>().size == 1, "functions/0.1 has 1 annotations");
        if (exists pk = funmod.findPackage("functions")) {
            check(pk.name=="functions", "Package name should be functions");
            check(pk.annotations<Shared>() nonempty, "Package should have annotations");
            //This is a FunctionDeclaration
            value helloFun = pk.getFunction("hello");
            if (exists helloFun) {
                check(helloFun.annotations<Shared>() nonempty, "functions.hello should be shared");
            } else {
                fail("Package should have function hello");
            }
            //TODO should this be visible?
            check(pk.getValue("lx") exists, "Package has value lx");
            check(pk.getMember<FunctionDeclaration>("repeat") exists, "Package has member repeat");
            check(pk.members<ClassOrInterfaceDeclaration>().size > 3, "Package should have at least 3 functions");
        } else {
            fail("Module functions/0.1 should have package 'functions'");
        }
    } else {
        fail("Runtime loading of module search");
    }
}
