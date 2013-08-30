/*
 * Do we really need to enforce that you can't 
 * ask for the annotations of a certain type for a
 * certain program element unless the annotation 
 * type can appear at that program element? Why not
 * just return no annotations?
 * 
 */

shared native Values annotations<Value,Values,ProgramElement>(
              Class<ConstrainedAnnotation<Value,Values,ProgramElement>> annotationType,
              ProgramElement programElement)
           given Value satisfies ConstrainedAnnotation<Value,Values,ProgramElement>
           //given Values of <Value?> | <Value[]>
           given ProgramElement satisfies Annotated;
