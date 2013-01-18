shared Callable<X,Args> compose<X,Y,Args>(X(Y) x, Callable<Y,Args> y) 
        given Args satisfies Anything[]
               //=> flatten((Args args) => x(y(*args)));
               => flatten((Args args) => x(unflatten(y)(args)));
