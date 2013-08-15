import ceylon.language.model { ... }
import ceylon.language.model.declaration {
  FunctionDeclaration, InterfaceDeclaration, ValueDeclaration
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
    FunctionDeclaration fdecl = `Iterable.taking`;
    value taking1 = fdecl.bindAndApply({1,2,3,4,5}, `Integer`);
    //TODO WTF can I do with taking1?
    InterfaceDeclaration idecl = `Iterable`;
    ValueDeclaration? cycledDecl = idecl.getMemberDeclaration("cycled");
    if (exists cycledDecl) {
      //TODO apply
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
