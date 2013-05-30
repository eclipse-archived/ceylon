import ceylon.language.metamodel{
    //type, 
    //annotations, optionalAnnotation, sequencedAnnotations,
    //Annotation2 = Annotation, 
    ConstrainedAnnotation, OptionalAnnotation, SequencedAnnotation 
    //Annotated,
    //ClassOrInterface, Class, Interface,
    //Function, Value
}
import ceylon.language.metamodel.declaration {
    AttributeDeclaration,
    VariableDeclaration,
    Declaration,
    FunctionDeclaration,
    ClassDeclaration,
    ClassOrInterfaceDeclaration,
    InterfaceDeclaration,
    Package, Module, Import
}


annotation class ConstrainedToClass() satisfies OptionalAnnotation<ConstrainedToClass, ClassDeclaration> {
}

annotation class ConstrainedToClassSeq() satisfies SequencedAnnotation<ConstrainedToClass, ClassDeclaration> {
}

annotation class ConstrainedToInterface() satisfies OptionalAnnotation<ConstrainedToInterface, InterfaceDeclaration> {
}

annotation class ConstrainedToInterfaceSeq() satisfies SequencedAnnotation<ConstrainedToInterface, InterfaceDeclaration> {
}

annotation class ConstrainedToFunction() satisfies OptionalAnnotation<ConstrainedToFunction, FunctionDeclaration> {
}

annotation class ConstrainedToFunctionSeq() satisfies SequencedAnnotation<ConstrainedToFunction, FunctionDeclaration> {
}

annotation class ConstrainedToValue() satisfies OptionalAnnotation<ConstrainedToValue, AttributeDeclaration> {
}

annotation class ConstrainedToValueSeq() satisfies SequencedAnnotation<ConstrainedToValue, AttributeDeclaration> {
}

annotation class ConstrainedToVariable() satisfies OptionalAnnotation<ConstrainedToVariable, VariableDeclaration> {
}

annotation class ConstrainedToVariableSeq() satisfies SequencedAnnotation<ConstrainedToVariable, VariableDeclaration> {
}

annotation class ConstrainedToPackage() satisfies OptionalAnnotation<ConstrainedToPackage, Package> {
}

annotation class ConstrainedToPackageSeq() satisfies SequencedAnnotation<ConstrainedToPackage, Package> {
}

annotation class ConstrainedToModule() satisfies OptionalAnnotation<ConstrainedToModule, Module> {
}

annotation class ConstrainedToModuleSeq() satisfies SequencedAnnotation<ConstrainedToModule, Module> {
}

annotation class ConstrainedToImport() satisfies OptionalAnnotation<ConstrainedToImport, Import> {
}

annotation class ConstrainedToImportSeq() satisfies SequencedAnnotation<ConstrainedToImport, Import> {
}

/* TODO 
annotation class ConstrainedToAlias() satisfies OptionalAnnotation<ConstrainedToAlias, AliasDeclaration> {
}
*/