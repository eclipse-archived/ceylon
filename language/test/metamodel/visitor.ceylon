import ceylon.language.meta { type }
import ceylon.language.meta.model { 
    Type,
    Class,
    ClassModel,
    Interface,
    InterfaceModel,
    ClassOrInterface,
    UnionType,
    IntersectionType,
    appliedNothingType = nothingType
}
import ceylon.language.meta.declaration { ... }

void visitStringHierarchy(){
    value classType = type("falbala");
    
    "metamodel is Class"
    assert(is Class<Anything,[String]> classType);
    
    value klass = classType.declaration;
    
    "metamodel class name is String"
    assert(klass.name == "String");
    
    value st = klass.extendedType;
    
    "super class exists"
    assert(exists st);
    
    "metamodel superclass name is Object"
    assert(st.declaration.name == "Object");
    
    queue = [klass];
    emptyQueue();
    
    visitInheritedTypes(classType);
}

void visitInheritedTypes(ClassOrInterface<Anything> type){
    output("Types extended by ``type.declaration.name``:\n");
    if(exists xt = type.extendedType){
        visitExtendedTypes(xt,0);
    }
    output("Types satisfied by ``type.declaration.name``:\n");
    for(sat in type.satisfiedTypes){
        visitSatisfiedTypes(sat,0);
    }
}

void visitExtendedTypes(ClassModel<Anything, Nothing> type, Integer level){
    visitType(type,level);
    output("\n");
    if(exists xt = type.extendedType){
        visitExtendedTypes(xt,level+2);
    }
}

void visitSatisfiedTypes(InterfaceModel<Anything> type, Integer level){
    visitType(type,level);
    output("\n");
    for(sat in type.satisfiedTypes){
        visitSatisfiedTypes(sat,level+2);
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
    if(klass.typeParameterDeclarations nonempty){
        output("<");
        output(",".join(klass.typeParameterDeclarations.map(function (TypeParameter tp) => tp.name)));
        output(">");
    }
    output("(");
    variable Boolean onceParameter = true;
    for(param in klass.parameterDeclarations){
        if(onceParameter){
            onceParameter = false;
        }else{
            output(", ");
        }
        visitOpenType(param.openType);
        output(" ``param.name``");
    }
    output(")");
    if(exists superType = klass.extendedType){
        output("\n  extends ");
        visitOpenType(superType);
        output("()");
        queueDeclaration(superType.declaration);
    }
    if(klass.satisfiedTypes nonempty){
        output("\n  satisfies ");
        variable Boolean once = true;
        for(interf in klass.satisfiedTypes){
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
    for(m in decl.memberDeclarations<NestableDeclaration>()){
        switch(m)
        case(is FunctionDeclaration){
            visitFunction(m);
        }
        case(is ValueDeclaration){
            visitValue(m);
        }
        case(is SetterDeclaration){
            // FIXME: implement
        }
        case(is ClassDeclaration){
            visitClass(m);
        }
        case(is InterfaceDeclaration){
            visitInterface(m);
        }
        case(is AliasDeclaration){
            // FIXME: implement
        }
    }
}
void visitFunction(FunctionDeclaration func) {
    output(" ");
    visitOpenType(func.openType);
    output(" ``func.name``(");
    variable Boolean onceParameter = true;
    output("(");
    for(param in func.parameterDeclarations){
        if(onceParameter){
            onceParameter = false;
        }else{
            output(", ");
        }
        visitOpenType(param.openType);
        output(" ``param.name``");
    }
    output(");\n");
}

void visitValue(ValueDeclaration val) {
    output(" ");
    visitOpenType(val.openType);
    output(" ``val.name``;\n");
}

void visitInterface(InterfaceDeclaration klass){
    output("interface ``klass.name``");
    if(klass.typeParameterDeclarations nonempty){
        output("<");
        output(",".join(klass.typeParameterDeclarations.map(function (TypeParameter tp) => tp.name)));
        output(">");
    }
    if(klass.satisfiedTypes nonempty){
        output("\n satisfies ");
        variable Boolean once = true;
        for(interf in klass.satisfiedTypes){
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
    if(is OpenClassOrInterfaceType pt){
        output(pt.declaration.name);
        variable Boolean once = true;
        if(pt.declaration.typeParameterDeclarations nonempty){
            output("<");
            for(TypeParameter tp in pt.declaration.typeParameterDeclarations){
                if(once){
                    once = false;
                }else{
                    output(", ");
                }
                OpenType? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitOpenType(arg);
                }else{
                    output("Unknown open Type Argument");
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
        output("Unsupported open type ATM: ``pt``");
    }
}

void visitType(Type<Anything> pt,Integer level){
    if(is ClassOrInterface<Anything> pt){
        if (level>0){
          process.write(" ".repeat(level));
        }
        output(pt.declaration.name);
        variable Boolean once = true;
        if(pt.declaration.typeParameterDeclarations nonempty){
            output("<");
            for(TypeParameter tp in pt.declaration.typeParameterDeclarations){
                if(once){
                    once = false;
                }else{
                    output(", ");
                }
                Type<Anything>? arg = pt.typeArguments.get(tp);
                if(exists arg){
                    visitType(arg,0);
                }else{
                    output("Unknown closed Type Argument");
                }
            }
            output(">");
        }
    }else if(is UnionType<Anything> pt){
        variable Boolean once = true;
        for(Type<Anything> type in pt.caseTypes){
            if(once){
                once = false;
            }else{
                output(" | ");
            }
            visitType(type,level+2);
        }
    }else if(is IntersectionType<Anything> pt){
        variable Boolean once = true;
        for(Type<Anything> type in pt.satisfiedTypes){
            if(once){
                once = false;
            }else{
                output(" & ");
            }
            visitType(type,level+2);
        }
    }else if(pt == appliedNothingType){
        output("Nothing");
    }else{
        output("Unsupported closed type ATM: ``pt``");
    }
}
