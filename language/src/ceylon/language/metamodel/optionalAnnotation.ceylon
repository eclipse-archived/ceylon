shared Value? optionalAnnotation<Value,ProgramElement>(
            ClassOrInterface<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value?,ProgramElement>(annotationType, programElement); 
}