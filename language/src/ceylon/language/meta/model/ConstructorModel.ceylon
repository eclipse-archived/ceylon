import ceylon.language.meta.declaration {
    ConstructorDeclaration
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"A constructor model represents the model of a Ceylon constructor that you can inspect.
 
 A constructor model can be either a [[Constructor]] (of a top level [[Class]])
 or a [[MemberConstructor]] (of a [[MemberClass]]).
 "
shared sealed interface ConstructorModel<out Type=Anything, in Arguments=Nothing> 
        satisfies Model
        given Arguments satisfies Anything[] {
    
    "The container class of this constructor"
    shared formal actual ClassModel<Type> container;
    
    shared formal actual ConstructorDeclaration declaration;
    
    "This function's parameter closed types"
    shared formal ClosedType<Anything>[] parameterTypes;
}