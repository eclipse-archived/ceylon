"Applies an arbitrary `Callable` to the specified arguments. The arguments
 are taken packaged in a tuple whose type is compatible with the `Callable`
 arguments tuple.
 
 In practice, this behaves as if the `Callable` were called with the elements
 of the tuple used as its arguments."
see(`function unflatten`)
shared Return apply<Return,Args>(Callable<Return,Args> f, Args args)
        given Args satisfies Anything[]
    => unflatten(f)(args);
