
"An annotation that may occur at most once at a single program element and 
 only on certain program elements.
 
 An optional annotation is declared simply by having the annotation class
 satisfy OptionalAnnotation instead of [[Annotation]]. For example 
 the following would only be allowed on `class` declarations:
 
     final annotation class ExampleClass() 
             satisfies OptionalAnnotation<Example, ClassDeclaration> {}
 
 At runtime a [[ceylon.language.meta.declaration::Declaration]] instance 
 can be queried for its `OptionalAnnotation`s of a certain type using 
 [[ceylon.language.meta::annotations]] or [[ceylon.language.meta::optionalAnnotation]].
 "
see(`interface Annotation`)
shared interface OptionalAnnotation<out Value, in ProgramElement=Annotated>
        of Value
        satisfies ConstrainedAnnotation<Value,Value?,ProgramElement>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {}

