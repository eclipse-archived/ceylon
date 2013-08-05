import ceylon.language.model { Annotation }

shared annotation final class AnnotationConstructor() satisfies Annotation<AnnotationConstructor> {}
shared annotation AnnotationConstructor annotationConstructor() => AnnotationConstructor();