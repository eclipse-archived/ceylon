//import ceylon.language.meta{}
import ceylon.language.meta.declaration{ValueDeclaration}

@nomodel
final annotation class Bug5779() satisfies OptionalAnnotation<Bug5779, ValueDeclaration> {}
@nomodel
annotation Bug5779 bug5779() => Bug5779();
@nomodel
class Bug5779Use(bug5779 shared Integer p, bug5779 shared Integer p2, a, a2) {
    
    bug5779 shared Integer a;
    bug5779 Integer a2;
}