"An annotation constrained to appear only on certain 
 program elements, and only with certain values. 
 
 This interface should not be satisfied directly. 
 Instead either [[OptionalAnnotation]] or [[SequencedAnnotation]] 
 should be satisfied."
see(`interface Annotation`)
shared interface ConstrainedAnnotation<out Value=Annotation, 
                                       out Values=Anything, 
                                       in ProgramElement=Nothing> 
        of Value
        //Note: adding the following constraint would
        //      make ConstrainedAnnotation a GADT, which
        //      the language does not currently support 
        //of OptionalAnnotation<Value,ProgramElement> | 
        //   SequencedAnnotation<Value,ProgramElement>
        satisfies Annotation
        given Value satisfies Annotation
        given ProgramElement satisfies Annotated {
    
    "Can this annotation can occur on the given program 
     element?"
    shared Boolean occurs(Annotated programElement) =>
            programElement is ProgramElement;
    
}
