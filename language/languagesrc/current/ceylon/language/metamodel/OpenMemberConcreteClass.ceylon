shared interface OpenMemberConcreteClass<in X, Y, P...>
        satisfies MemberConcreteClass {

    shared formal void intercept<S>( Y onCreate(S instance, Y proceed(P... args), P... args) )()
                    given S abstracts X;

}