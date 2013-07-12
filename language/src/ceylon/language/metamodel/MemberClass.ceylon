
shared interface MemberClass<in Container, out Type, in Arguments>
        satisfies ClassType<Type, Arguments> & Member<Container, Class<Type, Arguments>>
        given Arguments satisfies Anything[] {
}
