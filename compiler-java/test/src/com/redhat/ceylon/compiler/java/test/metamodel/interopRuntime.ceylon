import ceylon.language.metamodel { Class, Attribute, Variable, Function }

void interopRuntime(){
    Class<JavaType,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]> javaType = `JavaType`;
    
    //
    // constructor
    
    value instance = javaType(true,1,2,3,4,1.0,2.0,'a',"a","b");

    //
    // methods
    assert(exists method = javaType.getFunction<JavaType,Function<Anything,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]>>("method"));
    method(instance)(true,1,2,3,4,1.0,2.0,'a',"a","b");

    assert(exists methodBoolean = javaType.getFunction<Anything,Function<Boolean,[]>>("methodBoolean"));
    assert(true == methodBoolean(instance)());

    assert(exists methodByte = javaType.getFunction<Anything,Function<Integer,[]>>("methodByte"));
    assert(1 == methodByte(instance)());

    assert(exists methodShort = javaType.getFunction<Anything,Function<Integer,[]>>("methodShort"));
    assert(2 == methodShort(instance)());

    assert(exists methodInt = javaType.getFunction<Anything,Function<Integer,[]>>("methodInt"));
    assert(3 == methodInt(instance)());

    assert(exists methodLong = javaType.getFunction<Anything,Function<Integer,[]>>("methodLong"));
    assert(4 == methodLong(instance)());

    assert(exists methodFloat = javaType.getFunction<Anything,Function<Float,[]>>("methodFloat"));
    assert(1.0 == methodFloat(instance)());

    assert(exists methodDouble = javaType.getFunction<Anything,Function<Float,[]>>("methodDouble"));
    assert(2.0 == methodDouble(instance)());

    assert(exists methodChar = javaType.getFunction<Anything,Function<Character,[]>>("methodChar"));
    assert('a' == methodChar(instance)());

    assert(exists methodStr = javaType.getFunction<Anything,Function<String,[]>>("methodStr"));
    assert("a" == methodStr(instance)());

    assert(exists methodObject = javaType.getFunction<Anything,Function<Object,[]>>("methodObject"));
    assert("b" == methodObject(instance)());

    //
    // properties
    
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