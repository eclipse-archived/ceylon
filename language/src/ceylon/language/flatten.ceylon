"Given a `Callable` with a single tuple parameter of type `[P1, P2, ..., Pn]`
 returns an equivalent `Callable` with the parameter types `P1`, `P2`, ..., `Pn`."
see(`function unflatten`)
shared native Callable<Return,Args> flatten<Return,Args>(Return tupleFunction(Args tuple))
        given Args satisfies Anything[];

"Given an arbitrary `Callable` with parameter types 
 `P1`, `P2`, ..., `Pn` returns an equivalent `Callable` which 
 takes a single tuple argument of type `[P1, P2, ..., Pn]`."
see(`function flatten`)
shared native Return unflatten<Return,Args>(Callable<Return,Args> flatFunction)(Args args)
        given Args satisfies Anything[];
