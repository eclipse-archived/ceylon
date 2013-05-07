import ceylon.language.metamodel { Function }

@noanno
shared void bug1133<Args>()
    given Args satisfies Anything[] {
    
    Callable<Anything, Args> o = nothing;
    
    if(is Function<Anything, []> o){
        Function<Anything, []> other = o;
    }
    if(is Function<Anything, [String]> o){
        Function<Anything, [String]> other = o;
    }
    if(is Callable<Anything, []> o){
    }
    if(is Callable<Anything, [String]> o){
    }
}
