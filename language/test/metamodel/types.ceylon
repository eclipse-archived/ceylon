import ceylon.language.meta.declaration { 
    ClassDeclaration, 
    CallableConstructorDeclaration,
    ValueConstructorDeclaration 
}
import ceylon.language.meta.model { 
    Class, 
    MemberClass, 
    Method, 
    Function,
    InvocationException,
    IncompatibleTypeException, 
    CallableConstructor,
    MemberClassCallableConstructor
}
import ceylon.language.meta{ type }

shared String toplevelString = "a";
shared Integer toplevelInteger = 1;
shared Float toplevelFloat = 1.2;
shared Character toplevelCharacter = 'a';
shared Boolean toplevelBoolean = true;
shared Object toplevelObject = 2;

shared variable String toplevelString2 = "a";
shared variable Integer toplevelInteger2 = 1;
shared variable Float toplevelFloat2 = 1.2;
shared variable Character toplevelCharacter2 = 'a';
shared variable Boolean toplevelBoolean2 = true;
shared variable Object toplevelObject2 = 2;

String privateToplevelAttribute = "a";
String privateToplevelFunction(){
    return "b";
}

shared object topLevelObjectDeclaration {
}

class PrivateClass(){
    String privateString = "a";
    String privateMethod(){
        // capture privateString
        privateString.iterator();
        return "b";
    }
    class Inner(){
        string = "c";
    }
    string = "d";
    shared class OtherInner(){}
}
class PrivateSubclass() extends PrivateClass() {}

shared class GettersAndRefs() {
    shared variable String strRef = "a";
    shared String strGetter => "a";
    assign strGetter {}
    shared String strMethod() => "a";
    
}

shared class NoParams(){
    shared variable String str2 = "a";
    shared variable Integer integer2 = 1;
    shared variable Float float2 = 1.2;
    shared variable Character character2 = 'a';
    shared variable Boolean boolean2 = true;
    shared variable Object obj2 = 2;

    shared String str = "a";
    shared Integer integer = 1;
    shared Float float = 1.2;
    shared Character character = 'a';
    shared Boolean boolean = true;
    shared NoParams obj => this;

    shared NoParams noParams() => this;

    shared NoParams fixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o){
        assert(s == "a");
        assert(i == 1);
        assert(f == 1.2);
        assert(c == 'a');
        assert(b == true);
        assert(is NoParams o);
        
        return this;
    }
    
    shared NoParams typeParams<T>(T s, Integer i)
        given T satisfies Object {
        
        assert(s == "a");
        assert(i == 1);
        
        // check that our reified T got passed correctly
        assert(is TypeParams<String> t = TypeParams<T>(s, i));
        
        return this;
    }
    
    shared String getString() => "a";
    shared Integer getInteger() => 1;
    shared Float getFloat() => 1.2;
    shared Character getCharacter() => 'a';
    shared Boolean getBoolean() => true;
    
    shared TPA & TPB intersection1 => nothing;
    shared TPB & TPA intersection2 => nothing;
    shared TPA & TPB & NoParams intersection3 => nothing;
    shared TPA | TPB union1 => nothing;
    shared TPB | TPA union2 => nothing;
    shared TPB | TPA | NoParams union3 => nothing;
    
    shared void tp1<T>(){}
}

shared class FixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o){
    assert(s == "a");
    assert(i == 1);
    assert(f == 1.2);
    assert(c == 'a');
    assert(b == true);
    assert(is NoParams o);
}

shared class DefaultedParams(Integer lastGiven = 0, String s = "a", Boolean b = true){
    if(lastGiven == 0){
        assert(s == "a");
        assert(b == true);
    }else if(lastGiven == 1){
        assert(s == "b");
        assert(b == true);
    }else if(lastGiven == 2){
        assert(s == "b");
        assert(b == false);
    }
}

shared class DefaultedParams2(Boolean set, Integer a = 1, Integer b = 2, Integer c = 3, Integer d = 4){
    if(set){
        assert(a == -1);
        assert(b == -2);
        assert(c == -3);
        assert(d == -4);
    }else{
        assert(a == 1);
        assert(b == 2);
        assert(c == 3);
        assert(d == 4);
    }
}

shared class TypeParams<T>(T s, Integer i)
    given T satisfies Object {
    
    assert(s == "a");
    assert(i == 1);
    
    shared T t1 = s;
    shared T t2 = s;
    
    shared T method<S>(T t, S s) => t;
}

shared class TypeParams2<T>() {
    shared T t1 => nothing;
}

shared class Sub1() extends TypeParams<Integer>(1, 1){}
shared class Sub2() extends TypeParams<String>("A", 1){}

shared void fixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o, NoParams oTyped){
    assert(s == "a");
    assert(i == 1);
    assert(f == 1.2);
    assert(c == 'a');
    assert(b == true);
    assert(is NoParams o);
}

