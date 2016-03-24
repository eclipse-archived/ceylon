import ceylon.language.meta.declaration { FunctionDeclaration, Declaration }

shared final annotation class Bug378Annotation(shared [Declaration*] args)
        satisfies OptionalAnnotation<Bug378Annotation, FunctionDeclaration> {
}

shared annotation Bug378Annotation bug378Annotation([Declaration*] args = [])
        => Bug378Annotation(args);

bug378Annotation shared void bug378() {}

@test
shared void testBug378() {
    value packages = `module annotations`.members;
    for (pkg in packages) {
        value members = pkg.annotatedMembers<FunctionDeclaration, Bug378Annotation>();
        print(members);
    }
}