shared interface Interface<Key, Value>{}

shared class Class<Key, Value>(Integer a = 2, Integer b = 3) 
    satisfies Interface<Key, Integer>{}

shared class SubClass<T>() extends Class<T, Integer>(){}

void reifiedInstantiate(){
    value c = Class<Integer,String>();
    value c2 = Class<Integer,String>{};
    value constr = Class<Integer,String>;
    value c3 = constr(3,4);
    value c4 = Class<Integer,String>{b = 5;};
    value c5 = Class();
}