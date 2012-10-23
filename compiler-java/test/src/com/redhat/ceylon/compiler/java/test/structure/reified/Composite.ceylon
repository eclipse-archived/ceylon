shared interface CompositeInterface<Key, Value>{}

shared class CompositeClass<Key, Value>() 
    satisfies CompositeInterface<Key&Number, Value|Integer>{}

void reifiedCompositeInstantiate<Key,Value>(){
    value c = CompositeClass<Key&Number,Value|Integer>();
}