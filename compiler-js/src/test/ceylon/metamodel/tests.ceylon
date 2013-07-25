import ceylon.language.metamodel { ... }
import check { check,results }

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
    Function<Integer,[{Integer+}]> ftypeParams = `sum<Integer>`;
    Function<Float,[Float,Float]> ftimes = `times<Float>`;
    Method<String,String,[Integer]> finitial = `String.initial`;
    Method<Iterable<String>,Iterable<String>,[Integer]> ftaking = `Iterable<String>.taking`;
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
    results();
}
