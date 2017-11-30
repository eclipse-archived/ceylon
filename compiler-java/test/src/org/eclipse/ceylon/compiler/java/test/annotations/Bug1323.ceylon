import ceylon.language.meta.declaration{FunctionDeclaration}

@nomodel
final annotation class Bug1323_1(Integer i, String* a) 
        satisfies SequencedAnnotation<Bug1323_1, FunctionDeclaration> {}
@nomodel
final annotation class Bug1323_2(Integer i, String[] a) 
        satisfies SequencedAnnotation<Bug1323_2, FunctionDeclaration> {}

@nomodel
annotation Bug1323_1 bug1223Spread1_1(Integer i, String* a) => Bug1323_1(i, *a);
@nomodel
annotation Bug1323_1 bug1223Spread1_2(Integer i, String[] a) => Bug1323_1(i, *a);
@nomodel
annotation Bug1323_1 bug1223Spread1_3(String[] a, Integer i=0) => Bug1323_1(i, *a);
@nomodel
annotation Bug1323_2 bug1223Spread2_1(Integer i, String* a) => Bug1323_2(i, a);
@nomodel
annotation Bug1323_2 bug1223Spread2_2(Integer i, String[] a) => Bug1323_2(i, a);
@nomodel
annotation Bug1323_2 bug1223Spread2_3(String[] a, Integer i=0) => Bug1323_2(i, a);

@nomodel
by {authors=["Gavin", "Tako"];}
bug1223Spread1_1{a=["a", "A"]; i=0;}
bug1223Spread1_2{a=["b", "B"]; i=0;}
bug1223Spread1_3{a=["c", "C"];}

bug1223Spread2_1{a=["d", "D"]; i=0;}
bug1223Spread2_2{a=["e", "E"]; i=0;}
bug1223Spread2_3{a=["f", "F"];}
shared void bug1323() {}