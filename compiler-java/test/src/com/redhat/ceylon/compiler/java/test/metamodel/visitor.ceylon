import ceylon.language.metamodel { 
    type,
    Type,
    Class,
    Interface,
    ClassOrInterface,
    UnionType,
    IntersectionType,
    appliedNothingType = nothingType
}
import ceylon.language.metamodel.declaration { ... }

void visitStringHierarchy(){
    value classType = type("falbala");
    
    doc "metamodel is Class"
    assert(is Class<Anything,[String]> classType);
    
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

void visitInheritedTypes(ClassOrInterface<Anything> type){
    output("Types extended by ``type.declaration.name``:\n");
    if(exists xt = type.superclass){
        visitExtendedTypes(xt);
    }
    output("Types satisfied by ``type.declaration.name``:\n");
    for(sat in type.interfaces){
        visitSatisfiedTypes(sat);
    }
}

void visitExtendedTypes(Class<Anything, Nothing> type){
    visitType(type);
    output("\n");
    if(exists xt = type.superclass){
        visitExtendedTypes(xt);
    }
}

void visitSatisfiedTypes(Interface<Anything> type){
    visitType(type);
    output("\n");
    for(sat in type.interfaces){
        visitSatisfiedTypes(sat);
    }
}

variable ClassOrInterfaceDeclaration[] queue = {};

void emptyQueue(){
    while(nonempty q = queue){
        visitDeclaration(q.first);
        queue = queue.rest;
    }
}

void queueDeclaration(ClassOrInterfaceDeclaration decl){
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
    if(is ClassDeclaration decl){
        visitClass(decl);
    }else if(is InterfaceDeclaration decl){
        visitInterface(decl);
    }else{
        output("Unknown declaration");
    }
}

void visitClass(ClassDeclaration klass){
    output("class ``klass.name``");
    if(klass.typeParameters nonempty){
        output("<");
        output(",".join(klass.typeParameters.map(function (TypeParameter tp) => tp.name)));
        output(">");
    }
    output("(");
    variable Boolean onceParameter = true;
    for(param in klass.parameters){
        if(onceParameter){
            onceParameter = false;
        }else{
            output(", ");
        }
        visitOpenType(param.type);
        output(" ``param.name``");
    }
    output(")");
    if(exists superType = klass.superclass){
        output("\n  extends ");
        visitOpenType(superType);
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
            visitOpenType(interf);
            queueDeclaration(interf.declaration);
        }
    }
    output(" {\n");
    visitMembers(klass);
    output("}\n");
}

void visitMembers(ClassOrInterfaceDeclaration decl){
    for(m in decl.members<Declaration>()){
        switch(m)
        case(is FunctionDeclaration){
            visitFunction(m);
        }
        case(is AttributeDeclaration){
            visitValue(m);
        }
        case(is ClassDeclaration){
            visitClass(m);
        }
        case(is InterfaceDeclaration){
            visitInterface(m);
        }
    }
}
void visitFunction(FunctionDeclaration func) {
    output(" ");
    visitOpenType(func.type);
    output(" ``func.name``");
    for(paramList in func.parameterLists){
        variable Boolean onceParameter = true;
        output("(");
        for(param in paramList){
            if(onceParameter){
                onceParameter = false;
            }else{
                output(", ");
            }
            visitOpenType(param.type);
            output(" ``param.name``");
        }
        output(")");
    }
    output(";\n");
}

void visitValue(AttributeDeclaration val) {
    output(" ");
    visitOpenType(val.type);
    output(" ``val.name``;\n");
}

void visitInterface(InterfaceDeclaration klass){
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
            visitOpenType(interf);
            queueDeclaration(interf.declaration);
        }
    }
    output(" {\n");
    visitMembers(klass);
    output("}\n");
}

void visitOpenType(OpenType pt){
    if(is OpenParameterisedType<ClassOrInterfaceDeclaration> pt){
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
                OpenType? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitOpenType(arg);
                }else{
                    output("Unknown Type Argument");
                }
            }
            output(">");
        }
    }else if(is OpenTypeVariable pt){
        output(pt.declaration.name);
    }else if(is OpenUnion pt){
        variable Boolean once = true;
        for(OpenType type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                output(" | ");
            }
            visitOpenType(type);
        }
    }else if(is OpenIntersection pt){
        variable Boolean once = true;
        for(OpenType type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                output(" & ");
            }
            visitOpenType(type);
        }
    }else if(pt == nothingType){
        output("Nothing");
    }else{
        output("Unsupported type ATM: ``pt``");
    }
}

void visitType(Type pt){
    if(is ClassOrInterface<Anything> pt){
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
                    visitType(arg);
                }else{
                    output("Unknown Type Argument");
                }
            }
            output(">");
        }
    }else if(is UnionType pt){
        variable Boolean once = true;
        for(Type type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                output(" | ");
            }
            visitType(type);
        }
    }else if(is IntersectionType pt){
        variable Boolean once = true;
        for(Type type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                output(" & ");
            }
            visitType(type);
        }
    }else if(pt == appliedNothingType){
        output("Nothing");
    }else{
        output("Unsupported type ATM: ``pt``");
    }
}
