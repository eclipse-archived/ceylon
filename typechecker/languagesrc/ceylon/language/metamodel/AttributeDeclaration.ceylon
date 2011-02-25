shared interface AttributeDeclaration<out T>
        satisfies MemberDeclaration & ValueDeclaration<T> {}