
shared interface Attribute<in Container, out Type>
        satisfies AttributeType<Type> & Member<Container, Value<Type>> {
}
