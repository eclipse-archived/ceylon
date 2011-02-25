shared interface Member<in X, out T>
        satisfies MemberDeclaration & Callable<T,X> {}