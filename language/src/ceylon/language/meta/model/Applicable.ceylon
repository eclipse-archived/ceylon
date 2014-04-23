"""Represents classes or functions that you can apply in a type-unsafe way.
   """
shared interface Applicable<out Type=Anything> {
    
    "Type-unsafe application, to be used when the argument types are unknown until runtime.
     
     This has the same behaviour as invoking the applicable directly, but exchanges compile-time type
     safety with runtime checks."
    throws(`class IncompatibleTypeException`, "If any argument is not assignable to this applicable's corresponding parameter")
    throws(`class InvocationException`, "If there are not enough or too many provided arguments")
    shared formal Type apply(Anything* arguments);
}
