shared Callable<Return,Rest> curry<Return,Argument,First,Rest>
            (Callable<Return,Tuple<Argument,First,Rest>> f)
            (First first)
        given First satisfies Argument 
        given Rest satisfies Argument[] 
                => flatten((Rest args) => unflatten(f)(Tuple(first, args)));