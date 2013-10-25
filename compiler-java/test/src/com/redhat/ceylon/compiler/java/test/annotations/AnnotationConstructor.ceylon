import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

shared annotation final class AnnotationConstructor() satisfies OptionalAnnotation<AnnotationConstructor, ClassOrInterfaceDeclaration> {}
shared annotation AnnotationConstructor annotationConstructor() => AnnotationConstructor();