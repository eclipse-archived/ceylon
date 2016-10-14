class Test5751() {
    // METHOD but not FIELD and setter generated => warning
    methodTarget
    shared late String s1;
    
    methodTarget
    shared variable String s2 = "";
    
    methodTarget // no warning, because we can annotate the setter separately
    shared String s3 => "";
    assign s3 {}
    
    // FIELD only, but two fields (due to late) => warning
    fieldTarget
    shared late String s4;
    
    fieldTarget
    shared late String s5;
    s5="";
    
    fieldTarget
    shared variable late String s6;
    s6="";
    
    // FIELD or METHOD (no warning, it goes on the field)
    fieldOrMethodTarget
    shared late String s7;
    fieldOrMethodTarget
    shared variable String s8="";
    
    // TODO what about parameters!
    
}