doc "An annotation. This interface encodes
     constraints upon the annotation in its
     type arguments."
shared interface ConstrainedAnnotation<out T, out S, in C>
    of OptionalAnnotation<T,C> | SequencedAnnotation<T,C>
    satisfies Annotation<T>
    given T satisfies Annotation<T>
    given C satisfies Annotated {}
