import ceylon.language.meta{...}
import ceylon.language.meta.declaration{...}

final annotation class Bug1676_2_A(Bug1676_2_B x) satisfies OptionalAnnotation<Bug1676_2_A, ClassDeclaration>{

}

final annotation class Bug1676_2_B() satisfies OptionalAnnotation<Bug1676_2_B, Annotated>{
    
}

@error:"illegal annotation argument: must be a literal value, metamodel reference, annotation instantiation, or parameter reference"
annotation Bug1676_2_A bug1676_2_A() => Bug1676_2_A(nothing);
annotation Bug1676_2_B bug1676_2_B(String s) => Bug1676_2_B();
@error:"missing argument to required parameter s of bug1676_2_B"
bug1676_2_A
bug1676_2_B
class Bug1676_2_site(Bug1676_2_B() bug1676_2_B) {
    
}

