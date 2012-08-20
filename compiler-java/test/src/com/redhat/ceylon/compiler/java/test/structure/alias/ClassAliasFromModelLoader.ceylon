@nomodel
void classAliasFromModelLoader(){
    value a = ClassAlias();
    value b = ClassAliasWithParameters(1, "a");
    value c = ClassAliasWithTypeParameters<Integer>(1);
    value d = ClassAliasWithTypeParameters2(1);
}