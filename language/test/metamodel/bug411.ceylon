import ceylon.language.meta.declaration { ClassDeclaration }

@test
shared void bug411() {
    value m = `module modules.imported`;
    assert(m.members.size == 1);
    assert(exists p = m.findPackage("modules.imported"));
    assert(p.members<ClassDeclaration>().size == 1);
}