shared void defaultedParams(Integer lastGiven = 0, String s = "a", Boolean b = true){
    if(lastGiven == 0){
        assert(s == "a");
        assert(b == true);
    }else if(lastGiven == 1){
        assert(s == "b");
        assert(b == true);
    }else if(lastGiven == 2){
        assert(s == "b");
        assert(b == false);
    }
}

shared void defaultedParams2(Boolean set, Integer a = 1, Integer b = 2, Integer c = 3, Integer d = 4){
    if(set){
        assert(a == -1);
        assert(b == -2);
        assert(c == -3);
        assert(d == -4);
    }else{
        assert(a == 1);
        assert(b == 2);
        assert(c == 3);
        assert(d == 4);
    }
}

shared void variadicParams(Integer count = 0, String* strings){
    assert(count == strings.size);
    for(s in strings){
        assert(s == "a");
    }
}

shared class VariadicParams(Integer count = 0, String* strings){
    assert(count == strings.size);
    for(s in strings){
        assert(s == "a");
    }
}
        
shared T typeParams<T>(T s, Integer i)
    given T satisfies Object {
    
    assert(s == "a");
    assert(i == 1);
    
    // check that our reified T got passed correctly
    assert(is TypeParams<String> t = TypeParams<T>(s, i));
    
    return s;
}

shared String getString() => "a";
shared Integer getInteger() => 1;
shared Float getFloat() => 1.2;
shared Character getCharacter() => 'a';
shared Boolean getBoolean() => true;
shared Object getObject() => 2;

shared NoParams getAndTakeNoParams(NoParams o) => o;

shared String toplevelWithMultipleParameterLists(Integer i)(String s) => s + i.string;

shared class ContainerClass(){
    shared class InnerClass(){}
    shared class DefaultedParams(Integer expected, Integer toCheck = 0){
        assert(expected == toCheck);
    }
    shared interface InnerInterface {}
    shared interface InnerInterface2 {}
    shared class InnerSubClass() extends InnerClass() satisfies InnerInterface {}
}

shared class ParameterisedContainerClass<Outer>(){
    shared class InnerClass<Inner>(){}
    shared interface InnerInterface<Inner>{}
}

shared interface ContainerInterface{
    shared class InnerClass(){}
}

shared class ContainerInterfaceImpl() satisfies ContainerInterface {}

shared alias TypeAliasToClass => NoParams;

shared alias TypeAliasToClassTP<J>
    given J satisfies Object
    => TypeParams<J>;

shared alias TypeAliasToUnion => Integer | String;

shared alias TypeAliasToMemberAndTopLevel => TPA & ContainerInterface.InnerClass;

shared interface TPA {}
shared interface TPB {}

shared class TP1() satisfies TPA & TPB {}
shared class TP2() satisfies TPA & TPB {}

shared class TypeParameterTest<P, in T = P, out V = Integer>()
    given P of TP1 | TP2 satisfies TPA & TPB {}

shared interface InterfaceWithCaseTypes of iwcta | iwctb {}
shared object iwcta satisfies InterfaceWithCaseTypes {}
shared object iwctb satisfies InterfaceWithCaseTypes {}

shared T typeParameterTest<T>() => nothing;

shared interface InterfaceWithSelfType<T> of T given T satisfies InterfaceWithSelfType<T>{}

shared abstract class Modifiers(){
    class NonShared(){}
    shared formal Boolean method();
    void privateMethod(){}
    shared actual default String string = "yup";
    annot
    String privateAttribute = "";
    shared class Private2() {}
    shared void capturingMethod(){
        value pa = privateAttribute;
        privateMethod();
    }
}
shared abstract class SubModifiers() extends Modifiers() {
    class SubPrivate(){}
}

shared final class Final(){}

shared class MyException() extends Exception("my exception"){}

shared class ThrowsMyException(Boolean t){
    if(t){
        throw MyException();
    }
    shared Integer getter {
        throw MyException();
    }
    assign getter {
        throw MyException();
    }
    shared Integer method() {
        throw MyException();
    }
}

shared class ThrowsException(Boolean t){
    if(t){
        throw Exception("exception");
    }
    shared Integer getter {
        throw Exception("exception");
    }
    assign getter {
        throw Exception("exception");
    }
    shared Integer method() {
        throw Exception("exception");
    }
}

shared class MyAssertionError() extends AssertionError("my error"){}

shared class ThrowsMyAssertionError(Boolean t){
    if(t){
        throw MyAssertionError();
    }
    shared Integer getter {
        throw MyAssertionError();
    }
    assign getter {
        throw MyAssertionError();
    }
    shared Integer method() {
        throw MyAssertionError();
    }
}

shared class ThrowsThrowable(Boolean t){
    if(t){
        throw (Exception("error") of Throwable);
    }
    shared Integer getter {
        throw (Exception("error") of Throwable);
    }
    assign getter {
        throw (Exception("error") of Throwable);
    }
    shared Integer method() {
        throw (Exception("error") of Throwable);
    }
}

