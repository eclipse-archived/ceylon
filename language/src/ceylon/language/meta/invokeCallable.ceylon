import ceylon.language.meta.model{IncompatibleTypeException}

"Invoke the given callable using the given positional arguments and 
 return the result, or throw `IncompatibleTypeException` if any 
 argument's runtime type is not assignable to the corresponding 
 parameter type of the callable. **This method in not typesafe**,
 the more usual way to invoke a `Callable` is to call it:
 
     function toString(Integer integer)
         => integer.string;
     Callable<String, [Integer]> callable = toString;
     
     // The usual way
     String s = callable(1);
     
     // The untypesafe way
     assert(is String s2=invokeCallable(callable, [1]));
 "
throws (`class IncompatibleTypeException`)
shared native Anything invokeCallable(Anything(*Nothing) callable, Anything[] arguments);