shared interface CompositeInterface<Key, Value>{}

shared class CompositeClass<Key, Value>()
// we used to have Key&Number here but it is now disallowed by https://github.com/ceylon/ceylon-spec/issues/596
    satisfies CompositeInterface<Key, Value|Integer>{}

void reifiedCompositeInstantiate<Key,Value>(){
    value c = CompositeClass<Key&Number,Value|Integer>();
}