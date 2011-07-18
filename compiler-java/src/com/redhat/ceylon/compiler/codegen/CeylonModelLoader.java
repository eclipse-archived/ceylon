package com.redhat.ceylon.compiler.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.TypeKind;
import javax.tools.JavaFileObject.Kind;

import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.BaseType;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.ClassDefinition;
import com.redhat.ceylon.compiler.typechecker.tree.Tree.CompilationUnit;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Attribute.Array;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.PackageSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Symbol.VarSymbol;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.jvm.ClassReader;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;

public class CeylonModelLoader implements ModelCompleter, ModelLoader {
    
    private Symtab symtab;
    private Table names;
    private Map<String, Declaration> declarationsByName = new HashMap<String, Declaration>();
    private ClassReader reader;
    private PhasedUnits phasedUnits;
    private com.redhat.ceylon.compiler.typechecker.context.Context ceylonContext;
    private TypeParser typeParser = new TypeParser();
    private Log log;

    private final Name metadataJavaAttribute; // com.redhat.ceylon.compiler.metadata.java.Attribute

    
    public static CeylonModelLoader instance(Context context) {
        CeylonModelLoader instance = context.get(CeylonModelLoader.class);
        if (instance == null) {
            instance = new CeylonModelLoader(context);
            context.put(CeylonModelLoader.class, instance);
        }
        return instance;
    }

    public CeylonModelLoader(Context context) {
        phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        ceylonContext = LanguageCompiler.getCeylonContextInstance(context);
        symtab = Symtab.instance(context);
        names = Name.Table.instance(context);
        reader = ClassReader.instance(context);
        log = Log.instance(context);

        metadataJavaAttribute = names.fromString("com.redhat.ceylon.compiler.metadata.java.Attribute");
    }

    public void loadRequiredModules(com.sun.tools.javac.util.List<JCCompilationUnit> trees) {
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        PackageSymbol ceylonPkg = reader.enterPackage(names.fromString("ceylon.language"));
        ceylonPkg.complete();
        PackageSymbol javaPkg = reader.enterPackage(names.fromString("java.lang"));
        javaPkg.complete();
        PackageSymbol modelPkg = reader.enterPackage(names.fromString("com.redhat.ceylon.compiler.metadata.java"));
        modelPkg.complete();
        
        /*
         * Eventually this will go away as we get a hook from the typechecker to load on demand, but
         * for now the typechecker requires at least ceylon.language to be loaded 
         */
        for(Symbol m : ceylonPkg.members().getElements()){
            convertToDeclaration(lookupClassSymbol(m.getQualifiedName().toString()));
        }
        
        for(final JCCompilationUnit tree : trees){
            CompilationUnit ceylonTree = ((CeylonCompilationUnit)tree).ceylonTree;
            final String pkgName = tree.getPackageName() != null ? tree.getPackageName().toString() : "";
            
            ceylonTree.visit(new Visitor(){
                
                void loadFromSource(Tree.Declaration decl){
                    String name = decl.getIdentifier().getText();
                    String fqn = pkgName.isEmpty() ? name : pkgName+"."+name;
                    reader.enterClass(names.fromString(fqn), tree.getSourceFile());
                }
                
                @Override
                public void visit(Tree.ClassDefinition that) {
                    loadFromSource(that);
                }
                
                @Override
                public void visit(Tree.InterfaceDefinition that) {
                    loadFromSource(that);
                }
                
                @Override
                public void visit(Tree.ObjectDefinition that) {
                    loadFromSource(that);
                }

                @Override
                public void visit(Tree.MethodDefinition that) {
                    loadFromSource(that);
                }

                @Override
                public void visit(Tree.AttributeDeclaration that) {
                    loadFromSource(that);
                }
            });
        }
    }

