@noanno
shared interface TestInterface<Key, Value>{}

@noanno
shared interface TestInterface2<out Key, out Value>{}

@noanno
shared class TestClass<Key, Value>() 
    satisfies TestInterface<Key, Integer>{}

@noanno
void test<Key,Value>(Object obj){
    if(is TestClass<String,Integer> obj){
    }
    if(is TestInterface<String,Value> obj){
    }
    if(is Key obj){
    }
    // optimised away
    if(is TestInterface2<Anything,Anything> obj){
    }
    // not optimised
    if(is TestInterface<Anything,Anything> obj){
    }
    if(is TestInterface<Anything,Integer> obj){
    }
}