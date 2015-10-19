shared interface Bug2361<Other> 
        given Other satisfies Bug2361<Other> {
    shared formal void bar(Other other);
}
shared void bug2361(){
    Bug2361<Nothing> foo = nothing;
    value func = foo.bar; // error
    foo.bar{other=nothing;};
    /*
    Bug2361<Nothing[2]> bar = nothing;
    value func2 = bar.bar; // error
    
    Bug2361<String|Integer> baz = nothing;
    value func3 = baz.bar; // error
     */
}