
shared interface VariableAttribute<in Container, Type>
        satisfies Member<Container, Variable<Type>> & Attribute<Container, Type>{
}
