"An annotation. This interface encodes constraints upon 
 the annotation in its type arguments."
shared interface ConstrainedAnnotation<out Value, out Values, in ProgramElement> 
        of Value
        //of OptionalAnnotation<Value,ProgramElement> | SequencedAnnotation<Value,ProgramElement>
        satisfies Annotation<Value>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {
    
    "Can this annotation can occur on the given program 
     element?"
    shared Boolean occurs(Annotated programElement) =>
            programElement is ProgramElement;
    
}
