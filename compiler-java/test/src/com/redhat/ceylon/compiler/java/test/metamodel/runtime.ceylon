shared class NoParams(){
    shared NoParams noParams() => NoParams();
    shared NoParams fixedParams(String s, Integer i) => NoParams();
    shared NoParams typeParams<T>(T s, Integer i) => NoParams();
    
    shared Integer i = 2;
    shared String s = "hello";
    shared NoParams o => this;
}

shared class FixedParams(String s, Integer i){}

shared class TypeParams<T>(T s, Integer i){}

@nomodel
shared void runtime() {
    value classType = type("falbala");
    
    doc "metamodel is AppliedClassType"
    assert(is AppliedClassType<Anything,[]> classType);

    value klass = classType.declaration;

    doc "metamodel class name is String"
    assert(klass.name == "String");

    value st = klass.superclass;

    doc "super class exists"
    assert(exists st);

    doc "metamodel superclass name is Object"
    assert(st.declaration.name == "Object");

    queue = [klass];
    emptyQueue();
    
    visitInheritedTypes(classType);
    
    value noParamsType = type(NoParams());
    assert(is AppliedClassType<NoParams, []> noParamsType);
    value noParamsClass = noParamsType.declaration;
    // not sure how to do this ATM
    //value noParamsType2 = noParamsClass.apply();
    //assert(is ClassType<NoParams, []> noParamsType2);
    value noParams = noParamsType();
    print(noParams);
    print("\n");
    
    value noParamsInstance = NoParams();
    if(is AppliedFunction<NoParams, [NoParams]> f = noParamsType.getFunction("noParams")){
        print("invocation1: `` f(noParamsInstance) ``\n");
        if(is Callable<NoParams, []> bf = f.bind(noParamsInstance)){
            print("invocation1: `` bf() ``\n");
        }
    }
    if(is AppliedFunction<NoParams, [NoParams, String, Integer]> f = noParamsType.getFunction("fixedParams")){
        print("invocation2: `` f(noParamsInstance, "a", 1) ``\n");
        if(is Callable<NoParams, [String, Integer]> bf = f.bind(noParamsInstance)){
            print("invocation2: `` bf("a", 1) ``\n");
        }
    }
    if(is AppliedFunction<NoParams, [NoParams, String, Integer]> f = noParamsType.getFunction("typeParams", classType)){
        print("invocation3: `` f(noParamsInstance, "a", 1) ``\n");
        if(is Callable<NoParams, [String, Integer]> bf = f.bind(noParamsInstance)){
            print("invocation3: `` bf("a", 1) ``\n");
        }
    }
    if(is AppliedValue<Integer> a = noParamsType.getAttribute("i")){
        print("attribute i: `` a.get(noParamsInstance) ``\n");
    }
    if(is AppliedValue<String> a = noParamsType.getAttribute("s")){
        print("attribute s: `` a.get(noParamsInstance) ``\n");
    }
    if(is AppliedValue<NoParams> a = noParamsType.getAttribute("o")){
        print("attribute o: `` a.get(noParamsInstance) ``\n");
    }

    value fixedParamsType = type(FixedParams("a", 1));
    assert(is AppliedClassType<FixedParams, [String, Integer]> fixedParamsType);
    value fixedParamsClass = fixedParamsType.declaration;
    value fixedParams = fixedParamsType("b", 2);
    print(fixedParams);
    print("\n");

    value typeParamsType = type(TypeParams("a", 1));
    assert(is AppliedClassType<TypeParams<String>, [String, Integer]> typeParamsType);
    value typeParamsClass = typeParamsType.declaration;
    value typeParams = typeParamsType("b", 2);
    print(typeParams);
    print("\n");
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
