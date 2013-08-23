"Curries a function, returning a function of multiple parameter lists,
 given a function of multiple parameters."
see(`function uncurry`, `function compose`)
shared Callable<Return,Rest> curry<Return,Argument,First,Rest>
            (Callable<Return,Tuple<Argument,First,Rest>> f)
            (First first)
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Rest args) 
                        => unflatten(f)(Tuple(first, args)));

"Uncurries a function, returning a function with multiple parameters, 
 given a function with multiple parameter lists."
see(`function curry`, `function compose`)
shared Callable<Return,Tuple<Argument,First,Rest>> uncurry<Return,Argument,First,Rest>
            (Callable<Return,Rest> f(First first))
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Tuple<Argument,First,Rest> args) 
                        => unflatten(f(args.first))(args.rest));
