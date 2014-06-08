import ceylon.language.meta.declaration { FunctionDeclaration }
import check { check, fail }

shared class Fuera() {
  shared class Dentro() {
    shared void f() {}
    shared class Cebolla() {
      shared void g() {}
    }
  }
}

shared annotation DumbAnnotation dumb() => DumbAnnotation();

shared final annotation class DumbAnnotation()
        satisfies OptionalAnnotation<DumbAnnotation,FunctionDeclaration> {}

shared class Padre() {
  shared void a() {}
  shared dumb void b() {}
}
shared class Hijo() {
  shared void c() {}
  shared dumb void d() {}
}

void issues() {
    try {
        value g1 = `function Fuera.Dentro.Cebolla.g`;
        value g2 = `Fuera.Dentro.Cebolla.g`;
        check(g1.name == "g", "Member method of member type declaration");
        check(g2.declaration.name == "g", "Member method of member type");
        check(`class Hijo`.annotatedMemberDeclarations<FunctionDeclaration,DumbAnnotation>().size==2, "annotatedMemberDeclarations");
    } catch (Exception e) {
        if ("Cannot read property '$$' of undefined" in e.message) {
            fail("Member declaration tests won't work in lexical scope style");
        } else {
            fail("Something bad: ``e.message``");
            e.printStackTrace();
        }
    }
}
