"A member interface model that you can inspect."
shared sealed interface MemberInterface<in Container, out Type>
    satisfies InterfaceModel<Type> & Member<Container, Interface<Type>> {
    
    shared actual formal Interface<Type> bind(Object container);
}
