@noanno
class SingletonCtors {
    shared actual String string;
    shared new one {
        string="one";
    }
    shared new two {
        string="two";
    }
    
    shared Integer use(SingletonCtors inst) {
        switch(inst)
        case(SingletonCtors.one) {
            return 1;
        }
        case(SingletonCtors.two) {
            return 2;
        }
        else{
            return 3;
        }
    }
    shared Integer use2(SingletonCtors inst) {
        switch(inst)
        case(one) {
            return 1;
        }
        case(two) {
            return 2;
        }
        else{
            return 3;
        }
    }
}
@noanno
class ClassMemberSingletonCtors() {
    shared class MemberClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    void use(ClassMemberSingletonCtors other) {
        assert(other.MemberClass.one != MemberClass.one);
        assert(other.MemberClass.one != this.MemberClass.one);
    }
}
/*interface InterfaceMemberSingletonCtors {
    shared class MemberClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    shared formal class MemberClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    shared default class MemberClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    //void use(MemberClass member) {
    //    assert(member == MemberClass.one);
    //}
}
void localSingletonCtors() {
    class LocalClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    print(LocalClass.one);
}*/