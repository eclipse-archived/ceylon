alias AdditionLikeOperation
        => <Number> given Number satisfies Summable<Number>
        => Number(Number,Number);

AdditionLikeOperation add = plus;
AdditionLikeOperation<Integer> addInts = plus<Integer>; 

void doSomething(AdditionLikeOperation addOp) {}

void testDoSomething() {
    doSomething(plus); // error
}

//TODO: fix this:
//alias MyMapFunction => Map;
alias MyMapFunction => <U,V> => Map;

MyMapFunction<String,String> map = nothing;

alias CurriedMapTypeFunction
        =>  <Key> given Key satisfies Object
        =>  <<Item> => Map<Key, Item>>;

alias StringKeyedMap => CurriedMapTypeFunction<String>;

interface Second<Box> given Box<T> {}

interface Boxy<T> {}

//TODO: FIX THIS!
//alias BoxRef => Boxy;
alias BoxRef => <T> => Boxy<T>;

Second<BoxRef> second = nothing;