    private Declaration convertToDeclaration(ClassSymbol classSymbol) {
        String className = classSymbol.className();
        if(declarationsByName.containsKey(className)){
            return declarationsByName.get(className);
        }
        Declaration decl = makeDeclaration(classSymbol);
        declarationsByName.put(className, decl);
        
        decl.setShared((classSymbol.flags() & Flags.PUBLIC) != 0);
        
        // find its module
        String pkgName = classSymbol.packge().getQualifiedName().toString();
        Module module = findOrCreateModule(pkgName);
        Package pkg = findOrCreatePackage(module, pkgName);
        // and add it there
        pkg.getMembers().add(decl);
        decl.setContainer(pkg);
        
        return decl;
    }

    private Declaration makeDeclaration(ClassSymbol classSymbol) {
        String name = classSymbol.getSimpleName().toString();
        Declaration decl;
        if(isCeylonToplevelAttribute(classSymbol)){
            decl = makeToplevelAttribute(classSymbol);
            decl.setName(unquote(name));
        }else{
            decl = makeLazyClassOrInterface(classSymbol);
            decl.setName(name);
        }
        return decl;
    }

    private String unquote(String name) {
        if (name.startsWith("$")) {
            return name.substring(1);
        }

        return name;
    }

    private boolean isCeylonToplevelAttribute(ClassSymbol classSymbol) {
        Symbol attributeSymbol = symtab.classes.get(metadataJavaAttribute);
        // symbol will be null if not yet encountered => the class cannot be annotated with it anyway
        return attributeSymbol != null && classSymbol.attribute(attributeSymbol) != null;
    }

    private Declaration makeToplevelAttribute(ClassSymbol classSymbol) {
        Value value = new LazyValue(classSymbol, this);
        value.setVariable(true);
        return value;
    }

    private ClassOrInterface makeLazyClassOrInterface(ClassSymbol classSymbol) {
        if(!classSymbol.isInterface()){
            Class klass = new LazyClass(classSymbol, this);
            klass.setAbstract((classSymbol.flags() & Flags.ABSTRACT) != 0);
            return klass;
        }else{
            return new LazyInterface(classSymbol, this);
        }
    }

    private Declaration convertToDeclaration(Type type, Scope scope) {
        String typeName;
        switch(type.getKind()){
        case VOID:    typeName = "java.lang.Void";    break;
        case BOOLEAN: typeName = "java.lang.Boolean"; break;
        case BYTE:    typeName = "java.lang.Byte"; break;
        case CHAR:    typeName = "java.lang.Character"; break;
        case SHORT:   typeName = "java.lang.Short"; break;
        case INT:     typeName = "java.lang.Integer"; break;
        case LONG:    typeName = "java.lang.Long"; break;
        case FLOAT:   typeName = "java.lang.Float"; break;
        case DOUBLE:  typeName = "java.lang.Double"; break;
        case ARRAY:
            Type componentType = ((Type.ArrayType)type).getComponentType();
            //throw new RuntimeException("Array type not implemented");
            //UnionType[Empty|Sequence<Natural>] casetypes 
            // producedtypes.typearguments: typeparam[element]->type[natural]
            TypeDeclaration emptyDecl = (TypeDeclaration)convertToDeclaration("ceylon.language.Empty");
            TypeDeclaration sequenceDecl = (TypeDeclaration)convertToDeclaration("ceylon.language.Sequence");
            UnionType unionType = new UnionType();
            List<ProducedType> caseTypes = new ArrayList<ProducedType>(2);
            caseTypes.add(emptyDecl.getType());
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(1);
            typeArguments.add(getType(componentType, scope));
            caseTypes.add(sequenceDecl.getProducedType(null, typeArguments));
            unionType.setCaseTypes(caseTypes);
            return unionType;
        case DECLARED:
            typeName = type.tsym.getQualifiedName().toString();
            break;
        case TYPEVAR:
            return safeLookupTypeParameter(scope, type.tsym.getQualifiedName().toString());
        case WILDCARD:
            // FIXME: wtf?
            typeName = "ceylon.language.Nothing";
            break;
        default:
            throw new RuntimeException("Failed to handle type "+type);
        }
        return convertToDeclaration(typeName);
    }
    
