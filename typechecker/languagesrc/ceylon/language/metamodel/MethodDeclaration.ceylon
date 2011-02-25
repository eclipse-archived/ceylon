shared interface MethodDeclaration<out R>
        satisfies MemberDeclaration & FunctionDeclaration<R> {}