import ceylon.language.meta.model {
    Attribute,
    Value
}

class Bug7370(){
    shared late String foo = "Hello";
}

@test
shared void bug7370(){
    Attribute<Bug7370,String> attribute = `Bug7370.foo`;
    Value<String> boundAttribute = attribute(Bug7370());
    assert("Hello"==boundAttribute.get());
}