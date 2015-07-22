import ceylon.language.meta.declaration{
    CallableConstructorDeclaration,
    ValueConstructorDeclaration,
    ClassWithConstructorsDeclaration,
    FunctionDeclaration,
    ValueDeclaration
}
import ceylon.language.meta.model {
    Class,
    Function,
    Value,
    Method,
    Attribute,
    MemberClass,
    CallableConstructor,
    ValueConstructor,
    MemberClassCallableConstructor,
    MemberClassValueConstructor
}


shared class ConstructorLiterals<T> {
    shared T t;
    abstract new abstr() {
        
    }
    shared new (T t) {
        this.t = t;
    }
    shared new other(T t) {
        this.t = t;
    }
    shared new extendsAbstr(T t) extends abstr() {
        this.t = t;
    }
    shared new variadic(T+ t) {
        this.t = t.first;
    }
    shared new defaulted(T t, T s=t) {
        this.t = s;
    }
    shared class Member {
        shared T t;
        shared new (T t) {
            this.t = t;
        }
        shared new other(T t) {
            this.t = t;
        }
    }
}

shared class ConstructorLiteralsSub extends ConstructorLiterals<String> {
    shared new val extends ConstructorLiterals<String>("") {}
    shared class Member2 {
        shared new val2 {}
    }
}

class ClassWithoutDefaultConstructor<T> {
    shared T t;
    shared new other(T t) {
        this.t = t;
    }
}

void defaultCtorDeclAssertions(CallableConstructorDeclaration decl) {
    assert("new com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals" == decl.string);
    assert(`class ConstructorLiterals`==decl.container);
    assert(!decl.annotation);
    assert(decl.shared);
    assert(!decl.abstract);
    assert(decl.annotated<SharedAnnotation>());
    assert(!decl.toplevel);
    
    // Best check a few things about abstract constructors.
    assert(is CallableConstructorDeclaration a=`class ConstructorLiterals`.getConstructorDeclaration("abstr"));
    assert(a.abstract);
    assert(a.annotated<AbstractAnnotation>());
    assert(!a.shared);
    assert(!a.annotated<SharedAnnotation>());
    assert(!a.toplevel);
}
void memberDefaultCtorDeclAsserions(CallableConstructorDeclaration decl) {
    assert("new com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals.Member" == decl.string);
    assert(`class ConstructorLiterals.Member`==decl.container);
    assert(!decl.annotation);
    assert(decl.shared);
    assert(!decl.abstract);
    assert(decl.annotated<SharedAnnotation>());
    assert(!decl.toplevel);
    
}
void valueCtorDeclAssertions(ValueConstructorDeclaration decl) {
    assert("new com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiteralsSub.val" == decl.string);
    assert(decl.container == `class ConstructorLiteralsSub`);
    assert(decl.shared);
    assert(!decl.formal);
    assert(!decl.default);
    assert(!decl.variable);
    assert(is Identifiable got = decl.get(),
        got === ConstructorLiteralsSub.val);
    // TODO check exception type: decl.memberGet("");
}
void memberValueCtorDeclAssertions(ValueConstructorDeclaration decl) {
    assert("new com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiteralsSub.Member2.val2" == decl.string);
    assert(decl.container == `class ConstructorLiteralsSub.Member2`);
    assert(decl.shared);
    assert(!decl.formal);
    assert(!decl.default);
    assert(!decl.variable);
    // TODO assert(is Identifiable got = decl.get(),
    //    got === ConstructorLiteralsSub.val.Member2.val2);
}
void callableCtorDeclAssertions(CallableConstructorDeclaration decl) {
    assert("new com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals.other" == decl.string);
    assert(!decl.abstract);
    assert(`class ConstructorLiterals` == decl.container);
    assert(!decl.defaultConstructor);
    // TODO decl.invoke([`String`], "fdvdfv").;
    assert(!decl.annotation);
    assert(!decl.defaulted);
    assert(is ValueDeclaration pd = decl.getParameterDeclaration("t"));
    assert("value com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals.other.t" == pd.string);
    assert(pd.parameter);
    // TODO assert(exists tp = decl.getTypeParameterDeclaration("T"));
    // TODO assert(`class ConstructorLiterals` == tp.container);
    assert(!decl.variadic);
    assert(!decl.actual);
    assert(!decl.abstract);
    assert(!decl.default);
    assert(!decl.formal);
    assert(!decl.toplevel);
}
void memberCallableCtorDeclAssertions(CallableConstructorDeclaration decl) {
    assert("new com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals.Member.other" == decl.string);
}

