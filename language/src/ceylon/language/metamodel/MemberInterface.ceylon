
shared interface MemberInterface<in Container, out Type>
    satisfies InterfaceModel<Type> & Member<Container, Interface<Type>> {}
