doc "An annotation."
shared interface Annotation<out T> 
        given T satisfies Annotation<T> {}
