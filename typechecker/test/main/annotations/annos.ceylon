import ceylon.language.model { Annotation }

@error annotation class NonfinalAnnotation() 
        satisfies Annotation<NonfinalAnnotation> {}
@error final annotation class GenericAnnotation<T>() 
        satisfies Annotation<GenericAnnotation<T>> {}
@error final annotation class NonemptyAnnotation() 
        satisfies Annotation<NonemptyAnnotation> { print("hello"); }

final annotation class ParameterizedAnnotation
        (int,float,char,str,bool,ann,iter,seq) 
        satisfies Annotation<ParameterizedAnnotation> {
    Integer int; Float float; Character char; String str; Boolean bool;
    ParameterizedAnnotation ann;
    {String*} iter;
    Float[] seq;
}

final annotation class BrokenParameterizedAnnotation1(@error Integer|Float num) 
        satisfies Annotation<BrokenParameterizedAnnotation1> {}
final annotation class BrokenParameterizedAnnotation2(@error Object obj) 
        satisfies Annotation<BrokenParameterizedAnnotation2> {}
final annotation class BrokenParameterizedAnnotation3(@error {String|Character*} iter) 
        satisfies Annotation<BrokenParameterizedAnnotation3> {}
final annotation class BrokenParameterizedAnnotation4(@error [Float|Integer*] seq) 
        satisfies Annotation<BrokenParameterizedAnnotation4> {}
final annotation class BrokenParameterizedAnnotation5(@error [Float,Integer] tup) 
        satisfies Annotation<BrokenParameterizedAnnotation5> {}


@error annotation GenericAnnotation<T> genericAnnotation() 
        => GenericAnnotation<T>();
@error annotation NonemptyAnnotation nonemptyAnnotation() {
    print("hello"); 
    return NonemptyAnnotation();
}

annotation ParameterizedAnnotation parameterizedAnnotation1
        (int,float,char,str,bool,ann,iter,seq) {
    Integer int; Float float; Character char; String str; Boolean bool;
    ParameterizedAnnotation ann;
    {String*} iter;
    Float[] seq;
    return ParameterizedAnnotation(int,float,char,str,bool,ann,iter,seq);
}

annotation ParameterizedAnnotation parameterizedAnnotation2
        (Integer int,
        Float float, 
        Character char, 
        String str, 
        Boolean bool,
        ParameterizedAnnotation ann,
        {String*} iter, 
        Float[]seq) => 
        ParameterizedAnnotation(int,float,char,str,bool,ann,iter,seq);

annotation BrokenParameterizedAnnotation1
        brokenParameterizedAnnotation1(@error Integer|Float num) 
        => BrokenParameterizedAnnotation1(num);
annotation BrokenParameterizedAnnotation2
        brokenParameterizedAnnotation2(@error Object obj) 
        => BrokenParameterizedAnnotation2(obj);
annotation BrokenParameterizedAnnotation3
        brokenParameterizedAnnotation3(@error {String|Character*} iter) 
        => BrokenParameterizedAnnotation3(iter);
annotation BrokenParameterizedAnnotation4
        brokenParameterizedAnnotation4(@error [Float|Integer*] seq) 
       => BrokenParameterizedAnnotation4(seq);
annotation BrokenParameterizedAnnotation5
        brokenParameterizedAnnotation5(@error [Float,Integer] tup) 
        => BrokenParameterizedAnnotation5(tup);

