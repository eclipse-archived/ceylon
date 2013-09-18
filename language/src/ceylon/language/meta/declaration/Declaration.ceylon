"A declaration.
 
 There are only two types of declarations:
 
 - [[AnnotatedDeclaration]]s such as modules, packages, classes or functions, and
 - [[TypeParameter]] declarations."
shared interface Declaration of AnnotatedDeclaration
                              | TypeParameter {
    
    "The name of this declaration. For example, the [[Declaration]] class is named \"Declaration\"."
    shared formal String name;
    
    "The qualified name of this declaration. This includes the container qualified name. For
     example, the [[Declaration]] class' qualified name is \"ceylon.language.meta.declaration::Declaration\"."
    shared formal String qualifiedName;
}
