
shared interface VariableAttribute<in Container, Type>
        satisfies Attribute<Container, Type> & Member<Container, Variable<Type>> {
}
