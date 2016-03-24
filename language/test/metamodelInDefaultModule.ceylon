import ceylon.language.meta.declaration { ClassDeclaration }

@test
shared void testMetamodelInDefaultModule() {
    check(!`package`.members<ClassDeclaration>().empty, "default module members");
}