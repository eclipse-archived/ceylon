"An annotation constrained to appear only on certain 
 program elements, and only with certain values. 
 
 This interface should not be satisfied directly. 
 Instead either [[OptionalAnnotation]] or [[SequencedAnnotation]] 
 should be satisfied."
see(`interface Annotation`)
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
