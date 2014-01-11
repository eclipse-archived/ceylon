"A reference to a function. The type arguments encode the 
 [[return type|Return]] of the function along with its 
 [[parameter types|Arguments]]. The parameter types are 
 represented by a tuple type. Functions declared `void`
 are considered to have the return type `Anything`.
 
 For example, the type of the function reference 
 `plus<Float>` to the function [[plus]] is:
 
     Callable<Float, [Float,Float]>
 
 which we usually abbreviate `Float(Float,Float)`.
 
 A variadic function is represented using an unterminated 
 tuple type. For example, the type of the function reference
 `concatenate<Object>` to the function [[concatenate]] is:
 
     Callable<Object[], [{Object*}*]>
 
 which we usually abbreviate `Object({Object*}*)`.
 
 A function with defaulted parameters is represented using
 a union type. For example, the type of the method reference
 `process.writeLine` to the method [[process.writeLine]] is:
 
     Callable<Anything, [String]|[]>
 
 which we usually abbreviate `Anything(String=)`.
 
 Any instance of `Callable` may be _invoked_ by supplying a 
 positional argument list:
 
     Float(Float,Float) add = plus<Float>;
     value four = add(2.0, 2.0);
 
 or by supplying a tuple containing the arguments:
 
     Float(Float,Float) add = plus<Float>;
     [Float,Float] twoAndTwo = [2.0, 2.0];
     value four = add(*twoAndTwo);
 
 The type of the tuple must be assignable to the type 
 argument of `Arguments`.
 
 This interface may not be implemented by user code."
see (`class Tuple`)
shared interface Callable<out Return, in Arguments> 
        given Arguments satisfies Anything[] {}