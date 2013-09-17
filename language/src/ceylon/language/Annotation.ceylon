"An annotation."
see(`interface ConstrainedAnnotation`)
shared interface Annotation<out Value> of Value
        given Value satisfies Annotation<Value> {}
