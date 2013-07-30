import ceylon.language.model{
    //type, 
    //annotations, optionalAnnotation, sequencedAnnotations,
    //Annotation2 = Annotation, 
    ConstrainedAnnotation, OptionalAnnotation, SequencedAnnotation 
    //Annotated,
    //ClassOrInterface, Class, Interface,
    //Function, Value
}
import ceylon.language.model.declaration {
    ValueDeclaration,
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

annotation class ConstrainedToClassSeq() satisfies SequencedAnnotation<ConstrainedToClassSeq, ClassDeclaration> {
}

annotation class ConstrainedToInterface() satisfies OptionalAnnotation<ConstrainedToInterface, InterfaceDeclaration> {
}

annotation class ConstrainedToInterfaceSeq() satisfies SequencedAnnotation<ConstrainedToInterfaceSeq, InterfaceDeclaration> {
}

annotation class ConstrainedToFunction() satisfies OptionalAnnotation<ConstrainedToFunction, FunctionDeclaration> {
}

annotation class ConstrainedToFunctionSeq() satisfies SequencedAnnotation<ConstrainedToFunctionSeq, FunctionDeclaration> {
}

annotation class ConstrainedToValue() satisfies OptionalAnnotation<ConstrainedToValue, ValueDeclaration> {
}

annotation class ConstrainedToValueSeq() satisfies SequencedAnnotation<ConstrainedToValueSeq, ValueDeclaration> {
}

annotation class ConstrainedToVariable() satisfies OptionalAnnotation<ConstrainedToVariable, VariableDeclaration> {
}

annotation class ConstrainedToVariableSeq() satisfies SequencedAnnotation<ConstrainedToVariableSeq, VariableDeclaration> {
}

annotation class ConstrainedToPackage() satisfies OptionalAnnotation<ConstrainedToPackage, Package> {
}

annotation class ConstrainedToPackageSeq() satisfies SequencedAnnotation<ConstrainedToPackageSeq, Package> {
}

annotation class ConstrainedToModule() satisfies OptionalAnnotation<ConstrainedToModule, Module> {
}

annotation class ConstrainedToModuleSeq() satisfies SequencedAnnotation<ConstrainedToModuleSeq, Module> {
}

annotation class ConstrainedToImport() satisfies OptionalAnnotation<ConstrainedToImport, Import> {
}

annotation class ConstrainedToImportSeq() satisfies SequencedAnnotation<ConstrainedToImportSeq, Import> {
}

/* TODO 
annotation class ConstrainedToAlias() satisfies OptionalAnnotation<ConstrainedToAlias, AliasDeclaration> {
}
*/