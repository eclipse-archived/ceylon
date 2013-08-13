import ceylon.language.model{SequencedAnnotation, OptionalAnnotation, Annotation}
import ceylon.language.model.declaration { Declaration, ClassOrInterfaceDeclaration }

final annotation class EnumeratedReference(shared Comparison comparison) 
        satisfies SequencedAnnotation<EnumeratedReference, ClassOrInterfaceDeclaration>{
}

annotation EnumeratedReference enumeratedReference(Comparison comparison)
    => EnumeratedReference(comparison);

final annotation class EnumeratedReferenceVariadic(shared Comparison* comparison) 
        satisfies SequencedAnnotation<EnumeratedReferenceVariadic, ClassOrInterfaceDeclaration>{
}

annotation EnumeratedReferenceVariadic enumeratedReferenceVariadic(Comparison* comparison)
    => EnumeratedReferenceVariadic(*comparison);

final annotation class EnumeratedReferenceDefaulted(shared Comparison comparison = larger) 
        satisfies SequencedAnnotation<EnumeratedReferenceDefaulted, ClassOrInterfaceDeclaration>{
}

annotation EnumeratedReferenceDefaulted enumeratedReferenceDefaulted(Comparison comparison=smaller)
    => EnumeratedReferenceDefaulted(comparison);


