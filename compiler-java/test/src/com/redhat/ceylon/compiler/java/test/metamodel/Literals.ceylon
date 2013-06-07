import ceylon.language.metamodel { ... }

@noanno
void literals<T>(){
    Class<Integer,[Integer]> integerType = `Integer`;
    Interface<Number> numberType = `Number`;
    Type parameterType = `T`;
    UnionType unionType = `Number|Closeable`;
    IntersectionType intersectionType = `Number&Closeable`;
    Type nothingType = `String&Number`;
}