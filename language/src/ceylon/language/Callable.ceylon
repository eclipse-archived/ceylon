"A reference to a function. The type arguments encode 
 the function return type and parameter types. The 
 parameter types are typically represented as a tuple
 type. For example, the type of the function reference 
 `plus<Float>` is:
 
     Callable<Float,[Float,Float]>
 
 which we usually abbreviate `Float(Float,Float)`. Any
 instance of `Callable` may be _invoked_ by supplying a 
 positional argument list:
 
     Float(Float,Float) add = plus<Float>;
     value four = add(2.0, 2.0);
 
 or by supplying a tuple containing the arguments:
 
     Float(Float,Float) add = plus<Float>;
     [Float,Float] twoAndTwo = [2.0, 2.0];
     value four = add(*twoAndTwo);
 
 This interface may not be implemented by user code."
see (`Tuple`)
shared interface Callable<out Return, in Arguments> 
        given Arguments satisfies Anything[] {}