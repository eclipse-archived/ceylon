shared interface Function<out R, P...>
        satisfies Callable<R, P...> & FunctionDeclaration<R> {}