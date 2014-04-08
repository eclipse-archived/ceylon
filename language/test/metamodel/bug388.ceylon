import ceylon.language.meta.declaration { FunctionDeclaration }

class Bug388() {
    by("nonone")
    Integer bar() => 0;
}

@test
shared void bug388() {
    value members = `class Bug388`.
            annotatedDeclaredMemberDeclarations<FunctionDeclaration, AuthorsAnnotation>();
    assert(members.size == 1);
    assert(exists bar = members.first, bar.name == "bar");
}