shared class ConstrainedTypeParams<A, B>()
    given A of String | Integer
    given B satisfies TPA {}

shared void constrainedTypeParams<A, B>()
    given A of String | Integer
    given B satisfies TPA {}

shared annotation final class Annot() satisfies OptionalAnnotation<Annot> {}
shared annotation Annot annot() => Annot();

shared interface Top<out A>{
    shared formal A inheritedMethod();
    shared formal A inheritedAttribute;
    shared class InheritedClass(){}
    shared interface InheritedInterface{}
}
shared interface Middle<out A> satisfies Top<A>{
}
shared abstract class MiddleClass<out A>() satisfies Middle<A>{}

shared abstract class BottomClass() extends MiddleClass<Object>() satisfies Middle<String>{
    String privateAttribute = "";
    void privateMethod(){}
    shared formal String declaredMethod(String s);
    shared formal String declaredAttribute;
    shared class DeclaredClass(){}
    shared interface DeclaredInterface{}
    shared void myOwnBottomMethod(){}
}

class MemberObjectContainer<T>(){
    shared object memberObject {
        shared Integer attribute = 2;
        shared T method<T>(T t) => t;
    }
    shared void test(){
        assert (`memberObject`.declaration==`value memberObject`);
        assert (`memberObject`.declaration.container==`class MemberObjectContainer`);
        assert(`memberObject`(this).get()==memberObject);
        
        assert(`value memberObject.attribute`.name == "attribute");
        assert(is ClassDeclaration memberObjectDecl = `value memberObject.attribute`.container);
        assert(memberObjectDecl == `class memberObject`);
        assert(is ClassDeclaration fooDecl = memberObjectDecl.container);
        assert(fooDecl == `class MemberObjectContainer`);
        
        // Commented until https://github.com/ceylon/ceylon-spec/issues/1162 is cleared up
        //assert(`memberObject.attribute`.declaration == `value memberObject.attribute`);
        //assert(`memberObject.attribute`(this).get() == 2);
        //assert(is MemberClass<MemberObjectContainer<T>,Basic,Nothing> memberObjectClass = `memberObject.attribute`.container);
        //assert(is Class<MemberObjectContainer<T>,[]> fooClass = memberObjectClass.container);
        //assert(fooClass == `MemberObjectContainer<T>`);
        
        assert(`function memberObject.method`.name == "method");
        assert(is ClassDeclaration memberObjectDecl2 = `function memberObject.method`.container);
        assert(memberObjectDecl2 == `class memberObject`);
        assert(is ClassDeclaration fooDecl2 = memberObjectDecl2.container);
        assert(fooDecl2 == `class MemberObjectContainer`);
        
        // Commented until https://github.com/ceylon/ceylon-spec/issues/1162 is cleared up
        //assert(`memberObject.method<Integer>`.declaration == `function memberObject.method`);
        //assert(`memberObject.method<Integer>`(this)(3) == 3);
        //assert(is MemberClass<MemberObjectContainer<T>,Basic,Nothing> memberObjectClass2 = `memberObject.method<Integer>`.container);
        //assert(is Class<MemberObjectContainer<T>,[]> fooClass2 = memberObjectClass2.container);
        //assert(fooClass2 == `MemberObjectContainer<T>`);
        
    }
}

shared object obj {
    shared Integer attribute = 2;
    shared T method<T>(T t) => t;
}

