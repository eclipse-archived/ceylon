@nomodel
class IntegerBoxing(){
    void m() {
        variable Integer localAttrInteger := +1;
        variable Object localAttrObject := +1;
        localAttrObject := localAttrInteger;
        Object localAttrConstantObject = localAttrInteger;
        if(is Integer localAttrConstantObject){
            localAttrInteger := localAttrConstantObject;
        }
        if(is Integer localAttrInteger){
            localAttrInteger := localAttrInteger;
        }
    }
}