    private Declaration convertToDeclaration(String typeName) {
        ClassSymbol classSymbol = lookupClassSymbol(typeName);
        if(classSymbol == null)
            throw new RuntimeException("Failed to resolve "+typeName);
        return convertToDeclaration(classSymbol);
    }

    private TypeParameter safeLookupTypeParameter(Scope scope, String name) {
        TypeParameter param = lookupTypeParameter(scope, name);
        if(param == null)
            throw new RuntimeException("Type param "+name+" not found in "+scope);
        return param;
    }
    
    private TypeParameter lookupTypeParameter(Scope scope, String name) {
        if(scope instanceof Method){
            for(TypeParameter param : ((Method) scope).getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            // look it up in its class
            return lookupTypeParameter(scope.getContainer(), name);
        }else if(scope instanceof ClassOrInterface){
            for(TypeParameter param : ((ClassOrInterface) scope).getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            // not found
            return null;
        }else
            throw new RuntimeException("Type param "+name+" lookup not supported for scope "+scope);
    }

    private ClassSymbol lookupClassSymbol(String name) {
        ClassSymbol classSymbol;

        String outerName = name;
        /*
         * This madness here tries to look for a class, and if it fails, tries to resolve it 
         * from its parent class. This is required because a.b.C.D (where D is an inner class
         * of C) is not found in symtab.classes but in C's ClassSymbol.enclosedElements.
         */
        do{
            classSymbol = symtab.classes.get(names.fromString(outerName));
            if(classSymbol != null){
                if(outerName.length() == name.length())
                    return classSymbol;
                else
                    return lookupInnerClass(classSymbol, name.substring(outerName.length()+1).split("\\."));
            }
            int lastDot = outerName.lastIndexOf(".");
            if(lastDot == -1 || lastDot == 0)
                return null;
            outerName = outerName.substring(0, lastDot);
        }while(classSymbol == null);
        return null;
    }

    private ClassSymbol lookupInnerClass(ClassSymbol classSymbol, String[] parts) {
        PART:
            for(String part : parts){
                for(Symbol s : classSymbol.getEnclosedElements()){
                    if(s instanceof ClassSymbol 
                            && s.getSimpleName().toString().equals(part)){
                        classSymbol = (ClassSymbol) s;
                        continue PART;
                    }
                }
                // didn't find the inner class
                return null;
            }
        return classSymbol;
    }

    public Package findOrCreatePackage(Module module, final String pkgName) {
        for(Package pkg : module.getPackages()){
            if(pkg.getNameAsString().equals(pkgName))
                return pkg;
        }
        Package pkg = new Package(){
            @Override
            public Declaration getDirectMember(String name) {
                // FIXME: some refactoring needed
                String className = pkgName.isEmpty() ? name : pkgName + "." + name;
                // we need its package ready first
                PackageSymbol javaPkg = reader.enterPackage(names.fromString(pkgName));
                javaPkg.complete();
                ClassSymbol classSymbol = lookupClassSymbol(className);
                // only get it from the classpath if we're not compiling it
                if(classSymbol != null && classSymbol.classfile.getKind() != Kind.SOURCE)
                    return convertToDeclaration(className);
                return super.getDirectMember(name);
            }
            @Override
            public Declaration getDirectMemberOrParameter(String name) {
                // FIXME: what's the difference?
                return getDirectMember(name);
            }
        };
        pkg.setModule(module);
        // FIXME: some refactoring needed
        pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));
        module.getPackages().add(pkg);
        return pkg;
    }

