import ceylon.language.meta.model { Class }
// Do we really need this? Isn't annotations sufficient?
"The value of given optional annotation type on the given program element, 
 or null if the program element was not annotated with that annotation type"
shared Value? optionalAnnotation<Value, in ProgramElement>(
            Class<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value?,ProgramElement>(annotationType, programElement);
}