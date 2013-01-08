class Canicalization() {
    
    interface X<T>{
        shared formal Iterable<T&Container<Object>> get;
    }
    
    interface Y{}
    
    X<Y> xy = nothing;
    @type:"Iterable<Canicalization.Y&Container<Object>>" value v1 = xy.get;
    X<Nothing> xb = nothing;
    @type:"Iterable<Nothing>" value v2 = xb.get;
    X<String> xs = nothing;
    @type:"Iterable<String>" value v3 = xs.get;
    X<Integer|String> xios = nothing;
    @type:"Iterable<Integer&Container<Object>|String>" value v4 = xios.get;
    X<Integer&String> xias = nothing;
    @type:"Iterable<Nothing>" value v5 = xias.get;
    X<Y&String> xyas = nothing;
    @type:"Iterable<Canicalization.Y&String>" value v6 = xyas.get;
    
}