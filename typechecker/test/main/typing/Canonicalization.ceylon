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
    X<Integer|String> xios_1 = nothing;
    @type:"Iterable<String,Null>" value v4_1 = xios_1.get;
    X<Object|String> xios_2 = nothing;
    @type:"Iterable<Container<Object,Null>,Null>" value v4_2 = xios_2.get;
    X<Y|String> xios_3 = nothing;
    @type:"Iterable<Canicalization.Y&Container<Object,Null>|String,Null>" value v4_3 = xios_3.get;
    X<Integer&String> xias = nothing;
    @type:"Iterable<Nothing,Null>" value v5 = xias.get;
    X<Y&String> xyas_1 = nothing;
    @type:"Iterable<Nothing,Null>" value v6_1 = xyas_1.get;
    X<Y&Category> xyas_2 = nothing;
    @type:"Iterable<Canicalization.Y&Container<Object,Null>,Null>" value v6_2 = xyas_2.get;
    X<Y&{Object*}> xyas_3 = nothing;
    @type:"Iterable<Canicalization.Y&Iterable<Object,Null>,Null>" value v6_3 = xyas_3.get;
    
}