void valueCtorModelAssertions(ValueConstructor<ConstructorLiteralsSub> model) {
    assert("com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiteralsSub.val" == model.string);
    assert(model == `new ConstructorLiteralsSub.val`.constructorApply<ConstructorLiteralsSub>());
}
void memberValueCtorModelAssertions(MemberClassValueConstructor<ConstructorLiteralsSub, ConstructorLiteralsSub.Member2> model) {
    assert("com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiteralsSub.Member2.val2" == model.string);
}
void callableCtorModelAssertions(CallableConstructor<ConstructorLiterals<String>,[String]> model) {
    assert("com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals<ceylon.language::String>.other" == model.string);
    assert(`new ConstructorLiterals.other` == model.declaration);
    assert(`ConstructorLiterals<String>` == model.type);
    assert([`String`] == model.parameterTypes);
    assert([`String`] == model.typeArgumentList);
    //invocation (as Callable)
    assert("hello" == model("hello").t);
    //apply
    assert("hello" == model.apply("hello").t);
    assert("hello" == model.namedApply{"t"->"hello"}.t);
    
    // Make assertions about other callable constructors
    assert("hello" == `ConstructorLiterals<String>.variadic`("hello", "world").t);
    //TODOassert("world" == `ConstructorLiterals<String>.defaulted`("hello", "world").t);
    
    assert(model == `new ConstructorLiterals.other`.apply<ConstructorLiterals<String>, [String]>(`String`));
}
void memberCallableCtorModelAssertions(MemberClassCallableConstructor<ConstructorLiterals<String>, ConstructorLiterals<String>.Member, [String]> model) {
    assert("com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals<ceylon.language::String>.Member.other" == model.string);
}

void defaultCtorModelAssertions(Class<ConstructorLiterals<String>,[String]> defaultCtorModel) {
    assert("com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals<ceylon.language::String>" == defaultCtorModel.string);
    assert(`class ConstructorLiterals` == defaultCtorModel.declaration);
    assert(! defaultCtorModel.container exists);
    assert([`String`] == defaultCtorModel.typeArgumentList);
    assert("hello" == defaultCtorModel("hello").t);
    assert("hello" == defaultCtorModel.apply("hello").t);
    assert("hello" == defaultCtorModel.namedApply{"t"->"hello"}.t);
    assert(`new ConstructorLiterals`.apply<ConstructorLiterals<String>,[String]>(`String`).container == `ConstructorLiterals<String>`);
}

void memberDefaultCtorModelAssertions(MemberClass<ConstructorLiterals<String>, ConstructorLiterals<String>.Member, [String]> defaultMemberCtorModel) {
    assert("com.redhat.ceylon.compiler.java.test.metamodel::ConstructorLiterals<ceylon.language::String>.Member" == defaultMemberCtorModel.string);
}

shared void constructorLiterals() {
    // Declarations
    defaultCtorDeclAssertions(`new ConstructorLiterals`);
    memberDefaultCtorDeclAsserions(`new ConstructorLiterals.Member`);
    
    valueCtorDeclAssertions(`new ConstructorLiteralsSub.val`);
    memberValueCtorDeclAssertions(`new ConstructorLiteralsSub.Member2.val2`);
    
    callableCtorDeclAssertions(`new ConstructorLiterals.other`);
    memberCallableCtorDeclAssertions(`new ConstructorLiterals.Member.other`);
    
    // Models
    defaultCtorModelAssertions(`ConstructorLiterals<String>`);
    memberDefaultCtorModelAssertions(`ConstructorLiterals<String>.Member`);
    
    valueCtorModelAssertions(`ConstructorLiteralsSub.val`);
    memberValueCtorModelAssertions(`ConstructorLiteralsSub.Member2.val2`);
    
    callableCtorModelAssertions(`ConstructorLiterals<String>.other`);
    memberCallableCtorModelAssertions(`ConstructorLiterals<String>.Member.other`);
     
    Class<ClassWithoutDefaultConstructor<String>, Nothing> xxx = `ClassWithoutDefaultConstructor<String>`;
     
}