    public Module findOrCreateModule(String pkgName) {
        java.util.List<String> moduleName;
        // FIXME: this is a rather simplistic view of the world
        if(pkgName == null)
            moduleName = Arrays.asList("<default module>");
        else if(pkgName.startsWith("java."))
            moduleName = Arrays.asList("java");
        else if(pkgName.startsWith("sun."))
            moduleName = Arrays.asList("sun");
        else
            moduleName = Arrays.asList(pkgName.split("\\."));
         Module module = phasedUnits.getModuleBuilder().getOrCreateModule(moduleName);
         // make sure that when we load the ceylon language module we set it to where
         // the typechecker will look for it
         if(pkgName != null
                 && pkgName.startsWith("ceylon.language.")
                 && ceylonContext.getModules().getLanguageModule() == null){
             ceylonContext.getModules().setLanguageModule(module);
         }
         // FIXME: this can't be that easy.
         module.setAvailable(true);
         return module;
    }

    private ProducedType getType(Type type, Scope scope) {
        TypeDeclaration declaration = (TypeDeclaration) convertToDeclaration(type, scope);
        com.sun.tools.javac.util.List<Type> javacTypeArguments = type.getTypeArguments();
        if(!javacTypeArguments.isEmpty()){
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(javacTypeArguments.size());
            for(Type typeArgument : javacTypeArguments){
                typeArguments.add((ProducedType) getType(typeArgument, scope));
            }
            return declaration.getProducedType(null, typeArguments);
        }
        return declaration.getType();
    }

    //
    // ModelCompleter
    
    @Override
    public void complete(LazyInterface iface) {
        complete(iface, iface.classSymbol);
    }

    @Override
    public void complete(LazyClass klass) {
        complete(klass, klass.classSymbol);
    }

    private void complete(ClassOrInterface klass, ClassSymbol classSymbol) {
        // FIXME: deal with toplevel methods and attributes
        // do its type parameters first
        setTypeParameters(klass, classSymbol);
        int constructorCount = 0;
        // then its methods
        for(Symbol member : classSymbol.getEnclosedElements()){
            // FIXME: deal with multiple constructors
            // FIXME: could be an attribute
            if(member instanceof MethodSymbol){
                MethodSymbol methodSymbol = (MethodSymbol) member;
                
                if(methodSymbol.isStatic())
                    continue;
                // FIXME: temporary, because some private classes from the jdk are referenced in private methods but not
                // available
                if(classSymbol.getQualifiedName().toString().startsWith("java.")
                        && (methodSymbol.flags() & Flags.PUBLIC) == 0)
                    continue;

                if(methodSymbol.isConstructor()){
                    constructorCount++;
                    // ignore the non-first ones
                    if(constructorCount > 1){
                        // only warn once
                        if(constructorCount == 2)
                            log.rawWarning(0, "Has multiple constructors: "+classSymbol.getQualifiedName());
                        continue;
                    }
                    setParameters((Class)klass, methodSymbol);
                    continue;
                }
                
                // normal method
                Method method = new Method();
                
                method.setShared((methodSymbol.flags() & Flags.PUBLIC) != 0);
                method.setContainer(klass);
                method.setName(methodSymbol.name.toString());
                klass.getMembers().add(method);
                
                // type params first
                setTypeParameters(method, methodSymbol);

                // now its parameters
                setParameters(method, methodSymbol);
                // FIXME: deal with type override by annotations
                method.setType(getType(methodSymbol.getReturnType(), method));
            }
        }
        if(klass instanceof Class && constructorCount == 0){
            // must be a default constructor
            ((Class)klass).setParameterList(new ParameterList());
        }
        
        setExtendedType(klass, classSymbol);
        setSatisfiedTypes(klass, classSymbol);
    }
    
