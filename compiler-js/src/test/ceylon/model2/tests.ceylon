import ceylon.language.meta{ ... }
import ceylon.language.meta.model { ... }
import ceylon.language.meta.declaration {
  FunctionDeclaration, InterfaceDeclaration, ValueDeclaration, ClassDeclaration
}
import check { check,fail,results }

void literals<T>() {
    Class<Integer,[Integer]> integerType = `Integer`;
    check(integerType(1) == 1, "Use Class as constructor");
    Interface<Number> numberType = `Number`;
    Type parameterType = `T`;
    Type nothingType = `String&Number`;
    UnionType unionType = `Number|Closeable`;
    check(unionType.caseTypes.size == 2, "UnionType case types ``unionType.caseTypes.size``, expected 2");
    IntersectionType intersectionType = `Number&Closeable`;
    check(intersectionType.satisfiedTypes.size == 2, "IntersectionType satisfied types ``intersectionType.satisfiedTypes.size``, expected 2");
    Function<Boolean,[{Boolean*}]> ftype = `any`;
    check(any{false,false,true}, "simple metamodel function");
    Function<Integer,[{Integer+}]> ftypeParams = `sum<Integer>`;
    check(sum{1,2,3,4}==10, "metamodel function w/type params 1");
    Function<Float,[Float,Float]> ftimes = `times<Float>`;
    check(ftimes(2.0,2.0)==4.0, "metamodel function w/type params 2");
    Method<String,String,[Integer]> finitial = `String.initial`;
    Method<Iterable<String>,Iterable<String>,[Integer]> ftaking = `Iterable<String>.taking`;
    FunctionDeclaration fdecl = `function Iterable.taking`;
    value taking1 = fdecl.bindAndApply({1,2,3,4,5});
    check(!taking1 is Function<String,[String]>, "taking1 is NOT String(String)");
    check(!taking1 is Function<{Integer*},[String]>, "taking1 is NOT {Integer*}(String)");
    if (is Function<Iterable<Integer>,[Integer]> taking1) {
      check(taking1(3).sequence=={1,2,3}, "Function Iterable.taking failed");
    } else {
      fail("taking1 should be Function<Iterable<Integer>,[Integer]>");
    }
    InterfaceDeclaration idecl = `interface Iterable`;
    check(!idecl.getMemberDeclaration<InterfaceDeclaration>("cycled") exists, "Iterable.cycled is not an interface");
    check(!idecl.getMemberDeclaration<FunctionDeclaration>("cycled") exists, "Iterable.cycled is not a method");
    check(!idecl.getMemberDeclaration<ClassDeclaration>("cycled") exists, "Iterable.cycled is not a class");
    value cycledDecl = idecl.getMemberDeclaration<ValueDeclaration>("cycled");
    if (exists cycledDecl) {
      //TODO apply
      value cycled1 = cycledDecl.apply(Singleton(1));
      if (is {Integer*} cycled1) {
        value iter = cycled1.iterator();
        for (i in 1..100) { iter.next(); }
        check(iter.next() == 1, "Iter 1");
      } else {
        fail("Applied 'cycled' should be {Integer*}");
      }
    } else {
      fail("Iterable should have value 'cycled'");
    }
}

class Example1(){}
class Example2() extends Example1(){}

void typeTests() {
  print("type(null):``type(null)``");
  print("type(Example1()):``type(Example1())``");
  print("type(Example2):``type(Example2)``");
  print("type('hello'):``type("hello")``");
  print("type({1,2,4,5}):``type({1,2,4,5})``");
  print("type(1..2):``type(1..2)``");
  print("type(test):``type(test)``");
  print("type('hello'.initial):``type("hello".initial)``");
}

shared void test() {
    print("Metamodel tests");
    literals<String>();
    typeTests();
    modulesTests();
    results();
}
