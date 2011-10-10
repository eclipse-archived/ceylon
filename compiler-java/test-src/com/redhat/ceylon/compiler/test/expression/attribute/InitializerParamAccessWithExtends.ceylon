@nomodel
class InitializerParamAccessWithExtends(Boolean b) extends Object() {
    Boolean m() {
        return b;
    }
}

@nomodel
class InitializerParamAccessWithExtends2(Boolean b) extends InitializerParamAccessWithExtends(b) {
    Boolean m() {
        return b;
    }
}
