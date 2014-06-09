import ceylon.language.meta.declaration {
  FunctionDeclaration, ClassOrInterfaceDeclaration
}
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
    } catch (Exception e) {
        if ("Cannot read property '$$' of undefined" in e.message) {
            fail("Member declaration tests won't work in lexical scope style");
        } else {
            fail("Something bad: ``e.message``");
            e.printStackTrace();
        }
    }
}
