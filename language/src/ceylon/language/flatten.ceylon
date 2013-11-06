"Given a function with a single parameter of tuple type 
 `[P1, P2, ..., Pn]`, return a function with multiple 
 parameters of type `P1`, `P2`, ..., `Pn`.
 
 That is, if `fun` has type `W([X,Y,Z])` then 
 `flatten(fun)` has type `W(X,Y,Z)`."
see(`function unflatten`)
shared native Callable<Return,Args> flatten<Return,Args>(Return tupleFunction(Args tuple))
        given Args satisfies Anything[];

"Given a function with parameter types 
 `P1`, `P2`, ..., `Pn`, return a function with a single
 parameter of tuple type `[P1, P2, ..., Pn]`.
 
 That is, if `fun` has type `W(X,Y,Z)` then 
 `unflatten(fun)` has type `W([X,Y,Z])`."
see(`function flatten`)
shared native Return unflatten<Return,Args>(Callable<Return,Args> flatFunction)(Args args)
        given Args satisfies Anything[];
