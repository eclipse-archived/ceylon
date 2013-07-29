import ceylon.language.model.declaration {
    ClassDeclaration
}

shared interface ClassModel<out Type, in Arguments>
    satisfies ClassOrInterface<Type>
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
}
