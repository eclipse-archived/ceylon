/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.redhat.ceylon.compiler.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.lang.model.type.TypeKind;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.util.Timer;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.PackageMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.compiler.loader.mirror.VariableMirror;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyClassAlias;
import com.redhat.ceylon.compiler.loader.model.LazyContainer;
import com.redhat.ceylon.compiler.loader.model.LazyElement;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyInterfaceAlias;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.loader.model.LazyPackage;
import com.redhat.ceylon.compiler.loader.model.LazyTypeAlias;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.analyzer.DeclarationVisitor;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Element;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ModuleImport;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeAlias;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;

/**
 * Abstract class of a model loader that can load a model from a compiled Java representation,
 * while being agnostic of the reflection API used to load the compiled Java representation.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class AbstractModelLoader implements ModelCompleter, ModelLoader {

    public static final String CEYLON_LANGUAGE = "ceylon.language";
    
    private static final String TIMER_MODEL_LOADER_CATEGORY = "model loader";
    public static final String JDK_MODULE_VERSION = "7";
    
    public static final String CEYLON_CEYLON_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ceylon";
    private static final String CEYLON_MODULE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Module";
    private static final String CEYLON_PACKAGE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Package";
    private static final String CEYLON_IGNORE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ignore";
    private static final String CEYLON_CLASS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Class";
    public static final String CEYLON_NAME_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Name";
    private static final String CEYLON_SEQUENCED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Sequenced";
    private static final String CEYLON_DEFAULTED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Defaulted";
    private static final String CEYLON_SATISFIED_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes";
    private static final String CEYLON_CASE_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CaseTypes";
    private static final String CEYLON_TYPE_PARAMETERS = "com.redhat.ceylon.compiler.java.metadata.TypeParameters";
    private static final String CEYLON_TYPE_INFO_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeInfo";
    public static final String CEYLON_ATTRIBUTE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Attribute";
    public static final String CEYLON_OBJECT_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Object";
    public static final String CEYLON_METHOD_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Method";
    public static final String CEYLON_CONTAINER_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Container";
    private static final String CEYLON_MEMBERS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Members";
    private static final String CEYLON_ANNOTATIONS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Annotations";
    public static final String CEYLON_VALUETYPE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.ValueType";
    public static final String CEYLON_ALIAS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Alias";
    public static final String CEYLON_TYPE_ALIAS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeAlias";

    private static final TypeMirror OBJECT_TYPE = simpleObjectType("java.lang.Object");
    private static final TypeMirror CEYLON_OBJECT_TYPE = simpleObjectType("ceylon.language.Object");
    private static final TypeMirror CEYLON_BASIC_TYPE = simpleObjectType("ceylon.language.Basic");
    private static final TypeMirror CEYLON_EXCEPTION_TYPE = simpleObjectType("ceylon.language.Exception");
    private static final TypeMirror CEYLON_REIFIED_TYPE_TYPE = simpleObjectType("com.redhat.ceylon.compiler.java.runtime.model.ReifiedType");
    
    private static final TypeMirror STRING_TYPE = simpleObjectType("java.lang.String");
    private static final TypeMirror CEYLON_STRING_TYPE = simpleObjectType("ceylon.language.String");
    
    private static final TypeMirror PRIM_BOOLEAN_TYPE = simpleObjectType("boolean", TypeKind.BOOLEAN);
    private static final TypeMirror BOOLEAN_TYPE = simpleObjectType("java.lang.Boolean");
    private static final TypeMirror CEYLON_BOOLEAN_TYPE = simpleObjectType("ceylon.language.Boolean");
    
    private static final TypeMirror PRIM_BYTE_TYPE = simpleObjectType("byte", TypeKind.BYTE);
    private static final TypeMirror BYTE_TYPE = simpleObjectType("java.lang.Byte");
    private static final TypeMirror PRIM_SHORT_TYPE = simpleObjectType("short", TypeKind.SHORT);
    private static final TypeMirror SHORT_TYPE = simpleObjectType("java.lang.Short");

    private static final TypeMirror PRIM_INT_TYPE = simpleObjectType("int", TypeKind.INT);
    private static final TypeMirror INTEGER_TYPE = simpleObjectType("java.lang.Integer");
    private static final TypeMirror PRIM_LONG_TYPE = simpleObjectType("long", TypeKind.LONG);
    private static final TypeMirror LONG_TYPE = simpleObjectType("java.lang.Long");
    private static final TypeMirror CEYLON_INTEGER_TYPE = simpleObjectType("ceylon.language.Integer");
    
    private static final TypeMirror PRIM_FLOAT_TYPE = simpleObjectType("float", TypeKind.FLOAT);
    private static final TypeMirror FLOAT_TYPE = simpleObjectType("java.lang.Float");
    private static final TypeMirror PRIM_DOUBLE_TYPE = simpleObjectType("double", TypeKind.DOUBLE);
    private static final TypeMirror DOUBLE_TYPE = simpleObjectType("java.lang.Double");
    private static final TypeMirror CEYLON_FLOAT_TYPE = simpleObjectType("ceylon.language.Float");
    
    private static final TypeMirror PRIM_CHAR_TYPE = simpleObjectType("char", TypeKind.CHAR);
    private static final TypeMirror CHARACTER_TYPE = simpleObjectType("java.lang.Character");
    private static final TypeMirror CEYLON_CHARACTER_TYPE = simpleObjectType("ceylon.language.Character");
    
    private static final TypeMirror CEYLON_ARRAY_TYPE = simpleObjectType("ceylon.language.Array");

    private static TypeMirror simpleObjectType(String name) {
        return new SimpleReflType(name, TypeKind.DECLARED);
    }

    private static TypeMirror simpleObjectType(String name, TypeKind kind) {
        return new SimpleReflType(name, TypeKind.DECLARED);
    }

    protected Map<String, Declaration> declarationsByName = new HashMap<String, Declaration>();
    protected Map<Package, Unit> unitsByPackage = new HashMap<Package, Unit>();
    protected TypeParser typeParser;
    protected Unit typeFactory;
    protected final Set<String> loadedPackages = new HashSet<String>();
    protected final Map<String,LazyPackage> packagesByName = new HashMap<String,LazyPackage>();
    protected boolean packageDescriptorsNeedLoading = false;
    protected boolean isBootstrap;
    protected ModuleManager moduleManager;
    protected Modules modules;
    protected Map<String, ClassMirror> classMirrorCache = new HashMap<String, ClassMirror>();
    protected boolean binaryCompatibilityErrorRaised = false;
    protected Timer timer;
    
    /**
     * Loads a given package, if required. This is mostly useful for the javac reflection impl.
     * 
     * @param packageName the package name to load
     * @param loadDeclarations true to load all the declarations in this package.
     * @return 
     */
    public abstract boolean loadPackage(String packageName, boolean loadDeclarations);
    
    /**
     * Looks up a ClassMirror by name. Uses cached results, and caches the result of calling lookupNewClassMirror
     * on cache misses.
     * 
     * @param name the name of the Class to load
     * @return a ClassMirror for the specified class, or null if not found.
     */
    public synchronized final ClassMirror lookupClassMirror(String name){
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            // we use containsKey to be able to cache null results
            if(classMirrorCache.containsKey(name))
                return classMirrorCache.get(name);
            ClassMirror mirror = lookupNewClassMirror(name);
            // we even cache null results
            classMirrorCache.put(name, mirror);
            return mirror;
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    protected boolean lastPartHasLowerInitial(String name) {
        int index = name.lastIndexOf('.');
        if (index != -1){
            name = name.substring(index+1);
        }
        if(!name.isEmpty()){
            char c = name.charAt(0);
            return Util.isLowerCase(c);
        }
        return false;
    }
    
    /**
     * Looks up a ClassMirror by name. Called by lookupClassMirror on cache misses.
     * 
     * @param name the name of the Class to load
     * @return a ClassMirror for the specified class, or null if not found.
     */
    protected abstract ClassMirror lookupNewClassMirror(String name);

    /**
     * Adds the given module to the set of modules from which we can load classes.
     * 
     * @param module the module
     * @param artifact the module's artifact, if any. Can be null. 
     */
    public abstract void addModuleToClassPath(Module module, ArtifactResult artifact);

    /**
     * Returns true if the given method is overriding an inherited method (from super class or interfaces).
     */
    protected abstract boolean isOverridingMethod(MethodMirror methodMirror);

    /**
     * Logs a warning.
     */
    protected abstract void logWarning(String message);

    /**
     * Logs a debug message.
     */
    protected abstract void logVerbose(String message);
    
    /**
     * Logs an error
     */
    protected abstract void logError(String message);
    
    public void loadStandardModules(){
        // set up the type factory
        Module languageModule = findOrCreateModule(CEYLON_LANGUAGE);
        addModuleToClassPath(languageModule, null);
        Package languagePackage = findOrCreatePackage(languageModule, CEYLON_LANGUAGE);
        typeFactory.setPackage(languagePackage);
        
        // make sure the jdk modules are loaded
        for(String jdkModule : JDKUtils.getJDKModuleNames())
            findOrCreateModule(jdkModule);
        for(String jdkOracleModule : JDKUtils.getOracleJDKModuleNames())
            findOrCreateModule(jdkOracleModule);
        
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        loadPackage("java.lang", false);
        loadPackage("com.redhat.ceylon.compiler.java.metadata", false);

        /*
         * We do not load the ceylon.language module from class files if we're bootstrapping it
         */
        if(!isBootstrap){
            loadPackage(CEYLON_LANGUAGE, true);
            loadPackage("ceylon.language.descriptor", true);
        }
    }

    /**
     * This is meant to be called if your subclass doesn't call loadStandardModules for whatever reason
     */
    public void setupWithNoStandardModules() {
        Module languageModule = modules.getLanguageModule();
        if(languageModule == null)
            throw new RuntimeException("Assertion failed: language module is null");
        Package languagePackage = languageModule.getPackage(CEYLON_LANGUAGE);
        if(languagePackage == null)
            throw new RuntimeException("Assertion failed: language package is null");
        typeFactory.setPackage(languagePackage);
    }

    enum ClassType {
        ATTRIBUTE, METHOD, OBJECT, CLASS, INTERFACE;
    }
    
    private ClassMirror loadClass(String pkgName, String className) {
        ClassMirror moduleClass = null;
        try{
            loadPackage(pkgName, false);
            moduleClass = lookupClassMirror(className);
        }catch(Exception x){
            logVerbose("[Failed to complete class "+className+"]");
        }
        return moduleClass;
    }

    private Declaration convertToDeclaration(TypeMirror type, Scope scope, DeclarationType declarationType) {
        String typeName;
        switch(type.getKind()){
        case VOID:    typeName = "ceylon.language.Anything"; break;
        case BOOLEAN: typeName = "java.lang.Boolean"; break;
        case BYTE:    typeName = "java.lang.Byte"; break;
        case CHAR:    typeName = "java.lang.Character"; break;
        case SHORT:   typeName = "java.lang.Short"; break;
        case INT:     typeName = "java.lang.Integer"; break;
        case LONG:    typeName = "java.lang.Long"; break;
        case FLOAT:   typeName = "java.lang.Float"; break;
        case DOUBLE:  typeName = "java.lang.Double"; break;
        case ARRAY:
            return typeFactory.getLanguageModuleDeclaration("Array");
        case DECLARED:
            typeName = type.getQualifiedName();
            break;
        case TYPEVAR:
            return safeLookupTypeParameter(scope, type.getQualifiedName());
        case WILDCARD:
            // FIXME: we shouldn't even get there, because if something contains a wildcard (Foo<?>) we erase it to
            // IdentifiableObject, so this shouldn't be reachable.
            typeName = "ceylon.language.Nothing";
            break;
        default:
            throw new RuntimeException("Failed to handle type "+type);
        }
        return convertToDeclaration(typeName, declarationType);
    }

    protected Declaration convertToDeclaration(ClassMirror classMirror, DeclarationType declarationType) {
        List<Declaration> decls = new ArrayList<Declaration>();
        boolean[] alreadyExists = new boolean[1];
        Declaration decl = getOrCreateDeclaration(classMirror, declarationType,
                decls, alreadyExists);
        
        if (alreadyExists[0]) {
            return decl;
        }
        
        // find its module
        String pkgName = classMirror.getPackage().getQualifiedName();
        Module module = findModuleForPackage(pkgName);
        LazyPackage pkg = findOrCreatePackage(module, pkgName);

        // find/make its Unit
        Unit unit = getCompiledUnit(pkg, classMirror);

        // set all the containers
        for(Declaration d : decls){
            d.setShared(classMirror.isPublic());
        
            // add it to its Unit
            d.setUnit(unit);
            unit.addDeclaration(d);

            setContainer(classMirror, d, pkg);
        }

        return decl;
    }

    private void setContainer(ClassMirror classMirror, Declaration d, LazyPackage pkg) {
        // add it to its package if it's not an inner class
        if(!classMirror.isInnerClass()){
            pkg.addMember(d);
            d.setContainer(pkg);
        }else if(d instanceof ClassOrInterface || d instanceof TypeAlias){
            // do overloads later, since their container is their abstract superclass's container and
            // we have to set that one first
            if(d instanceof Class == false || !((Class)d).isOverloaded()){
                ClassOrInterface container = getContainer(classMirror);
                d.setContainer(container);
                // let's not trigger lazy-loading
                ((LazyContainer)container).addMember(d);
                // now we can do overloads
                if(d instanceof Class && ((Class)d).getOverloads() != null){
                    for(Declaration overload : ((Class)d).getOverloads()){
                        overload.setContainer(container);
                        // let's not trigger lazy-loading
                        ((LazyContainer)container).addMember(d);
                    }
                }
            }
        }
    }

    private ClassOrInterface getContainer(ClassMirror classMirror) {
        AnnotationMirror containerAnnotation = classMirror.getAnnotation(CEYLON_CONTAINER_ANNOTATION);
        if(containerAnnotation != null){
            String name = (String) containerAnnotation.getValue("name");
            String javaClass = (String) containerAnnotation.getValue("javaClass");
            String packageName = (String) containerAnnotation.getValue("packageName");
            String javaClassName = assembleJavaClass(javaClass, packageName);
            Declaration containerDecl = convertToDeclaration(javaClassName, DeclarationType.TYPE);
            if(containerDecl == null)
                throw new ModelResolutionException("Failed to load outer type " + name 
                        + " for inner type " + classMirror.getQualifiedName().toString()
                        + ", java class: " + javaClass);
            return (ClassOrInterface) containerDecl;
        }else{
            return (ClassOrInterface) convertToDeclaration(classMirror.getEnclosingClass(), DeclarationType.TYPE);
        }
    }

    protected Declaration getOrCreateDeclaration(ClassMirror classMirror,
            DeclarationType declarationType, List<Declaration> decls, boolean[] alreadyExists) {
        alreadyExists[0] = false;
        Declaration decl = null;
        String className = classMirror.getQualifiedName();
        ClassType type;
        String prefix;
        if(classMirror.isCeylonToplevelAttribute()){
            type = ClassType.ATTRIBUTE;
            prefix = "V";
        }else if(classMirror.isCeylonToplevelMethod()){
            type = ClassType.METHOD;
            prefix = "V";
        }else if(classMirror.isCeylonToplevelObject()){
            type = ClassType.OBJECT;
            // depends on which one we want
            prefix = declarationType == DeclarationType.TYPE ? "C" : "V";
        }else if(classMirror.isInterface()){
            type = ClassType.INTERFACE;
            prefix = "C";
        }else{
            type = ClassType.CLASS;
            prefix = "C";
        }
        String key = prefix + className;
        // see if we already have it
        if(declarationsByName.containsKey(key)){
            alreadyExists[0] = true;
            return declarationsByName.get(key);
        }
        
        checkBinaryCompatibility(classMirror);
        
        // make it
        switch(type){
        case ATTRIBUTE:
            decl = makeToplevelAttribute(classMirror);
            break;
        case METHOD:
            decl = makeToplevelMethod(classMirror);
            break;
        case OBJECT:
            // we first make a class
            Declaration objectClassDecl = makeLazyClass(classMirror, null, null, true);
            declarationsByName.put("C"+className, objectClassDecl);
            decls.add(objectClassDecl);
            // then we make a value for it
            Declaration objectDecl = makeToplevelAttribute(classMirror);
            declarationsByName.put("V"+className, objectDecl);
            decls.add(objectDecl);
            // which one did we want?
            decl = declarationType == DeclarationType.TYPE ? objectClassDecl : objectDecl;
            break;
        case CLASS:
            if(classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION) != null){
                decl = makeClassAlias(classMirror);
            }else if(classMirror.getAnnotation(CEYLON_TYPE_ALIAS_ANNOTATION) != null){
                decl = makeTypeAlias(classMirror);
            }else{
                List<MethodMirror> constructors = getClassConstructors(classMirror);
                if (!constructors.isEmpty()) {
                    if (constructors.size() > 1) {
                        // If the class has multiple constructors we make a copy of the class
                        // for each one (each with it's own single constructor) and make them
                        // a subclass of the original
                        Class supercls = makeLazyClass(classMirror, null, null, false);
                        supercls.setAbstraction(true);
                        List<Declaration> overloads = new ArrayList<Declaration>(constructors.size());
                        for (MethodMirror constructor : constructors) {
                            // FIXME: tmp hack to skip constructors that have type params as we don't handle them yet
                            if(!constructor.getTypeParameters().isEmpty())
                                continue;
                            LazyClass subdecl = makeLazyClass(classMirror, supercls, constructor, false);
                            subdecl.setOverloaded(true);
                            overloads.add(subdecl);
                            decls.add(subdecl);
                        }
                        supercls.setOverloads(overloads);
                        decl = supercls;
                    } else {
                        MethodMirror constructor = constructors.get(0);
                        decl = makeLazyClass(classMirror, null, constructor, false);
                    }
                } else {
                    decl = makeLazyClass(classMirror, null, null, false);
                }
            }
            break;
        case INTERFACE:
            if(classMirror.getAnnotation(CEYLON_ALIAS_ANNOTATION) != null){
                decl = makeInterfaceAlias(classMirror);
            }else{
                decl = makeLazyInterface(classMirror);
            }
            break;
        }


        // objects have special handling above
        if(type != ClassType.OBJECT){
            declarationsByName.put(key, decl);
            decls.add(decl);
        }
        
        return decl;
    }

    private Declaration makeClassAlias(ClassMirror classMirror) {
        return new LazyClassAlias(classMirror, this);
    }

    private Declaration makeTypeAlias(ClassMirror classMirror) {
        return new LazyTypeAlias(classMirror, this);
    }

    private Declaration makeInterfaceAlias(ClassMirror classMirror) {
        return new LazyInterfaceAlias(classMirror, this);
    }

    private void checkBinaryCompatibility(ClassMirror classMirror) {
        // let's not report it twice
        if(binaryCompatibilityErrorRaised)
            return;
        AnnotationMirror annotation = classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION);
        if(annotation == null)
            return; // Java class, no check
        Integer major = (Integer) annotation.getValue("major");
        if(major == null)
            major = 0;
        Integer minor = (Integer) annotation.getValue("minor");
        if(minor == null)
            minor = 0;
        if(major != Versions.JVM_BINARY_MAJOR_VERSION
                || minor != Versions.JVM_BINARY_MINOR_VERSION){
            logError("Ceylon class " + classMirror.getQualifiedName() + " was compiled by an incompatible version of the Ceylon compiler"
                    +"\nThe class was compiled using "+major+"."+minor+"."
                    +"\nThis compiler supports "+Versions.JVM_BINARY_MAJOR_VERSION+"."+Versions.JVM_BINARY_MINOR_VERSION+"."
                    +"\nPlease try to recompile your module using a compatible compiler."
                    +"\nBinary compatibility will only be supported after Ceylon 1.0.");
            binaryCompatibilityErrorRaised = true;
        }
    }

    private List<MethodMirror> getClassConstructors(ClassMirror classMirror) {
        LinkedList<MethodMirror> constructors = new LinkedList<MethodMirror>();
        for(MethodMirror methodMirror : classMirror.getDirectMethods()){
            // We skip members marked with @Ignore
            if(methodMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(methodMirror.isConstructor()) {
                constructors.add(methodMirror);
            }
        }
        return constructors;
    }

    protected Unit getCompiledUnit(LazyPackage pkg, ClassMirror classMirror) {
        Unit unit = unitsByPackage.get(pkg);
        if(unit == null){
            unit = new LazyUnit();
            unit.setPackage(pkg);
            unitsByPackage.put(pkg, unit);
        }
        return unit;
    }

    protected LazyValue makeToplevelAttribute(ClassMirror classMirror) {
        LazyValue value = new LazyValue(classMirror, this);
        return value;
    }

    protected LazyMethod makeToplevelMethod(ClassMirror classMirror) {
        LazyMethod method = new LazyMethod(classMirror, this);
        return method;
    }
    
    protected LazyClass makeLazyClass(ClassMirror classMirror, Class superClass, MethodMirror constructor, boolean forTopLevelObject) {
        LazyClass klass = new LazyClass(classMirror, this, superClass, constructor, forTopLevelObject);
        klass.setAnonymous(classMirror.getAnnotation(CEYLON_OBJECT_ANNOTATION) != null);
        if(klass.isCeylon())
            klass.setAbstract(isAnnotated(classMirror, "abstract"));
        else
            klass.setAbstract(classMirror.isAbstract());
        klass.setFormal(isAnnotated(classMirror, "formal"));
        klass.setDefault(isAnnotated(classMirror, "default"));
        klass.setActual(isAnnotated(classMirror, "actual"));
        klass.setFinal(classMirror.isFinal());
        return klass;
    }

    protected LazyInterface makeLazyInterface(ClassMirror classMirror) {
        LazyInterface iface = new LazyInterface(classMirror, this);
        return iface;
    }

    public synchronized Declaration convertToDeclaration(String typeName, DeclarationType declarationType) {
        // FIXME: this needs to move to the type parser and report warnings
        //This should be done where the TypeInfo annotation is parsed
        //to avoid retarded errors because of a space after a comma
        typeName = typeName.trim();
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            if ("ceylon.language.Nothing".equals(typeName)) {
                return new NothingType(typeFactory);
            } else if ("java.lang.Throwable".equals(typeName)) {
                return convertToDeclaration("ceylon.language.Exception", declarationType);
            }
            ClassMirror classMirror = lookupClassMirror(typeName);
            if (classMirror == null) {
                String simpleName = typeName.substring(typeName.lastIndexOf(".")+1);
                Declaration languageModuleDeclaration = typeFactory.getLanguageModuleDeclaration(simpleName);
                if (languageModuleDeclaration != null) {
                    return languageModuleDeclaration;
                }
                throw new ModelResolutionException("Failed to resolve "+typeName);
            }
            // we only allow source loading when it's java code we're compiling in the same go
            // (well, technically before the ceylon code)
            if(classMirror.isLoadedFromSource() && !classMirror.isJavaSource())
                return null;
            return convertToDeclaration(classMirror, declarationType);
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    protected TypeParameter safeLookupTypeParameter(Scope scope, String name) {
        TypeParameter param = lookupTypeParameter(scope, name);
        if(param == null)
            throw new ModelResolutionException("Type param "+name+" not found in "+scope);
        return param;
    }
    
    private TypeParameter lookupTypeParameter(Scope scope, String name) {
        if(scope instanceof Method){
            Method m = (Method) scope;
            for(TypeParameter param : m.getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            if (!m.isToplevel()) {
                // look it up in its container
                return lookupTypeParameter(scope.getContainer(), name);
            } else {
                // not found
                return null;
            }
        }else if(scope instanceof ClassOrInterface
                || scope instanceof TypeAlias){
            TypeDeclaration decl = (TypeDeclaration) scope;
            for(TypeParameter param : decl.getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            if (!decl.isToplevel()) {
                // look it up in its container
                return lookupTypeParameter(scope.getContainer(), name);
            } else {
                // not found
                return null;
            }
        }else
            throw new ModelResolutionException("Type param "+name+" lookup not supported for scope "+scope);
    }
    
    //
    // Packages
    
    public synchronized Package findPackage(String pkgName) {
        pkgName = Util.quoteJavaKeywords(pkgName);
        return packagesByName.get(pkgName);
    }

    public synchronized LazyPackage findExistingPackage(Module module, String pkgName){
        String quotedPkgName = Util.quoteJavaKeywords(pkgName);
        LazyPackage pkg = findCachedPackage(module, quotedPkgName);
        if(pkg != null)
            return pkg;
        // special case for the jdk module
        String moduleName = module.getNameAsString();
        if(AbstractModelLoader.isJDKModule(moduleName)){
            if(JDKUtils.isJDKPackage(moduleName, pkgName) || JDKUtils.isOracleJDKPackage(moduleName, pkgName)){
                return findOrCreatePackage(module, pkgName);
            }
            return null;
        }
        // only create it if it exists
        if(loadPackage(pkgName, false)){
            return findOrCreatePackage(module, pkgName);
        }
        return null;
    }
    
    private LazyPackage findCachedPackage(Module module, String quotedPkgName) {
        LazyPackage pkg = packagesByName.get(quotedPkgName);
        if(pkg != null){
            // only return it if it matches the module we're looking for, because if it doesn't we have an issue already logged
            // for a direct dependency on same module different versions logged, so no need to confuse this further
            if(module != null && pkg.getModule() != null && !module.equals(pkg.getModule()))
                return null;
            return pkg;
        }
        return null;
    }

    public synchronized LazyPackage findOrCreatePackage(Module module, final String pkgName) {
        String quotedPkgName = Util.quoteJavaKeywords(pkgName);
        LazyPackage pkg = findCachedPackage(module, quotedPkgName);
        if(pkg != null)
            return pkg;
        // try to find it from the module, perhaps it already got created and we didn't catch it
        if(module instanceof LazyModule){
            pkg = (LazyPackage) ((LazyModule) module).findPackageNoLazyLoading(pkgName);
        }else if(module != null){
            pkg = (LazyPackage) module.getDirectPackage(pkgName);
        }
        boolean isNew = pkg == null;
        if(pkg == null){
            pkg = new LazyPackage(this);
            // FIXME: some refactoring needed
            pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));
        }
        packagesByName.put(quotedPkgName, pkg);

        // only bind it if we already have a module
        if(isNew && module != null){
            pkg.setModule(module);
            if(module instanceof LazyModule)
                ((LazyModule) module).addPackage(pkg);
            else
                module.getPackages().add(pkg);
        }
        
        // only load package descriptors for new packages after a certain phase
        if(packageDescriptorsNeedLoading)
            loadPackageDescriptor(pkg);
        
        return pkg;
    }

    public synchronized void loadPackageDescriptors() {
        for(Package pkg : packagesByName.values()){
            loadPackageDescriptor(pkg);
        }
        packageDescriptorsNeedLoading  = true;
    }

    private void loadPackageDescriptor(Package pkg) {
        // let's not load package descriptors for Java modules
        if(pkg.getModule() != null 
                && ((LazyModule)pkg.getModule()).isJava()){
            pkg.setShared(true);
            return;
        }
        String quotedQualifiedName = Util.quoteJavaKeywords(pkg.getQualifiedNameString());
        // FIXME: not sure the toplevel package can have a package declaration
        String className = quotedQualifiedName.isEmpty() ? "package" : quotedQualifiedName + ".package";
        logVerbose("[Trying to look up package from "+className+"]");
        ClassMirror packageClass = loadClass(quotedQualifiedName, className);
        if(packageClass == null){
            logVerbose("[Failed to complete "+className+"]");
            // missing: leave it private
            return;
        }
        // did we compile it from source or class?
        if(packageClass.isLoadedFromSource()){
            // must have come from source, in which case we walked it and
            // loaded its values already
            logVerbose("[We are compiling the package "+className+"]");
            return;
        }
        loadCompiledPackage(packageClass, pkg);
    }

    private void loadCompiledPackage(ClassMirror packageClass, Package pkg) {
        String name = getAnnotationStringValue(packageClass, CEYLON_PACKAGE_ANNOTATION, "name");
        Boolean shared = getAnnotationBooleanValue(packageClass, CEYLON_PACKAGE_ANNOTATION, "shared");
        // FIXME: validate the name?
        if(name == null || name.isEmpty()){
            logWarning("Package class "+pkg.getQualifiedNameString()+" contains no name, ignoring it");
            return;
        }
        if(shared == null){
            logWarning("Package class "+pkg.getQualifiedNameString()+" contains no shared, ignoring it");
            return;
        }
        pkg.setShared(shared);
    }

    protected Module lookupModuleInternal(String packageName) {
        for(Module module : modules.getListOfModules()){
            // don't try the default module because it will always say yes
            if(module.isDefault())
                continue;
            if(module instanceof LazyModule){
                if(((LazyModule)module).containsPackage(packageName))
                    return module;
            }else if(isSubPackage(module.getNameAsString(), packageName))
                return module;
        }
        return modules.getDefaultModule();
    }

    private boolean isSubPackage(String moduleName, String pkgName) {
        return pkgName.equals(moduleName)
                || pkgName.startsWith(moduleName+".");
    }

    //
    // Modules
    
    /**
     * Finds or creates a new module. This is mostly useful to force creation of modules such as jdk
     * or ceylon.language modules.
     */
    public synchronized Module findOrCreateModule(String moduleName) {
        boolean isJava = false;
        boolean defaultModule = false;

        // make sure it isn't loaded
        Module module = getLoadedModule(moduleName);
        if(module != null)
            return module;
        
        if(JDKUtils.isJDKModule(moduleName) || JDKUtils.isOracleJDKModule(moduleName)){
            isJava = true;
        }
        
        java.util.List<String> moduleNameList = Arrays.asList(moduleName.split("\\."));
        module = moduleManager.getOrCreateModule(moduleNameList, null);
        // make sure that when we load the ceylon language module we set it to where
        // the typechecker will look for it
        if(moduleName.equals(CEYLON_LANGUAGE)
                 && modules.getLanguageModule() == null){
             modules.setLanguageModule(module);
        }
         
        // TRICKY We do this only when isJava is true to prevent resetting
        // the value to false by mistake. LazyModule get's created with
        // this attribute to false by default, so it should work
        if (isJava && module instanceof LazyModule) {
            ((LazyModule)module).setJava(true);
        }
        
        // FIXME: this can't be that easy.
        module.setAvailable(true);
        module.setDefault(defaultModule);
        return module;
    }

    private Module findModuleForPackage(String pkgName) {
        Module module = lookupModuleInternal(pkgName);
        if (module != null) {
            return module;
        }
        throw new ModelResolutionException("Failed to find module for package "+pkgName);
    }

    public synchronized Module loadCompiledModule(String pkgName) {
        if(pkgName.isEmpty())
            return null;
        String moduleClassName = pkgName + ".module";
        logVerbose("[Trying to look up module from "+moduleClassName+"]");
        ClassMirror moduleClass = loadClass(pkgName, moduleClassName);
        if(moduleClass != null){
            // load its module annotation
            Module module = loadCompiledModule(moduleClass, moduleClassName);
            if(module != null)
                return module;
        }
        // keep looking up
        int lastDot = pkgName.lastIndexOf(".");
        if(lastDot == -1)
            return null;
        String parentPackageName = pkgName.substring(0, lastDot);
        return loadCompiledModule(parentPackageName);
    }

    private Module loadCompiledModule(ClassMirror moduleClass, String moduleClassName) {
        String name = getAnnotationStringValue(moduleClass, CEYLON_MODULE_ANNOTATION, "name");
        String version = getAnnotationStringValue(moduleClass, CEYLON_MODULE_ANNOTATION, "version");
        // FIXME: validate the name?
        if(name == null || name.isEmpty()){
            logWarning("Module class "+moduleClassName+" contains no name, ignoring it");
            return null;
        }
        if(version == null || version.isEmpty()){
            logWarning("Module class "+moduleClassName+" contains no version, ignoring it");
            return null;
        }
        Module module = moduleManager.getOrCreateModule(Arrays.asList(name.split("\\.")), version);
        int major = getAnnotationIntegerValue(moduleClass, CEYLON_CEYLON_ANNOTATION, "major", 0);
        int minor = getAnnotationIntegerValue(moduleClass, CEYLON_CEYLON_ANNOTATION, "minor", 0);
        module.setMajor(major);
        module.setMinor(minor);

        List<AnnotationMirror> imports = getAnnotationArrayValue(moduleClass, CEYLON_MODULE_ANNOTATION, "dependencies");
        if(imports != null){
            for (AnnotationMirror importAttribute : imports) {
                String dependencyName = (String) importAttribute.getValue("name");
                if (dependencyName != null) {
                    String dependencyVersion = (String) importAttribute.getValue("version");

                    Module dependency = moduleManager.getOrCreateModule(ModuleManager.splitModuleName(dependencyName), dependencyVersion);

                    Boolean optionalVal = (Boolean) importAttribute.getValue("optional");

                    Boolean exportVal = (Boolean) importAttribute.getValue("export");

                    ModuleImport moduleImport = moduleManager.findImport(module, dependency);
                    if (moduleImport == null) {
                        boolean optional = optionalVal != null && optionalVal;
                        boolean export = exportVal != null && exportVal;
                        moduleImport = new ModuleImport(dependency, optional, export);
                        module.getImports().add(moduleImport);
                    }
                }
            }
        }
        
        module.setAvailable(true);
        
        modules.getListOfModules().add(module);
        Module languageModule = modules.getLanguageModule();
        module.setLanguageModule(languageModule);
        if(module != languageModule){
            ModuleImport moduleImport = moduleManager.findImport(module, languageModule);
            if (moduleImport == null) {
                moduleImport = new ModuleImport(languageModule, false, false);
                module.getImports().add(moduleImport);
            }
        }
        
        return module;
    }  
    
    //
    // Utils for loading type info from the model
    
    @SuppressWarnings("unchecked")
    private <T> List<T> getAnnotationArrayValue(AnnotatedMirror mirror, String type, String field) {
        return (List<T>) getAnnotationValue(mirror, type, field);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getAnnotationArrayValue(AnnotatedMirror mirror, String type) {
        return (List<T>) getAnnotationValue(mirror, type);
    }

    private String getAnnotationStringValue(AnnotatedMirror mirror, String type) {
        return getAnnotationStringValue(mirror, type, "value");
    }
    
    private String getAnnotationStringValue(AnnotatedMirror mirror, String type, String field) {
        return (String) getAnnotationValue(mirror, type, field);
    }

    private Boolean getAnnotationBooleanValue(AnnotatedMirror mirror, String type, String field) {
        return (Boolean) getAnnotationValue(mirror, type, field);
    }

    private int getAnnotationIntegerValue(AnnotatedMirror mirror, String type, String field, int defaultValue) {
        Integer val = (Integer) getAnnotationValue(mirror, type, field);
        return val != null ? val : defaultValue;
    }

    private Object getAnnotationValue(AnnotatedMirror mirror, String type) {
        return getAnnotationValue(mirror, type, "value");
    }
    
    private Object getAnnotationValue(AnnotatedMirror mirror, String type, String fieldName) {
        AnnotationMirror annotation = mirror.getAnnotation(type);
        if(annotation != null){
            return annotation.getValue(fieldName);
        }
        return null;
    }

    //
    // ModelCompleter
    
    @Override
    public synchronized void complete(LazyInterface iface) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        complete(iface, iface.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyInterface iface) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeTypeParameters(iface, iface.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void complete(LazyClass klass) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        complete(klass, klass.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyClass klass) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeTypeParameters(klass, klass.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyClassAlias lazyClassAlias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAliasTypeParameters(lazyClassAlias, lazyClassAlias.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }
    
    @Override
    public synchronized void completeTypeParameters(LazyInterfaceAlias lazyInterfaceAlias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAliasTypeParameters(lazyInterfaceAlias, lazyInterfaceAlias.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void completeTypeParameters(LazyTypeAlias lazyTypeAlias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAliasTypeParameters(lazyTypeAlias, lazyTypeAlias.classMirror);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    @Override
    public synchronized void complete(LazyInterfaceAlias alias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAlias(alias, alias.classMirror, CEYLON_ALIAS_ANNOTATION);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }
    
    @Override
    public synchronized void complete(LazyClassAlias alias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAlias(alias, alias.classMirror, CEYLON_ALIAS_ANNOTATION);
        // must be a class
        Class declaration = (Class) alias.getExtendedType().getDeclaration();
        
        // copy the parameters from the extended type
        alias.setParameterList(copyParameterList(alias, declaration));
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    private ParameterList copyParameterList(LazyClassAlias alias, Class declaration) {
        ParameterList newList = new ParameterList();
        // FIXME: multiple param lists?
        newList.setNamedParametersSupported(declaration.getParameterList().isNamedParametersSupported());
        for(Parameter p : declaration.getParameterList().getParameters()){
            // FIXME: functionalparams?
            Parameter newParam = new ValueParameter();
            newParam.setName(p.getName());
            newParam.setContainer(alias);
            DeclarationVisitor.setVisibleScope(newParam);
            newParam.setDeclaration(alias);
            newParam.setSequenced(p.isSequenced());
            newParam.setUnboxed(p.getUnboxed());
            newParam.setUncheckedNullType(p.hasUncheckedNullType());
            newParam.setUnit(p.getUnit());
            newParam.setType(p.getProducedTypedReference(alias.getExtendedType(), Collections.<ProducedType>emptyList()).getType());
            alias.addMember(newParam);
            newList.getParameters().add(newParam);
        }
        return newList;
    }

    @Override
    public synchronized void complete(LazyTypeAlias alias) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        completeLazyAlias(alias, alias.classMirror, CEYLON_TYPE_ALIAS_ANNOTATION);
        timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
    }

    private void completeLazyAliasTypeParameters(TypeDeclaration alias, ClassMirror mirror) {
        // type parameters
        setTypeParameters(alias, mirror);
    }

    private void completeLazyAlias(TypeDeclaration alias, ClassMirror mirror, String aliasAnnotationName) {
        // now resolve the extended type
        AnnotationMirror aliasAnnotation = mirror.getAnnotation(aliasAnnotationName);
        String extendedTypeString = (String) aliasAnnotation.getValue();
        
        ProducedType extendedType = decodeType(extendedTypeString, alias);
        alias.setExtendedType(extendedType);
    }

    private void completeTypeParameters(ClassOrInterface klass, ClassMirror classMirror) {
        setTypeParameters(klass, classMirror);
    }

    private void complete(ClassOrInterface klass, ClassMirror classMirror) {
        Map<MethodMirror, List<MethodMirror>> variables = new HashMap<MethodMirror, List<MethodMirror>>();
        boolean isFromJDK = isFromJDK(classMirror);
        boolean isCeylon = (classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null);
        
        // now that everything has containers, do the inner classes
        if(klass instanceof Class == false || !((Class)klass).isOverloaded()){
            // this will not load inner classes of overloads, but that's fine since we want them in the
            // abstracted super class (the real one)
            addInnerClasses(klass, classMirror);
        }

        // Java classes with multiple constructors get turned into multiple Ceylon classes
        // Here we get the specific constructor that was assigned to us (if any)
        MethodMirror constructor = null;
        if (klass instanceof LazyClass) {
            constructor = ((LazyClass)klass).getConstructor();
        }
            
        // Turn a list of possibly overloaded methods into a map
        // of lists that contain methods with the same name
        Map<String, List<MethodMirror>> methods = new LinkedHashMap<String, List<MethodMirror>>();
        for(MethodMirror methodMirror : classMirror.getDirectMethods()){
            // We skip members marked with @Ignore
            if(methodMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(methodMirror.isStaticInit())
                continue;
            if(isCeylon && methodMirror.isStatic())
                continue;
            // FIXME: temporary, because some private classes from the jdk are
            // referenced in private methods but not available
            if(isFromJDK && !methodMirror.isPublic())
                continue;
            String methodName = methodMirror.getName();
            List<MethodMirror> homonyms = methods.get(methodName);
            if (homonyms == null) {
                homonyms = new LinkedList<MethodMirror>();
                methods.put(methodName, homonyms);
            }
            homonyms.add(methodMirror);
        }
        
        // Add the methods
        for(List<MethodMirror> methodMirrors : methods.values()){
            boolean isOverloaded = methodMirrors.size() > 1;
            
            List<Declaration> overloads = (isOverloaded) ? new ArrayList<Declaration>(methodMirrors.size()) : null;
            for (MethodMirror methodMirror : methodMirrors) {
                String methodName = methodMirror.getName();
                if(methodMirror.isConstructor()) {
                    if (methodMirror == constructor) {
                        if(!(klass instanceof LazyClass) || !((LazyClass)klass).isTopLevelObjectType())
                            setParameters((Class)klass, methodMirror, isCeylon, klass);
                    }
                } else if(isGetter(methodMirror)) {
                    // simple attribute
                    addValue(klass, methodMirror, getJavaAttributeName(methodName), isCeylon);
                } else if(isSetter(methodMirror)) {
                    // We skip setters for now and handle them later
                    variables.put(methodMirror, methodMirrors);
                } else if(isHashAttribute(methodMirror)) {
                    // ERASURE
                    // Un-erasing 'hash' attribute from 'hashCode' method
                    addValue(klass, methodMirror, "hash", isCeylon);
                } else if(isStringAttribute(methodMirror)) {
                    // ERASURE
                    // Un-erasing 'string' attribute from 'toString' method
                    addValue(klass, methodMirror, "string", isCeylon);
                } else {
                    // normal method
                    Method m = addMethod(klass, methodMirror, isCeylon, isOverloaded);
                    if (isOverloaded) {
                        overloads.add(m);
                    }
                }
            }
            
            if (overloads != null && !overloads.isEmpty()) {
                // We create an extra "abstraction" method for overloaded methods
                Method abstractionMethod = addMethod(klass, methodMirrors.get(0), false, false);
                abstractionMethod.setAbstraction(true);
                abstractionMethod.setOverloads(overloads);
                abstractionMethod.setType(new UnknownType(typeFactory).getType());
            }
        }

        for(FieldMirror fieldMirror : classMirror.getDirectFields()){
            // We skip members marked with @Ignore
            if(fieldMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(isCeylon && fieldMirror.isStatic())
                continue;
            // FIXME: temporary, because some private classes from the jdk are
            // referenced in private methods but not available
            if(isFromJDK && !fieldMirror.isPublic())
                continue;
            String name = fieldMirror.getName();
            // skip the field if "we've already got one"
            if(klass.getDirectMember(name, null, false) != null)
                continue;
            addValue(klass, fieldMirror, isCeylon);
        }
        
        // Now mark all Values for which Setters exist as variable
        for(Entry<MethodMirror, List<MethodMirror>> setterEntry : variables.entrySet()){
            MethodMirror setter = setterEntry.getKey();
            String name = getJavaAttributeName(setter.getName());
            Declaration decl = klass.getMember(name, null, false);
            boolean foundGetter = false;
            if (decl != null && decl instanceof Value) {
                Value value = (Value)decl;
                VariableMirror setterParam = setter.getParameters().get(0);
                try{
                    ProducedType paramType = obtainType(setterParam.getType(), setterParam, klass, VarianceLocation.INVARIANT);
                    // only add the setter if it has exactly the same type as the getter
                    if(paramType.isExactly(value.getType())){
                        foundGetter = true;
                        value.setVariable(true);
                        if(decl instanceof JavaBeanValue)
                            ((JavaBeanValue)decl).setSetterName(setter.getName());
                    }else
                        logVerbose("Setter parameter type for "+name+" does not match corresponding getter type, adding setter as a method");
                }catch(TypeParserException x){
                    logError("Invalid type signature for setter of "+klass.getQualifiedNameString()+"."+setter.getName()+": "+x.getMessage());
                    throw x;
                }
            } 
            
            if(!foundGetter){
                // it was not a setter, it was a method, let's add it as such
                addMethod(klass, setter, isCeylon, false);
            }
        }

        // Now marry-up attributes and parameters)
        if (klass instanceof Class) {
            for (Declaration m : klass.getMembers()) {
                if (m instanceof Value) {
                    Value v = (Value)m;
                    Parameter p = ((Class)klass).getParameter(v.getName());
                    if (p instanceof ValueParameter) {
                        ((ValueParameter)p).setHidden(true);    
                    }
                }
            }
        }
        
        
        klass.setStaticallyImportable(!isCeylon && classMirror.isStatic());
        
        setExtendedType(klass, classMirror);
        setSatisfiedTypes(klass, classMirror);
        setCaseTypes(klass, classMirror);
        fillRefinedDeclarations(klass);
        setAnnotations(klass, classMirror);
    }

    private boolean isFromJDK(ClassMirror classMirror) {
        String pkgName = classMirror.getPackage().getQualifiedName();
        return JDKUtils.isJDKAnyPackage(pkgName) || JDKUtils.isOracleJDKAnyPackage(pkgName);
    }

    private void setAnnotations(Declaration decl, AnnotatedMirror classMirror) {
        List<AnnotationMirror> annotations = getAnnotationArrayValue(classMirror, CEYLON_ANNOTATIONS_ANNOTATION);
        if(annotations == null)
            return;
        for(AnnotationMirror annotation : annotations){
            decl.getAnnotations().add(readModelAnnotation(annotation));
        }
    }

    private Annotation readModelAnnotation(AnnotationMirror annotation) {
        Annotation modelAnnotation = new Annotation();
        modelAnnotation.setName((String) annotation.getValue());
        @SuppressWarnings("unchecked")
        List<String> arguments = (List<String>) annotation.getValue("arguments");
        if(arguments != null){
            modelAnnotation.getPositionalArguments().addAll(arguments);
        }else{
            @SuppressWarnings("unchecked")
            List<AnnotationMirror> namedArguments = (List<AnnotationMirror>) annotation.getValue("namedArguments");
            if(namedArguments != null){
                for(AnnotationMirror namedArgument : namedArguments){
                    String argName = (String) namedArgument.getValue("name");
                    String argValue = (String) namedArgument.getValue("value");
                    modelAnnotation.getNamedArguments().put(argName, argValue);
                }
            }
        }
        return modelAnnotation;
    }

    private void addInnerClasses(ClassOrInterface klass, ClassMirror classMirror) {
        AnnotationMirror membersAnnotation = classMirror.getAnnotation(CEYLON_MEMBERS_ANNOTATION);
        if(membersAnnotation == null)
            addInnerClassesFromMirror(klass, classMirror);
        else
            addInnerClassesFromAnnotation(klass, membersAnnotation);
    }

    private void addInnerClassesFromAnnotation(ClassOrInterface klass, AnnotationMirror membersAnnotation) {
        List<AnnotationMirror> members = (List<AnnotationMirror>) membersAnnotation.getValue();
        for(AnnotationMirror member : members){
            String name = (String) member.getValue("name");
            String javaClass = (String) member.getValue("javaClass");
            String packageName = (String) member.getValue("packageName");
            String javaClassName = assembleJavaClass(javaClass, packageName);
            Declaration innerDecl = convertToDeclaration(javaClassName, DeclarationType.TYPE);
            if(innerDecl == null)
                throw new ModelResolutionException("Failed to load inner type " + name 
                        + " for outer type " + klass.getQualifiedNameString() 
                        + ", java class: " + javaClass);
        }
    }

    /**
     * Allows subclasses to do something to the class name
     */
    protected String assembleJavaClass(String javaClass, String packageName) {
        return javaClass;
    }

    private void addInnerClassesFromMirror(ClassOrInterface klass, ClassMirror classMirror) {
        boolean isJDK = isFromJDK(classMirror);
        for(ClassMirror innerClass : classMirror.getDirectInnerClasses()){
            // We skip members marked with @Ignore
            if(innerClass.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            // We skip anonymous inner classes
            if(innerClass.isAnonymous())
                continue;
            // We skip private classes, otherwise the JDK has a ton of unresolved things
            if(isJDK && !innerClass.isPublic())
                continue;
            Declaration innerDecl = convertToDeclaration(innerClass, DeclarationType.TYPE);
            // no need to set its container as that's now handled by convertToDeclaration
        }
    }

    private Method addMethod(ClassOrInterface klass, MethodMirror methodMirror, boolean isCeylon, boolean isOverloaded) {
        JavaMethod method = new JavaMethod();
        
        method.setContainer(klass);
        method.setRealName(methodMirror.getName());
        method.setName(Util.strip(methodMirror.getName()));
        method.setUnit(klass.getUnit());
        method.setOverloaded(isOverloaded);
        setMethodOrValueFlags(klass, methodMirror, method);

        // type params first
        setTypeParameters(method, methodMirror);

        // now its parameters
        if(isEqualsMethod(methodMirror))
            setEqualsParameters(method, methodMirror);
        else
            setParameters(method, methodMirror, isCeylon, klass);
        
        // and its return type
        try{
            ProducedType type = obtainType(methodMirror.getReturnType(), methodMirror, method, VarianceLocation.COVARIANT);
            method.setType(type);
            method.setUncheckedNullType(!isCeylon && !methodMirror.getReturnType().isPrimitive());
            method.setDeclaredAnything(methodMirror.isDeclaredVoid());
            type.setRaw(methodMirror.getReturnType().isRaw());
        }catch(TypeParserException x){
            logError("Invalid type signature for method return type of "+klass.getQualifiedNameString()+"."+methodMirror.getName()+": "+x.getMessage());
            throw x;
        }
        
        markUnboxed(method, methodMirror.getReturnType());
        markTypeErased(method, methodMirror, methodMirror.getReturnType());
        setAnnotations(method, methodMirror);
        
        klass.getMembers().add(method);
        
        return method;
    }

    private void fillRefinedDeclarations(ClassOrInterface klass) {
        for(Declaration member : klass.getMembers()){
            // do not trigger a type load (by calling isActual()) for Java inner classes since they
            // can never be actual
            if(member instanceof ClassOrInterface && !Decl.isCeylon((ClassOrInterface)member))
                continue;
            if(member.isActual()){
                member.setRefinedDeclaration(findRefinedDeclaration(klass, member.getName(), getSignature(member), false));
            }
        }
    }

    private List<ProducedType> getSignature(Declaration decl) {
        List<ProducedType> result = null;
        if (decl instanceof Functional) {
            Functional func = (Functional)decl;
            if (func.getParameterLists().size() > 0) {
                List<Parameter> params = func.getParameterLists().get(0).getParameters();
                result = new ArrayList<ProducedType>(params.size());
                for (Parameter p : params) {
                    result.add(p.getType());
                }
            }
        }
        return result;
    }
    
    private Declaration findRefinedDeclaration(ClassOrInterface decl, String name, List<ProducedType> signature, boolean ellipsis) {
        Declaration refinedDeclaration = decl.getRefinedMember(name, signature, ellipsis);
        if(refinedDeclaration == null)
            throw new ModelResolutionException("Failed to find refined declaration for "+name);
        return refinedDeclaration;
    }

    private boolean isStartOfJavaBeanPropertyName(char c){
        return Character.isUpperCase(c) || c == '_'; 
    }
    
    private boolean isGetter(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesGet = name.length() > 3 && name.startsWith("get") && isStartOfJavaBeanPropertyName(name.charAt(3));
        boolean matchesIs = name.length() > 2 && name.startsWith("is") && isStartOfJavaBeanPropertyName(name.charAt(2));
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        boolean hasNonVoidReturn = (methodMirror.getReturnType().getKind() != TypeKind.VOID);
        return (matchesGet || matchesIs) && hasNoParams && hasNonVoidReturn;
    }
    
    private boolean isSetter(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesSet = name.length() > 3 && name.startsWith("set") && isStartOfJavaBeanPropertyName(name.charAt(3));
        boolean hasOneParam = methodMirror.getParameters().size() == 1;
        boolean hasVoidReturn = (methodMirror.getReturnType().getKind() == TypeKind.VOID);
        return matchesSet && hasOneParam && hasVoidReturn;
    }

    private boolean isHashAttribute(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesName = "hashCode".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }
    
    private boolean isStringAttribute(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesName = "toString".equals(name);
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        return matchesName && hasNoParams;
    }

    private boolean isEqualsMethod(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        if(!"equals".equals(name)
                || methodMirror.getParameters().size() != 1)
            return false;
        VariableMirror param = methodMirror.getParameters().get(0);
        return sameType(param.getType(), OBJECT_TYPE);
    }

    private void setEqualsParameters(Method decl, MethodMirror methodMirror) {
        ParameterList parameters = new ParameterList();
        decl.addParameterList(parameters);
        ValueParameter parameter = new ValueParameter();
        parameter.setUnit(decl.getUnit());
        parameter.setContainer((Scope) decl);
        parameter.setName("that");
        parameter.setType(getType(CEYLON_OBJECT_TYPE, decl, VarianceLocation.INVARIANT));
        parameter.setDeclaration((Declaration) decl);
        parameters.getParameters().add(parameter);
    }

    private String getJavaAttributeName(String getterName) {
        if (getterName.startsWith("get") || getterName.startsWith("set")) {
            return getJavaBeanName(getterName.substring(3));
        } else if (getterName.startsWith("is")) {
            // Starts with "is"
            return getJavaBeanName(getterName.substring(2));
        } else {
            throw new RuntimeException("Illegal java getter/setter name");
        }
    }

    private String getJavaBeanName(String name) {
        // See https://github.com/ceylon/ceylon-compiler/issues/340
        // make it lowercase until the first non-uppercase
        char[] newName = name.toCharArray();
        for(int i=0;i<newName.length;i++){
            char c = newName[i];
            if(Character.isLowerCase(c)){
                // if we had more than one upper-case, we leave the last uppercase: getURLDecoder -> urlDecoder
                if(i > 1){
                    newName[i-1] = Character.toUpperCase(newName[i-1]);
                }
                break;
            }
            newName[i] = Character.toLowerCase(c);
        }
        return new String(newName);
    }

    private void addValue(ClassOrInterface klass, FieldMirror fieldMirror, boolean isCeylon) {
        // make sure it's a FieldValue so we can figure it out in the backend
        Value value = new FieldValue();
        value.setContainer(klass);
        value.setName(fieldMirror.getName());
        value.setUnit(klass.getUnit());
        value.setShared(fieldMirror.isPublic() || fieldMirror.isProtected());
        value.setProtectedVisibility(fieldMirror.isProtected());
        value.setStaticallyImportable(fieldMirror.isStatic());
        // field can't be abstract or interface, so not formal
        // can we override fields? good question. Not really, but from an external point of view?
        // FIXME: figure this out: (default)
        // FIXME: for the same reason, can it be an overriding field? (actual)
        value.setVariable(!fieldMirror.isFinal());
        try{
            ProducedType type = obtainType(fieldMirror.getType(), fieldMirror, klass, VarianceLocation.INVARIANT);
            value.setType(type);
            value.setUncheckedNullType(!isCeylon && !fieldMirror.getType().isPrimitive());
            type.setRaw(fieldMirror.getType().isRaw());
        }catch(TypeParserException x){
            logError("Invalid type signature for field "+klass.getQualifiedNameString()+"."+value.getName()+": "+x.getMessage());
            throw x;
        }
        markUnboxed(value, fieldMirror.getType());
        markTypeErased(value, fieldMirror, fieldMirror.getType());
        klass.getMembers().add(value);
    }
    
    private void addValue(ClassOrInterface klass, MethodMirror methodMirror, String methodName, boolean isCeylon) {
        JavaBeanValue value = new JavaBeanValue();
        value.setGetterName(methodMirror.getName());
        value.setContainer(klass);
        value.setName(methodName);
        value.setUnit(klass.getUnit());
        setMethodOrValueFlags(klass, methodMirror, value);
        try{
            ProducedType type = obtainType(methodMirror.getReturnType(), methodMirror, klass, VarianceLocation.INVARIANT);
            value.setType(type);
            value.setUncheckedNullType(!isCeylon && !methodMirror.getReturnType().isPrimitive());
            type.setRaw(methodMirror.getReturnType().isRaw());
        }catch(TypeParserException x){
            logError("Invalid type signature for getter type of "+klass.getQualifiedNameString()+"."+methodName+": "+x.getMessage());
            throw x;
        }
        markUnboxed(value, methodMirror.getReturnType());
        markTypeErased(value, methodMirror, methodMirror.getReturnType());
        setAnnotations(value, methodMirror);
        klass.getMembers().add(value);
    }

    private void setMethodOrValueFlags(ClassOrInterface klass, MethodMirror methodMirror, MethodOrValue decl) {
        decl.setShared(methodMirror.isPublic() || methodMirror.isProtected());
        decl.setProtectedVisibility(methodMirror.isProtected());
        if(// for class members we rely on abstract bit
           (klass instanceof Class 
                   && methodMirror.isAbstract())
           // Java interfaces are formal
           || (klass instanceof Interface
                   && !((LazyInterface)klass).isCeylon())
           // For Ceylon interfaces we rely on annotation
           || isAnnotated(methodMirror, "formal")) {
            decl.setFormal(true);
        } else {
            if (// for class members we rely on final/static bits
                (klass instanceof Class
                        && !methodMirror.isFinal() 
                        && !methodMirror.isStatic())
                // Java interfaces are never final
                || (klass instanceof Interface
                        && !((LazyInterface)klass).isCeylon())
                // For Ceylon interfaces we rely on annotation
                || isAnnotated(methodMirror, "default")){
                decl.setDefault(true);
            }
        }
        decl.setStaticallyImportable(methodMirror.isStatic());
        if(isOverridingMethod(methodMirror)
                // For Ceylon interfaces we rely on annotation
                || (klass instanceof LazyInterface 
                        && ((LazyInterface)klass).isCeylon()
                        && isAnnotated(methodMirror, "actual"))){
            decl.setActual(true);
        }
    }
    
    private boolean isAnnotated(AnnotatedMirror annotatedMirror, String name) {
        AnnotationMirror annotations = annotatedMirror.getAnnotation(CEYLON_ANNOTATIONS_ANNOTATION);
        if(annotations == null)
            return false;
        @SuppressWarnings("unchecked")
        List<AnnotationMirror> annotationsList = (List<AnnotationMirror>)annotations.getValue();
        for(AnnotationMirror annotation : annotationsList){
            if(name.equals(annotation.getValue()))
                return true;
        }
        return false;
    }

    private void setExtendedType(ClassOrInterface klass, ClassMirror classMirror) {
        // look at its super type
        TypeMirror superClass = classMirror.getSuperclass();
        ProducedType extendedType;
        
        if(klass instanceof Interface){
            // interfaces need to have their superclass set to Object
            if(superClass == null || superClass.getKind() == TypeKind.NONE)
                extendedType = getType(CEYLON_OBJECT_TYPE, klass, VarianceLocation.INVARIANT);
            else
                extendedType = getType(superClass, klass, VarianceLocation.INVARIANT);
        }else{
            String className = classMirror.getQualifiedName();
            String superClassName = superClass == null ? null : superClass.getQualifiedName();
            if(className.equals("ceylon.language.Anything")){
                // ceylon.language.Anything has no super type
                extendedType = null;
            }else if(className.equals("java.lang.Object")){
                // we pretend its superclass is something else, but note that in theory we shouldn't 
                // be seeing j.l.Object at all due to unerasure
                extendedType = getType(CEYLON_BASIC_TYPE, klass, VarianceLocation.INVARIANT);
            }else{
                // read it from annotation first
                String annotationSuperClassName = getAnnotationStringValue(classMirror, CEYLON_CLASS_ANNOTATION, "extendsType");
                if(annotationSuperClassName != null && !annotationSuperClassName.isEmpty()){
                    try{
                        extendedType = decodeType(annotationSuperClassName, klass);
                    }catch(TypeParserException x){
                        logError("Invalid type signature for super class of "+className+": "+x.getMessage());
                        throw x;
                    }
                }else{
                    // read it from the Java super type
                    // now deal with type erasure, avoid having Object as superclass
                    if("java.lang.Object".equals(superClassName)){
                        extendedType = getType(CEYLON_BASIC_TYPE, klass, VarianceLocation.INVARIANT);
                    }else{
                        extendedType = getType(superClass, klass, VarianceLocation.INVARIANT);
                    }
                }
            }
        }
        if(extendedType != null)
            klass.setExtendedType(extendedType);
    }

    private void setParameters(Functional decl, MethodMirror methodMirror, boolean isCeylon, Scope container) {
        ParameterList parameters = new ParameterList();
        parameters.setNamedParametersSupported(isCeylon);
        decl.addParameterList(parameters);
        int parameterCount = methodMirror.getParameters().size();
        int parameterIndex = 0;
        
        for(VariableMirror paramMirror : methodMirror.getParameters()){
            // ignore some parameters
            if(paramMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            
            boolean isLastParameter = parameterIndex == parameterCount - 1;
            boolean isVariadic = isLastParameter && methodMirror.isVariadic();
            
            ValueParameter parameter = new ValueParameter();
            parameter.setContainer((Scope) decl);
            DeclarationVisitor.setVisibleScope(parameter);
            parameter.setUnit(((Element)decl).getUnit());
            if(decl instanceof Class){
                ((Class)decl).getMembers().add(parameter);
            }
            String paramName = getAnnotationStringValue(paramMirror, CEYLON_NAME_ANNOTATION);
            // use whatever param name we find as default
            if(paramName == null)
                paramName = paramMirror.getName();
            parameter.setName(paramName);
            TypeMirror typeMirror = paramMirror.getType();
            try{
                ProducedType type = obtainType(typeMirror, paramMirror, (Scope) decl, VarianceLocation.CONTRAVARIANT);
                if(isVariadic){
                    // we have a varargs param, we should have an Array<T> type, which we need to turn into Iterable<T>
                    if(type.getDeclaration() != typeFactory.getArrayDeclaration()
                            || type.getTypeArgumentList().isEmpty()){
                        logError("Variadic argument is not of type Array<T>, this is most likely a bug in the compiler");
                    }else{
                        // get its first type param
                        type = type.getTypeArgumentList().get(0);
                        // possibly make it optional
                        TypeMirror variadicType = typeMirror.getComponentType();
                        if(!isCeylon && !variadicType.isPrimitive()){
                            // Java parameters are all optional unless primitives
                            ProducedType optionalType = typeFactory.getOptionalType(type);
                            optionalType.setUnderlyingType(type.getUnderlyingType());
                            type = optionalType;
                        }
                        // turn it into a Sequential<T>
                        type = typeFactory.getSequentialType(type);
                    }
                }else{
                    // variadic params may technically be null in Java, but it Ceylon sequenced params may not
                    // so it breaks the typechecker logic for handling them, and it will always be a case of bugs
                    // in the java side so let's not allow this
                    if(!isCeylon && !typeMirror.isPrimitive()){
                        // Java parameters are all optional unless primitives
                        ProducedType optionalType = typeFactory.getOptionalType(type);
                        optionalType.setUnderlyingType(type.getUnderlyingType());
                        type = optionalType;
                    }
                }
                parameter.setType(type );
            }catch(TypeParserException x){
                logError("Invalid type signature for parameter "+paramName+" of "+container.getQualifiedNameString()+"."+methodMirror.getName()+": "+x.getMessage());
                throw x;
            }
            if(paramMirror.getAnnotation(CEYLON_SEQUENCED_ANNOTATION) != null
                    || isVariadic)
                parameter.setSequenced(true);
            if(paramMirror.getAnnotation(CEYLON_DEFAULTED_ANNOTATION) != null)
                parameter.setDefaulted(true);
            // if it's variadic, consider the array element type (T[] == T...) for boxing rules
            markUnboxed(parameter, isVariadic ? 
                    paramMirror.getType().getComponentType()
                    : paramMirror.getType());
            parameter.setDeclaration((Declaration) decl);
            parameters.getParameters().add(parameter);
            
            parameterIndex++;
        }
    }

    private void markTypeErased(TypedDeclaration decl, AnnotatedMirror typedMirror, TypeMirror type) {
        if (getAnnotationBooleanValue(typedMirror, CEYLON_TYPE_INFO_ANNOTATION, "erased") == Boolean.TRUE) {
            decl.setTypeErased(true);
        } else {
            decl.setTypeErased(sameType(type, OBJECT_TYPE));
        }
    }

    private void markUnboxed(TypedDeclaration decl, TypeMirror type) {
        boolean unboxed = false;
        if(type.isPrimitive() 
                || type.getKind() == TypeKind.ARRAY
                || sameType(type, STRING_TYPE)
                || Util.isUnboxedVoid(decl)) {
            unboxed = true;
        }
        decl.setUnboxed(unboxed);
    }

    @Override
    public synchronized void complete(LazyValue value) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            MethodMirror meth = null;
            for (MethodMirror m : value.classMirror.getDirectMethods()) {
                // We skip members marked with @Ignore
                if(m.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                    continue;

                if (m.getName().equals(
                        Naming.getGetterName(value))
                        && m.isStatic() && m.getParameters().size() == 0) {
                    meth = m;
                }
                if (m.getName().equals(
                        Naming.getSetterName(value))
                        && m.isStatic() && m.getParameters().size() == 1) {
                    value.setVariable(true);
                }
            }
            if(meth == null || meth.getReturnType() == null)
                throw new ModelResolutionException("Failed to find toplevel attribute "+value.getName());

            try{
                value.setType(obtainType(meth.getReturnType(), meth, null, VarianceLocation.INVARIANT));
            }catch(TypeParserException x){
                logError("Invalid type signature for toplevel attribute of "+value.getQualifiedNameString()+": "+x.getMessage());
                throw x;
            }
            setAnnotations(value, meth);
            markUnboxed(value, meth.getReturnType());
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
    }

    @Override
    public synchronized void complete(LazyMethod method) {
        timer.startIgnore(TIMER_MODEL_LOADER_CATEGORY);
        try{
            MethodMirror meth = null;
            String lookupName = method.getName();
            for(MethodMirror m : method.classMirror.getDirectMethods()){
                // We skip members marked with @Ignore
                if(m.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                    continue;

                if(Util.strip(m.getName()).equals(lookupName)){
                    meth = m;
                    break;
                }
            }
            if(meth == null || meth.getReturnType() == null)
                throw new ModelResolutionException("Failed to find toplevel method "+method.getName());

            // type params first
            setTypeParameters(method, meth);

            // now its parameters
            setParameters(method, meth, true /* toplevel methods are always Ceylon */, method);
            try{
                method.setType(obtainType(meth.getReturnType(), meth, method, VarianceLocation.COVARIANT));
                method.setDeclaredAnything(meth.isDeclaredVoid());
            }catch(TypeParserException x){
                logError("Invalid type signature for toplevel method of "+method.getQualifiedNameString()+": "+x.getMessage());
                throw x;
            }
            markUnboxed(method, meth.getReturnType());
            markTypeErased(method, meth, meth.getReturnType());

            setAnnotations(method, meth);
        }finally{
            timer.stopIgnore(TIMER_MODEL_LOADER_CATEGORY);
        }
     }
    
    //
    // Satisfied Types
    
    private List<String> getSatisfiedTypesFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationArrayValue(symbol, CEYLON_SATISFIED_TYPES_ANNOTATION);
    }
    
    private void setSatisfiedTypes(ClassOrInterface klass, ClassMirror classMirror) {
        List<String> satisfiedTypes = getSatisfiedTypesFromAnnotations(classMirror);
        if(satisfiedTypes != null){
            try{
                klass.getSatisfiedTypes().addAll(getTypesList(satisfiedTypes, klass));
            }catch(TypeParserException x){
                logError("Invalid type signature for satisfied types of "+klass.getQualifiedNameString()+": "+x.getMessage());
                throw x;
            }
        }else{
            for(TypeMirror iface : classMirror.getInterfaces()){
                // ignore ReifiedType interfaces
                if(sameType(iface, CEYLON_REIFIED_TYPE_TYPE))
                    continue;
                try{
                    klass.getSatisfiedTypes().add(getType(iface, klass, VarianceLocation.INVARIANT));
                }catch(ModelResolutionException x){
                    PackageMirror classPackage = classMirror.getPackage();
                    if(JDKUtils.isJDKAnyPackage(classPackage.getQualifiedName())){
                        if(iface.getKind() == TypeKind.DECLARED){
                            // check if it's a JDK thing
                            ClassMirror ifaceClass = iface.getDeclaredClass();
                            PackageMirror ifacePackage = ifaceClass.getPackage();
                            if(JDKUtils.isOracleJDKAnyPackage(ifacePackage.getQualifiedName())){
                                // just log and ignore it
                                logMissingOracleType(iface.getQualifiedName());
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }

    //
    // Case Types
    
    private List<String> getCaseTypesFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationArrayValue(symbol, CEYLON_CASE_TYPES_ANNOTATION);
    }
    
    private String getSelfTypeFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationStringValue(symbol, CEYLON_CASE_TYPES_ANNOTATION, "of");
    }

    private void setCaseTypes(ClassOrInterface klass, ClassMirror classMirror) {
        String selfType = getSelfTypeFromAnnotations(classMirror);
        if(selfType != null && !selfType.isEmpty()){
            try{
                ProducedType type = decodeType(selfType, klass);
                if(!(type.getDeclaration() instanceof TypeParameter)){
                    logError("Invalid type signature for self type of "+klass.getQualifiedNameString()+": "+selfType+" is not a type parameter");
                }else{
                    klass.setSelfType(type);
                    List<ProducedType> caseTypes = new LinkedList<ProducedType>();
                    caseTypes.add(type);
                    klass.setCaseTypes(caseTypes);
                }
            }catch(TypeParserException x){
                logError("Invalid type signature for self type of "+klass.getQualifiedNameString()+": "+x.getMessage());
                throw x;
            }
        } else {
            List<String> caseTypes = getCaseTypesFromAnnotations(classMirror);
            if(caseTypes != null && !caseTypes.isEmpty()){
                try{
                    klass.setCaseTypes(getTypesList(caseTypes, klass));
                }catch(TypeParserException x){
                    logError("Invalid type signature for case types of "+klass.getQualifiedNameString()+": "+x.getMessage());
                    throw x;
                }
            }
        }
    }

    private List<ProducedType> getTypesList(List<String> caseTypes, Scope scope) {
        List<ProducedType> producedTypes = new LinkedList<ProducedType>();
        for(String type : caseTypes){
            producedTypes.add(decodeType(type, scope));
        }
        return producedTypes;
    }

    //
    // Type parameters loading

    @SuppressWarnings("unchecked")
    private List<AnnotationMirror> getTypeParametersFromAnnotations(AnnotatedMirror symbol) {
        return (List<AnnotationMirror>) getAnnotationValue(symbol, CEYLON_TYPE_PARAMETERS);
    }

    // from our annotation
    @SuppressWarnings("deprecation")
    private void setTypeParametersFromAnnotations(Scope scope, List<TypeParameter> params, AnnotatedMirror mirror, List<AnnotationMirror> typeParameters) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        String selfTypeName = getSelfTypeFromAnnotations(mirror);
        for(AnnotationMirror typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            param.setDeclaration((Declaration) scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName((String)typeParam.getValue("value"));
            param.setExtendedType(typeFactory.getAnythingDeclaration().getType());
            
            String varianceName = (String) typeParam.getValue("variance");
            if(varianceName != null){
                if(varianceName.equals("IN")){
                    param.setContravariant(true);
                }else if(varianceName.equals("OUT"))
                    param.setCovariant(true);
            }
            
            // If this is a self type param then link it to its type's declaration
            if (param.getName().equals(selfTypeName)) {
                param.setSelfTypedDeclaration((TypeDeclaration)scope);
            }
            
            params.add(param);
        }
        
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(AnnotationMirror typeParam : typeParameters){
            TypeParameter param = paramsIterator.next();
            
            @SuppressWarnings("unchecked")
            List<String> satisfiesAttribute = (List<String>)typeParam.getValue("satisfies");
            setListOfTypes(param.getSatisfiedTypes(), satisfiesAttribute, scope,
                    "Invalid type signature for satisfies of type parameter "+param.getName()+" of "+scope.getQualifiedNameString()+": ");

            @SuppressWarnings("unchecked")
            List<String> caseTypesAttribute = (List<String>)typeParam.getValue("caseTypes");
            if(caseTypesAttribute != null && !caseTypesAttribute.isEmpty())
                param.setCaseTypes(new LinkedList<ProducedType>());
            setListOfTypes(param.getCaseTypes(), caseTypesAttribute, scope,
                    "Invalid type signature for case types of type parameter "+param.getName()+" of "+scope.getQualifiedNameString()+": ");

            @SuppressWarnings("unchecked")
            String defaultValueAttribute = (String)typeParam.getValue("defaultValue");
            if(defaultValueAttribute != null && !defaultValueAttribute.isEmpty()){
                try{
                    ProducedType decodedType = decodeType(defaultValueAttribute, scope);
                    param.setDefaultTypeArgument(decodedType);
                    param.setDefaulted(true);
                }catch(TypeParserException x){
                    logError("Failed to decode defaultValue for @TypeParameter: "+defaultValueAttribute);
                    throw x;
                }
            }
        }
    }

    private void setListOfTypes(List<ProducedType> destinationTypeList, List<String> serialisedTypes, Scope scope, String errorPrefix) {
        if(serialisedTypes != null){
            for (String serialisedType : serialisedTypes) {
                try{
                    ProducedType decodedType = decodeType(serialisedType, scope);
                    destinationTypeList.add(decodedType);
                }catch(TypeParserException x){
                    logError(errorPrefix+x.getMessage());
                    throw x;
                }
            }
        }
    }

    // from java type info
    @SuppressWarnings("deprecation")
    private void setTypeParameters(Scope scope, List<TypeParameter> params, List<TypeParameterMirror> typeParameters) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        for(TypeParameterMirror typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            param.setDeclaration((Declaration) scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName(typeParam.getName());
            param.setExtendedType(typeFactory.getAnythingDeclaration().getType());
            params.add(param);
        }
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(TypeParameterMirror typeParam : typeParameters){
            TypeParameter param = paramsIterator.next();
            List<TypeMirror> bounds = typeParam.getBounds();
            for(TypeMirror bound : bounds){
                ProducedType boundType;
                // we turn java's default upper bound java.lang.Object into ceylon.language.Object
                if(sameType(bound, OBJECT_TYPE)){
                    // avoid adding java's default upper bound if it's just there with no meaning
                    if(bounds.size() == 1)
                        break;
                    boundType = getType(CEYLON_OBJECT_TYPE, scope, VarianceLocation.INVARIANT);
                }else
                    boundType = getType(bound, scope, VarianceLocation.INVARIANT);
                param.getSatisfiedTypes().add(boundType);
            }
        }
    }

    // method
    private void setTypeParameters(Method method, MethodMirror methodMirror) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        method.setTypeParameters(params);
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(methodMirror);
        if(typeParameters != null) {
            setTypeParametersFromAnnotations(method, params, methodMirror, typeParameters);
        } else {
            setTypeParameters(method, params, methodMirror.getTypeParameters());
        }
    }

    // class
    private void setTypeParameters(TypeDeclaration klass, ClassMirror classMirror) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        klass.setTypeParameters(params);
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(classMirror);
        if(typeParameters != null) {
            setTypeParametersFromAnnotations(klass, params, classMirror, typeParameters);
        } else {
            setTypeParameters(klass, params, classMirror.getTypeParameters());
        }
    }        

    //
    // TypeParsing and ModelLoader

    private ProducedType decodeType(String value, Scope scope) {
        try{
            return typeParser.decodeType(value, scope);
        }catch(TypeParserException x){
            logError(x.getMessage());
        }catch(ModelResolutionException x){
            logError(x.getMessage());
        }
        return new UnknownType(typeFactory).getType();
    }
    
    /** Warning: only valid for toplevel types, not for type parameters */
    private ProducedType obtainType(TypeMirror type, AnnotatedMirror symbol, Scope scope, VarianceLocation variance) {
        String typeName = getAnnotationStringValue(symbol, CEYLON_TYPE_INFO_ANNOTATION);
        if (typeName != null) {
            ProducedType ret = decodeType(typeName, scope);
            // even decoded types need to fit with the reality of the underlying type
            ret.setUnderlyingType(getUnderlyingType(type, TypeLocation.TOPLEVEL));
            return ret;
        } else {
            return obtainType(type, scope, TypeLocation.TOPLEVEL, variance);
        }
    }
    
    private enum TypeLocation {
        TOPLEVEL, TYPE_PARAM;
    }
    
    private enum VarianceLocation {
        /**
         * Used in parameter
         */
        CONTRAVARIANT,
        /**
         * Used in method return value
         */
        COVARIANT,
        /**
         * For field
         */
        INVARIANT;
    }

    private String getUnderlyingType(TypeMirror type, TypeLocation location){
        // don't erase to c.l.String if in a type param location
        if ((sameType(type, STRING_TYPE) && location != TypeLocation.TYPE_PARAM)
            || sameType(type, PRIM_BYTE_TYPE)
            || sameType(type, PRIM_SHORT_TYPE)
            || sameType(type, PRIM_INT_TYPE)
            || sameType(type, PRIM_FLOAT_TYPE)
            || sameType(type, PRIM_CHAR_TYPE)) {
            return type.getQualifiedName();
        }
        return null;
    }
    
    private ProducedType obtainType(TypeMirror type, Scope scope, TypeLocation location, VarianceLocation variance) {
        TypeMirror originalType = type;
        // ERASURE
        
        // don't erase to c.l.String if in a type param location
        if (sameType(type, STRING_TYPE) && location != TypeLocation.TYPE_PARAM) {
            type = CEYLON_STRING_TYPE;
            
        } else if (sameType(type, PRIM_BOOLEAN_TYPE)) {
            type = CEYLON_BOOLEAN_TYPE;
            
        } else if (sameType(type, PRIM_BYTE_TYPE)) {
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_SHORT_TYPE)) {
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_INT_TYPE)) {
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_LONG_TYPE)) {
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_FLOAT_TYPE)) {
            type = CEYLON_FLOAT_TYPE;
            
        } else if (sameType(type, PRIM_DOUBLE_TYPE)) {
            type = CEYLON_FLOAT_TYPE;
            
        } else if (sameType(type, PRIM_CHAR_TYPE)) {
            type = CEYLON_CHARACTER_TYPE;
            
        } else if (sameType(type, OBJECT_TYPE)) {
            type = CEYLON_OBJECT_TYPE;
        }
        
        ProducedType ret = getType(type, scope, variance);
        if (ret.getUnderlyingType() == null) {
            ret.setUnderlyingType(getUnderlyingType(originalType, location));
        }
        return ret;
    }
    
    private boolean sameType(TypeMirror t1, TypeMirror t2) {
        // make sure we deal with arrays which can't have a qualified name
        if(t1.getKind() == TypeKind.ARRAY){
            if(t2.getKind() != TypeKind.ARRAY)
                return false;
            return sameType(t1.getComponentType(), t2.getComponentType());
        }
        if(t2.getKind() == TypeKind.ARRAY)
            return false;
        // the rest should be OK
        return t1.getQualifiedName().equals(t2.getQualifiedName());
    }
    
    @Override
    public Declaration getDeclaration(String typeName, DeclarationType declarationType) {
        return convertToDeclaration(typeName, declarationType);
    }

    private ProducedType getType(TypeMirror type, Scope scope, VarianceLocation variance) {
        Declaration decl = convertToDeclaration(type, scope, DeclarationType.TYPE);
        TypeDeclaration declaration = (TypeDeclaration) decl;
        if (type.getKind() == TypeKind.ARRAY) {
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(1);
            TypeMirror ct = type.getComponentType();
            // Although we are putting that type into a type param, we have special handling
            // for arrays and so we should treat it as a toplevel (magic boxing of strings)
            ProducedType ctpt = (ProducedType) obtainType(ct, scope, TypeLocation.TOPLEVEL, variance);
            typeArguments.add(ctpt);
            ProducedType arrayType = declaration.getProducedType(getQualifyingType(declaration), typeArguments);
            if (ctpt.getUnderlyingType() != null) {
                arrayType.setUnderlyingType(ctpt.getUnderlyingType() + "[]");
            }
            return arrayType;
        } else {
            List<TypeMirror> javacTypeArguments = type.getTypeArguments();
            if(!javacTypeArguments.isEmpty()){
                List<ProducedType> typeArguments = new ArrayList<ProducedType>(javacTypeArguments.size());
                for(TypeMirror typeArgument : javacTypeArguments){
                    // if a single type argument is a wildcard, we erase to Object
                    if(typeArgument.getKind() == TypeKind.WILDCARD){
                        if(variance == VarianceLocation.CONTRAVARIANT || Decl.isCeylon(declaration)){
                            TypeMirror bound = typeArgument.getUpperBound();
                            if(bound == null)
                                bound = typeArgument.getLowerBound();
                            // if it has no bound let's take Object
                            if(bound == null){
                                // add the type arg and move to the next one
                                typeArguments.add(typeFactory.getObjectDeclaration().getType());
                                continue;
                            }
                            typeArgument = bound;
                        }else
                            return typeFactory.getObjectDeclaration().getType();
                    }
                    typeArguments.add((ProducedType) obtainType(typeArgument, scope, TypeLocation.TYPE_PARAM, variance));
                }
                return declaration.getProducedType(getQualifyingType(declaration), typeArguments);
            }
            return declaration.getType();
        }
    }

    private ProducedType getQualifyingType(TypeDeclaration declaration) {
        // As taken from ProducedType.getType():
        if (declaration.isMember()) {
            return((ClassOrInterface) declaration.getContainer()).getType();
        }
        return null;
    }

    @Override
    public synchronized ProducedType getType(String pkgName, String name, Scope scope) {
        if(scope != null){
            TypeParameter typeParameter = lookupTypeParameter(scope, name);
            if(typeParameter != null)
                return typeParameter.getType();
        }
        if(!isBootstrap || !name.startsWith(CEYLON_LANGUAGE)) {
            if(scope != null && pkgName != null){
                Package containingPackage = Decl.getPackageContainer(scope);
                Package pkg = containingPackage.getModule().getPackage(pkgName);
                String relativeName = null;
                String unquotedName = name.replace("$", "");
                if(!pkgName.isEmpty()){
                    if(unquotedName.startsWith(pkgName+"."))
                        relativeName = unquotedName.substring(pkgName.length()+1);
                    // else we don't try it's not in this package
                }else
                    relativeName = unquotedName;
                if(relativeName != null && pkg != null){
                    Declaration declaration = pkg.getDirectMember(relativeName, null, false);
                    // if we get a value, we want its type
                    if(declaration instanceof Value
                            && ((Value)declaration).getTypeDeclaration().getName().equals(relativeName))
                        declaration = ((Value)declaration).getTypeDeclaration();
                    if(declaration instanceof TypeDeclaration)
                        return ((TypeDeclaration)declaration).getType();
                    // if we have something but it's not a type decl, it's a:
                    // - value that's not an object (why would we get its type here?)
                    // - method (doesn't have a type of the same name)
                    if(declaration != null)
                        return null;
                }
            }
            Declaration declaration = convertToDeclaration(name, DeclarationType.TYPE);
            if(declaration instanceof TypeDeclaration)
                return ((TypeDeclaration)declaration).getType();
            // we're looking for type declarations, so anything else doesn't work for us
            return null;
        }
        
        // make sure we don't return anything for ceylon.language
        if(name.equals(CEYLON_LANGUAGE))
            return null;
        
        // we're bootstrapping ceylon.language so we need to return the ProducedTypes straight from the model we're compiling
        Module languageModule = modules.getLanguageModule();
        String simpleName = name.substring(name.lastIndexOf(".")+1);
        // Nothing is a special case with no real decl
        if(name.equals("ceylon.language.Nothing"))
            return typeFactory.getNothingDeclaration().getType();
        for(Package pkg : languageModule.getPackages()){
            Declaration member = pkg.getDirectMember(simpleName, null, false);
            // if we get a value, we want its type
            if(member instanceof Value
                    && ((Value)member).getTypeDeclaration().getName().equals(simpleName)){
                member = ((Value)member).getTypeDeclaration();
            }
            if(member instanceof TypeDeclaration)
                return ((TypeDeclaration)member).getType();
        }
        throw new ModelResolutionException("Failed to look up given type in language module while bootstrapping: "+name);
    }

    public synchronized void removeDeclarations(List<Declaration> declarations) {
        List<String> keysToRemove = new ArrayList<String>();
        for (String name : declarationsByName.keySet()) {
            String nameWithoutPrefix = name.substring(1);
            for (Declaration decl : declarations) {
                if (nameWithoutPrefix.equals(decl.getQualifiedNameString())) {
                    keysToRemove.add(name);
                }
            }
        }
        for (String keyToRemove : keysToRemove) {
            declarationsByName.remove(keyToRemove);
        }
        
        for (Declaration decl : declarations) {
            if (decl instanceof LazyClass || decl instanceof LazyInterface) {
                classMirrorCache.remove(decl.getQualifiedNameString());
            }
        }
    }
    
    public synchronized void printStats(){
        int loaded = 0;
        class Stats {
            int loaded, total;
        }
        Map<Package, Stats> loadedByPackage = new HashMap<Package, Stats>();
        for(Declaration decl : declarationsByName.values()){
            if(decl instanceof LazyElement){
                Package pkg = getPackage(decl);
                Stats stats = loadedByPackage.get(pkg);
                if(stats == null){
                    stats = new Stats();
                    loadedByPackage.put(pkg, stats);
                }
                stats.total++;
                if(((LazyElement)decl).isLoaded()){
                    loaded++;
                    stats.loaded++;
                }
            }
        }
        logVerbose("[Model loader: "+loaded+"(loaded)/"+declarationsByName.size()+"(total) declarations]");
        for(Entry<Package, Stats> packageEntry : loadedByPackage.entrySet()){
            logVerbose("[ Package "+packageEntry.getKey().getNameAsString()+": "
                    +packageEntry.getValue().loaded+"(loaded)/"+packageEntry.getValue().total+"(total) declarations]");
        }
    }

    private static Package getPackage(Object decl) {
        if(decl == null)
            return null;
        if(decl instanceof Package)
            return (Package) decl;
        return getPackage(((Declaration)decl).getContainer());
    }
    
    protected void logMissingOracleType(String type) {
        logVerbose("Hopefully harmless completion failure in model loader: "+type
                +". This is most likely when the JDK depends on Oracle private classes that we can't find."
                +" As a result some model information will be incomplete.");
    }

    public void setupSourceFileObjects(List<?> treeHolders) {
    }

    public static boolean isJDKModule(String name) {
        return JDKUtils.isJDKModule(name)
                || JDKUtils.isOracleJDKModule(name);
    }
    
    @Override
    public Module getLoadedModule(String moduleName) {
        for(Module mod : modules.getListOfModules()){
            if(mod.getNameAsString().equals(moduleName))
                return mod;
        }
        return null;
    }
}
