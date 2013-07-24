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
}

shared void test() {
    print("Metamodel tests");
    literals<String>();
    results();
}
