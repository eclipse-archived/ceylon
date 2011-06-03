doc "An annotation."
shared interface Annotation<out Value> 
        given Value satisfies Annotation<Value> {}
