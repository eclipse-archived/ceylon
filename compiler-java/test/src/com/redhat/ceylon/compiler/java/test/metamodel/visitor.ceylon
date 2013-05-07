import ceylon.language.metamodel { 
    type,
    AppliedType,
    AppliedClass = Class,
    AppliedInterface = Interface,
    AppliedClassOrInterface = ClassOrInterface,
    AppliedUnionType = UnionType,
    AppliedIntersectionType = IntersectionType,
    appliedNothingType = nothingType
}
import ceylon.language.metamodel.untyped { ... }

void visitStringHierarchy(){
    value classType = type("falbala");
    
    doc "metamodel is AppliedClass"
    assert(is AppliedClass<Anything,[]> classType);
    
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
}

void visitInheritedTypes(AppliedClassOrInterface<Anything> type){
    output("Types extended by ``type.declaration.name``:\n");
    if(exists xt = type.superclass){
        visitExtendedTypes(xt);
    }
    output("Types satisfied by ``type.declaration.name``:\n");
    for(sat in type.interfaces){
        visitSatisfiedTypes(sat);
    }
}

void visitExtendedTypes(AppliedClass<Anything, Nothing> type){
    visitAppliedProducedType(type);
    output("\n");
    if(exists xt = type.superclass){
        visitExtendedTypes(xt);
    }
}

void visitSatisfiedTypes(AppliedInterface<Anything> type){
    visitAppliedProducedType(type);
    output("\n");
    for(sat in type.interfaces){
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

void output(Anything obj){
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
        output("Unknown declaration");
    }
}

void visitClass(Class klass){
    output("class ``klass.name``");
    if(klass.typeParameters nonempty){
        output("<");
        output(",".join(klass.typeParameters.map(function (TypeParameter tp) => tp.name)));
        output(">");
    }
    output("()");
    if(exists superType = klass.superclass){
        output("\n  extends ");
        visitProducedType(superType);
        output("()");
        queueDeclaration(superType.declaration);
    }
    if(klass.interfaces nonempty){
        output("\n  satisfies ");
        variable Boolean once = true;
        for(interf in klass.interfaces){
            if(once){
                once = false;
            }else{
                output("\n   & ");
            }
            visitProducedType(interf);
            queueDeclaration(interf.declaration);
        }
    }
    output(" {\n");
    for(m in klass.members<Function>()){
        visitFunction(m());
    }
    for(v in klass.members<Value>()){
        visitValue(v());
    }
    output("}\n");
}

void visitFunction(Function func) {
    output(" ");
    visitProducedType(func.type);
    output(" ``func.name``();\n");
}

void visitValue(Value val) {
    output(" ");
    visitProducedType(val.type);
    output(" ``val.name``;\n");
}

void visitInterface(Interface klass){
    output("interface ``klass.name``");
    if(klass.typeParameters nonempty){
        output("<");
        output(",".join(klass.typeParameters.map(function (TypeParameter tp) => tp.name)));
        output(">");
    }
    if(klass.interfaces nonempty){
        output("\n satisfies ");
        variable Boolean once = true;
        for(interf in klass.interfaces){
            if(once){
                once = false;
            }else{
                output("\n   & ");
            }
            visitProducedType(interf);
            queueDeclaration(interf.declaration);
        }
    }
    output(" {\n");
    for(m in klass.members<Function>()){
        visitFunction(m());
    }
    for(v in klass.members<Value>()){
        visitValue(v());
    }
    output("}\n");
}

void visitProducedType(Type pt){
    if(is ParameterisedType<ClassOrInterface> pt){
        output(pt.declaration.name);
        variable Boolean once = true;
        if(pt.declaration.typeParameters nonempty){
            output("<");
            for(TypeParameter tp in pt.declaration.typeParameters){
                if(once){
                    once = false;
                }else{
                    output(", ");
                }
                Type? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitProducedType(arg);
                }else{
                    output("Unknown Type Argument");
                }
            }
            output(">");
        }
    }else if(is TypeParameterType pt){
        output(pt.declaration.name);
    }else if(is UnionType pt){
        variable Boolean once = true;
        for(Type type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                output(" | ");
            }
            visitProducedType(type);
        }
    }else if(is IntersectionType pt){
        variable Boolean once = true;
        for(Type type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                output(" & ");
            }
            visitProducedType(type);
        }
    }else if(pt == nothingType){
        output("Nothing");
    }else{
        output("Unsupported type ATM: ``type``");
    }
}

void visitAppliedProducedType(AppliedType pt){
    if(is AppliedClassOrInterface<Anything> pt){
        output(pt.declaration.name);
        variable Boolean once = true;
        if(pt.declaration.typeParameters nonempty){
            output("<");
            for(TypeParameter tp in pt.declaration.typeParameters){
                if(once){
                    once = false;
                }else{
                    output(", ");
                }
                AppliedType? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitAppliedProducedType(arg);
                }else{
                    output("Unknown Type Argument");
                }
            }
            output(">");
        }
    }else if(is AppliedUnionType pt){
        variable Boolean once = true;
        for(AppliedType type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                output(" | ");
            }
            visitAppliedProducedType(type);
        }
    }else if(is AppliedIntersectionType pt){
        variable Boolean once = true;
        for(AppliedType type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                output(" & ");
            }
            visitAppliedProducedType(type);
        }
    }else if(pt == appliedNothingType){
        output("Nothing");
    }else{
        output("Unsupported type ATM");
    }
}
