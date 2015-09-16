import ceylon.language.meta.model{
    Type,
    Function, 
    Method
}

"""Abstraction over declarations which can be invoked, namely functions, methods and constructors """
shared sealed interface FunctionDeclaration
        satisfies FunctionOrValueDeclaration & FunctionalDeclaration {
    
    shared actual formal Function<Return, Arguments> apply<Return=Anything, Arguments=Nothing>(Type<>* typeArguments)
            given Arguments satisfies Anything[];
    
    shared actual formal Method<Container, Return, Arguments> memberApply<Container=Nothing, Return=Anything, Arguments=Nothing>(Type<Object> containerType, Type<>* typeArguments)
            given Arguments satisfies Anything[];
}
