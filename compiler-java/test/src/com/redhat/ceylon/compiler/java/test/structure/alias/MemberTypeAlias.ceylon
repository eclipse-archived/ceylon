shared class MemberTypeAliasClass(){
    
    shared class TypeAliasClass(){}
    shared interface TypeAliasInterface{}
    
    shared alias TypeAlias1 => TypeAliasClass & TypeAliasInterface;
    shared alias TypeAlias2 => TypeAliasClass | TypeAliasInterface;
    shared alias TypeAlias3 => TypeAliasClass;
    shared alias TypeAlias4 => TypeAliasInterface;
    
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

shared interface MemberTypeAliasInterface{
    
    shared class TypeAliasClass(){}
    shared interface TypeAliasInterface{}
    
    shared alias TypeAlias1 => TypeAliasClass & TypeAliasInterface;
    shared alias TypeAlias2 => TypeAliasClass | TypeAliasInterface;
    shared alias TypeAlias3 => TypeAliasClass;
    shared alias TypeAlias4 => TypeAliasInterface;
    
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