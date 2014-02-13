"Given a function with parameter types 
 `P1`, `P2`, ..., `Pn`, return a function with a single
 parameter of tuple type `[P1, P2, ..., Pn]`.
 
 That is, if `fun` has type `W(X,Y,Z)` then `unflatten(fun)` 
 has type `W([X,Y,Z])`."
see(`function flatten`)
shared native Return unflatten<Return,Args>(Callable<Return,Args> flatFunction)(Args args)
        given Args satisfies Anything[];
