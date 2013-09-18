
"""An annotation that may occur multiple times at a single program element, 
   and only on certain program elemenets.
 
   A sequenced annotation is declared simply by having the annotation class
   satisfy SequencedAnnotation instead of [[Annotation]]. For example 
   the following would only be allowed on `class` declarations, 
   functions or methods:
 
       alias ExecutableDeclaration 
              => ClassOrInterfaceDeclaration|FunctionDeclaration
       "Documents a pattern that the annotated element is particpating in"
       final annotation class Pattern(String name) 
               satisfies SequencedAnnotation<Pattern, ExecutableDeclaration> {
       }
   
   At runtime a [[ceylon.language.meta.declaration::Declaration]] instance 
   can be queried for its `SequencedAnnotation`s of a certain type using 
   [[ceylon.language.meta::annotations]] or [[ceylon.language.meta::sequencedAnnotations]].
 """
see(`interface Annotation`)
shared interface SequencedAnnotation<out Value, in ProgramElement=Annotated>
        of Value
        satisfies ConstrainedAnnotation<Value,Value[],ProgramElement>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {}
