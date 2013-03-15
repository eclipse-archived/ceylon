import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}

@nomodel
annotation class AnnotationClassCharacter(Character c) satisfies SequencedAnnotation<AnnotationClassCharacter, Type<Anything>>{}
@nomodel
annotation class AnnotationClassCharacterDefaulted(Character c='a') satisfies OptionalAnnotation<AnnotationClassCharacterDefaulted, Type<Anything>>{}
//@nomodel
//annotation AnnotationClassCharacter annotationClassCharacter(Character c) => AnnotationClassCharacter(c);