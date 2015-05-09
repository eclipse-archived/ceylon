@error abstract class SubInteger() extends Integer() {}
@error abstract class SubFloat() extends Integer() {}
@error abstract class SubChar() extends Character() {}
@error abstract class SubBoolean() extends Boolean() {}
@error abstract class SubString() extends String() {}
@error abstract class SubEntry() extends Entry<String,String>("hello", "world") {}
@error abstract class SubRange() extends Range<Integer>(1,2) {}
@error class MyCallable() satisfies Callable<Float,String,String> {}

@error class SubclassWithoutSuperTypeArgs() extends SequenceBuilder() {}
@error class SubclassWithSuperTypeArgs() extends Exception<String>() {}
@error class SubclassWithExtraSuperTypeArgs() extends SequenceBuilder<String,Integer>() {}

@error interface SubWithoutSuperTypeArgs satisfies Comparable {}
@error interface SubWithSuperTypeArgs satisfies Sized<String> {}
@error interface SubWithSuperTypeArgs satisfies Iterable<String,String> {}
@error interface SubWithExtraSuperTypeArgs satisfies Comparable<SubWithExtraSuperTypeArgs,Integer> {}
