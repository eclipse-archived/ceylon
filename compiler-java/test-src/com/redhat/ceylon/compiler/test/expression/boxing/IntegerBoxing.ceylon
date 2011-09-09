@nomodel
class IntegerBoxing(){
    T id<T>(T x){
        return x;
    }
    void m() {
        // decl
        variable Integer localAttrInteger := +1;
        variable Integer localAttrOptionalInteger := +1;
        variable Object localAttrObject := +1;
        
        // assign
        localAttrInteger := localAttrInteger;
        localAttrOptionalInteger := localAttrInteger;
        localAttrObject := localAttrInteger;
        
        localAttrInteger := id(+1);
        localAttrOptionalInteger := id(+1);
        localAttrObject := id(+1);
        
        Object localAttrConstantObject = localAttrInteger;
        Integer localAttrConstantInteger = localAttrInteger;
        
        // is
        if(is Integer localAttrConstantObject){
            localAttrInteger := localAttrConstantObject;
        }
        if(is Integer localAttrConstantInteger){
            localAttrInteger := localAttrConstantInteger;
        }
        if(is String localAttrConstantObject){
            String foo = localAttrConstantObject;
        }
        if(is String localAttrConstantInteger){
            String foo = localAttrConstantInteger;
        }
        
        localAttrInteger := localAttrInteger + +2; 
        localAttrObject := localAttrInteger + +2; 
    }
}