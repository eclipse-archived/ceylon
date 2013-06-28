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

    // variadic

    assert(exists methodBooleanVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Boolean, Boolean*]>>("methodBooleanVarargs"));
    methodBooleanVarargs(instance)(0, true);
    methodBooleanVarargs(instance)(1, true, true);
    methodBooleanVarargs(instance)(2, true, true, true);

    assert(exists methodByteVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Integer*]>>("methodByteVarargs"));
    methodByteVarargs(instance)(1, 1);

    assert(exists methodShortVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Integer*]>>("methodShortVarargs"));
    methodShortVarargs(instance)(2, 2);

    assert(exists methodIntVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Integer*]>>("methodIntVarargs"));
    methodIntVarargs(instance)(3, 3);

    assert(exists methodLongVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Integer*]>>("methodLongVarargs"));
    methodLongVarargs(instance)(4, 4);

    assert(exists methodFloatVarargs = javaType.getFunction<Anything,Function<Anything,[Float, Float*]>>("methodFloatVarargs"));
    methodFloatVarargs(instance)(1.0, 1.0);

    assert(exists methodDoubleVarargs = javaType.getFunction<Anything,Function<Anything,[Float, Float*]>>("methodDoubleVarargs"));
    methodDoubleVarargs(instance)(2.0, 2.0);

    assert(exists methodCharVarargs = javaType.getFunction<Anything,Function<Anything,[Character, Character*]>>("methodCharVarargs"));
    methodCharVarargs(instance)('a', 'a');

    assert(exists methodJavaStringVarargs = javaType.getFunction<Anything,Function<Anything,[String, String*]>>("methodJavaStringVarargs"));
    methodJavaStringVarargs(instance)("a", "a");

    assert(exists methodObjectVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Object, Object*]>>("methodObjectVarargs"));
    methodObjectVarargs(instance)(0, "b");
    methodObjectVarargs(instance)(1, "b", "b");
    methodObjectVarargs(instance)(2, "b", "b", "b");

    assert(exists methodBoundObjectVarargs = javaType.getFunction<Anything,Function<Anything,[Integer, Integer, Integer*]>>("methodBoundObjectVarargs", `Integer`));
    methodBoundObjectVarargs(instance)(0, 1);
    methodBoundObjectVarargs(instance)(1, 1, 1);
    methodBoundObjectVarargs(instance)(2, 1, 1, 1);

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
    
    //
    // fields
    
    assert(exists booleanFieldAttr = javaType.getAttribute<JavaType,Variable<Boolean>>("booleanField"));
    assert(booleanFieldAttr(instance).get() == true);
    booleanFieldAttr(instance).set(true);

    assert(exists byteFieldAttr = javaType.getAttribute<JavaType,Variable<Integer>>("byteField"));
    assert(byteFieldAttr(instance).get() == 1);
    byteFieldAttr(instance).set(1);

    assert(exists shortFieldAttr = javaType.getAttribute<JavaType,Variable<Integer>>("shortField"));
    assert(shortFieldAttr(instance).get() == 2);
    shortFieldAttr(instance).set(2);

    assert(exists intFieldAttr = javaType.getAttribute<JavaType,Variable<Integer>>("intField"));
    assert(intFieldAttr(instance).get() == 3);
    intFieldAttr(instance).set(3);
    
    assert(exists longFieldAttr = javaType.getAttribute<JavaType,Variable<Integer>>("longField"));
    assert(longFieldAttr(instance).get() == 4);
    longFieldAttr(instance).set(4);

    assert(exists floatFieldAttr = javaType.getAttribute<JavaType,Variable<Float>>("floatField"));
    assert(floatFieldAttr(instance).get() == 1.0);
    floatFieldAttr(instance).set(1.0);

    assert(exists doubleFieldAttr = javaType.getAttribute<JavaType,Variable<Float>>("doubleField"));
    assert(doubleFieldAttr(instance).get() == 2.0);
    doubleFieldAttr(instance).set(2.0);

    assert(exists charFieldAttr = javaType.getAttribute<JavaType,Variable<Character>>("charField"));
    assert(charFieldAttr(instance).get() == 'a');
    charFieldAttr(instance).set('a');

    assert(exists stringFieldAttr = javaType.getAttribute<JavaType,Variable<String>>("stringField"));
    assert(stringFieldAttr(instance).get() == "a");
    stringFieldAttr(instance).set("a");

    assert(exists objectFieldAttr = javaType.getAttribute<JavaType,Variable<Object>>("objectField"));
    assert(objectFieldAttr(instance).get() == "b");
    objectFieldAttr(instance).set("b");

    //
    // Member types
    
    assert(exists memberMember = javaType.getClassOrInterface<JavaType, Class<JavaType.Member, [Boolean]>>("Member"));
    memberMember(instance)(true);

    assert(exists memberVarargsMember = javaType.getClassOrInterface<JavaType, Class<JavaType.MemberVarargs, [Integer, Boolean, Boolean*]>>("MemberVarargs"));
    memberVarargsMember(instance)(0, true);
    memberVarargsMember(instance)(1, true, true);
    memberVarargsMember(instance)(2, true, true, true);
}