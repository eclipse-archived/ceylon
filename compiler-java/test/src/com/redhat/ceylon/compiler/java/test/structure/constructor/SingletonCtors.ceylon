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
        new nonShared {
            string="nonShared";
        }
    }
    void use(ClassMemberSingletonCtors other) {
        assert(other.MemberClass.one != MemberClass.one);
        assert(other.MemberClass.one != this.MemberClass.one);
    }
}
@noanno
Basic localSingletonCtors() {
    class LocalClass {
        shared actual String string;
        shared new one {
            string="one";
        }
    }
    return LocalClass.one;
}
@noanno
void singletonCtors() {
    Basic o1 = localSingletonCtors();
    Basic o2 = localSingletonCtors();
    assert(!o1===o2);
}