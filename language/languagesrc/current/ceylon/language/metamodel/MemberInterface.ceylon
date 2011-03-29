shared interface MemberInterface<in X, out Y>
        satisfies MemberType<X, Y> & Member<X, Interface<Y>> {}