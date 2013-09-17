import ceylon.language.meta.model { Class }

/*
 * Do we really need to enforce that you can't 
 * ask for the annotations of a certain type for a
 * certain program element unless the annotation 
 * type can appear at that program element? Why not
 * just return no annotations?
 * 
 */

"The annotations of the given type on the given program element. For example:
 
     // Does the process declaration have the Shared annotation?
     value isShared = annotations(`Shared`, `value process`) exists;
 
 The annotations may be returned in any order.
 "
shared native Values annotations<Value,Values, in ProgramElement>(
              Class<ConstrainedAnnotation<Value,Values,ProgramElement>> annotationType,
              ProgramElement programElement)
           given Value satisfies ConstrainedAnnotation<Value,Values,ProgramElement>
           //given Values of <Value?> | <Value[]>
           given ProgramElement satisfies Annotated;
