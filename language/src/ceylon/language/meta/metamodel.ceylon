import ceylon.language.meta.model { Class, ClosedType = Type }

"Returns the closed type and model of a given instance. Since only classes
 can be instantiated, this will always be a [[Class]] model."
shared native Class<Type,Nothing> type<out Type>(Type instance)
    given Type satisfies Anything;

"Functional equivalent to type literals. Allows you to get a closed type instance
 for a given type argument.
 
 For example:
 
     assert(is Interface<List<Integer>> listOfIntegers = typeLiteral<List<Integer>>());
 "
shared native ClosedType<Type> typeLiteral<out Type>()
    given Type satisfies Anything;
