shared class NoParams(){
    shared variable String str2 = "a";
    shared variable Integer integer2 = 1;
    shared variable Float float2 = 1.2;
    shared variable Character character2 = 'a';
    shared variable Boolean boolean2 = true;
    shared variable Object obj2 = 2;

    shared String str = "a";
    shared Integer integer = 1;
    shared Float float = 1.2;
    shared Character character = 'a';
    shared Boolean boolean = true;
    shared NoParams obj => this;

    shared NoParams noParams() => this;

    shared NoParams fixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o){
        assert(s == "a");
        assert(i == 1);
        assert(f == 1.2);
        assert(c == 'a');
        assert(b == true);
        assert(is NoParams o);
        
        return this;
    }
    
    shared NoParams typeParams<T>(T s, Integer i)
        given T satisfies Object {
        
        assert(s == "a");
        assert(i == 1);
        
        // check that our reified T got passed correctly
        assert(is TypeParams<String> t = TypeParams<T>(s, i));
        
        return this;
    }
    
    shared String getString() => "a";
    shared Integer getInteger() => 1;
    shared Float getFloat() => 1.2;
    shared Character getCharacter() => 'a';
    shared Boolean getBoolean() => true;
}

shared class FixedParams(String s, Integer i, Float f, Character c, Boolean b, Object o){
    assert(s == "a");
    assert(i == 1);
    assert(f == 1.2);
    assert(c == 'a');
    assert(b == true);
    assert(is NoParams o);
}

shared class TypeParams<T>(T s, Integer i)
    given T satisfies Object {
    
    assert(s == "a");
    assert(i == 1);
}

void checkConstructors(){
    // no parameters
    value noParamsType = type(NoParams());
    assert(is AppliedClassType<NoParams, []> noParamsType);
    value noParamsClass = noParamsType.declaration;
    assert(noParamsClass.name == "NoParams");
    // not sure how to do this ATM
    //value noParamsType2 = noParamsClass.apply();
    //assert(is ClassType<NoParams, []> noParamsType2);
    Anything noParams = noParamsType();
    assert(is NoParams noParams);
    
    // static parameters
    value fixedParamsType = type(FixedParams("a", 1, 1.2, 'a', true, noParams));
    assert(is AppliedClassType<FixedParams, [String, Integer, Float, Character, Boolean, Object]> fixedParamsType);
    value fixedParamsClass = fixedParamsType.declaration;
    assert(fixedParamsClass.name == "FixedParams");
    Anything fixedParams = fixedParamsType("a", 1, 1.2, 'a', true, noParams);
    assert(is FixedParams fixedParams);

    // typed parameters
    value typeParamsType = type(TypeParams("a", 1));
    assert(is AppliedClassType<TypeParams<String>, [String, Integer]> typeParamsType);
    value typeParamsClass = typeParamsType.declaration;
    assert(typeParamsClass.name == "TypeParams");
    Anything typeParams = typeParamsType("a", 1);
    // this checks that we did pass the reified type arguments correctly
    assert(is TypeParams<String> typeParams);
}

void checkMemberAttributes(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is AppliedClassType<NoParams, []> noParamsType);
    
    assert(is AppliedValue<String> string = noParamsType.getAttribute("str"));
    assert(!is AppliedVariable<String> string);
    assert(string.get(noParamsInstance) == "a");
    
    assert(is AppliedValue<Integer> integer = noParamsType.getAttribute("integer"));
    assert(integer.get(noParamsInstance) == 1);
    
    assert(is AppliedValue<Float> float = noParamsType.getAttribute("float"));
    assert(float.get(noParamsInstance) == 1.2);
    
    assert(is AppliedValue<Character> character = noParamsType.getAttribute("character"));
    assert(character.get(noParamsInstance) == 'a');
    
    assert(is AppliedValue<Boolean> boolean = noParamsType.getAttribute("boolean"));
    assert(boolean.get(noParamsInstance) == true);
    
    assert(is AppliedValue<NoParams> obj = noParamsType.getAttribute("obj"));
    assert(obj.get(noParamsInstance) === noParamsInstance);

    assert(is AppliedVariable<String> string2 = noParamsType.getAttribute("str2"));
    assert(string2.get(noParamsInstance) == "a");
    string2.set(noParamsInstance, "b");
    assert(string2.get(noParamsInstance) == "b");
    assert(noParamsInstance.str2 == "b");
    
    assert(is AppliedVariable<Integer> integer2 = noParamsType.getAttribute("integer2"));
    assert(integer2.get(noParamsInstance) == 1);
    integer2.set(noParamsInstance, 2);
    assert(integer2.get(noParamsInstance) == 2);
    assert(noParamsInstance.integer2 == 2);

    assert(is AppliedVariable<Float> float2 = noParamsType.getAttribute("float2"));
    assert(float2.get(noParamsInstance) == 1.2);
    float2.set(noParamsInstance, 2.1);
    assert(float2.get(noParamsInstance) == 2.1);
    assert(noParamsInstance.float2 == 2.1);
    
    assert(is AppliedVariable<Character> character2 = noParamsType.getAttribute("character2"));
    assert(character2.get(noParamsInstance) == 'a');
    character2.set(noParamsInstance, 'b');
    assert(character2.get(noParamsInstance) == 'b');
    assert(noParamsInstance.character2 == 'b');
    
    assert(is AppliedVariable<Boolean> boolean2 = noParamsType.getAttribute("boolean2"));
    assert(boolean2.get(noParamsInstance) == true);
    boolean2.set(noParamsInstance, false);
    assert(boolean2.get(noParamsInstance) == false);
    assert(noParamsInstance.boolean2 == false);
    
    assert(is AppliedVariable<Object> obj2 = noParamsType.getAttribute("obj2"));
    assert(obj2.get(noParamsInstance) == 2);
    obj2.set(noParamsInstance, 3);
    assert(obj2.get(noParamsInstance) == 3);
    assert(noParamsInstance.obj2 == 3);
}

