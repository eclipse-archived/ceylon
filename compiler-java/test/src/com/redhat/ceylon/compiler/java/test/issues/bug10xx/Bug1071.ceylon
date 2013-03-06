@nomodel
class Bug1071(Float f()=>nothing) {}
@nomodel
class Bug1071_2<T>(T f()=>nothing) {}
@nomodel
void bug1071() {
    Bug1071 { function f() { return nothing; } };
    Bug1071_2<Float> { function f() { return nothing; } };
}