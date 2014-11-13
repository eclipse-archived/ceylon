@noanno
class ClassMemberCtorAlias() {
    class Member {
        shared new Member(Integer i) {}
        shared new Other(Integer i) {}
    }
    class AliasMember(Integer j) => Member.Member(j);
    class AliasOther(Integer j) => Member.Other(j);
    void use() {
        AliasMember(0);
        AliasOther(0);
    }
    class AliasMemberSub(Integer k) extends AliasMember(k) {}
    class AliasOtherSub(Integer k) extends AliasOther(k) {} 
}