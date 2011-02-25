shared interface MemberClass<in X, out Y>
        satisfies MemberType<X, Y> & Member<X, Class<Y>> & MemberClassDeclaration<Y> {}