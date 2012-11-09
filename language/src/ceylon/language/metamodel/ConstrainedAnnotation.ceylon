doc "An annotation. This interface encodes
     constraints upon the annotation in its
     type arguments."
shared interface ConstrainedAnnotation<out Value, out Values, in ProgramElement>
        //of OptionalAnnotation<Value,ProgramElement> | SequencedAnnotation<Value,ProgramElement>
        satisfies Annotation<Value>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {
    shared Boolean occurs(Annotated programElement) {
        throw;
        //return programElement is ProgramElement;
    }
}
