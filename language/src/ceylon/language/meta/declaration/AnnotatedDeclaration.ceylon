import ceylon.language{AnnotationType = Annotation}

"Declaration which can be annotated, such as:
 
 - [[NestableDeclaration]]
 - [[Module]]
 - [[Package]]
 
 You can query annotations that are placed on a given annotated declaration with:
 
 "
shared interface AnnotatedDeclaration of NestableDeclaration
                                       | Module
                                       | Package
    satisfies Declaration & Annotated {

    """The annotation instances of the given 
       annotation type on this declaration.
       
       For example, you can list all the [[See|ceylon.language::See]] 
       annotations on [[List|ceylon.language::List]]
       with the following code:
       
           for(annot in `interface List`.annotations<See>()){
               for(elems in annot.programElements){
                   print("See: ``elems``");
               }
           }
       
       Alternatively, you can use the [[ceylon.language.meta::annotations]] 
       function.
    """
    
    shared formal Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType<Annotation>;
}
