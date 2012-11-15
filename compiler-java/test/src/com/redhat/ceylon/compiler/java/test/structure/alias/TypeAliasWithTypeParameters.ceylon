@nomodel
shared class TypeAliasWithTypeParametersClass<T>(){}

@nomodel
shared interface TypeAliasWithTypeParametersInterface<T>{}

alias TypeAliasWithTypeParameters1 => TypeAliasWithTypeParametersClass<Integer> & TypeAliasWithTypeParametersInterface<String>;
alias TypeAliasWithTypeParameters2<T> => TypeAliasWithTypeParametersClass<T> | TypeAliasWithTypeParametersInterface<T>;
alias TypeAliasWithTypeParameters3<T,V> => TypeAliasWithTypeParametersClass<T>;
alias TypeAliasWithTypeParameters4<T,V> => TypeAliasWithTypeParametersInterface<V>;

TypeAliasWithTypeParameters1 typeAliasWithTypeParametersMethod1(TypeAliasWithTypeParameters1 f){
    return f;
}

TypeAliasWithTypeParameters2<Integer> typeAliasWithTypeParametersMethod2(TypeAliasWithTypeParameters2<Integer> f){
    return f;
}

TypeAliasWithTypeParameters3<T,V> typeAliasWithTypeParametersMethod3<T,V>(TypeAliasWithTypeParameters3<T,V> f){
    return f;
}

TypeAliasWithTypeParameters4<T,V> typeAliasWithTypeParametersMethod4<T,V>(TypeAliasWithTypeParameters4<T,V> f){
    return f;
}