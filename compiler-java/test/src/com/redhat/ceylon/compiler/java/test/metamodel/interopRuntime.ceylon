import ceylon.language.metamodel { Class, Attribute, Variable }

void interopRuntime(){
    Class<JavaType,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]> javaType = `JavaType`;
    
    value instance = javaType(true,1,2,3,4,1.0,2.0,'a',"a","b");
    
    assert(exists booleanAttr = javaType.getAttribute<JavaType,Variable<Boolean>>("boolean"));
    assert(booleanAttr(instance).get() == true);
    booleanAttr(instance).set(true);

    assert(exists byteAttr = javaType.getAttribute<JavaType,Variable<Integer>>("byte"));
    assert(byteAttr(instance).get() == 1);
    byteAttr(instance).set(1);

    assert(exists shortAttr = javaType.getAttribute<JavaType,Variable<Integer>>("short"));
    assert(shortAttr(instance).get() == 2);
    shortAttr(instance).set(2);

    assert(exists intAttr = javaType.getAttribute<JavaType,Variable<Integer>>("int"));
    assert(intAttr(instance).get() == 3);
    intAttr(instance).set(3);
    
    assert(exists longAttr = javaType.getAttribute<JavaType,Variable<Integer>>("long"));
    assert(longAttr(instance).get() == 4);
    longAttr(instance).set(4);

    assert(exists floatAttr = javaType.getAttribute<JavaType,Variable<Float>>("float"));
    assert(floatAttr(instance).get() == 1.0);
    floatAttr(instance).set(1.0);

    assert(exists doubleAttr = javaType.getAttribute<JavaType,Variable<Float>>("double"));
    assert(doubleAttr(instance).get() == 2.0);
    doubleAttr(instance).set(2.0);

    assert(exists charAttr = javaType.getAttribute<JavaType,Variable<Character>>("char"));
    assert(charAttr(instance).get() == 'a');
    charAttr(instance).set('a');

    assert(exists stringAttr = javaType.getAttribute<JavaType,Variable<String>>("str"));
    assert(stringAttr(instance).get() == "a");
    stringAttr(instance).set("a");

    assert(exists objectAttr = javaType.getAttribute<JavaType,Variable<Object>>("object"));
    assert(objectAttr(instance).get() == "b");
    objectAttr(instance).set("b");
}