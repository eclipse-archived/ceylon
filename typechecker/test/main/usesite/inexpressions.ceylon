void someFunction<T>(T t) {}

@error Anything(String) sf1 = `someFunction<in String>`;
@error Anything(String) sf2 = someFunction<in String>;
