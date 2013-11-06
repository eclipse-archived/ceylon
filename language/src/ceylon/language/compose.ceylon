"Given a function with return type `Y`, and a second 
 function with a single parameter also of type `Y`, 
 return the composition of the two functions. The
 first function may have any number of parameters.
 
 For any such functions `f()` and `g()`,
 
     compose(g,f)(*args)==g(f(*args))
 
 for every possible argument tuple `args` of `f()`."
see(`function curry`, `function uncurry`)
shared Callable<X,Args> compose<X,Y,Args>(X(Y) x, Callable<Y,Args> y) 
        given Args satisfies Anything[]
               //=> flatten((Args args) => x(y(*args)));
               => flatten((Args args) => x(unflatten(y)(args)));