shared class ValueConstructors {
    shared String ctor;
    shared new sharedCtor {
        ctor = "sharedCtor";
    }
    "doc"
    new nonSharedCtor {
        ctor = "otherCtor";
    }
    shared void test() {
        testModels();
        testDeclarations();
        Member.sharedCtor.testModels();
        Member.sharedCtor.testDeclarations();
    }
    shared void testModels() {
        value sc = `ValueConstructors.sharedCtor`;
        value nsc = `ValueConstructors.nonSharedCtor`;
        
        // declaration
        assert(sc.declaration == `new ValueConstructors.sharedCtor`);
        assert(nsc.declaration == `new ValueConstructors.nonSharedCtor`);
        
        // container
        assert(! sc.container exists);
        assert(! nsc.container exists);
        
        // get
        assert(sc.get() == ValueConstructors.sharedCtor);
        assert(nsc.get() == ValueConstructors.nonSharedCtor);
        
        // type
        assert(sc.type == `ValueConstructors`);
        assert(nsc.type == `ValueConstructors`);
        
        assert(`ValueConstructors` ==type(ValueConstructors.sharedCtor));
        
        // Class.constructors
        value ctors = `ValueConstructors`.getValueConstructors();
        assert(sc in ctors);
        assert(ctors.size == 1);
        
        // Class.declaredConstructors
        value declaredCtors = `ValueConstructors`.getDeclaredValueConstructors();
        assert(sc in declaredCtors);
        assert(nsc in declaredCtors);
        assert(declaredCtors.size == 2);
        
        value declaredCtors2 = `ValueConstructors`.getDeclaredValueConstructors(`DocAnnotation`);
        assert(nsc in declaredCtors2);
        assert(declaredCtors2.size == 1);
    }
    shared void testDeclarations() {
        value sc = `new ValueConstructors.sharedCtor`;
        value nsc = `new ValueConstructors.nonSharedCtor`;
        
        assert("sharedCtor" == sc.name);
        assert("nonSharedCtor" == nsc.name);
        
        print(sc.qualifiedName);
        assert("metamodel::ValueConstructors.sharedCtor" == sc.qualifiedName);
        assert("metamodel::ValueConstructors.nonSharedCtor" == nsc.qualifiedName);
        
        assert(sc.container == `class ValueConstructors`);
        assert(nsc.container == `class ValueConstructors`);
        
        print(sc.openType);
        assert(sc.openType == `class ValueConstructors`.openType);
        assert(nsc.openType == `class ValueConstructors`.openType);
        
        
        assert(`ValueConstructors.sharedCtor` == sc.apply<ValueConstructors>());
        assert(`ValueConstructors.nonSharedCtor` == nsc.apply<ValueConstructors>());
    }
    
    
    class Member {
        shared String ctor;
        shared new sharedCtor {
            this.ctor = "sharedCtor";
        }
        "doc"
        new nonSharedCtor {
            this.ctor = "nonSharedCtor";
        }
        "doc"
        new callable() {
            this.ctor = "callable";
        }
        shared void testModels() {
            value sc = `sharedCtor`;
            value nsc = `nonSharedCtor`;
            
            // declaration
            assert(sc.declaration == `new ValueConstructors.Member.sharedCtor`);
            assert(nsc.declaration == `new ValueConstructors.Member.nonSharedCtor`);
            
            // container
            assert(sc.container== `ValueConstructors`);
            assert(nsc.container == `ValueConstructors`);
            
            // get
            //assert(sc.memberGet() == Member.sharedCtor);
            //assert(nsc.memberGet() == Member.nonSharedCtor);
            
            // type
            assert(sc.type == `ValueConstructors.Member`);
            assert(nsc.type == `ValueConstructors.Member`);
            
            assert(`Member` ==type(Member.sharedCtor));
            
            // Class.constructors
            value ctors = `Member`.getValueConstructors();
            assert(sc in ctors);
            assert(ctors.size == 1);
            
            // Class.declaredConstructors
            value declaredCtors = `Member`.getDeclaredValueConstructors();
            assert(sc in declaredCtors);
            assert(nsc in declaredCtors);
            assert(declaredCtors.size == 2);
            
            value declaredCtors2 = `Member`.getDeclaredValueConstructors(`DocAnnotation`);
            assert(nsc in declaredCtors2);
            assert(declaredCtors2.size == 1);
        }
        shared void testDeclarations() {
            value sc = `new sharedCtor`;
            value nsc = `new nonSharedCtor`;
            
            assert("sharedCtor" == sc.name);
            assert("nonSharedCtor" == nsc.name);
            
            assert("metamodel::ValueConstructors.Member.sharedCtor" == sc.qualifiedName);
            assert("metamodel::ValueConstructors.Member.nonSharedCtor" == nsc.qualifiedName);
            
            assert(sc.container == `class Member`);
            assert(nsc.container == `class Member`);
            
            assert(sc.openType == `class Member`.openType);
            assert(nsc.openType == `class Member`.openType);
            
            assert(`Member.sharedCtor` == sc.memberApply<ValueConstructors, Member>(`ValueConstructors`));
            assert(`Member.nonSharedCtor` == nsc.memberApply<ValueConstructors, Member>(`ValueConstructors`));
        }
        
    }
    
}

