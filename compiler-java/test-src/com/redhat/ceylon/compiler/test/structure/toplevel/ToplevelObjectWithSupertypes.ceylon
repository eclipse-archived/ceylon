@nomodel
class C (Natural n){
}

@nomodel
interface I {
 shared formal Boolean id(Boolean x);
}

@nomodel
shared object x extends C(2) satisfies I {
    shared actual Boolean id(Boolean x) {
        return x;
    }
}
