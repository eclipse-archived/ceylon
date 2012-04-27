@error abstract class SubInteger() extends Integer() {}
@error abstract class SubFloat() extends Integer() {}
@error abstract class SubChar() extends Character() {}
@error abstract class SubBoolean() extends Boolean() {}
@error abstract class SubString() extends String() {}
@error abstract class SubEntry() extends Entry<String,String>("hello", "world") {}
@error abstract class SubRange() extends Range<Integer>(1,2) {}
@error class MyCallable() satisfies Callable<Float,String,String> {}