"An annotation that may occur at most once
 at a single program element."
shared interface OptionalAnnotation<out Value, in ProgramElement=Annotated>
        of Value
        satisfies ConstrainedAnnotation<Value,Value?,ProgramElement>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {}


shared Value? optionalAnnotation<Value,ProgramElement>(
            ClassOrInterface<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value?,ProgramElement>(annotationType, programElement);
}