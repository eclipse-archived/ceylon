import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassCharacter(shared Character c) satisfies SequencedAnnotation<AnnotationClassCharacter, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassCharacterDefaulted(shared Character c='a') satisfies OptionalAnnotation<AnnotationClassCharacterDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassCharacter annotationClassCharacter(Character c='z') => AnnotationClassCharacter(c);
@nomodel
annotationClassCharacter()
class AnnotationClassCharacter_callsite() {}