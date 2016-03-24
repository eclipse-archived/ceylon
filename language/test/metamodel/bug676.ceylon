import ceylon.language.meta.declaration { ValueDeclaration }

class Bug676Bar(variable Integer x) {
    
}

@test
shared void bug676() {
    assert(is ValueDeclaration xDecl=`class Bug676Bar`.parameterDeclarations[0]);
    try{
        xDecl.memberGet(Bug676Bar(1));
        assert(false);
    }catch(Exception x){
        assert(x.message == "Attribute x is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
    }
    try{
        xDecl.memberSet(Bug676Bar(1), 2);
        assert(false);
    }catch(Exception x){
        assert(x.message == "Attribute x is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
    }
    value attr = xDecl.memberApply<Bug676Bar,Integer>(`Bug676Bar`);
    try{
        value val = attr(Bug676Bar(1));
        assert(false);
    }catch(Exception x){
        assert(x.message == "Attribute x is neither captured nor shared so it has no physical storage allocated and cannot be read by the metamodel");
    }
}
