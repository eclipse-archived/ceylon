import ceylon.language.model { modules }
import ceylon.language.model.declaration { Import, FunctionDeclaration }
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
        if (exists pk = funmod.findPackage("functions")) {
            check(pk.name=="functions", "Package name should be functions");
            check(pk.annotations<Shared>() nonempty, "Package should have annotations");
            //TODO check it's shared
            check(pk.getFunction("hello") exists, "Package has function hello");
            //TODO should this be visible?
            check(pk.getValue("lx") exists, "Package has value lx");
            check(pk.getMember<FunctionDeclaration>("repeat") exists, "Package has member repeat");
        } else {
            fail("Module functions/0.1 should have package 'functions'");
        }
    } else {
        fail("Runtime loading of module search");
    }
}