shared class Constructors<T> {
    shared Anything arg;
    shared new (T? t=null){
        arg = t;
    }
    "doc"
    shared new otherCtor(Integer i){
        arg = i;
    }
    new nonSharedCtor(Boolean b){
        arg = b;
    }
    
    shared class Member {
        shared Anything arg;
        shared new (T? t=null) {arg = t;}
        "doc"
        shared new otherCtor(Integer i) {arg = i;}
        new nonSharedCtor(Boolean b) {arg = b;}
        shared MemberClassCallableConstructor<Constructors<T>, Member, [Boolean]> nonShared {
            assert(is MemberClassCallableConstructor<Constructors<T>, Member, [Boolean]> r = `Member`.getDeclaredConstructor<[Boolean]>("nonSharedCtor"));
            return r;
        }
        shared CallableConstructorDeclaration nonSharedDecl => `new nonSharedCtor`;
    }
    class NonSharedMember {
        shared Anything arg;
        shared new (T? t=null) {arg = t;}
        "doc"
        shared new otherCtor(Integer i) {arg = i;}
        new nonSharedCtor(Boolean b) {arg = b;}
        shared MemberClassCallableConstructor<Constructors<T>, NonSharedMember, [Boolean]> nonShared {
            assert(is MemberClassCallableConstructor<Constructors<T>, NonSharedMember, [Boolean]> r = `NonSharedMember`.getDeclaredConstructor<[Boolean]>("nonSharedCtor"));
            return r;
        }
        shared CallableConstructorDeclaration nonSharedDecl => `new nonSharedCtor`;
    }
    shared void test() {
        testDeclarations();
        testModels();
        testMemberDeclarations();
        testMemberModels();
    }
    
    shared void testMemberModels() {
        value member = Member();
        print(type(`Member`.getConstructor("")));
        assert(is MemberClassCallableConstructor<Constructors<T>,Member,[T?]|[]> memberMember = `Member`.getConstructor<[T?]|[]>(""));
        value memberOther = `Member.otherCtor`;
        value memberNonShared = member.nonShared;
        
        value nonSharedMember = NonSharedMember();
        assert(is MemberClassCallableConstructor<Constructors<T>,NonSharedMember,[T?]|[]> nonSharedMemberMember = `NonSharedMember`.getConstructor<[T?]|[]>(""));
        value nonSharedMemberOther = `NonSharedMember.otherCtor`;
        value nonSharedMemberNonShared = nonSharedMember.nonShared;
        
        // declaration
        assert(`new Member` == memberMember.declaration);
        assert(`new Member.otherCtor` == memberOther.declaration);
        assert(member.nonSharedDecl == memberNonShared.declaration);
        
        //containers
        assert(type(this) == memberMember.container);
        assert(type(this) == memberOther.container);
        assert(type(this) == memberNonShared.container);
        
        
        // call
        CallableConstructor<Member, []|[T?]> memberMemberCtor = memberMember(this);
        memberMemberCtor();
        assert(is T tt = "");
        memberMemberCtor(tt);
        CallableConstructor<Member, [Integer]> memberOtherCtor = memberOther(this);
        memberOtherCtor(1);
        CallableConstructor<Member, [Boolean]> memberNonSharedCtor = memberNonShared(this);
        memberNonSharedCtor(true);
        
        CallableConstructor<NonSharedMember, []|[T?]> nonSharedMemberMemberCtor = nonSharedMemberMember(this);
        nonSharedMemberMemberCtor();
        nonSharedMemberMemberCtor(tt);
        CallableConstructor<NonSharedMember, [Integer]> nonSharedMemberOtherCtor = nonSharedMemberOther(this);
        nonSharedMemberOtherCtor(1);
        CallableConstructor<NonSharedMember, [Boolean]> nonSharedMemberNonSharedCtor = nonSharedMemberNonShared(this);
        nonSharedMemberNonSharedCtor(true);

        // bind
        memberMember.bind(this)();
        memberMember.bind(this)(tt);
        memberOther.bind(this)(1);
        memberNonShared.bind(this)(true);
        
        nonSharedMemberMember.bind(this)();
        nonSharedMemberMember.bind(this)(tt);
        nonSharedMemberOther.bind(this)(1);
        nonSharedMemberNonShared.bind(this)(true);
        
        assert(exists dc = `Member`.defaultConstructor, dc == memberMember);
        
        // Class.constructors
        value ctors = `Member`.getCallableConstructors<Nothing>();
        assert(memberMember in ctors);
        assert(memberOther in ctors);
        assert(ctors.size == 2);
        
        // Class.declaredConstructors
        value declaredCtors = `Member`.getDeclaredCallableConstructors<Nothing>();
        assert(memberMember in declaredCtors);
        assert(memberOther in declaredCtors);
        assert(memberNonShared in declaredCtors);
        assert(declaredCtors.size == 3);
        
    }
    void testMemberDeclarations() {
        value memberDefault = `new Member`;
        value memberOther = `new Member.otherCtor`;
        value memberNonShared = Member().nonSharedDecl;
        
        value nonSharedDefault = `new NonSharedMember`;
        value nonSharedOther = `new NonSharedMember.otherCtor`;
        value nonSharedNonShared = NonSharedMember().nonSharedDecl;
        
        value appliedDefault = memberDefault.memberApply<Constructors<String>, Member, []|[String?]>(`Constructors<String>`);
        assert(exists defaultModel = `Constructors<String>.Member`.getConstructor(""),
            defaultModel == appliedDefault);
        value appliedOther = memberOther.memberApply<Constructors<String>, Member, [Integer]>(`Constructors<String>`);
        assert(exists otherModel = `Constructors<String>.Member`.getConstructor<[Integer]>("otherCtor"),
            otherModel == appliedOther);
        value appliedNonShared = memberNonShared.memberApply<Constructors<String>, Member, [Boolean]>(`Constructors<String>`);
        assert(exists nonSharedModel = `Constructors<String>.Member`.getDeclaredConstructor<[Boolean]>("nonSharedCtor"),
            nonSharedModel == appliedNonShared);
        
        value appliednonSharedDefault = nonSharedDefault.memberApply<Constructors<String>, NonSharedMember, []|[String?]>(`Constructors<String>`);
        assert(exists nonShareddefaultModel = `Constructors<String>.NonSharedMember`.getConstructor(""),
            nonShareddefaultModel == appliednonSharedDefault);
        value appliednonSharedOther = nonSharedOther.memberApply<Constructors<String>, NonSharedMember, [Integer]>(`Constructors<String>`);
        assert(exists nonSharedotherModel = `Constructors<String>.NonSharedMember`.getConstructor<[Integer]>("otherCtor"),
            nonSharedotherModel == appliednonSharedOther);
        value nonSharedappliedNonShared = nonSharedNonShared.memberApply<Constructors<String>, NonSharedMember, [Boolean]>(`Constructors<String>`);
        assert(exists nonSharednonSharedModel = `Constructors<String>.NonSharedMember`.getDeclaredConstructor<[Boolean]>("nonSharedCtor"),
            nonSharednonSharedModel == nonSharedappliedNonShared);
        
        // memberInvoke
        value container = Constructors<String>();
        assert(is Member instDefault = memberDefault.memberInvoke(container, [], "hello"));
        assert(is Object argDefault = instDefault.arg,
            argDefault == "hello");
        assert(is Member instOther = memberOther.memberInvoke(container, [], 1));
        assert(is Object argOther = instOther.arg,
            argOther== 1);
        assert(is Member instNonShared = memberNonShared.memberInvoke(container, [], true));
        assert(is Object argNonShared = instNonShared.arg,
            argNonShared == true);
        
        assert(is NonSharedMember instnonSharedDefault = nonSharedDefault.memberInvoke(container, [], "hello"));
        assert(is Object argnonSharedDefault = instnonSharedDefault.arg,
            argnonSharedDefault == "hello");
        assert(is NonSharedMember instnonSharedOther = nonSharedOther.memberInvoke(container, [], 1));
        assert(is Object argnonSharedOther = instnonSharedOther.arg,
            argnonSharedOther== 1);
        assert(is NonSharedMember instnonSharedNonShared = nonSharedNonShared.memberInvoke(container, [], true));
        assert(is Object argnonSharedNonShared = instnonSharedNonShared.arg,
            argnonSharedNonShared == true);
    }
    
    shared void testModels() {
        assert(is CallableConstructor<Constructors<T>,[T?]|[]> def = `Constructors<T>`.getConstructor<[T?]|[]>(""));
        value other = `otherCtor`;
        assert(is CallableConstructor<Constructors<T>,[Boolean]> nonShared = `Constructors<T>`.getDeclaredConstructor<[Boolean]>("nonSharedCtor"));
        
        // declaration
        assert(`new Constructors` == def.declaration);
        assert(`new otherCtor` == other.declaration);
        assert(`new nonSharedCtor` == nonShared.declaration);
        
        //container
        assert(! def.container exists);
        assert(! other.container exists);
        assert(! nonShared.container exists);
        
        // call
        "calling Constructor model of default constructor with defaulted argument" 
        assert(! def().arg exists);
        "calling Constructor model of default constructor with given argument"
        assert(is T a = "",
            exists a1 = def(a).arg,
            "" == a1);
        "calling Constructor model of non-default constructor"
        assert(exists a2 = other(1).arg, 
            1==a2);
        "calling Constructor model of non-shared, non-default constructor"
        assert(exists a3 = nonShared(true).arg, 
            a3==true);
        
        // apply
        //Anything xx = def.apply();
        "apply()ing Constructor model of default constructor with defaulted argument"
        assert(is Constructors<String> x1 = def.apply(),
            ! x1.arg exists);
        assert(! def.apply().arg exists); 
        assert(is Constructors<String> x2 = def.apply(""),
            exists y2 = x2.arg,
            "" == y2);
        try {
            def.apply("", "");
            throw;
        } catch (InvocationException e) {
        }
        
        //assert(is Constructors<String> x3 = other.apply(),
        //    ! x3.arg exists);
        assert(is Constructors<String> x4 = other.apply(1),
            exists y4 = x4.arg,
            1 == y4);
        try {
            other.apply(1, "");
            throw;
        } catch (InvocationException e) {
        }
        try {
            other.apply("");
            throw;
        } catch (IncompatibleTypeException e) {
        }
        
        //assert(is Constructors<String> x5 = nonShared.apply(),
            //! x5.arg exists);
        assert(is Constructors<String> x6 = nonShared.apply(true),
            exists y6 = x6.arg,
            true == y6);
        try {
            nonShared.apply(true, "");
            throw;
        } catch (InvocationException e) {
        }
        try {
            nonShared.apply("");
            throw;
        } catch (IncompatibleTypeException e) {
        }
        
        // namedApply()
        assert(exists defAppliedArg = def.namedApply{"t" -> "Hello"}.arg,
            "Hello" == defAppliedArg);
        assert(exists otherAppliedArg = other.namedApply{"i" -> 42}.arg,
            42 == otherAppliedArg);
        assert(exists nonSharedAppliedArg = nonShared.namedApply{"b" -> true}.arg,
            nonSharedAppliedArg == true);
        
        assert(exists dc = `Constructors<String>`.defaultConstructor, dc == def);
        
        // Class.constructors
        variable value ctors = `Constructors<String>`.getCallableConstructors<Nothing>();
        assert(def in ctors);
        assert(other in ctors);
        assert(ctors.size == 2);
        
        ctors = `Constructors<String>`.getCallableConstructors<Nothing>(`DocAnnotation`);
        assert(other in ctors);
        assert(ctors.size == 1);
        
        // Class.declaredConstructors
        variable value declaredCtors = `Constructors<String>`.getDeclaredCallableConstructors<Nothing>();
        assert(def in declaredCtors);
        assert(other in declaredCtors);
        assert(nonShared in declaredCtors);
        assert(declaredCtors.size == 3);
        
        value declaredCtors2 = `Constructors<String>`.getDeclaredCallableConstructors<[Boolean]>();
        assert(nonShared in declaredCtors2);
        assert(declaredCtors2.size == 1);
    }
    
    shared void testDeclarations() {
        value def = `new Constructors`;
        value other = `new otherCtor`;
        value nonShared = `new nonSharedCtor`;
        
        assert(def.defaultConstructor);
        assert(!other.defaultConstructor);
        assert(!nonShared.defaultConstructor);
        
        assert("" == def.name);
        assert("otherCtor" == other.name);
        assert("nonSharedCtor" == nonShared.name);
        
        assert("metamodel::Constructors" == def.qualifiedName);
        assert("metamodel::Constructors.otherCtor" == other.qualifiedName);
        assert("metamodel::Constructors.nonSharedCtor" == nonShared.qualifiedName);
        
        assert(!def.annotation);
        assert(!other.annotation);
        assert(!nonShared.annotation);
        
        assert(def.shared);
        assert(other.shared);
        assert(!nonShared.shared);
        
        assert(def.shared);
        assert(other.shared);
        assert(!nonShared.shared);
        
        assert(is ClassDeclaration cls = `package`.getClassOrInterface("Constructors"));
        
        assert(cls == def.container);
        assert(cls == other.container);
        assert(cls == nonShared.container);
        
        assert(cls.openType == def.openType);
        assert(cls.openType == other.openType);
        assert(cls.openType == nonShared.openType);
        
        //parameters
        assert(1 == def.parameterDeclarations.size);
        assert(exists def1 = def.parameterDeclarations.first);
        assert(def1.name == "t");
        assert(exists def12 = def.getParameterDeclaration("t"));
        assert(def12 == def1);
        
        assert(1 == other.parameterDeclarations.size);
        assert(exists other1 = other.parameterDeclarations.first);
        assert(other1.name == "i");
        assert(exists other12 = other.getParameterDeclaration("i"));
        assert(other12 == other1);
        
        assert(1 == nonShared.parameterDeclarations.size);
        assert(exists nonShared1 = nonShared.parameterDeclarations.first);
        assert(nonShared1.name == "b");
        assert(exists nonShared12 = nonShared.getParameterDeclaration("b"));
        assert(nonShared12 == nonShared1);
        
        assert(!def.annotations<SharedAnnotation>().empty);
        assert(!other.annotations<SharedAnnotation>().empty);
        assert(nonShared.annotations<SharedAnnotation>().empty);
        
        assert(exists c1 = cls.getConstructorDeclaration(""),
            def == c1);
        assert(exists c2 = cls.getConstructorDeclaration("otherCtor"),
            other == c2);
        assert(exists c3 = cls.getConstructorDeclaration("nonSharedCtor"),
            nonShared == c3);
        
        value ctors = cls.constructorDeclarations();
        assert(ctors.size == 3);
        assert(def in ctors);
        assert(other in ctors);
        assert(nonShared in ctors);
        
        // ConstructorDeclaration.apply();
        assert(exists defaultModel = `Constructors<String>`.getConstructor<[]|[String?]>(""),
            def.apply<Constructors<String>, []|[String?]>(`String`) == defaultModel);
        assert(other.apply<Constructors<String>, [Integer]>(`String`) == `Constructors<String>.otherCtor`);
        assert(nonShared.apply<Constructors<String>, [Boolean]>(`String`) == `Constructors<String>.nonSharedCtor`);
        
        // getTypeParameterDeclaration() and typeParameterDeclarations
        assert(exists defTp = def.getTypeParameterDeclaration("T"));
        assert([defTp] == def.typeParameterDeclarations);
        
        assert(exists otherTp = other.getTypeParameterDeclaration("T"));
        assert([otherTp] == other.typeParameterDeclarations);
        
        assert(exists nonSharedTp = nonShared.getTypeParameterDeclaration("T"));
        assert([nonSharedTp] == nonShared.typeParameterDeclarations);
        
        // invoke
        assert(is Constructors<String> defInvoked = def.invoke([`String`], ""),
            exists defInvokedArg = defInvoked.arg,
            "" == defInvokedArg);
        assert(is Constructors<String> otherInvoked = other.invoke([`String`], 42),
            exists otherInvokedArg = otherInvoked.arg,
            42 == otherInvokedArg);
        assert(is Constructors<String> nonSharedInvoked = nonShared.invoke([`String`], true),
            exists nonSharedInvokedArg = nonSharedInvoked.arg,
            true == nonSharedInvokedArg);
        
    }
}

