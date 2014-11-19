import ceylon.language.meta.declaration {
    ConstructorDeclaration,
    NestableDeclaration
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"A constructor model represents the model of a Ceylon constructor that you can inspect.
 
 A constructor model can be either a [[Constructor]] (of a top level [[Class]])
 or a [[MemberConstructor]] (of a [[MemberClass]]).
 "
shared sealed interface ConstructorModel<out Type=Anything, in Arguments=Nothing>
        satisfies Functional & Declared
        given Arguments satisfies Anything[] {
    
    "The declaration for this model."
    shared actual formal ConstructorDeclaration declaration;
    
    "The container class of this constructor"
    shared formal ClassModel<Type> container;
    
}