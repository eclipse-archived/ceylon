@nomodel
shared interface TestInterface<Key, Value>{}

@nomodel
shared class TestClass<Key, Value>() 
    satisfies TestInterface<Key, Integer>{}

@nomodel
void test<Key,Value>(Object obj){
    if(is TestClass<String,Integer> obj){
    }
    if(is TestInterface<String,Value> obj){
    }
    if(is Key obj){
    }
}