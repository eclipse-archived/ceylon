class FunctionalParameter(
    Anything fn(String s)) {
    shared default void m(Anything mfn(String s)) {
    }
}

void functionalParameter(Anything fn(String s)) {
}
void functionalParameterMpl(Anything fn(String s)(String s2)) {
}
void functionalParameterFp(Anything fn(Anything fn2(String s2))) {
}

class FunctionalParameterAlias(Anything g(String s)) => FunctionalParameter(g);


