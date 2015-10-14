import javax.inject{
    inject,
    inject__FIELD,
    named,
    singleton,
    qualifier,
    scope
}
@nomodel
class JavaxInjectInject {
    inject
    shared new (String x) {
        
    }
    inject named("dave") late String dave;
    inject variable String steve = "";
    inject__FIELD variable String darren = "";
}

inject variable String javaxInjectInjectSetter = "";// TODO
inject__FIELD variable String javaxInjectInjectField = "";// TODO

@nomodel
singleton
class JavaxInjectSingleton() {
    
}
@nomodel
qualifier
final annotation class JavaxInjectQualifier() {}
@nomodel
scope
final annotation class JavaxInjectScope() {}