shared interface InterfaceConstructors<T> {
    // basically the same as above, but with member classes 
    // of an interface rather than a class
    shared class Member {
        shared new (T? t=null) {
            
        }
        new nonSharedCtor(T? t=null) {
            
        }
        shared MemberClassCallableConstructor<InterfaceConstructors<T>, Member, []|[T?]> nonShared {
            assert(is MemberClassCallableConstructor<InterfaceConstructors<T>, Member, []|[T?]> r = `Member`.getDeclaredConstructor("nonSharedCtor"));
            return r;
        }
        shared CallableConstructorDeclaration nonSharedDecl => `new nonSharedCtor`;
    }
    class NonSharedMember {
        shared new (T? t=null) {
            
        }
        new nonSharedCtor(T? t=null) {
            
        }
        shared MemberClassCallableConstructor<InterfaceConstructors<T>, NonSharedMember, []|[T?]> nonShared {
            assert(is MemberClassCallableConstructor<InterfaceConstructors<T>, NonSharedMember, []|[T?]> r = `NonSharedMember`.getDeclaredConstructor("nonSharedCtor"));
            return r;
        }
        shared CallableConstructorDeclaration nonSharedDecl => `new nonSharedCtor`;
    }
    shared void test() {
        assert(is T tt = "");
        value memberInst = Member();
        value nonSharedMemberInst = NonSharedMember();
        
        assert(is MemberClassCallableConstructor<InterfaceConstructors<T>,Member,[T?]|[]> mmm = `Member`.getConstructor(""));
        assert(is MemberClassCallableConstructor<InterfaceConstructors<T>,NonSharedMember,[T?]|[]> nsm = `NonSharedMember`.getConstructor(""));
        
        assert(`new Member` == mmm.declaration);
        assert(memberInst.nonSharedDecl == memberInst.nonShared.declaration);
        assert(`new NonSharedMember` == nsm.declaration);
        assert(nonSharedMemberInst.nonSharedDecl == nonSharedMemberInst.nonShared.declaration);
    
        mmm(this)();
        mmm(this)(tt);
        memberInst.nonShared(this)();
        memberInst.nonShared(this)(tt);
        nsm(this)();
        nsm(this)(tt);
        nonSharedMemberInst.nonShared(this)();
        nonSharedMemberInst.nonShared(this)(tt);
    }
}
class ClassWithInitializer(String s) {
    
}
class ClassWithDefaultConstructor {
    shared new (String s) {
        
    }
}
class ClassWithNonDefaultConstructor {
    shared new nnew(String s) {
    }
}
class UninstantiableClass {
    shared new nnew(String s) {
    }
}
class ClassConstructorsOfEveryArity {
    shared new fixed0() {}
    shared new fixed1(String s1) {}
    shared new fixed2(String s1, String s2) {}
    shared new fixed3(String s1, String s2, String s3) {}
    shared new fixed4(String s1, String s2, String s3, String s4) {}
    shared new fixed5(String s1, String s2, String s3, String s4, String s5) {}
    
    shared new star1(String* s1) {}
    shared new star2(String s1, String* s2) {}
    shared new star3(String s1, String s2, String* s3) {}
    shared new star4(String s1, String s2, String s3, String* s4) {}
    shared new star5(String s1, String s2, String s3, String s4, String* s5) {}
    
    shared new plus1(String+ s1) {}
    shared new plus2(String s1, String+ s2) {}
    shared new plus3(String s1, String s2, String+ s3) {}
    shared new plus4(String s1, String s2, String s3, String+ s4) {}
    shared new plus5(String s1, String s2, String s3, String s4, String+ s5) {}
}

class ClosedEnumValueConstructors
    of alpha | beta {
    shared new alpha {}
    new beta {}
    shared ClosedEnumValueConstructors leakBeta() => beta;
}

class OpenEnumValueConstructors {
    shared new alpha {}
    new beta {}
}
