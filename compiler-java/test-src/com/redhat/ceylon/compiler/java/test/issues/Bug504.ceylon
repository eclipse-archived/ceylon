@nomodel
shared void bug504_foo2(Object? a1, Object? a2) {
}
@nomodel
shared void bug504_foo3(Object? a1, Object? a2, Object? a3) {
}
@nomodel
shared void bug504_bar(){
    bug504_foo2(null, {""}[].size);
    bug504_foo3(null, null, {""}[].size);
    bug504_foo3(null, {""}[].size, {""}[].size);
}
