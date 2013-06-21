import ceylon.language.metamodel { Class, Attribute }

void interopRuntime(){
    Class<JavaType,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]> javaType = `JavaType`;
    
    value instance = javaType(true,1,2,3,4,1.0,2.0,'a',"a","b");
    
    assert(exists booleanAttr = javaType.getAttribute<JavaType,Attribute<Boolean>>("boolean"));
    assert(booleanAttr(instance).get() == true);

    assert(exists byteAttr = javaType.getAttribute<JavaType,Attribute<Integer>>("byte"));
    assert(byteAttr(instance).get() == 1);

    assert(exists shortAttr = javaType.getAttribute<JavaType,Attribute<Integer>>("short"));
    assert(shortAttr(instance).get() == 2);

    assert(exists intAttr = javaType.getAttribute<JavaType,Attribute<Integer>>("int"));
    assert(intAttr(instance).get() == 3);
    
    assert(exists longAttr = javaType.getAttribute<JavaType,Attribute<Integer>>("long"));
    assert(longAttr(instance).get() == 4);

    assert(exists floatAttr = javaType.getAttribute<JavaType,Attribute<Float>>("float"));
    assert(floatAttr(instance).get() == 1.0);

    assert(exists doubleAttr = javaType.getAttribute<JavaType,Attribute<Float>>("double"));
    assert(doubleAttr(instance).get() == 2.0);

    assert(exists charAttr = javaType.getAttribute<JavaType,Attribute<Character>>("char"));
    assert(charAttr(instance).get() == 'a');

    assert(exists stringAttr = javaType.getAttribute<JavaType,Attribute<String>>("str"));
    assert(stringAttr(instance).get() == "a");

    assert(exists objectAttr = javaType.getAttribute<JavaType,Attribute<Object>>("object"));
    assert(objectAttr(instance).get() == "b");
}