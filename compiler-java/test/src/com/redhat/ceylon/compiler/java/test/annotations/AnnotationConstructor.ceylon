import ceylon.language.model { Annotation }

shared annotation class AnnotationConstructor() satisfies Annotation<AnnotationConstructor> {}
shared annotation AnnotationConstructor annotationConstructor() => AnnotationConstructor();