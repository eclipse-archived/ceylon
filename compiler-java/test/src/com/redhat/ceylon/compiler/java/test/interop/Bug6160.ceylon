import java.lang{ObjectArray}

class Bug6160Alias<T>(Integer length, T t)
        given T satisfies Object 
        => ObjectArray<T>(length, t);
alias Bug6160Alias2<T>
        given T satisfies Object
        => ObjectArray<T>;

void bug6160<X,Y>() {
    @error:"illegal type argument in Java array type: arrays with element type Nothing are not permitted"
    ObjectArray<Nothing> v;
    @error:"illegal type argument in Java array type: arrays with element type X&Y are not permitted"
    ObjectArray<X&Y> v1;
    @error:"illegal type argument in Java array type: arrays with element type X|Y are not permitted"
    ObjectArray<X|Y> v2;
    ObjectArray<X?> v3;// OK
    
    @error:"illegal type argument in Java array type: arrays with element type Nothing are not permitted"
    Bug6160Alias<Nothing> v4;
    @error:"illegal type argument in Java array type: arrays with element type Nothing are not permitted"
    Bug6160Alias2<Nothing> v5;
}