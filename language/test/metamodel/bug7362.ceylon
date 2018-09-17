import ceylon.language.meta.declaration {
    ClassDeclaration,
    CallableConstructorDeclaration
}

serializable
class Bug7362(shared String source) {}

serializable shared
class Bug7361(
    shared String source,
    shared String description,
    shared Integer count = 0) {}

@test
shared void bug7362() {
    ClassDeclaration decl = `Bug7362`.declaration;
    assert (exists firstConstructor = decl.constructorDeclarations().first,
        is CallableConstructorDeclaration firstConstructor);
    assert (firstConstructor.invoke([], "hello") is Bug7362);
}

@test
shared void bug7361() {
    ClassDeclaration decl = `Bug7361`.declaration;
    assert (exists firstConstructor = decl.constructorDeclarations().first,
        is CallableConstructorDeclaration firstConstructor);
    assert (firstConstructor.invoke([], "hello", "world") is Bug7361);
}