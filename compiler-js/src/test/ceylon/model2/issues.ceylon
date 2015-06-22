import ceylon.language.meta.declaration {
  FunctionDeclaration, ClassOrInterfaceDeclaration, Package, ValueDeclaration
}
import ceylon.language.meta.model {
  MemberClass
}
import ceylon.language.meta { type }

import check { check, fail }

shared class Fuera() {
  shared class Dentro() {
    shared void f() {}
    shared class Cebolla() {
      shared void g() {}
    }
  }
  shared interface Vacia {}
}

shared annotation DumbAnnotation dumb() => DumbAnnotation();

shared final annotation class DumbAnnotation()
        satisfies OptionalAnnotation<DumbAnnotation,FunctionDeclaration> {}

shared class Padre() {
  shared void a() {}
  shared dumb void b() {}
}
shared class Hijo() extends Padre() {
  shared void c() {}
  shared dumb void d() {}
}
class Issue669<Z,A>() {
  shared class Middle<T>() {
    shared class Inner(){}
  }
}

void issues() {
    try {
        value g1 = `function Fuera.Dentro.Cebolla.g`;
        value g2 = `Fuera.Dentro.Cebolla.g`;
        check(g1.name == "g", "Member method of member type declaration");
        check(g2.declaration.name == "g", "Member method of member type");
        value methods = `class Hijo`.annotatedMemberDeclarations<FunctionDeclaration,DumbAnnotation>();
        value declMethods = `class Hijo`.annotatedDeclaredMemberDeclarations<FunctionDeclaration,DumbAnnotation>();
        check(methods.size==2, "annotatedMemberDeclarations expected 2, got ``methods.size``: ``methods``");
        check(declMethods.size==1, "annotatedMemberDeclarations expected 2, got ``declMethods.size``: ``declMethods``");
        value types = `class Fuera`.memberDeclarations<ClassOrInterfaceDeclaration>();
        check(types.size==2, "member types expected 2, got ``types.size``: ``types``");
    } catch (Throwable e) {
        if ("Cannot read property '$$' of undefined" in e.message) {
            fail("Member declaration tests won't work in lexical scope style");
        } else {
            fail("Something bad: ``e.message``");
            e.printStackTrace();
        }
    }
    value module489=`module nesting`;
    check(module489.string=="module nesting/0.1", "#489.1 module is ' ``module489.string``'");
    value packs489=module489.members;
    check(packs489.size==2, "#489.2");
    void checkPackage(Package p) {
        value funs=p.members<FunctionDeclaration>();
        check(funs.size>=2, "#489.5 expected 2 functions found ``funs``");
        value vals=p.members<ValueDeclaration>();
        check(vals.size>=4, "#489.6 expected 4 values found ``vals``");
        value typs=p.members<ClassOrInterfaceDeclaration>();
        check(typs.size>=2, "#489.7 expected 2 classes found ``typs``");
    }
    if (exists rootp489=packs489.find((p)=>p.name=="nesting")) {
        checkPackage(rootp489);
    } else {
        fail("#489.3 package 'nesting' not found");
    }
    if (exists subp489=packs489.find((p)=>p.name=="nesting.sub")) {
        checkPackage(subp489);
    } else {
        fail("#489.4 package 'nesting.sub' not found");
    }
    value model669 = type(Issue669<String,Integer>().Middle<Float>().Inner());
    assert (is MemberClass<Issue669<String,Integer>.Middle<Float>,Issue669<String,Integer>.Middle<Float>.Inner,[]> model669);
    if (exists c669=model669.container) {
      check(c669.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>", "langmod #669.1 says ``c669``");
    } else {
      fail("langmod #669 model669.container should exist");
    }
    check(model669.declaringType.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>", "langmod #669.2 says ``model669.declaringType``");
    check(model669.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>.Inner", "langmod #669.3 says ``model669``");
    value static669=`Issue669<String,Integer>.Middle<Float>.Inner`;
    check(static669.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>.Inner", "langmod #669.4 says ``static669``");
    check("ceylon.language::Integer," in type([].withTrailing(1).withTrailing(1).withTrailing(1)).string, "lang#703.1");
    variable [Integer*] issue703=[];
    issue703=issue703.withTrailing(1);
    issue703=issue703.withTrailing(1);
    issue703=issue703.withTrailing(1);
    check("ceylon.language::Integer," in type(issue703).string, "lang#703.2");
    check(type(identity<Integer>).string=="ceylon.language::identity<ceylon.language::Integer>", "#591");
}
