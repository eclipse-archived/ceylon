shared Callable<Return,Rest> curry<Return,Argument,First,Rest>
            (Callable<Return,Tuple<Argument,First,Rest>> f)
            (First first)
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Rest args) 
                        => unflatten(f)(Tuple(first, args)));

shared Callable<Return,Tuple<Argument,First,Rest>> uncurry<Return,Argument,First,Rest>
            (Callable<Return,Rest> f(First first))
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Tuple<Argument,First,Rest> args) 
                        => unflatten(f(args.first))(args.rest));
