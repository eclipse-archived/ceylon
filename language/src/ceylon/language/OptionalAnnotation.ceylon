
"An annotation that may occur at most once
 at a single program element."
shared interface OptionalAnnotation<out Value, in ProgramElement=Annotated>
        of Value
        satisfies ConstrainedAnnotation<Value,Value?,ProgramElement>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {}

