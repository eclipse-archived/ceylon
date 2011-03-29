shared interface Method<in X, out R, P...>
        satisfies Member<X, Function<R, P...>> & MethodDeclaration<R> {}