import ceylon.language.model { modules }
import check { check,fail }

void modulesTests() {
    print("Testing modules");
    check(modules.default exists, "Default module found");
    check(modules.list nonempty, "Loaded modules");
    check(modules.find("check", "0.1") exists, "Loaded module search");
    if (exists funmod = modules.find("functions", "0.1")) {
        print("``funmod.name``/``funmod.version`` loaded at runtime");
    } else {
        fail("Runtime loading of module search");
    }
}
