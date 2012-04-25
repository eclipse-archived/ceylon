@nomodel
shared void bug504_foo(Object? x, Object? got) {
    
}
@nomodel
shared void bug504_bar(){
    bug504_foo(null, {""}[].size);
}
