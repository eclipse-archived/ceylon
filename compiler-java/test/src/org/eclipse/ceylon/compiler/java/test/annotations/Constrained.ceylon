import ceylon.language.meta.declaration {
    ValueDeclaration,
    Declaration,
    FunctionDeclaration,
    FunctionOrValueDeclaration,
    ClassDeclaration,
    ClassOrInterfaceDeclaration,
    InterfaceDeclaration,
    ConstructorDeclaration,
    SetterDeclaration,
    AliasDeclaration,
    Package, Module, Import,
    AnnotatedDeclaration
}


annotation final class ConstrainedToClass() satisfies OptionalAnnotation<ConstrainedToClass, ClassDeclaration> {
}

annotation final class ConstrainedToClassSeq() satisfies SequencedAnnotation<ConstrainedToClassSeq, ClassDeclaration> {
}

annotation final class ConstrainedToInterface() satisfies OptionalAnnotation<ConstrainedToInterface, InterfaceDeclaration> {
}

annotation final class ConstrainedToInterfaceSeq() satisfies SequencedAnnotation<ConstrainedToInterfaceSeq, InterfaceDeclaration> {
}

annotation final class ConstrainedToClassOrInterface() satisfies OptionalAnnotation<ConstrainedToClassOrInterface, ClassOrInterfaceDeclaration> {
}
annotation final class ConstrainedToClassOrInterface2() satisfies OptionalAnnotation<ConstrainedToClassOrInterface2, ClassDeclaration|InterfaceDeclaration> {
}

annotation final class ConstrainedToFunction() satisfies OptionalAnnotation<ConstrainedToFunction, FunctionDeclaration> {
}

annotation final class ConstrainedToFunctionSeq() satisfies SequencedAnnotation<ConstrainedToFunctionSeq, FunctionDeclaration> {
}

annotation final class ConstrainedToValue() satisfies OptionalAnnotation<ConstrainedToValue, ValueDeclaration> {
}

annotation final class ConstrainedToValueSeq() satisfies SequencedAnnotation<ConstrainedToValueSeq, ValueDeclaration> {
}

annotation final class ConstrainedToFunctionOrValue() satisfies OptionalAnnotation<ConstrainedToFunctionOrValue, FunctionOrValueDeclaration> {
}
annotation final class ConstrainedToFunctionOrValue2() satisfies OptionalAnnotation<ConstrainedToFunctionOrValue2, FunctionDeclaration|ValueDeclaration> {
}

annotation final class ConstrainedToConstructor() satisfies OptionalAnnotation<ConstrainedToConstructor, ConstructorDeclaration> {
}
annotation final class ConstrainedToConstructorOrFunction() satisfies OptionalAnnotation<ConstrainedToConstructorOrFunction, ConstructorDeclaration|FunctionDeclaration> {
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

annotation final class ConstrainedToAlias() satisfies OptionalAnnotation<ConstrainedToAlias, AliasDeclaration> {
}
annotation final class ConstrainedToSetter() satisfies OptionalAnnotation<ConstrainedToSetter, SetterDeclaration> {
}
annotation final class ConstrainedToAnnotatedDeclaration() satisfies OptionalAnnotation<ConstrainedToAnnotatedDeclaration, AnnotatedDeclaration> {
}
annotation final class ConstrainedToAnnotated() satisfies OptionalAnnotation<ConstrainedToAnnotated, Annotated> {
}
