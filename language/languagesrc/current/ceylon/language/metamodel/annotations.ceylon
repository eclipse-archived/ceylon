/*
 * Do we really need to enforce that you can't 
 * ask for the annotations of a certain type for a
 * certain program element unless the annotation 
 * type can appear at that program element? Why not
 * just return no annotations?
 * 
 */

shared Values annotations<Value,Values,ProgramElement>(
              Type<ConstrainedAnnotation<Value,Values,ProgramElement>> annotationType,
              ProgramElement programElement)
           given Value satisfies ConstrainedAnnotation<Value,Values,ProgramElement>
           //given Values of (Value?) | (Value[])
           given ProgramElement satisfies Annotated { throw; }

shared Value? optionalAnnotation<Value,ProgramElement>(
            Type<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value?,ProgramElement>(annotationType, programElement); 
}

shared Value[] sequencedAnnotations<Value,ProgramElement>(
            Type<SequencedAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies SequencedAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value[],ProgramElement>(annotationType, programElement); 
}