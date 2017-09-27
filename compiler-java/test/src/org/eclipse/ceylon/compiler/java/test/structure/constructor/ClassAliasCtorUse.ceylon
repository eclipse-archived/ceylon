
void useClassAliasCtor() {
    ClassAliasCtor x = ClassAliasCtorDefault(0);
    ClassAliasCtor x1 = ClassAliasCtorDefault{j=0;};
    ClassAliasCtor y = ClassAliasCtorQualDefault(0);
    ClassAliasCtor y1 = ClassAliasCtorQualDefault{j=0;};
    ClassAliasCtor z = ClassAliasCtorOther(0);
    ClassAliasCtor z1 = ClassAliasCtorOther{j=0;};
    ClassAliasCtor zz = ClassAliasCtorAlias(0);
    ClassAliasCtor zz1 = ClassAliasCtorAlias{j=0;};
}
class ClassAliasCtorDefaultSub(Integer j) extends ClassAliasCtorDefault(j) {}
class ClassAliasCtorQualDefaultSub(Integer j) extends ClassAliasCtorQualDefault(j) {}
class ClassAliasCtorOtherSub(Integer j) extends ClassAliasCtorOther(j) {}
class ClassAliasCtorAliasSub(Integer j) extends ClassAliasCtorAlias(j){}
class ClassAliasCtorAliasSub2 extends ClassAliasCtorAlias{
    shared new (Integer j) extends ClassAliasCtorAlias(j) {}
} 
