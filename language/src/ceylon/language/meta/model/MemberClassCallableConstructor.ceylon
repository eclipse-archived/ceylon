import ceylon.language.meta.declaration{CallableConstructorDeclaration}


"A model for a callable constructor of a member class."
see(`interface MemberClassValueConstructor`)
shared sealed interface MemberClassCallableConstructor<in Container=Nothing, out Type=Object, in Arguments=Nothing>
        satisfies FunctionModel<Type, Arguments> & Qualified<CallableConstructor<Type, Arguments>, Container>
        given Arguments satisfies Anything[] {
    
    
    "This constructor's declaration."
    shared formal actual CallableConstructorDeclaration declaration;
    
    shared formal actual MemberClass<Container, Type> type;
    
    "The class containing this constructor; the type of instances produced 
     by this constructor."
    shared actual formal ClassModel<Type> container;
    
    shared actual formal CallableConstructor<Type, Arguments> bind(Object container);
}
