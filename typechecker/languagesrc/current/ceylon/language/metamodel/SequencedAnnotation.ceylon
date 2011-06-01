doc "An annotation that may occur
     multiple times at a single 
     program element."
shared interface SequencedAnnotation<out T, in C>
    satisfies ConstrainedAnnotation<T,T[],C>
    given T satisfies Annotation<T>
    given C satisfies Annotated {}
