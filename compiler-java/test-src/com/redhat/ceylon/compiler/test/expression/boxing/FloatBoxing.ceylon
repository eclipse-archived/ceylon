@nomodel
class FloatBoxing(){
    T id<T>(T x){
        return x;
    }
    void m() {
        // decl
        variable Float localAttrFloat := +1.0;
        variable Float? localAttrOptionalFloat := +1.0;
        variable Object localAttrObject := +1.0;
        
        // assign
        localAttrFloat := localAttrFloat;
        localAttrOptionalFloat := localAttrFloat;
        localAttrObject := localAttrFloat;
        
        localAttrFloat := id(+1.0);
        localAttrOptionalFloat := id(+1.0);
        localAttrObject := id(+1.0);
        
        Object localAttrConstantObject = localAttrFloat;
        Float localAttrConstantFloat = localAttrFloat;
        
        // is
        if(is Float localAttrConstantObject){
            localAttrFloat := localAttrConstantObject;
        }
        if(is Float localAttrConstantFloat){
            localAttrFloat := localAttrConstantFloat;
        }
        if(is String localAttrConstantObject){
            String foo = localAttrConstantObject;
        }
        if(is String localAttrConstantFloat){
            String foo = localAttrConstantFloat;
        }
        
        localAttrFloat := localAttrFloat + +2.0; 
        localAttrObject := localAttrFloat + +2.0; 
    }
}