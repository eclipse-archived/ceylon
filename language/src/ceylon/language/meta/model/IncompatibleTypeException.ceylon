"Thrown when you invoke metamodel methods with invalid or incompatible type arguments.
 
 For example if you try to get an attribute from a class and expect an attribute of `String`
 type but it is an attribute of `Integer` type.
 "
shared class IncompatibleTypeException(String message) extends Exception(message){}
