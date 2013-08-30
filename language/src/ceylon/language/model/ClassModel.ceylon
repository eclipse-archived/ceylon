import ceylon.language.model.declaration {
    ClassDeclaration
}

shared interface ClassModel<out Type=Anything, in Arguments=Nothing>
    satisfies ClassOrInterface<Type>
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
}
