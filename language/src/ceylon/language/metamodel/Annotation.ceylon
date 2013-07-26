"An annotation."
shared interface Annotation<out Value> of Value
        given Value satisfies Annotation<Value> {}
