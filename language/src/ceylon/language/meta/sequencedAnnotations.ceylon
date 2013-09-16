import ceylon.language.meta.model { Class }

shared Value[] sequencedAnnotations<Value, in ProgramElement>(
            Class<SequencedAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies SequencedAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value[],ProgramElement>(annotationType, programElement);
}