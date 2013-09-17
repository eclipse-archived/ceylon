import ceylon.language.meta.model { Class }
// TODO Do we really need this? Isn't annotations sufficient?
"The values of given sequenced annotation type on the given program element, 
 or empty if the program element was not annotated with that annotation type.
 
 The annotations may be returned in any order.
 "
shared Value[] sequencedAnnotations<Value, in ProgramElement>(
            Class<SequencedAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies SequencedAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value[],ProgramElement>(annotationType, programElement);
}