import ceylon.language.meta.declaration {
    ClassDeclaration,
    CallableConstructorDeclaration
}

serializable
class Bug7362(shared String source) {}

@test
void bug7362() {
    ClassDeclaration decl = `Bug7362`.declaration;
    assert (exists firstConstructor = decl.constructorDeclarations().first,
        is CallableConstructorDeclaration firstConstructor);
    assert (firstConstructor.invoke([], "hello") is Bug7362);
}