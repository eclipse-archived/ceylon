class SetterTarget(param3, param4) {
    methodTarget__SETTER
    shared late String attr;
    
    @error//:"no target for methodTarget__SETTER annotation: @Target of @interface MethodTarget lists [SETTER] but annotated element tranforms to [LOCAL_VARIABLE]"
    methodTarget__SETTER
    late String attr2;
    
    methodTarget__SETTER
    shared variable String attr3 = "";
    
    @error//:"no target for methodTarget__SETTER annotation: @Target of @interface MethodTarget lists [SETTER] but annotated element tranforms to [LOCAL_VARIABLE]"
    methodTarget__SETTER
    variable String attr4 = "";
    
    methodTarget__SETTER
    shared variable String param3;
    
    @error//:"no target for methodTarget__SETTER annotation: @Target of @interface MethodTarget lists [SETTER] but annotated element tranforms to [PARAMETER]"
    methodTarget__SETTER
    variable String param4;
    
    
}