@noanno
class ClassMemberCtorAlias() {
    class Member {
        shared new (Integer i) {}
        shared new other(Integer i) {}
    }
    class AliasMember(Integer j) => Member(j);
    class AliasOther(Integer j) => Member.other(j);
    void use() {
        AliasMember(0);
        AliasOther(0);
    }
    class AliasMemberSub(Integer k) extends AliasMember(k) {}
    class AliasOtherSub(Integer k) extends AliasOther(k) {} 
}