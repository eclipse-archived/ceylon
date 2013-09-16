
shared interface Attribute<in Container, out Type=Anything>
        satisfies ValueModel<Type> & Member<Container, Value<Type>> {
}
