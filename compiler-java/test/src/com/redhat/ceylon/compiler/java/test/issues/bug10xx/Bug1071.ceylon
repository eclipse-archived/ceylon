@nomodel
class Bug1071(Float f()=>nothing) {}
@nomodel
void bug1071() {
    Bug1071 { function f() { return nothing; } };
}