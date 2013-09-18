"A member interface model that you can inspect."
shared interface MemberInterface<in Container, out Type=Anything>
    satisfies InterfaceModel<Type> & Member<Container, Interface<Type>> {}
