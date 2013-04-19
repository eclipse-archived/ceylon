import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}

@noanno
annotation class AnnotationClassCharacter(shared Character c) satisfies SequencedAnnotation<AnnotationClassCharacter, Type<Anything>>{}
@noanno
annotation class AnnotationClassCharacterDefaulted(shared Character c='a') satisfies OptionalAnnotation<AnnotationClassCharacterDefaulted, Type<Anything>>{}
@noanno
annotation AnnotationClassCharacter annotationClassCharacter(Character c='z') => AnnotationClassCharacter(c);
@noanno
annotationClassCharacter()
class AnnotationClassCharacter_callsite() {}