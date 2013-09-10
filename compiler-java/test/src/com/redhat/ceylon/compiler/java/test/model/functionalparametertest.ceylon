String f(String s) => s;

class FunctionalParameterSub() extends FunctionalParameter(f) {
}
class FunctionalParameterAlias2(Anything g(String s)) => FunctionalParameter(g);
class FunctionalParameterAlias3(Anything g(String s)) => FunctionalParameterAlias(g);

void checkFunctionalParameters() {
    FunctionalParameter(f).m(f);
    FunctionalParameterAlias2(f).m(f);
    FunctionalParameterAlias3(f).m(f);
    functionalParameter(f);
    
    function mpl(String s1)(String s2) => nothing;
    functionalParameterMpl(mpl);
    
    function fp(Anything fp1(String s2)) => nothing;
    functionalParameterFp(fp);
}