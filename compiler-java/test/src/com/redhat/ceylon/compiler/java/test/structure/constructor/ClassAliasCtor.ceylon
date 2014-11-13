
class ClassAliasCtor {
    shared new ClassAliasCtor(Integer i) {
        
    }
    shared new Other(Integer i) {
        
    }
}

class ClassAliasCtorDefault(Integer j) => ClassAliasCtor(j);

class ClassAliasCtorQualDefault(Integer j) => ClassAliasCtor.ClassAliasCtor(j);

class ClassAliasCtorOther(Integer j) => ClassAliasCtor.Other(j);

class ClassAliasCtorAlias(Integer j) => ClassAliasCtorOther(j);