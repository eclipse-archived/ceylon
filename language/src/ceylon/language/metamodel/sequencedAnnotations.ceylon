shared Value[] sequencedAnnotations<Value,ProgramElement>(
            ClassOrInterface<SequencedAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies SequencedAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value[],ProgramElement>(annotationType, programElement); 
}