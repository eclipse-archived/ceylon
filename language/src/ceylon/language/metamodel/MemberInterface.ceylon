
shared interface MemberInterface<in Container, out Type>
    satisfies InterfaceType<Type> & Member<Container, Interface<Type>> {}