void checkMemberFunctions(){
    value noParamsInstance = NoParams();
    value noParamsType = type(noParamsInstance);
    assert(is AppliedClassType<NoParams, []> noParamsType);
    assert(is AppliedClassType<String, []> stringType = type("foo"));
    
    assert(is AppliedFunction<NoParams, [NoParams]> f1 = noParamsType.getFunction("noParams"));
    Anything o1 = f1(noParamsInstance);
    assert(is NoParams o1);
    assert(is Callable<NoParams, []> bf1 = f1.bind(noParamsInstance));
    Anything o2 = bf1();
    assert(is NoParams o2);
    
    assert(is AppliedFunction<NoParams, [NoParams, String, Integer, Float, Character, Boolean, Object]> f2 = noParamsType.getFunction("fixedParams"));
    Anything o3 = f2(noParamsInstance, "a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o3);
    assert(is Callable<NoParams, [String, Integer, Float, Character, Boolean, Object]> bf2 = f2.bind(noParamsInstance));
    Anything o4 = bf2("a", 1, 1.2, 'a', true, noParamsInstance);
    assert(is NoParams o4);
    
    assert(is AppliedFunction<NoParams, [NoParams, String, Integer]> f3 = noParamsType.getFunction("typeParams", stringType));
    Anything o5 = f3(noParamsInstance, "a", 1);
    assert(is NoParams o5);
    assert(is Callable<NoParams, [String, Integer]> bf3 = f3.bind(noParamsInstance));
    Anything o6 = bf3("a", 1);
    assert(is NoParams o6);

    assert(is AppliedFunction<String, [NoParams]> f4 = noParamsType.getFunction("getString"));
    assert(f4(noParamsInstance) == "a");
    assert(is AppliedFunction<Integer, [NoParams]> f5 = noParamsType.getFunction("getInteger"));
    assert(f5(noParamsInstance) == 1);
    assert(is AppliedFunction<Float, [NoParams]> f6 = noParamsType.getFunction("getFloat"));
    assert(f6(noParamsInstance) == 1.2);
    assert(is AppliedFunction<Character, [NoParams]> f7 = noParamsType.getFunction("getCharacter"));
    assert(f7(noParamsInstance) == 'a');
    assert(is AppliedFunction<Boolean, [NoParams]> f8 = noParamsType.getFunction("getBoolean"));
    assert(f8(noParamsInstance) == true);
}

@nomodel
shared void runtime() {
//    value classType = type("falbala");
//    
//    doc "metamodel is AppliedClassType"
//    assert(is AppliedClassType<Anything,[]> classType);
//
//    value klass = classType.declaration;
//
//    doc "metamodel class name is String"
//    assert(klass.name == "String");
//
//    value st = klass.superclass;
//
//    doc "super class exists"
//    assert(exists st);
//
//    doc "metamodel superclass name is Object"
//    assert(st.declaration.name == "Object");
//
//    queue = [klass];
//    emptyQueue();
//    
//    visitInheritedTypes(classType);

    checkConstructors();    

    checkMemberFunctions();

    checkMemberAttributes();
}

void visitInheritedTypes(AppliedClassOrInterfaceType<Anything> type){
    print("Types extended by ``type.declaration.name``:\n");
    if(exists AppliedClassType<Anything, Nothing[]> xt = type.superclass){
        visitExtendedTypes(xt);
    }
    print("Types satisfied by ``type.declaration.name``:\n");
    for(AppliedInterfaceType<Anything> sat in type.interfaces){
        visitSatisfiedTypes(sat);
    }
}

void visitExtendedTypes(AppliedClassType<Anything, Nothing[]> type){
    visitAppliedProducedType(type);
    print("\n");
    if(exists AppliedClassType<Anything, Nothing[]> xt = type.superclass){
        visitExtendedTypes(xt);
    }
}

void visitSatisfiedTypes(AppliedInterfaceType<Anything> type){
    visitAppliedProducedType(type);
    print("\n");
    for(AppliedInterfaceType<Anything> sat in type.interfaces){
        visitSatisfiedTypes(sat);
    }
}

variable ClassOrInterface[] queue = {};

void emptyQueue(){
    while(nonempty q = queue){
        visitDeclaration(q.first);
        queue = queue.rest;
    }
}

void queueDeclaration(ClassOrInterface decl){
    queue = queue.withTrailing(decl);
}

void print(Anything obj){
    if(exists obj){
        process.write(obj.string);
    }else{
        process.write("<null>");
    }
}

void visitDeclaration(Declaration decl){
    if(is Class decl){
        visitClass(decl);
    }else if(is Interface decl){
        visitInterface(decl);
    }else{
        print("Unknown declaration");
    }
}

void visitClass(Class klass){
    print("class ``klass.name``");
    if(klass.typeParameters nonempty){
        print("<");
        print(",".join(klass.typeParameters.map(function (TypeParameter tp) => tp.name)));
        print(">");
    }
    print("()");
    if(exists ClassType superType = klass.superclass){
        print("\n  extends ");
        visitProducedType(superType);
        print("()");
        queueDeclaration(superType.declaration);
    }
    if(klass.interfaces nonempty){
        print("\n  satisfies ");
        variable Boolean once = true;
        for(InterfaceType interf in klass.interfaces){
            if(once){
                once = false;
            }else{
                print("\n   & ");
            }
            visitProducedType(interf);
            queueDeclaration(interf.declaration);
        }
    }
    print(" {\n");
    for(m in klass.members<Anything, Function>()){
        visitFunction(m("ignored"));
    }
    for(v in klass.members<Anything, Value<Anything>>()){
        visitValue(v("ignored"));
    }
    print("}\n");
}

void visitFunction(Function func) {
    print(" ");
    visitProducedType(func.type);
    print(" ``func.name``();\n");
}

void visitValue(Value<Anything> val) {
    print(" ");
    visitProducedType(val.type);
    print(" ``val.name``;\n");
}

void visitInterface(Interface klass){
    print("interface ``klass.name``");
    if(klass.typeParameters nonempty){
        print("<");
        print(",".join(klass.typeParameters.map(function (TypeParameter tp) => tp.name)));
        print(">");
    }
    if(klass.interfaces nonempty){
        print("\n satisfies ");
        variable Boolean once = true;
        for(InterfaceType interf in klass.interfaces){
            if(once){
                once = false;
            }else{
                print("\n   & ");
            }
            visitProducedType(interf);
            queueDeclaration(interf.declaration);
        }
    }
    print(" {\n");
    for(m in klass.members<Anything, Function>()){
        visitFunction(m("ignored"));
    }
    for(v in klass.members<Anything, Value<Anything>>()){
        visitValue(v("ignored"));
    }
    print("}\n");
}

void visitProducedType(ProducedType pt){
    if(is ClassOrInterfaceType pt){
        print(pt.declaration.name);
        variable Boolean once = true;
        if(pt.declaration.typeParameters nonempty){
            print("<");
            for(TypeParameter tp in pt.declaration.typeParameters){
                if(once){
                    once = false;
                }else{
                    print(", ");
                }
                ProducedType? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitProducedType(arg);
                }else{
                    print("Unknown Type Argument");
                }
            }
            print(">");
        }
    }else if(is TypeParameterType pt){
        print(pt.declaration.name);
    }else if(is UnionType pt){
        variable Boolean once = true;
        for(ProducedType type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                print(" | ");
            }
            visitProducedType(type);
        }
    }else if(is IntersectionType pt){
        variable Boolean once = true;
        for(ProducedType type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                print(" & ");
            }
            visitProducedType(type);
        }
    }else if(pt == nothingType){
        print("Nothing");
    }else{
        print("Unsupported type ATM");
    }
}

void visitAppliedProducedType(AppliedProducedType pt){
    if(is AppliedClassOrInterfaceType<Anything> pt){
        print(pt.declaration.name);
        variable Boolean once = true;
        if(pt.declaration.typeParameters nonempty){
            print("<");
            for(TypeParameter tp in pt.declaration.typeParameters){
                if(once){
                    once = false;
                }else{
                    print(", ");
                }
                AppliedProducedType? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitAppliedProducedType(arg);
                }else{
                    print("Unknown Type Argument");
                }
            }
            print(">");
        }
    }else if(is AppliedUnionType pt){
        variable Boolean once = true;
        for(AppliedProducedType type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                print(" | ");
            }
            visitAppliedProducedType(type);
        }
    }else if(is AppliedIntersectionType pt){
        variable Boolean once = true;
        for(AppliedProducedType type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                print(" & ");
            }
            visitAppliedProducedType(type);
        }
    }else if(pt == appliedNothingType){
        print("Nothing");
    }else{
        print("Unsupported type ATM");
    }
}
