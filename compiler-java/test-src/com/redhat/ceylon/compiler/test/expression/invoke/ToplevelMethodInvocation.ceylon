@nomodel
void foo(){
}

@nomodel
Natural bar(Natural i, String j){
    return 1;
}

@nomodel
class ToplevelMethodInvocation(){
    Natural m() {
        foo();
        return bar(1, "");
    }
}