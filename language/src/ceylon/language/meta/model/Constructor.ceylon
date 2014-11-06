import ceylon.language.meta.declaration {
    ConstructorDeclaration
}
import ceylon.language.meta.model {
    ClosedType=Type
}
"""A constructor model that represents the model of 
   a constructor of a top level Ceylon [[Class]] that 
   you can instantiate and inspect.
   """
shared sealed interface Constructor<out Type=Anything, in Arguments=Nothing> 
        satisfies ConstructorModel<Type, Arguments>
            & Callable<Type, Arguments> 
            & Applicable<Type> 
        given Arguments satisfies Anything[] {
    
    "The container class of this constructor"
    shared formal actual Class<Type> container;
}