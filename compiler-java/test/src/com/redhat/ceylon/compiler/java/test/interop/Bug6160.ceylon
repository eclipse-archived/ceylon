import java.lang{ObjectArray}

class Bug6160Alias<T>(Integer length, T t)
        given T satisfies Object 
        => ObjectArray<T>(length, t);
alias Bug6160Alias2<T>
        given T satisfies Object
        => ObjectArray<T>;

void bug6160<X,Y>() given X satisfies Object given Y satisfies Object {
    @error:"illegal Java array element type: arrays with element type 'Nothing' may not be instantiated"
    ObjectArray<Nothing> v = ObjectArray<Nothing>(1, nothing);
    @error:"illegal Java array element type: arrays with element type 'X&Y' may not be instantiated"
    ObjectArray<X&Y> v1 = ObjectArray<X&Y>(1, nothing);
    @error:"illegal Java array element type: arrays with element type 'X|Y' may not be instantiated"
    ObjectArray<X|Y> v2 = ObjectArray<X|Y>(1, nothing);
    //ObjectArray<X?> v3 = ObjectArray<X?>(1, nothing);// OK
    
    @error:"illegal Java array element type: arrays with element type 'Nothing' may not be instantiated"
    Bug6160Alias<Nothing> v4 = Bug6160Alias<Nothing>(1, nothing);
    @error:"illegal Java array element type: arrays with element type 'Nothing' may not be instantiated"
    Bug6160Alias2<Nothing> v5 = Bug6160Alias2<Nothing>(1, nothing);
}