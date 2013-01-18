shared Callable<Callable<Result,FirstArgs>,SecondArgs>
shuffle<Result,FirstArgs,SecondArgs>(
            Callable<Callable<Result,SecondArgs>,FirstArgs> f)
        given FirstArgs satisfies Anything[]
        given SecondArgs satisfies Anything[]
            => flatten((SecondArgs secondArgs) 
                => flatten((FirstArgs firstArgs)
                    => unflatten(unflatten(f)(firstArgs))(secondArgs)));