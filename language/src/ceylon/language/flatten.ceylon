shared native Callable<Return,Args> flatten<Return,Args>(Return tupleFunction(Args tuple))
        given Args satisfies Anything[];

shared native Return unflatten<Return,Args>(Callable<Return,Args> flatFunction)(Args args)
        given Args satisfies Anything[] =>
                flatFunction(*args);
