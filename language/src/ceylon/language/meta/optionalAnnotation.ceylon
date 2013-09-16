import ceylon.language.meta.model { Class }

shared Value? optionalAnnotation<Value, in ProgramElement>(
            Class<OptionalAnnotation<Value,ProgramElement>> annotationType,
            ProgramElement programElement)
        given Value satisfies OptionalAnnotation<Value,ProgramElement>
        given ProgramElement satisfies Annotated { 
    return annotations<Value,Value?,ProgramElement>(annotationType, programElement);
}