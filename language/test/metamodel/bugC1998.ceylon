import ceylon.language.meta{
    typeLiteral,type
}
import ceylon.language.meta.model{
    Attribute,
    Value,
    Method,
    Function,
    MemberClass,
    Class,
    ClassOrInterface
}

class ClassContainer1998(){
    shared object nestee {
        shared object nested {
            shared Integer attribute = 2;
            shared Integer method() {
                value c = `Klass`;
                return 2;
            }
            shared class Klass() {}
            shared class Ctor {
                shared new ctor() {}
                shared new other() {}
            }
        }
        shared void test(ClassContainer1998 other){
            Attribute<\Inested,Integer,Nothing> v = `\Inested.attribute`;
            print(v(other.nestee.nested).get());
            Method<\Inested,Integer,[]> f = `\Inested.method`;
            print(f(other.nestee.nested)());
            MemberClass<\Inested,\Inested.Klass,[]> c = `\Inested.Klass`;
            print(c(other.nestee.nested)());
            assert(is MemberClass<\Inested,\Inested.Klass,[]> xx=typeLiteral<\Inested.Klass>());
            print(xx(other.nestee.nested)());
        }
    }
    
    shared object memberObject {
        shared Integer attribute = 2;
        shared Integer method() => 2;
        shared class Klass() {}
        shared class Ctor {
            shared new ctor() {}
            shared new other() {}
        }
    }
    shared void test(ClassContainer1998 other){
        Attribute<\ImemberObject,Integer,Nothing> v = `\ImemberObject.attribute`;
        print(v(other.memberObject).get());
        assert(is MemberClass<ClassContainer1998,\ImemberObject,Nothing> mo = typeLiteral<\ImemberObject>());
        assert(exists a = mo.getAttribute<ClassContainer1998.\ImemberObject,Integer>("attribute"));
        a(other.memberObject);
        Method<\ImemberObject,Integer,[]> f = `\ImemberObject.method`;
        print(f(other.memberObject)());
        MemberClass<\ImemberObject,\ImemberObject.Klass,[]> c = `\ImemberObject.Klass`;
        print(c(other.memberObject)());
        nestee.test(other);
        assert(is MemberClass<ClassContainer1998.\ImemberObject,\ImemberObject.Klass,[]> xx=typeLiteral<\ImemberObject.Klass>());
        // TODO print(xx(other.memberObject)());
        // TODO MemberClass<ClassContainer1998,\ImemberObject.Ctor,[]> ctor = `memberObject.Ctor`;
        //print(ctor(other)());
    }
}
object toplevel1998 {
    shared class Member() {
        shared object memberObject {
            shared Integer attribute = 2;
            shared Integer method() => 2;
            shared class Klass() {}
            shared class Ctor {
                shared new ctor() {}
                shared new other() {}
            }
        }
        shared void test(Member other){
            Attribute<\ImemberObject,Integer,Nothing> v = `\ImemberObject.attribute`;
            print(v(other.memberObject).get());
            Method<\ImemberObject,Integer,[]> f = `\ImemberObject.method`;
            print(f(other.memberObject)());
            MemberClass<\ImemberObject,\ImemberObject.Klass,[]> c = `\ImemberObject.Klass`;
            print(c(other.memberObject)());
        }
    }
    shared object x { 
        shared Integer attribute = 2;
        shared Integer method() => 2;
        shared class Klass() {}
        
        shared object y {
            shared Integer attribute = 2;
            shared Integer method() => 2;
            shared class Klass() {}
            
        }
        shared void test(){
            Attribute<\Iy,Integer,Nothing> v = `\Iy.attribute`;
            print(v(y).get());
            Method<\Iy,Integer,[]> f = `\Iy.method`;
            print(f(y)());
            MemberClass<\Iy,\Iy.Klass,[]> c = `\Iy.Klass`;
            print(c(y)());
        }
    }
}

@test
shared void bugC1998() {
    value a = let (inst = ClassContainer1998()) inst.test(inst);
    value b = let (inst = toplevel1998.Member()) inst.test(inst);
    toplevel1998.x.test();
    Attribute<\Itoplevel1998,\Itoplevel1998.\Ix,Nothing> x = `\Itoplevel1998.x`;
    assert(toplevel1998.x === x(toplevel1998).get());
    Attribute<\Itoplevel1998.\Ix,\Itoplevel1998.\Ix.\Iy,Nothing> y = `\Itoplevel1998.\Ix.y`;
    assert(toplevel1998.x.y === y(toplevel1998.x).get());
}