    private void setExtendedType(ClassOrInterface klass, ClassSymbol classSymbol) {
        // look at its super type
        Type superClass = classSymbol.getSuperclass();
        ProducedType extendedType = null;
        
        if(klass instanceof Interface){
            // interfaces need to have their superclass set to Object
            if(superClass.getKind() == TypeKind.NONE)
                extendedType = getType("ceylon.language.Object", klass);
            else
                extendedType = getType(superClass, klass);
        }else{
            String className = classSymbol.getQualifiedName().toString();
            if(className.equals("ceylon.language.Void")){
                // ceylon.language.Void has no super type
            }else if(className.equals("java.lang.Object")){
                // we pretend its superclass is something else, but note that in theory we shouldn't 
                // be seeing j.l.Object at all due to unerasure
                extendedType = getType("ceylon.language.IdentifiableObject", klass);
            }else{
                // now deal with type erasure, avoid having Object as superclass
                String superClassName = superClass.tsym.getQualifiedName().toString();
                if(superClassName.equals("java.lang.Object")){
                    // FIXME: deal with @TypeInfo
                    extendedType = getType("ceylon.language.IdentifiableObject", klass);
                }else{
                    extendedType = getType(superClass, klass);
                }
            }
        }
        if(extendedType != null)
            klass.setExtendedType(extendedType);
    }

    private void setParameters(Functional klass, MethodSymbol methodSymbol) {
        ParameterList parameters = new ParameterList();
        klass.addParameterList(parameters);
        for(VarSymbol paramSymbol : methodSymbol.params()){
            ValueParameter parameter = new ValueParameter();
            parameter.setContainer((Scope) klass);
            String paramName = getAnnotationStringValue(paramSymbol, "com.redhat.ceylon.compiler.metadata.java.Name");
            // use whatever param name we find as default
            if(paramName == null)
                parameter.setName(paramSymbol.name.toString());
            // FIXME: deal with type override by annotations
            parameter.setType(getType(paramSymbol.type, (Scope) klass));
            parameters.getParameters().add(parameter);
        }
    }

    @Override
    public void complete(LazyValue value) {
        Type type = null;
        for(Symbol member : value.classSymbol.members().getElements()){
            if(member instanceof MethodSymbol){
                MethodSymbol method = (MethodSymbol) member;
                if(method.name.toString().equals(Util.getGetterName(value.getName()))
                        && method.isStatic()
                        && method.params().size() == 0){
                    type = method.getReturnType();
                    break;
                }
            }
        }
        if(type == null)
            throw new RuntimeException("Failed to find type for toplevel attribute "+value.getName());
        value.setType(getType(type, null));
    }

    //
    // Utils for loading type info from the model
    
    private Compound getAnnotation(Symbol symbol, String name) {
        com.sun.tools.javac.util.List<Compound> annotations = symbol.getAnnotationMirrors();
        for(Compound annotation : annotations){
            if(annotation.type.tsym.getQualifiedName().toString().equals(name))
                return annotation;
        }
        return null;
    }

    private Array getAnnotationArrayValue(Symbol symbol, String name) {
        Compound annotation = getAnnotation(symbol, name);
        if(annotation != null)
            return (Array)annotation.member(names.fromString("value"));
        return null;
    }

    private String getAnnotationStringValue(Symbol symbol, String name) {
        Compound annotation = getAnnotation(symbol, name);
        if(annotation != null)
            return (String)annotation.member(names.fromString("value")).getValue();
        return null;
    }

    //
    // Satisfied Types
    
    private Array getSatisfiedTypesFromAnnotations(Symbol symbol) {
        return getAnnotationArrayValue(symbol, "com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes");
    }
    
    private void setSatisfiedTypes(ClassOrInterface klass, ClassSymbol classSymbol) {
        Array satisfiedTypes = getSatisfiedTypesFromAnnotations(classSymbol);
        if(satisfiedTypes != null){
            klass.getSatisfiedTypes().addAll(getSatisfiedTypes(satisfiedTypes, klass));
        }else{
            for(Type iface : classSymbol.getInterfaces()){
                klass.getSatisfiedTypes().add(getType(iface, klass));
            }
        }
    }

