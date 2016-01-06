Box<Item> boxit<Box, Item>
        (<T> => Box<T>(T) boxer, Item item)
        given Box<T>
        =>  boxer(item);

shared void boxtest() {
    variable Object boxed;
    boxed = boxit<Singleton, String>(Singleton, "hello");
    assert(is Singleton<String> b=boxed, "hello" == b.first);
    
    boxed = boxit<Singleton, String>(<T> (T t) => Singleton(t), "hello");
    assert(is Singleton<String> b2=boxed, "hello" == b2.first);
    
    /*boxed = boxit<Singleton, String> {
        function boxer<T> (T t) => Singleton(t); 
        item="hello";
    };*/
    
    <T> => Singleton<T>(T) boxr = <T> (T t) => Singleton(t);
    boxed = boxit<Singleton, String>(boxr, "hello");
}