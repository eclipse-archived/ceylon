
shared interface MemberClass<in Container, out Type=Anything, in Arguments=Nothing>
        satisfies ClassModel<Type, Arguments> & Member<Container, Class<Type, Arguments>>
        given Arguments satisfies Anything[] {
}
