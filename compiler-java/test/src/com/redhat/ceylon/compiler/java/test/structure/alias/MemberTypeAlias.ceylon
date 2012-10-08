class MemberTypeAlias(){
    
    @nomodel
    class TypeAliasClass(){}
    @nomodel
    interface TypeAliasInterface{}
    
    alias TypeAlias1 = TypeAliasClass & TypeAliasInterface;
    alias TypeAlias2 = TypeAliasClass | TypeAliasInterface;
    alias TypeAlias3 = TypeAliasClass;
    alias TypeAlias4 = TypeAliasInterface;
    
    TypeAlias1 classAliasMethod1(TypeAlias1 f){
        return f;
    }
    
    TypeAlias2 classAliasMethod2(TypeAlias2 f){
        return f;
    }
    
    TypeAlias3 classAliasMethod3(TypeAlias3 f){
        return f;
    }
    
    TypeAlias4 classAliasMethod4(TypeAlias4 f){
        return f;
    }
}