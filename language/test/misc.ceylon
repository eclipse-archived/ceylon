@test
by()
//see()
tagged()
shared void misc() {
    
    check(String("hello")=="hello", "args");
    check(String { 'h', 'i' }=="hi", "sequenced args");
    check(String { characters=['h', 'i']; }=="hi", "named sequenced args");
    check(String({})=="", "empty arg");
    check(String{}=="", "no named args");
    check(concatenate()==[], "no args");
    check(concatenate("hello", " ", "world")=="hello world".sequence(), "no args");
    check(concatenate(*{"hello", " ", "world"})=="hello world".sequence(), "spread arg");
            
    variable Integer? x = 0;
    while (exists y = x) { 
        x = null; 
    }
    check(!x exists, "while exists");
    
    variable value s = "hello";
    while (nonempty chars = s.sequence()) { 
        s=""; 
    }
    check(s=="", "while nonempty");
    
    for (n->e in Array(0..10).sequence().indexed) {
        check(n==e, "entry iteration ``n`` != ``e``");
    }
    
    //Test empty varargs
    //see(); 
    by(); tagged();
    concatenate();
    ",".join{};
    StringBuilder().appendAll{};
    //LazyList<Nothing>(); LazyMap<Nothing,Nothing>(); LazySet<Nothing>();
    [1,2,3].getAll([]);
    [1,2,3].definesAny([]);
    [1,2,3].definesEvery([]);
    [1,2,3].containsAny([]);
    [1,2,3].containsEvery([]);
    check({1,null,3}.first exists, "first [1]");
    check(!{null,2,3}.first exists, "first [2]");
    check(!{null,null,3}.first exists, "first [3]");
    {Integer*} noints={};
    check(!noints.first exists, "first [4]");
    print(null);
}
