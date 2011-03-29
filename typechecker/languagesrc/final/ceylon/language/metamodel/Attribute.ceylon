shared interface Attribute<in X, out T>
        satisfies Member<X, Value<T>> & AttributeDeclaration<T> {}