    private Collection<? extends ProducedType> getSatisfiedTypes(Array satisfiedTypes, Scope scope) {
        List<ProducedType> producedTypes = new LinkedList<ProducedType>();
        for(Attribute type : satisfiedTypes.values){
            producedTypes.add(decodeType((String) type.getValue(), scope));
        }
        return producedTypes;
    }

    //
    // Type parameters loading

    private Array getTypeParametersFromAnnotations(Symbol symbol) {
        return getAnnotationArrayValue(symbol, "com.redhat.ceylon.compiler.metadata.java.TypeParameters");
    }

    // from our annotation
    private void setTypeParameters(Scope scope, List<TypeParameter> params, Array typeParameters) {
        for(Attribute attribute : typeParameters.values){
            Compound typeParam = (Compound) attribute;
            TypeParameter param = new TypeParameter();
            param.setContainer(scope);
            param.setName((String)typeParam.member(names.fromString("value")).getValue());
            params.add(param);
            
            Attribute varianceAttribute = typeParam.member(names.fromString("variance"));
            if(varianceAttribute != null){
                VarSymbol variance = (VarSymbol) varianceAttribute.getValue();
                String varianceName = variance.name.toString();
                if(varianceName.equals("IN")){
                    param.setContravariant(true);
                }else if(varianceName.equals("OUT"))
                    param.setCovariant(true);
            }
            
            // FIXME: I'm pretty sure we can have bounds that refer to method 
            // params, so we need to do this in two phases
            Attribute satisfiesAttribute = typeParam.member(names.fromString("satisfies"));
            if(satisfiesAttribute != null){
                String satisfies = (String) satisfiesAttribute.getValue();
                if(!satisfies.isEmpty()){
                    ProducedType satisfiesType = decodeType(satisfies, scope);
                    param.getSatisfiedTypes().add(satisfiesType);
                }
            }
        }
    }

    // from java type info
    private void setTypeParameters(Scope scope, List<TypeParameter> params, com.sun.tools.javac.util.List<TypeSymbol> typeParameters) {
        for(TypeSymbol typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setContainer(scope);
            param.setName(typeParam.name.toString());
            params.add(param);
            
            // FIXME: I'm pretty sure we can have bounds that refer to method 
            // params, so we need to do this in two phases
            if(!typeParam.getBounds().isEmpty()){
                for(Type bound : typeParam.getBounds()){
                    // we turn java's default upper bound java.lang.Object into ceylon.language.Object
                    if(bound.tsym == symtab.objectType.tsym)
                        bound = symtab.ceylonObjectType;
                    param.getSatisfiedTypes().add(getType(bound, scope));
                }
            }
        }
    }

    // method
    private void setTypeParameters(Method method, MethodSymbol methodSymbol) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        method.setTypeParameters(params);
        Array typeParameters = getTypeParametersFromAnnotations(methodSymbol);
        if(typeParameters != null)
            setTypeParameters(method, params, typeParameters);
        else
            setTypeParameters(method, params, methodSymbol.getTypeParameters());
    }

    // class
    private void setTypeParameters(ClassOrInterface klass, ClassSymbol classSymbol) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        klass.setTypeParameters(params);
        Array typeParameters = getTypeParametersFromAnnotations(classSymbol);
        if(typeParameters != null)
            setTypeParameters(klass, params, typeParameters);
        else
            setTypeParameters(klass, params, classSymbol.getTypeParameters());
    }        

    //
    // TypeParsing and ModelLoader

    private ProducedType decodeType(String value, Scope scope) {
        return typeParser .decodeType(value, scope, this);
    }

    @Override
    public ProducedType getType(String name, Scope scope) {
        if(scope != null){
            TypeParameter typeParameter = lookupTypeParameter(scope, name);
            if(typeParameter != null)
                return typeParameter.getType();
        }
        return ((TypeDeclaration)convertToDeclaration(name)).getType();
    }

}
