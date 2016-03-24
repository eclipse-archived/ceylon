import ceylon.language.meta.declaration {
    ClassDeclaration,
    FunctionDeclaration,
    ValueDeclaration,
    OpenTypeVariable
}
import ceylon.language.meta {
    type
}

@test
shared void bug5893(){
    print(`String`.getValueConstructors());
}