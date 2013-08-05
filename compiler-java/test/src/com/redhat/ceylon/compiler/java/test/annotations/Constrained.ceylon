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


annotation final class ConstrainedToClass() satisfies OptionalAnnotation<ConstrainedToClass, ClassDeclaration> {
}

annotation final class ConstrainedToClassSeq() satisfies SequencedAnnotation<ConstrainedToClassSeq, ClassDeclaration> {
}

annotation final class ConstrainedToInterface() satisfies OptionalAnnotation<ConstrainedToInterface, InterfaceDeclaration> {
}

annotation final class ConstrainedToInterfaceSeq() satisfies SequencedAnnotation<ConstrainedToInterfaceSeq, InterfaceDeclaration> {
}

annotation final class ConstrainedToFunction() satisfies OptionalAnnotation<ConstrainedToFunction, FunctionDeclaration> {
}

annotation final class ConstrainedToFunctionSeq() satisfies SequencedAnnotation<ConstrainedToFunctionSeq, FunctionDeclaration> {
}

annotation final class ConstrainedToValue() satisfies OptionalAnnotation<ConstrainedToValue, ValueDeclaration> {
}

annotation final class ConstrainedToValueSeq() satisfies SequencedAnnotation<ConstrainedToValueSeq, ValueDeclaration> {
}

annotation final class ConstrainedToVariable() satisfies OptionalAnnotation<ConstrainedToVariable, VariableDeclaration> {
}

annotation final class ConstrainedToVariableSeq() satisfies SequencedAnnotation<ConstrainedToVariableSeq, VariableDeclaration> {
}

annotation final class ConstrainedToPackage() satisfies OptionalAnnotation<ConstrainedToPackage, Package> {
}

annotation final class ConstrainedToPackageSeq() satisfies SequencedAnnotation<ConstrainedToPackageSeq, Package> {
}

annotation final class ConstrainedToModule() satisfies OptionalAnnotation<ConstrainedToModule, Module> {
}

annotation final class ConstrainedToModuleSeq() satisfies SequencedAnnotation<ConstrainedToModuleSeq, Module> {
}

annotation final class ConstrainedToImport() satisfies OptionalAnnotation<ConstrainedToImport, Import> {
}

annotation final class ConstrainedToImportSeq() satisfies SequencedAnnotation<ConstrainedToImportSeq, Import> {
}

/* TODO 
annotation final class ConstrainedToAlias() satisfies OptionalAnnotation<ConstrainedToAlias, AliasDeclaration> {
}
*/