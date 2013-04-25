@nomodel
shared void bug1133<Args>()
    given Args satisfies Anything[] {
    
    Callable<Anything, Args> o = nothing;
    
    if(is AppliedFunction<Anything, []> o){
        AppliedFunction<Anything, []> other = o;
    }
    if(is AppliedFunction<Anything, [String]> o){
        AppliedFunction<Anything, [String]> other = o;
    }
    if(is Callable<Anything, []> o){
    }
    if(is Callable<Anything, [String]> o){
    }
}
