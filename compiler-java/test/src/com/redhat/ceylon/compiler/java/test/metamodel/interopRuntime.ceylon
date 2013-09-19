import ceylon.language.meta.model { Class, Attribute, Variable, Function, VariableAttribute }

void interopRuntime(){
    Class<JavaType,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]> javaType = `JavaType`;
    
    //
    // constructor
    
    value instance = javaType(true,1,2,3,4,1.0,2.0,'a',"a","b");

    //
    // methods
    
    assert(exists method = javaType.getMethod<JavaType,Anything,[Boolean,Integer,Integer,Integer,Integer,Float,Float,Character,String,Object]>("method"));
    method(instance)(true,1,2,3,4,1.0,2.0,'a',"a","b");

    assert(exists methodBoolean = javaType.getMethod<Anything,Boolean,[]>("methodBoolean"));
    assert(true == methodBoolean(instance)());

    assert(exists methodByte = javaType.getMethod<Anything,Integer,[]>("methodByte"));
    assert(1 == methodByte(instance)());

    assert(exists methodShort = javaType.getMethod<Anything,Integer,[]>("methodShort"));
    assert(2 == methodShort(instance)());

    assert(exists methodInt = javaType.getMethod<Anything,Integer,[]>("methodInt"));
    assert(3 == methodInt(instance)());

    assert(exists methodLong = javaType.getMethod<Anything,Integer,[]>("methodLong"));
    assert(4 == methodLong(instance)());

    assert(exists methodFloat = javaType.getMethod<Anything,Float,[]>("methodFloat"));
    assert(1.0 == methodFloat(instance)());

    assert(exists methodDouble = javaType.getMethod<Anything,Float,[]>("methodDouble"));
    assert(2.0 == methodDouble(instance)());

    assert(exists methodChar = javaType.getMethod<Anything,Character,[]>("methodChar"));
    assert('a' == methodChar(instance)());

    assert(exists methodStr = javaType.getMethod<Anything,String,[]>("methodStr"));
    assert("a" == methodStr(instance)());

    assert(exists methodObject = javaType.getMethod<Anything,Object,[]>("methodObject"));
    assert("b" == methodObject(instance)());

    // variadic

    assert(exists methodBooleanVarargs = javaType.getMethod<Anything,Anything,[Integer, Boolean, Boolean*]>("methodBooleanVarargs"));
    methodBooleanVarargs(instance)(0, true);
    methodBooleanVarargs(instance)(1, true, true);
    methodBooleanVarargs(instance)(2, true, true, true);

    assert(exists methodByteVarargs = javaType.getMethod<Anything,Anything,[Integer, Integer*]>("methodByteVarargs"));
    methodByteVarargs(instance)(1, 1);

    assert(exists methodShortVarargs = javaType.getMethod<Anything,Anything,[Integer, Integer*]>("methodShortVarargs"));
    methodShortVarargs(instance)(2, 2);

    assert(exists methodIntVarargs = javaType.getMethod<Anything,Anything,[Integer, Integer*]>("methodIntVarargs"));
    methodIntVarargs(instance)(3, 3);

    assert(exists methodLongVarargs = javaType.getMethod<Anything,Anything,[Integer, Integer*]>("methodLongVarargs"));
    methodLongVarargs(instance)(4, 4);

    assert(exists methodFloatVarargs = javaType.getMethod<Anything,Anything,[Float, Float*]>("methodFloatVarargs"));
    methodFloatVarargs(instance)(1.0, 1.0);

    assert(exists methodDoubleVarargs = javaType.getMethod<Anything,Anything,[Float, Float*]>("methodDoubleVarargs"));
    methodDoubleVarargs(instance)(2.0, 2.0);

    assert(exists methodCharVarargs = javaType.getMethod<Anything,Anything,[Character, Character*]>("methodCharVarargs"));
    methodCharVarargs(instance)('a', 'a');

    assert(exists methodJavaStringVarargs = javaType.getMethod<Anything,Anything,[String, String*]>("methodJavaStringVarargs"));
    methodJavaStringVarargs(instance)("a", "a");

    assert(exists methodObjectVarargs = javaType.getMethod<Anything,Anything,[Integer, Object, Object*]>("methodObjectVarargs"));
    methodObjectVarargs(instance)(0, "b");
    methodObjectVarargs(instance)(1, "b", "b");
    methodObjectVarargs(instance)(2, "b", "b", "b");

    assert(exists methodBoundObjectVarargs = javaType.getMethod<Anything,Anything,[Integer, Integer, Integer*]>("methodBoundObjectVarargs", `Integer`));
    methodBoundObjectVarargs(instance)(0, 1);
    methodBoundObjectVarargs(instance)(1, 1, 1);
    methodBoundObjectVarargs(instance)(2, 1, 1, 1);

    //
    // properties
    
    assert(is VariableAttribute<JavaType,Boolean> booleanAttr = javaType.getAttribute<JavaType,Boolean>("boolean"));
    assert(booleanAttr(instance).get() == true);
    booleanAttr(instance).set(true);

    assert(is VariableAttribute<JavaType,Integer> byteAttr = javaType.getAttribute<JavaType,Integer>("byte"));
    assert(byteAttr(instance).get() == 1);
    byteAttr(instance).set(1);

    assert(is VariableAttribute<JavaType,Integer> shortAttr = javaType.getAttribute<JavaType,Integer>("short"));
    assert(shortAttr(instance).get() == 2);
    shortAttr(instance).set(2);

    assert(is VariableAttribute<JavaType,Integer> intAttr = javaType.getAttribute<JavaType,Integer>("int"));
    assert(intAttr(instance).get() == 3);
    intAttr(instance).set(3);
    
    assert(is VariableAttribute<JavaType,Integer>  longAttr = javaType.getAttribute<JavaType,Integer>("long"));
    assert(longAttr(instance).get() == 4);
    longAttr(instance).set(4);

    assert(is VariableAttribute<JavaType,Float>  floatAttr = javaType.getAttribute<JavaType,Float>("float"));
    assert(floatAttr(instance).get() == 1.0);
    floatAttr(instance).set(1.0);

    assert(is VariableAttribute<JavaType,Float>  doubleAttr = javaType.getAttribute<JavaType,Float>("double"));
    assert(doubleAttr(instance).get() == 2.0);
    doubleAttr(instance).set(2.0);

    assert(is VariableAttribute<JavaType,Character>  charAttr = javaType.getAttribute<JavaType,Character>("char"));
    assert(charAttr(instance).get() == 'a');
    charAttr(instance).set('a');

    assert(is VariableAttribute<JavaType,String>  stringAttr = javaType.getAttribute<JavaType,String>("str"));
    assert(stringAttr(instance).get() == "a");
    stringAttr(instance).set("a");

    assert(is VariableAttribute<JavaType,Object> objectAttr = javaType.getAttribute<JavaType,Object>("object"));
    assert(objectAttr(instance).get() == "b");
    objectAttr(instance).set("b");
    
    //
    // fields
    
    assert(is VariableAttribute<JavaType,Boolean> booleanFieldAttr = javaType.getAttribute<JavaType,Boolean>("booleanField"));
    assert(booleanFieldAttr(instance).get() == true);
    booleanFieldAttr(instance).set(true);

    assert(is VariableAttribute<JavaType,Integer> byteFieldAttr = javaType.getAttribute<JavaType,Integer>("byteField"));
    assert(byteFieldAttr(instance).get() == 1);
    byteFieldAttr(instance).set(1);

    assert(is VariableAttribute<JavaType,Integer> shortFieldAttr = javaType.getAttribute<JavaType,Integer>("shortField"));
    assert(shortFieldAttr(instance).get() == 2);
    shortFieldAttr(instance).set(2);

    assert(is VariableAttribute<JavaType,Integer> intFieldAttr = javaType.getAttribute<JavaType,Integer>("intField"));
    assert(intFieldAttr(instance).get() == 3);
    intFieldAttr(instance).set(3);
    
    assert(is VariableAttribute<JavaType,Integer> longFieldAttr = javaType.getAttribute<JavaType,Integer>("longField"));
    assert(longFieldAttr(instance).get() == 4);
    longFieldAttr(instance).set(4);

    assert(is VariableAttribute<JavaType,Float> floatFieldAttr = javaType.getAttribute<JavaType,Float>("floatField"));
    assert(floatFieldAttr(instance).get() == 1.0);
    floatFieldAttr(instance).set(1.0);

    assert(is VariableAttribute<JavaType,Float> doubleFieldAttr = javaType.getAttribute<JavaType,Float>("doubleField"));
    assert(doubleFieldAttr(instance).get() == 2.0);
    doubleFieldAttr(instance).set(2.0);

    assert(is VariableAttribute<JavaType,Character> charFieldAttr = javaType.getAttribute<JavaType,Character>("charField"));
    assert(charFieldAttr(instance).get() == 'a');
    charFieldAttr(instance).set('a');

    assert(is VariableAttribute<JavaType,String> stringFieldAttr = javaType.getAttribute<JavaType,String>("stringField"));
    assert(stringFieldAttr(instance).get() == "a");
    stringFieldAttr(instance).set("a");

    assert(is VariableAttribute<JavaType,Object> objectFieldAttr = javaType.getAttribute<JavaType,Object>("objectField"));
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