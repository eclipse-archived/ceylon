import ceylon.language.meta.model { ClosedType = Type }

"Functional equivalent to type literals. Allows you to get a closed type instance
 for a given type argument.

 For example:

     assert(is Interface<List<Integer>> listOfIntegers = typeLiteral<List<Integer>>());
 "
shared native ClosedType<Type> typeLiteral<out Type>()
    given Type satisfies Anything;
