import ceylon.language.meta.declaration {
    ConstructorDeclaration
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"""A constructor model that represents the model of a constructor of a  
   Ceylon [[MemberClass]] that you can instantiate and inspect.
   """
shared sealed interface MemberConstructor<ClassContainer, out Type=Anything, in Arguments=Nothing> 
        satisfies ConstructorModel<Type, Arguments> 
            & Member<ClassContainer, Constructor<Type,Arguments>>
        given Arguments satisfies Anything[] 
        given ClassContainer satisfies ClassModel<Type> {
    
    "The container class of this constructor"
    shared formal actual ClassContainer container;
    
    shared actual formal Constructor<Type, Arguments> bind(Object container);
}