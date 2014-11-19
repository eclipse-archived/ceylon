shared interface Contained {
    "This declaration's package container."
    shared formal Package containingPackage;
    
    "This declaration's module container."
    shared formal Module containingModule;
    
    "This declaration's immediate container, which can be either a [[NestableDeclaration]]
     or a [[Package]]."
    shared formal NestableDeclaration|Package container;
    
    "True if this declaration is a toplevel declaration."
    shared formal Boolean toplevel;
}