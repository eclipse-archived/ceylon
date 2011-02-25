shared interface MemberType<in X, out Y>
        satisfies Type<Y> & Member<X, Type<Y>> {}