import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.metamodel.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassCharacter(shared Character c) satisfies SequencedAnnotation<AnnotationClassCharacter, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassCharacterDefaulted(shared Character c='a') satisfies OptionalAnnotation<AnnotationClassCharacterDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassCharacter annotationClassCharacter(Character c='z') => AnnotationClassCharacter(c);
@nomodel
annotationClassCharacter()
class AnnotationClassCharacter_callsite() {}