doc "An annotation that may occur
     at most once at a single 
     program element."
shared interface OptionalAnnotation<out T, in C>
    satisfies ConstrainedAnnotation<T,T?,C>
    given T satisfies Annotation<T>
    given C satisfies Annotated {}
