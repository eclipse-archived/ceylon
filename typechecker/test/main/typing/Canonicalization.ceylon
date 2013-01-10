class Canicalization() {
    
    interface X<T>{
        shared formal Iterable<T&Container<Object>> get;
    }
    
    interface Y{}
    
    X<Y> xy = nothing;
    @type:"Iterable<Canicalization.Y&Container<Object,Null>,Null>" value v1 = xy.get;
    X<Nothing> xb = nothing;
    @type:"Iterable<Nothing,Null>" value v2 = xb.get;
    X<String> xs = nothing;
    @type:"Iterable<String,Null>" value v3 = xs.get;
    X<Integer|String> xios = nothing;
    @type:"Iterable<Integer&Container<Object,Null>|String,Null>" value v4 = xios.get;
    X<Integer&String> xias = nothing;
    @type:"Iterable<Nothing,Null>" value v5 = xias.get;
    X<Y&String> xyas = nothing;
    @type:"Iterable<Canicalization.Y&String,Null>" value v6 = xyas.get;
    
}