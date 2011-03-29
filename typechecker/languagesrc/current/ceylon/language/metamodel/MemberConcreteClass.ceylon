shared interface MemberConcreteClass<in X, out Y, P...>
        satisfies MemberType<X, Y> & Member<X, ConcreteClass<Y,P...>> {}