import ceylon.language.meta{
    annotations, 
    optionalAnnotation, 
    sequencedAnnotations
}
"""The supertype of all *annotation classes*. 
   
   ### Annotation classes
 
   An *annotation class* must satisfy `Annotation`,
   [[OptionalAnnotation]],  or [[SequencedAnnotation]] and must be 
   annotated `final annotation`. For example:
   
       "An annotation class."
       final annotation class Example(shared String description) 
             satisfies Annotation {}
 
   Annotation classes which satisfy `Annotation` directly may be applied 
   to any program element that supports annotations (see [[Annotated]]). 
   In practice, annotation classes often satisfy [[OptionalAnnotation]] 
   or [[SequencedAnnotation]] in order to prevent annotations being 
   applied to inappropriate program elements.
   
   Each initializer parameter of an annotation class must have one of the 
   following types:
   
   * `Integer`, `Float`, `Character`, or `String`,
   * an enumerated type whose cases are all anonymous classes, 
     such as `Boolean`,
   * a subtype of [[ceylon.language.meta.declaration::Declaration]]
   * an annotation class,
   * `{T*}` or `[T*]` where `T` is a legal annotation parameter type, or
   * any tuple type whose element types are legal annotation parameter types.
   
   An initializer parameter of an annotation class may be variadic 
   or defaulted.
   
   ### Annotation constructors
   
   An *annotation constructor* is simply a top level function, annotated with 
   `annotation` whose return type is an annotation class type. For example:
   
       "An annotation constructor."
       annotation Example example(String description="") 
           => Example(description);
   
   Each parameter of an annotation constructor must have one of the 
   following types:
   
   * `Integer`, `Float`, `Character`, or `String`,
   * an enumerated type whose cases are all anonymous classes, 
     such as `Boolean`,
   * a subtype of [[ceylon.language.meta.declaration::Declaration]],
   * an annotation type,
   * `{T*}` or `[T*]` where `T` is a legal annotation constructor parameter 
     type, or
   * any tuple type whose element types are legal annotation constructor 
     parameter types.
   
   A parameter of an annotation constructor may be variadic or defaulted.
   
   The constructor must simply instantiate and return the annotation class, 
   and there are strict rules about the arguments to the instantiation.
   
   An annotation class can have multiple annotation constructors.
   """
see(`interface OptionalAnnotation`,
    `interface SequencedAnnotation`,
    `function annotations`, 
    `function optionalAnnotation`, 
    `function sequencedAnnotations`)
shared interface Annotation 
        of ConstrainedAnnotation<Annotation,Anything,Nothing> {}
