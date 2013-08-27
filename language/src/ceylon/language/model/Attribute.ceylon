
shared interface Attribute<in Container, out Type>
        satisfies ValueModel<Type> & Member<Container, Value<Type>> {
}
