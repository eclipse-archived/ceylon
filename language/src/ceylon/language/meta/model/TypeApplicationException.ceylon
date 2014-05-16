"Thrown when declarations are applied with invalid or incompatible type arguments.
 Also throw when trying to apply member declarations with no containers, or toplevel
 declarations with a container.
 
 For example if you try to apply `Foo` with `String`, hoping to get a `Foo<String>`
 but the type parameter for `Foo` only accepts types that satisfy `Numeric`.
 "
shared class TypeApplicationException(String message) extends Exception(message){}
