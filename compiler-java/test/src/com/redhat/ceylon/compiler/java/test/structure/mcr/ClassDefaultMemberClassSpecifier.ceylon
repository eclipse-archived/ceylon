@nomodel
class ClassDefaultMemberClassSpecifier(ClassDefaultMemberClassSpecifier other) {
    shared default class Member() {
    }
    ClassDefaultMemberClassSpecifier.Member s() => ClassDefaultMemberClassSpecifier.Member();
    ClassDefaultMemberClassSpecifier.Member s2() => other.Member();
}