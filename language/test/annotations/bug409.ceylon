import ceylon.language.meta.declaration{ ... }

@test
see(`value process.arguments`)
shared void bug409() {
    `package annotations`.annotatedMembers<FunctionDeclaration|ClassDeclaration, SharedAnnotation|DocAnnotation>();
}
