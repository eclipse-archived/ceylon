void testMetamodel() {
    value m = type("falbala");
    
    check(m is Class<String, []>, "metamodel is Class");    
    if(is Class<String, []> m){
        check(m.name == "String", "metamodel class name is String");
    }
}
