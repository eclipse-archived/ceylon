"Given a function with two parameter lists, return
 a function with the order of the argument lists 
 reversed. The parameter lists may have any number
 of parameters.
 
 That is, if `fun` has type `W(A,B)(X,Y,Z)` then 
 `shuffle(fun)` has type `W(X,Y,Z)(A,B)`.
 
 This function is often used in conjunction with
 `curry()`."
see (`function curry`)
shared Callable<Callable<Result,FirstArgs>,SecondArgs>
shuffle<Result,FirstArgs,SecondArgs>(
            Callable<Callable<Result,SecondArgs>,FirstArgs> f)
        given FirstArgs satisfies Anything[]
        given SecondArgs satisfies Anything[]
            => flatten((SecondArgs secondArgs) 
                => flatten((FirstArgs firstArgs)
                    => unflatten(unflatten(f)(firstArgs))(secondArgs)));