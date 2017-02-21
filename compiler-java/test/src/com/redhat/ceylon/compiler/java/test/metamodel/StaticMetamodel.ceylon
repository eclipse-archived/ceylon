import ceylon.language.meta.declaration{
    ValueDeclaration,
    FunctionDeclaration,
    ClassDeclaration
}
import ceylon.language.meta.model{
    Attribute,
    Method,
    MemberClass
}

class StaticMetamodel {
    shared static variable String attribute = "";
    shared static String method(String t) {
        return t;
    }
    shared static class Member(String t) {
        shared actual Boolean equals(Object other) {
            if (is Member other) {
                return t==other.t;
            } else {
                return false;
            }
        }
        shared actual Integer hash => t.hash;
    }
    shared new () {}
}
class StaticMetamodelGeneric<T> given T satisfies Object {
    shared static T? attribute => null;
    shared static T method<U>(T t, U u) {
        return t;
    }
    shared static class Member<U>(T t) {
        shared actual Boolean equals(Object other) {
            if (is Member<U> other) {
                return t==other.t;
            } else {
                return false;
            }
        }
        shared actual Integer hash => t.hash;
    }
    shared static class Member2<U> {
        T t;
        shared new (T t, U u) {
            this.t=t;
        }
        shared actual Boolean equals(Object other) {
            if (is Member2<U> other) {
                return t==other.t;
            } else {
                return false;
            }
        }
        shared actual Integer hash => t.hash;
    }
    shared new () {
    }
}

shared void staticMetamodel() {
    void assertEq(Anything o1, Anything o2) {
        if (exists o1) {
            assert(exists o2,
            o1 == o2);
        } else {
            assert(!exists o2);
        }
    }
    
    ValueDeclaration ad = `value StaticMetamodel.attribute`;
    assert(ad.static);
    Attribute<Null,String,String> a =  `StaticMetamodel.attribute`;
    assert(a.declaration == ad);
    assertEq("", ad.staticGet(`StaticMetamodel`));
    assertEq("", ad.memberGet(StaticMetamodel()));
    assert("" == a(null).get());
    a(null).set("hello");
    assert("hello" == a.bind(null).get());
    
    ValueDeclaration adg = `value StaticMetamodelGeneric.attribute`;
    assert(adg.static);
    Attribute<Null,String?,Nothing> ag =  `StaticMetamodelGeneric<String>.attribute`;
    assert(ag.declaration == adg);
    assertEq(null, adg.staticGet(`StaticMetamodelGeneric<String>`));
    assertEq(null, adg.memberGet(StaticMetamodelGeneric<String>()));
    assertEq(null, ag(null).get());
    assertEq(null, ag.bind(null).get());
    
    FunctionDeclaration fd = `function StaticMetamodel.method`;
    assert(fd.static);
    Method<Null,String,[String]> f = `StaticMetamodel.method`;
    assert(f.declaration == fd);
    assertEq("alice", fd.staticInvoke(`StaticMetamodel`, [], "alice"));
    assertEq("bob", fd.memberInvoke(StaticMetamodel(), [], "bob"));
    assert("carol" == f(null)("carol"));
    assert("dave" == f.bind(null).apply("dave"));
    
    FunctionDeclaration fdg = `function StaticMetamodelGeneric.method`;
    assert(fdg.static);
    Method<Null,String,[String, Integer]> fg = `StaticMetamodelGeneric<String>.method<Integer>`;
    assert(fg.declaration == fdg);
    assertEq("alice", fdg.staticInvoke(`StaticMetamodelGeneric<String>`, [`Integer`], "alice", 1));//here
    assertEq("bob", fdg.memberInvoke(StaticMetamodelGeneric<String>(), [`Integer`], "bob", 1));
    assert("carol" == fg(null)("carol", 1));
    assert("dave" == fg.bind(null).apply("dave", 1));
    
    //assert(is String staticResult = `function StaticMembers.method`.staticInvoke(`StaticMembers<String>`, [`Integer`], *["", 1]),
    //    staticResult == "");
    
    ClassDeclaration cd = `class StaticMetamodel.Member`;
    assert(cd.static);
    MemberClass<Null,StaticMetamodel.Member,[String]> c = `StaticMetamodel.Member`;
    assert(c.declaration == cd);
    assertEq(StaticMetamodel.Member("alice"), cd.staticInstantiate(`StaticMetamodel`, [], "alice"));
    assertEq(StaticMetamodel.Member("bob"), cd.memberInstantiate(StaticMetamodel(), [], "bob"));
    assert(StaticMetamodel.Member("carol") == c(null)("carol"));
    assert(StaticMetamodel.Member("dave") == c.bind(null).apply("dave"));
    
    ClassDeclaration cdg = `class StaticMetamodelGeneric.Member`;
    assert(cdg.static);
    MemberClass<Null,StaticMetamodelGeneric<String>.Member<Integer>,[String]> cg = `StaticMetamodelGeneric<String>.Member<Integer>`;
    assert(cg.declaration == cdg);
    assertEq(StaticMetamodelGeneric<String>.Member<Integer>("alice"), cdg.staticInstantiate(`StaticMetamodelGeneric<String>`, [`Integer`], "alice"));//here
    assertEq(StaticMetamodelGeneric<String>.Member<Integer>("bob"), cdg.memberInstantiate(StaticMetamodelGeneric<String>(), [`Integer`], "bob"));
    assert(StaticMetamodelGeneric<String>.Member<Integer>("carol") == cg(null)("carol"));
    assert(StaticMetamodelGeneric<String>.Member<Integer>("dave") == cg.bind(null).apply("dave"));
    
    ClassDeclaration cdg2 = `class StaticMetamodelGeneric.Member2`;
    assert(cdg2.static);
    MemberClass<Null,StaticMetamodelGeneric<String>.Member2<Integer>,[String,Integer]> cg2 = `StaticMetamodelGeneric<String>.Member2<Integer>`;
    assert(cg2.declaration == cdg2);
    assertEq(StaticMetamodelGeneric<String>.Member2<Integer>("alice", 1), cdg2.staticInstantiate(`StaticMetamodelGeneric<String>`, [`Integer`], *["alice", 1]));
    assertEq(StaticMetamodelGeneric<String>.Member2<Integer>("bob", 1), cdg2.memberInstantiate(StaticMetamodelGeneric<String>(), [`Integer`], "bob", 1));
    assert(StaticMetamodelGeneric<String>.Member2<Integer>("carol", 1) == cg2(null)("carol", 1));
    assert(StaticMetamodelGeneric<String>.Member2<Integer>("dave", 1) == cg2.bind(null).apply("dave", 1));
}