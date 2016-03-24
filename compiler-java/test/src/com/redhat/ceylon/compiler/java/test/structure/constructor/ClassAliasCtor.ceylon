
class ClassAliasCtor {
    shared new (Integer i) {
        
    }
    shared new other(Integer i) {
        
    }
}

class ClassAliasCtorDefault(Integer j) => ClassAliasCtor(j);

class ClassAliasCtorQualDefault(Integer j) => ClassAliasCtor(j);

class ClassAliasCtorOther(Integer j) => ClassAliasCtor.other(j);

class ClassAliasCtorAlias(Integer j) => ClassAliasCtorOther(j);