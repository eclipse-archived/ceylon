
shared interface Attribute<in Container, out Type>
        satisfies AttributeModel<Type> & Member<Container, Value<Type>> {
}
