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
shared void bug783(){
    value decl = `function Iterable.map`;
    assert(is FunctionDeclaration collecting = decl.parameterDeclarations[0]);
    assert(collecting.parameterDeclarations.size == 1);
    assert(is ValueDeclaration elem = collecting.parameterDeclarations[0]);
    assert(is OpenTypeVariable collectingType = collecting.openType,
           collectingType.declaration.name == "Result");
    assert(is OpenTypeVariable elemType = elem.openType,
           elemType.declaration.name == "Element");
}