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

class ClassContainer(){
    shared object nestee {
        shared object nested {
            shared Integer attribute = 2;
            shared Integer method() {
                value c = `Klass`;
                return 2;
            }
            shared class Klass() {}
            shared class Ctor {
                shared new Ctor() {}
                shared new Other() {}
            }
        }
        shared void test(ClassContainer other){
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
            shared new Ctor() {}
            shared new Other() {}
        }
    }
    shared void test(ClassContainer other){
        Attribute<\ImemberObject,Integer,Nothing> v = `\ImemberObject.attribute`;
        print(v(other.memberObject).get());
        assert(is MemberClass<ClassContainer,\ImemberObject,Nothing> mo = typeLiteral<\ImemberObject>());
        assert(exists a = mo.getAttribute<ClassContainer.\ImemberObject,Integer>("attribute"));
        a(other.memberObject);
        Method<\ImemberObject,Integer,[]> f = `\ImemberObject.method`;
        print(f(other.memberObject)());
        MemberClass<\ImemberObject,\ImemberObject.Klass,[]> c = `\ImemberObject.Klass`;
        print(c(other.memberObject)());
        nestee.test(other);
        assert(is MemberClass<ClassContainer.\ImemberObject,\ImemberObject.Klass,[]> xx=typeLiteral<\ImemberObject.Klass>());
        // TODO print(xx(other.memberObject)());
        // TODO MemberClass<ClassContainer,\ImemberObject.Ctor,[]> ctor = `memberObject.Ctor`;
        //print(ctor(other)());
    }
}
object toplevel {
    shared class Member() {
        shared object memberObject {
            shared Integer attribute = 2;
            shared Integer method() => 2;
            shared class Klass() {}
            shared class Ctor {
                shared new Ctor() {}
                shared new Other() {}
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

void run() {
    value a = let (inst = ClassContainer()) inst.test(inst);
    value b = let (inst = toplevel.Member()) inst.test(inst);
    toplevel.x.test();
    Attribute<\Itoplevel,\Itoplevel.\Ix,Nothing> x = `\Itoplevel.x`;
    assert(toplevel.x === x(toplevel).get());
    Attribute<\Itoplevel.\Ix,\Itoplevel.\Ix.\Iy,Nothing> y = `\Itoplevel.\Ix.y`;
    assert(toplevel.x.y === y(toplevel.x).get());
}
