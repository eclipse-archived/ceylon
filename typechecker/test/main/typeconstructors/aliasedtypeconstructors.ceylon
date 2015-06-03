alias AdditionLikeOperation
        => <Number> given Number satisfies Summable<Number>
        => Number(Number,Number);

AdditionLikeOperation add = plus;
AdditionLikeOperation<Integer> addInts = plus<Integer>;
AdditionLikeOperation<Float> addFloats = plus<Float>;  

<Number> given Number satisfies Summable<Number>
        => Number(Number,Number) addRef = add;

void doSomething(AdditionLikeOperation addOp) {}

void testDoSomething() {
    doSomething(plus);
    Float z = add(1.0,2.0);
    Integer k = add(1,2);
    addFloats(2.0, 3.0);
    addInts(2, 3);
}

//TODO: fix this:
//alias MyMapFunction => Map;
alias MyMapFunction => <U,V> => Map;
MyMapFunction<String,String> map = nothing;

alias CurriedMapTypeFunction
        =>  <Key> given Key satisfies Object
        =>  <<Item> => Map<Key, Item>>;
Map<String,Integer> strIntMap = nothing;
alias StringKeyedMap => CurriedMapTypeFunction<String>;
StringKeyedMap<Integer> strIntMapRef = strIntMap; 

interface Second<Box> given Box<T> {}
interface Boxy<T> {}
//TODO: FIX THIS!
//alias BoxRef => Boxy;
alias BoxRef => <T> => Boxy<T>;
Second<BoxRef> second = nothing;
