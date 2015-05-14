"An annotation constrained to appear only on certain program 
 elements, and only with certain values. 
 
 This interface should never be satisfied directly by any
 annotation type. Instead, either [[OptionalAnnotation]] or 
 [[SequencedAnnotation]] should be satisfied by the 
 annotation type.
 
 The type parameters encode information about the annotation
 type and its constraints:
 
  - [[Value]] represents the type of the annotation itself, 
  - [[ProgramElement]] represents a constraint on the type 
    of the annotated program element _reference expression 
    type_, for example, 
    [[ceylon.language.meta.declaration::ClassDeclaration]] 
    or [[ceylon.language.meta.declaration::Module]], where
    [[Annotated]] means there is no constraint,
  - [[Type]] is a constraint on the type or return type of
    the annotated program element, where `Anything` means
    there is no constraint, and
  - [[Arguments]] is a constraint on the parameter types of
    the annotated program element (encoded as a tuple type), 
    where `Nothing` means there is no constraint."
see (`interface Annotation`,
     `interface OptionalAnnotation`,
     `interface SequencedAnnotation`)
shared interface ConstrainedAnnotation<out Value=Annotation, 
            out Values=Anything, in ProgramElement=Nothing,
            out Type=Anything, in Arguments=Nothing> 
        of Value
        //Note: adding the following constraint would
        //      make ConstrainedAnnotation a GADT, which
        //      the language does not currently support 
        //of OptionalAnnotation<Value,ProgramElement> | 
        //   SequencedAnnotation<Value,ProgramElement>
        satisfies Annotation
        given Value satisfies Annotation
        given ProgramElement satisfies Annotated
        given Arguments satisfies Anything[] {

}
