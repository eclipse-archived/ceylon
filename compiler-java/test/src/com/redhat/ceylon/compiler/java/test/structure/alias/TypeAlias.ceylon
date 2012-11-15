shared class TypeAliasClass(){}

shared interface TypeAliasInterface{}

alias TypeAlias1 => TypeAliasClass & TypeAliasInterface;
alias TypeAlias2 => TypeAliasClass | TypeAliasInterface;
alias TypeAlias3 => TypeAliasClass;
alias TypeAlias4 => TypeAliasInterface;

TypeAlias1 typeAliasMethod1(TypeAlias1 f){
    return f;
}

TypeAlias2 typeAliasMethod2(TypeAlias2 f){
    return f;
}

TypeAlias3 typeAliasMethod3(TypeAlias3 f){
    return f;
}

TypeAlias4 typeAliasMethod4(TypeAlias4 f){
    return f;
}