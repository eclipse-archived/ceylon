
"A setter declaration represents the assign block of a [[ValueDeclaration]]."
shared sealed interface SetterDeclaration
        satisfies NestableDeclaration {

    "The variable value this setter is for."
    shared formal ValueDeclaration variable;

    actual => variable.actual;
    
    formal => variable.formal;

    default => variable.default;
    
    shared => variable.shared;

    containingPackage => variable.containingPackage;
    
    containingModule => variable.containingModule;
    
    container => variable.container;

    toplevel => variable.toplevel;
}
