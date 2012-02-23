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

import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.loader.mirror.AnnotatedMirror;
import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.compiler.loader.mirror.ClassMirror;
import com.redhat.ceylon.compiler.loader.mirror.FieldMirror;
import com.redhat.ceylon.compiler.loader.mirror.MethodMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeMirror;
import com.redhat.ceylon.compiler.loader.mirror.TypeParameterMirror;
import com.redhat.ceylon.compiler.loader.mirror.VariableMirror;
import com.redhat.ceylon.compiler.loader.model.FieldValue;
import com.redhat.ceylon.compiler.loader.model.JavaBeanValue;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyContainer;
import com.redhat.ceylon.compiler.loader.model.LazyElement;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyModule;
import com.redhat.ceylon.compiler.loader.model.LazyPackage;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.analyzer.ModuleManager;
import com.redhat.ceylon.compiler.typechecker.io.VirtualFile;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.BottomType;
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
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnionType;
import com.redhat.ceylon.compiler.typechecker.model.Unit;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.redhat.ceylon.compiler.typechecker.tree.Tree;
import com.redhat.ceylon.compiler.typechecker.tree.Visitor;

/**
 * Abstract class of a model loader that can load a model from a compiled Java representation,
 * while being agnostic of the reflection API used to load the compiled Java representation.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public abstract class AbstractModelLoader implements ModelCompleter, ModelLoader {

    private static final String CEYLON_CEYLON_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ceylon";
    private static final String CEYLON_MODULE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Module";
    private static final String CEYLON_PACKAGE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Package";
    private static final String CEYLON_IGNORE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Ignore";
    private static final String CEYLON_CLASS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Class";
    private static final String CEYLON_NAME_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Name";
    private static final String CEYLON_SEQUENCED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Sequenced";
    private static final String CEYLON_DEFAULTED_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Defaulted";
    private static final String CEYLON_SATISFIED_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes";
    private static final String CEYLON_CASE_TYPES_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.CaseTypes";
    private static final String CEYLON_TYPE_PARAMETERS = "com.redhat.ceylon.compiler.java.metadata.TypeParameters";
    private static final String CEYLON_TYPE_INFO_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.TypeInfo";
    public static final String CEYLON_ATTRIBUTE_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Attribute";
    public static final String CEYLON_OBJECT_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Object";
    public static final String CEYLON_METHOD_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Method";
    private static final String CEYLON_ANNOTATIONS_ANNOTATION = "com.redhat.ceylon.compiler.java.metadata.Annotations";

    private static final TypeMirror OBJECT_TYPE = simpleObjectType("java.lang.Object");
    private static final TypeMirror CEYLON_OBJECT_TYPE = simpleObjectType("ceylon.language.Object");
    private static final TypeMirror CEYLON_IDENTIFIABLE_OBJECT_TYPE = simpleObjectType("ceylon.language.IdentifiableObject");
    private static final TypeMirror CEYLON_EXCEPTION_TYPE = simpleObjectType("ceylon.language.Exception");;
    
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

    /**
     * Loads a given package, if required. This is mostly useful for the javac reflection impl.
     * 
     * @param packageName the package name to load
     * @param loadDeclarations true to load all the declarations in this package.
     */
    public abstract void loadPackage(String packageName, boolean loadDeclarations);
    
    /**
     * Looks up a ClassMirror by name. Uses cached results, and caches the result of calling lookupNewClassMirror
     * on cache misses.
     * 
     * @param name the name of the Class to load
     * @return a ClassMirror for the specified class, or null if not found.
     */
    public final ClassMirror lookupClassMirror(String name){
        // we use containsKey to be able to cache null results
        if(classMirrorCache.containsKey(name))
            return classMirrorCache.get(name);
        ClassMirror mirror = lookupNewClassMirror(name);
        // we even cache null results
        classMirrorCache.put(name, mirror);
        return mirror;
    }

    /**
     * Looks up a ClassMirror by name. Called by lookupClassMirror on cache misses.
     * 
     * @param name the name of the Class to load
     * @return a ClassMirror for the specified class, or null if not found.
     */
    public abstract ClassMirror lookupNewClassMirror(String name);

    /**
     * Adds the given module to the set of modules from which we can load classes.
     * 
     * @param module the module
     * @param artifact the module's artifact, if any. Can be null. 
     */
    public abstract void addModuleToClassPath(Module module, VirtualFile artifact);

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
        Module languageModule = findOrCreateModule("ceylon.language");
        addModuleToClassPath(languageModule, null);
        Package languagePackage = findOrCreatePackage(languageModule, "ceylon.language");
        typeFactory.setPackage(languagePackage);
        /*
         * We start by loading java.lang and ceylon.language because we will need them no matter what.
         */
        loadPackage("java.lang", false);
        loadPackage("com.redhat.ceylon.compiler.java.metadata", false);

        /*
         * We do not load the ceylon.language module from class files if we're bootstrapping it
         */
        if(!isBootstrap){
            loadPackage("ceylon.language", true);
            loadPackage("ceylon.language.descriptor", true);
        }
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
        case VOID:    typeName = "ceylon.language.Void"; break;
        case BOOLEAN: typeName = "java.lang.Boolean"; break;
        case BYTE:    typeName = "java.lang.Byte"; break;
        case CHAR:    typeName = "java.lang.Character"; break;
        case SHORT:   typeName = "java.lang.Short"; break;
        case INT:     typeName = "java.lang.Integer"; break;
        case LONG:    typeName = "java.lang.Long"; break;
        case FLOAT:   typeName = "java.lang.Float"; break;
        case DOUBLE:  typeName = "java.lang.Double"; break;
        case ARRAY:
            TypeMirror componentType = type.getComponentType();
            //throw new RuntimeException("Array type not implemented");
            //UnionType[Empty|Sequence<Natural>] casetypes 
            // producedtypes.typearguments: typeparam[element]->type[natural]
            TypeDeclaration emptyDecl = (TypeDeclaration)convertToDeclaration("ceylon.language.Empty", DeclarationType.TYPE);
            TypeDeclaration sequenceDecl = (TypeDeclaration)convertToDeclaration("ceylon.language.Sequence", DeclarationType.TYPE);
            UnionType unionType = new UnionType(typeFactory);
            List<ProducedType> caseTypes = new ArrayList<ProducedType>(2);
            caseTypes.add(emptyDecl.getType());
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(1);
            typeArguments.add(getType(componentType, scope));
            caseTypes.add(sequenceDecl.getProducedType(null, typeArguments));
            unionType.setCaseTypes(caseTypes);
            return unionType;
        case DECLARED:
            typeName = type.getQualifiedName();
            break;
        case TYPEVAR:
            return safeLookupTypeParameter(scope, type.getQualifiedName());
        case WILDCARD:
            // FIXME: we shouldn't even get there, because if something contains a wildcard (Foo<?>) we erase it to
            // IdentifiableObject, so this shouldn't be reachable.
            typeName = "ceylon.language.Bottom";
            break;
        default:
            throw new RuntimeException("Failed to handle type "+type);
        }
        return convertToDeclaration(typeName, declarationType);
    }

    protected Declaration convertToDeclaration(ClassMirror classMirror, DeclarationType declarationType) {
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
            return declarationsByName.get(key);
        }
        
        // make it
        Declaration decl = null;
        List<Declaration> decls = new ArrayList<Declaration>(2);
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
            List<MethodMirror> constructors = getClassConstructors(classMirror);
            if (!constructors.isEmpty()) {
                if (constructors.size() > 1) {
                    // If the class has multiple constructors we make a copy of the class
                    // for each one (each with it's own single constructor) and make them
                    // a subclass of the original
                    Class supercls = makeLazyClass(classMirror, null, null, false);
                    supercls.setAbstraction(true);
                    for (MethodMirror constructor : constructors) {
                        Declaration subdecl = makeLazyClass(classMirror, supercls, constructor, false);
                        decls.add(subdecl);
                    }
                    decl = supercls;
                } else {
                    MethodMirror constructor = constructors.get(0);
                    decl = makeLazyClass(classMirror, null, constructor, false);
                }
            } else {
                decl = makeLazyClass(classMirror, null, null, false);
            }
            break;
        case INTERFACE:
            decl = makeLazyInterface(classMirror);
            break;
        }

        // objects have special handling above
        if(type != ClassType.OBJECT){
            declarationsByName.put(key, decl);
            decls.add(decl);
        }

        // find its module
        String pkgName = classMirror.getPackage().getQualifiedName();
        Module module = findOrCreateModule(pkgName);
        LazyPackage pkg = findOrCreatePackage(module, pkgName);

        // find/make its Unit
        Unit unit = getCompiledUnit(pkg, classMirror);

        for(Declaration d : decls){
            d.setShared(classMirror.isPublic());
        
            // add it to its package if it's not an inner class
            if(!classMirror.isInnerClass()){
                pkg.addMember(d);
                d.setContainer(pkg);
            }

            // add it to its Unit
            d.setUnit(unit);
            unit.getDeclarations().add(d);
        }
        
        return decl;
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
            unit = new Unit();
            unit.setPackage(pkg);
            unitsByPackage.put(pkg, unit);
        }
        return unit;
    }

    private Declaration makeToplevelAttribute(ClassMirror classMirror) {
        Value value = new LazyValue(classMirror, this);
        return value;
    }

    private Declaration makeToplevelMethod(ClassMirror classMirror) {
        LazyMethod method = new LazyMethod(classMirror, this);
        return method;
    }
    
    private Class makeLazyClass(ClassMirror classMirror, Class superClass, MethodMirror constructor, boolean forTopLevelObject) {
        return new LazyClass(classMirror, this, superClass, constructor, forTopLevelObject);
    }

    private Interface makeLazyInterface(ClassMirror classMirror) {
        return new LazyInterface(classMirror, this);
    }

    public Declaration convertToDeclaration(String typeName, DeclarationType declarationType) {
        if ("ceylon.language.Bottom".equals(typeName)) {
            return new BottomType(typeFactory);
        } else if ("java.lang.Exception".equals(typeName)) {
            return convertToDeclaration("ceylon.language.Exception", declarationType);
        }
        ClassMirror classMirror = lookupClassMirror(typeName);
        if (classMirror == null) {
            Declaration languageModuleDeclaration = typeFactory.getLanguageModuleDeclaration(typeName);
            if (languageModuleDeclaration != null) {
                return languageModuleDeclaration;
            }
            throw new RuntimeException("Failed to resolve "+typeName);
        }
        return convertToDeclaration(classMirror, declarationType);
    }

    protected TypeParameter safeLookupTypeParameter(Scope scope, String name) {
        TypeParameter param = lookupTypeParameter(scope, name);
        if(param == null)
            throw new RuntimeException("Type param "+name+" not found in "+scope);
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
        }else if(scope instanceof ClassOrInterface){
            ClassOrInterface klass = (ClassOrInterface) scope;
            for(TypeParameter param : klass.getTypeParameters()){
                if(param.getName().equals(name))
                    return param;
            }
            if (!klass.isToplevel()) {
                // look it up in its container
                return lookupTypeParameter(scope.getContainer(), name);
            } else {
                // not found
                return null;
            }
        }else
            throw new RuntimeException("Type param "+name+" lookup not supported for scope "+scope);
    }
    
    //
    // Packages
    
    public Package findPackage(String pkgName) {
        pkgName = Util.quoteJavaKeywords(pkgName);
        return packagesByName.get(pkgName);
    }

    public LazyPackage findOrCreatePackage(Module module, final String pkgName) {
        String quotedPkgName = Util.quoteJavaKeywords(pkgName);
        LazyPackage pkg = packagesByName.get(quotedPkgName);
        if(pkg != null)
            return pkg;
        pkg = new LazyPackage(this);
        packagesByName.put(quotedPkgName, pkg);
        // FIXME: some refactoring needed
        pkg.setName(pkgName == null ? Collections.<String>emptyList() : Arrays.asList(pkgName.split("\\.")));

        // only bind it if we already have a module
        if(module != null){
            pkg.setModule(module);
            module.getPackages().add(pkg);
        }
        
        // only load package descriptors for new packages after a certain phase
        if(packageDescriptorsNeedLoading)
            loadPackageDescriptor(pkg);
        
        return pkg;
    }

    public void loadPackageDescriptors() {
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
        String className = quotedQualifiedName + ".$package";
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

    //
    // Modules
    public Module findOrCreateModule(String pkgName) {
        java.util.List<String> moduleName;
        boolean isJava = false;
        boolean defaultModule = false;
        // FIXME: this is a rather simplistic view of the world
        if(pkgName == null){
            moduleName = Arrays.asList(Module.DEFAULT_MODULE_NAME);
            defaultModule = true;
        }else if(pkgName.startsWith("java.")){
            moduleName = Arrays.asList("java");
            isJava = true;
        } else if(pkgName.startsWith("sun.")){
            moduleName = Arrays.asList("sun");
            isJava = true;
        } else if(pkgName.startsWith("ceylon.language."))
            moduleName = Arrays.asList("ceylon","language");
        else
            moduleName = Arrays.asList(pkgName.split("\\."));
        
        Module module = moduleManager.getOrCreateModule(moduleName, null);
        // make sure that when we load the ceylon language module we set it to where
        // the typechecker will look for it
        if(pkgName != null
                 && pkgName.startsWith("ceylon.language.")
                 && modules.getLanguageModule() == null){
             modules.setLanguageModule(module);
         }
         
         if (module instanceof LazyModule) {
             ((LazyModule)module).setJava(isJava);
         }
         // FIXME: this can't be that easy.
         module.setAvailable(true);
         module.setDefault(defaultModule);
         return module;
    }

    public Module loadCompiledModule(String pkgName) {
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

        List<AnnotationMirror> imports = getAnnotationArrayValue(moduleClass, CEYLON_MODULE_ANNOTATION, "dependencies");
        for (AnnotationMirror importAttribute : imports) {
            String dependencyName = (String) importAttribute.getValue("name");
            if (dependencyName != null) {
                if (! dependencyName.equals("java")) {
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
    public void complete(LazyInterface iface) {
        complete(iface, iface.classMirror);
    }

    @Override
    public void completeTypeParameters(LazyInterface iface) {
        completeTypeParameters(iface, iface.classMirror);
    }

    @Override
    public void complete(LazyClass klass) {
        complete(klass, klass.classMirror);
    }

    @Override
    public void completeTypeParameters(LazyClass klass) {
        completeTypeParameters(klass, klass.classMirror);
    }

    private void completeTypeParameters(ClassOrInterface klass, ClassMirror classMirror) {
        setTypeParameters(klass, classMirror);
    }

    private void complete(ClassOrInterface klass, ClassMirror classMirror) {
        Map<MethodMirror, List<MethodMirror>> variables = new HashMap<MethodMirror, List<MethodMirror>>();
        String qualifiedName = classMirror.getQualifiedName();
        boolean isJava = qualifiedName.startsWith("java.");
        boolean isCeylon = (classMirror.getAnnotation(CEYLON_CEYLON_ANNOTATION) != null);
        
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
            if(isJava && !methodMirror.isPublic())
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
            boolean first = true;
            
            for (MethodMirror methodMirror : methodMirrors) {
                String methodName = methodMirror.getName();
                if(methodMirror.isConstructor()) {
                    if (methodMirror == constructor) {
                        ((Class)klass).setOverloaded(isOverloaded);
                        if(!(klass instanceof LazyClass) || !((LazyClass)klass).isTopLevelObjectType())
                            setParameters((Class)klass, methodMirror, isCeylon);
                    }
                } else if(isGetter(methodMirror)) {
                    // simple attribute
                    addValue(klass, methodMirror, getJavaAttributeName(methodName));
                } else if(isSetter(methodMirror)) {
                    // We skip setters for now and handle them later
                    variables.put(methodMirror, methodMirrors);
                } else if(isHashAttribute(methodMirror)) {
                    // ERASURE
                    // Un-erasing 'hash' attribute from 'hashCode' method
                    addValue(klass, methodMirror, "hash");
                } else if(isStringAttribute(methodMirror)) {
                    // ERASURE
                    // Un-erasing 'string' attribute from 'toString' method
                    addValue(klass, methodMirror, "string");
                } else {
                    if (first && isOverloaded) {
                        // We create an extra "abstraction" method for overloaded methods
                        Method abstractionMethod = addMethod(klass, methodMirror, false, false);
                        abstractionMethod.setAbstraction(true);
                        abstractionMethod.setType(new UnknownType(typeFactory).getType());
                        first = false;
                    }
                    // normal method
                    addMethod(klass, methodMirror, isCeylon, isOverloaded);
                }
            }
        }

        for(FieldMirror fieldMirror : classMirror.getDirectFields()){
            // We skip members marked with @Ignore
            if(fieldMirror.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            if(isCeylon && fieldMirror.isStatic())
                continue;
            String name = fieldMirror.getName();
            // skip the field if "we've already got one"
            if(klass.getDirectMember(name, null) != null)
                continue;
            addValue(klass, fieldMirror);
        }
        
        // Now mark all Values for which Setters exist as variable
        for(Entry<MethodMirror, List<MethodMirror>> setterEntry : variables.entrySet()){
            MethodMirror setter = setterEntry.getKey();
            String name = getJavaAttributeName(setter.getName());
            Declaration decl = klass.getMember(name, null);
            boolean foundGetter = false;
            if (decl != null && decl instanceof Value) {
                Value value = (Value)decl;
                VariableMirror setterParam = setter.getParameters().get(0);
                ProducedType paramType = obtainType(setterParam.getType(), setterParam, klass);
                // only add the setter if it has exactly the same type as the getter
                if(paramType.isExactly(value.getType())){
                    foundGetter = true;
                    value.setVariable(true);
                    if(decl instanceof JavaBeanValue)
                        ((JavaBeanValue)decl).setSetterName(setter.getName());
                }else
                    logWarning("Setter parameter type for "+name+" does not match corresponding getter type, adding setter as a method");
            } 
            
            if(!foundGetter){
                // it was not a setter, it was a method, let's add it as such
                addMethod(klass, setter, isCeylon, false);
            }
        }

        klass.setStaticallyImportable(!isCeylon && classMirror.isStatic());
        
        setExtendedType(klass, classMirror);
        setSatisfiedTypes(klass, classMirror);
        setCaseTypes(klass, classMirror);
        fillRefinedDeclarations(klass);
        addInnerClasses(klass, classMirror);
        setAnnotations(klass, classMirror);
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
        for(ClassMirror innerClass : classMirror.getDirectInnerClasses()){
            // We skip members marked with @Ignore
            if(innerClass.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            Declaration innerDecl = convertToDeclaration(innerClass, DeclarationType.TYPE);
            innerDecl.setContainer(klass);
            // let's not trigger lazy-loading
            ((LazyContainer)klass).addMember(innerDecl);
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
            setParameters(method, methodMirror, isCeylon);
        
        // and its return type
        ProducedType type = obtainType(methodMirror.getReturnType(), methodMirror, method);
        method.setType(type);
        
        markUnboxed(method, methodMirror.getReturnType());
        setAnnotations(method, methodMirror);
        
        klass.getMembers().add(method);
        
        return method;
    }

    private void fillRefinedDeclarations(ClassOrInterface klass) {
        for(Declaration member : klass.getMembers()){
            if(member.isActual()){
                member.setRefinedDeclaration(findRefinedDeclaration(klass, member.getName(), getSignature(member)));
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
    
    private Declaration findRefinedDeclaration(ClassOrInterface decl, String name, List<ProducedType> signature) {
        Declaration refinedDeclaration = decl.getRefinedMember(name, signature);
        if(refinedDeclaration == null)
            throw new RuntimeException("Failed to find refined declaration for "+name);
        return refinedDeclaration;
    }

    private boolean isGetter(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesGet = name.length() > 3 && name.startsWith("get") && Character.isUpperCase(name.charAt(3));
        boolean matchesIs = name.length() > 2 && name.startsWith("is") && Character.isUpperCase(name.charAt(2));
        boolean hasNoParams = methodMirror.getParameters().size() == 0;
        boolean hasNonVoidReturn = (methodMirror.getReturnType().getKind() != TypeKind.VOID);
        return (matchesGet || matchesIs) && hasNoParams && hasNonVoidReturn;
    }
    
    private boolean isSetter(MethodMirror methodMirror) {
        String name = methodMirror.getName();
        boolean matchesSet = name.length() > 3 && name.startsWith("set") && Character.isUpperCase(name.charAt(3));
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
        parameter.setType(getType(CEYLON_OBJECT_TYPE, decl));
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

    private void addValue(ClassOrInterface klass, FieldMirror fieldMirror) {
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
        value.setType(obtainType(fieldMirror.getType(), fieldMirror, klass));
        markUnboxed(value, fieldMirror.getType());
        klass.getMembers().add(value);
    }
    
    private void addValue(ClassOrInterface klass, MethodMirror methodMirror, String methodName) {
        JavaBeanValue value = new JavaBeanValue();
        value.setGetterName(methodMirror.getName());
        value.setContainer(klass);
        value.setName(methodName);
        value.setUnit(klass.getUnit());
        setMethodOrValueFlags(klass, methodMirror, value);
        value.setType(obtainType(methodMirror.getReturnType(), methodMirror, klass));
        markUnboxed(value, methodMirror.getReturnType());
        setAnnotations(value, methodMirror);
        klass.getMembers().add(value);
    }

    private void setMethodOrValueFlags(ClassOrInterface klass, MethodMirror methodMirror, MethodOrValue decl) {
        decl.setShared(methodMirror.isPublic() || methodMirror.isProtected());
        decl.setProtectedVisibility(methodMirror.isProtected());
        if(methodMirror.isAbstract() || klass instanceof Interface) {
            decl.setFormal(true);
        } else {
            if (!methodMirror.isFinal()) {
                decl.setDefault(true);
            }
        }
        decl.setStaticallyImportable(methodMirror.isStatic());
        if(isOverridingMethod(methodMirror)){
            decl.setActual(true);
        }
    }
    
    private void setExtendedType(ClassOrInterface klass, ClassMirror classMirror) {
        // look at its super type
        TypeMirror superClass = classMirror.getSuperclass();
        ProducedType extendedType;
        
        if(klass instanceof Interface){
            // interfaces need to have their superclass set to Object
            if(superClass == null || superClass.getKind() == TypeKind.NONE)
                extendedType = getType(CEYLON_OBJECT_TYPE, klass);
            else
                extendedType = getType(superClass, klass);
        }else{
            String className = classMirror.getQualifiedName();
            String superClassName = superClass == null ? null : superClass.getQualifiedName();
            if(className.equals("ceylon.language.Void")){
                // ceylon.language.Void has no super type
                extendedType = null;
            }else if(className.equals("java.lang.Object")){
                // we pretend its superclass is something else, but note that in theory we shouldn't 
                // be seeing j.l.Object at all due to unerasure
                extendedType = getType(CEYLON_IDENTIFIABLE_OBJECT_TYPE, klass);
            }else if("java.lang.Exception".equals(superClassName)){
                // we pretend that a subclass of j.l.Excpetion is really a subclass of c.l.Excpetion
                extendedType = getType(CEYLON_EXCEPTION_TYPE, klass);
            }else{
                // read it from annotation first
                String annotationSuperClassName = getAnnotationStringValue(classMirror, CEYLON_CLASS_ANNOTATION, "extendsType");
                if(annotationSuperClassName != null && !annotationSuperClassName.isEmpty()){
                    extendedType = decodeType(annotationSuperClassName, klass);
                }else{
                    // read it from the Java super type
                    // now deal with type erasure, avoid having Object as superclass
                    if("java.lang.Object".equals(superClassName)){
                        extendedType = getType(CEYLON_IDENTIFIABLE_OBJECT_TYPE, klass);
                    }else{
                        extendedType = getType(superClass, klass);
                    }
                }
            }
        }
        if(extendedType != null)
            klass.setExtendedType(extendedType);
    }

    private void setParameters(Functional decl, MethodMirror methodMirror, boolean isCeylon) {
        ParameterList parameters = new ParameterList();
        decl.addParameterList(parameters);
        for(VariableMirror paramMirror : methodMirror.getParameters()){
            ValueParameter parameter = new ValueParameter();
            parameter.setContainer((Scope) decl);
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
            ProducedType type = obtainType(typeMirror, paramMirror, (Scope) decl);
            if(!isCeylon && !typeMirror.isPrimitive()){
                // Java parameters are all optional unless primitives
                ProducedType optionalType = typeFactory.getOptionalType(type);
                optionalType.setUnderlyingType(type.getUnderlyingType());
                type = optionalType;
            }
            parameter.setType(type );
            if(paramMirror.getAnnotation(CEYLON_SEQUENCED_ANNOTATION) != null)
                parameter.setSequenced(true);
            if(paramMirror.getAnnotation(CEYLON_DEFAULTED_ANNOTATION) != null)
                parameter.setDefaulted(true);
            markUnboxed(parameter, paramMirror.getType());
            parameter.setDeclaration((Declaration) decl);
            parameters.getParameters().add(parameter);
        }
    }

    private void markUnboxed(TypedDeclaration decl, TypeMirror type) {
        boolean unboxed = false;
        if(type.isPrimitive() || type.getKind() == TypeKind.ARRAY
                || sameType(type, STRING_TYPE)) {
            unboxed = true;
        }
        decl.setUnboxed(unboxed);
    }

    @Override
    public void complete(LazyValue value) {
        MethodMirror meth = null;
        for (MethodMirror m : value.classMirror.getDirectMethods()) {
            // We skip members marked with @Ignore
            if(m.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            
            if (m.getName().equals(
                    Util.getGetterName(value.getName()))
                    && m.isStatic() && m.getParameters().size() == 0) {
                meth = m;
            }
            if (m.getName().equals(
                    Util.getSetterName(value.getName()))
                    && m.isStatic() && m.getParameters().size() == 1) {
                value.setVariable(true);
            }
        }
        if(meth == null || meth.getReturnType() == null)
            throw new RuntimeException("Failed to find toplevel attribute "+value.getName());
        
        value.setType(obtainType(meth.getReturnType(), meth, null));
        setAnnotations(value, meth);
        markUnboxed(value, meth.getReturnType());
    }

    @Override
    public void complete(LazyMethod method) {
        MethodMirror meth = null;
        String lookupName = Util.quoteIfJavaKeyword(method.getName());
        for(MethodMirror m : method.classMirror.getDirectMethods()){
            // We skip members marked with @Ignore
            if(m.getAnnotation(CEYLON_IGNORE_ANNOTATION) != null)
                continue;
            
            if(m.getName().equals(lookupName)){
                meth = m;
                break;
            }
        }
        if(meth == null || meth.getReturnType() == null)
            throw new RuntimeException("Failed to find toplevel method "+method.getName());
        
        // type params first
        setTypeParameters(method, meth);

        // now its parameters
        setParameters(method, meth, true /* toplevel methods are always Ceylon */);
        method.setType(obtainType(meth.getReturnType(), meth, method));
        markUnboxed(method, meth.getReturnType());
        
        setAnnotations(method, meth);
     }
    
    //
    // Satisfied Types
    
    private List<String> getSatisfiedTypesFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationArrayValue(symbol, CEYLON_SATISFIED_TYPES_ANNOTATION);
    }
    
    private void setSatisfiedTypes(ClassOrInterface klass, ClassMirror classMirror) {
        List<String> satisfiedTypes = getSatisfiedTypesFromAnnotations(classMirror);
        if(satisfiedTypes != null){
            klass.getSatisfiedTypes().addAll(getTypesList(satisfiedTypes, klass));
        }else{
            for(TypeMirror iface : classMirror.getInterfaces()){
                klass.getSatisfiedTypes().add(getType(iface, klass));
            }
        }
    }

    //
    // Case Types
    
    private List<String> getCaseTypesFromAnnotations(AnnotatedMirror symbol) {
        return getAnnotationArrayValue(symbol, CEYLON_CASE_TYPES_ANNOTATION);
    }
    
    private void setCaseTypes(ClassOrInterface klass, ClassMirror classMirror) {
        List<String> caseTypes = getCaseTypesFromAnnotations(classMirror);
        if(caseTypes != null){
            klass.setCaseTypes(getTypesList(caseTypes, klass));
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
    private void setTypeParametersFromAnnotations(Scope scope, List<TypeParameter> params, List<AnnotationMirror> typeParameters) {
        // We must first add every type param, before we resolve the bounds, which can
        // refer to type params.
        for(AnnotationMirror typeParam : typeParameters){
            TypeParameter param = new TypeParameter();
            param.setUnit(((Element)scope).getUnit());
            param.setContainer(scope);
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName((String)typeParam.getValue("value"));
            param.setExtendedType(typeFactory.getVoidDeclaration().getType());
            params.add(param);
            
            String varianceName = (String) typeParam.getValue("variance");
            if(varianceName != null){
                if(varianceName.equals("IN")){
                    param.setContravariant(true);
                }else if(varianceName.equals("OUT"))
                    param.setCovariant(true);
            }
        }
        // Now all type params have been set, we can resolve the references parts
        Iterator<TypeParameter> paramsIterator = params.iterator();
        for(AnnotationMirror typeParam : typeParameters){
            TypeParameter param = paramsIterator.next();
            @SuppressWarnings("unchecked")
            List<String> satisfiesAttribute = (List<String>)typeParam.getValue("satisfies");
            if(satisfiesAttribute != null){
                for (String satisfy : satisfiesAttribute) {
                    ProducedType satisfiesType = decodeType(satisfy, scope);
                    param.getSatisfiedTypes().add(satisfiesType);
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
            // let's not trigger the lazy-loading if we're completing a LazyClass/LazyInterface
            if(scope instanceof LazyContainer)
                ((LazyContainer)scope).addMember(param);
            else // must be a method
                scope.getMembers().add(param);
            param.setName(typeParam.getName());
            param.setExtendedType(typeFactory.getVoidDeclaration().getType());
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
                    boundType = getType(CEYLON_OBJECT_TYPE, scope);
                }else
                    boundType = getType(bound, scope);
                param.getSatisfiedTypes().add(boundType);
            }
        }
    }

    // method
    private void setTypeParameters(Method method, MethodMirror methodMirror) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        method.setTypeParameters(params);
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(methodMirror);
        if(typeParameters != null)
            setTypeParametersFromAnnotations(method, params, typeParameters);
        else
            setTypeParameters(method, params, methodMirror.getTypeParameters());
    }

    // class
    private void setTypeParameters(ClassOrInterface klass, ClassMirror classMirror) {
        List<TypeParameter> params = new LinkedList<TypeParameter>();
        klass.setTypeParameters(params);
        List<AnnotationMirror> typeParameters = getTypeParametersFromAnnotations(classMirror);
        if(typeParameters != null)
            setTypeParametersFromAnnotations(klass, params, typeParameters);
        else
            setTypeParameters(klass, params, classMirror.getTypeParameters());
    }        

    //
    // TypeParsing and ModelLoader

    private ProducedType decodeType(String value, Scope scope) {
        return typeParser.decodeType(value, scope);
    }
    
    private ProducedType obtainType(TypeMirror type, AnnotatedMirror symbol, Scope scope) {
        String typeName = getAnnotationStringValue(symbol, CEYLON_TYPE_INFO_ANNOTATION);
        if (typeName != null) {
            return decodeType(typeName, scope);
        } else {
            return obtainType(type, scope);
        }
    }
    
    private ProducedType obtainType(TypeMirror type, Scope scope) {
        String underlyingType = null;
        // ERASURE
        if (sameType(type, STRING_TYPE)) {
            underlyingType = type.getQualifiedName();
            type = CEYLON_STRING_TYPE;
            
        } else if (sameType(type, PRIM_BOOLEAN_TYPE)) {
            type = CEYLON_BOOLEAN_TYPE;
            
        } else if (sameType(type, PRIM_BYTE_TYPE)) {
            underlyingType = type.getQualifiedName();
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_SHORT_TYPE)) {
            underlyingType = type.getQualifiedName();
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_INT_TYPE)) {
            underlyingType = type.getQualifiedName();
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_LONG_TYPE)) {
            type = CEYLON_INTEGER_TYPE;
            
        } else if (sameType(type, PRIM_FLOAT_TYPE)) {
            underlyingType = type.getQualifiedName();
            type = CEYLON_FLOAT_TYPE;
            
        } else if (sameType(type, PRIM_DOUBLE_TYPE)) {
            type = CEYLON_FLOAT_TYPE;
            
        } else if (sameType(type, PRIM_CHAR_TYPE)) {
            underlyingType = type.getQualifiedName();
            type = CEYLON_CHARACTER_TYPE;
            
        } else if (sameType(type, OBJECT_TYPE)) {
            type = CEYLON_OBJECT_TYPE;
        }
        
        ProducedType ret = getType(type, scope);
        if(underlyingType != null)
            ret.setUnderlyingType(underlyingType);
        return ret;
    }
    
    private boolean sameType(TypeMirror t1, TypeMirror t2) {
        return t1.getQualifiedName().equals(t2.getQualifiedName());
    }
    
    @Override
    public Declaration getDeclaration(String typeName, DeclarationType declarationType) {
        return convertToDeclaration(typeName, declarationType);
    }

    private ProducedType getType(TypeMirror type, Scope scope) {
        if (type.getKind() == TypeKind.ARRAY) {
            Declaration decl = typeFactory.getLanguageModuleDeclaration("Array");
            TypeDeclaration declaration = (TypeDeclaration) decl;
            List<ProducedType> typeArguments = new ArrayList<ProducedType>(1);
            typeArguments.add((ProducedType) obtainType(type.getComponentType(), scope));
            return declaration.getProducedType(null, typeArguments);
        } else {
            Declaration decl = convertToDeclaration(type, scope, DeclarationType.TYPE);
            TypeDeclaration declaration = (TypeDeclaration) decl;
            List<TypeMirror> javacTypeArguments = type.getTypeArguments();
            if(!javacTypeArguments.isEmpty()){
                List<ProducedType> typeArguments = new ArrayList<ProducedType>(javacTypeArguments.size());
                for(TypeMirror typeArgument : javacTypeArguments){
                    // if a single type argument is a wildcard, we erase to Object
                    if(typeArgument.getKind() == TypeKind.WILDCARD)
                        return typeFactory.getObjectDeclaration().getType();
                    typeArguments.add((ProducedType) getType(typeArgument, scope));
                }
                return declaration.getProducedType(null, typeArguments);
            }
            return declaration.getType();
        }
    }

    @Override
    public ProducedType getType(String name, Scope scope) {
        if(scope != null){
            TypeParameter typeParameter = lookupTypeParameter(scope, name);
            if(typeParameter != null)
                return typeParameter.getType();
        }
        if(!isBootstrap || !name.startsWith("ceylon.language"))
            return ((TypeDeclaration)convertToDeclaration(name, DeclarationType.TYPE)).getType();
        // we're bootstrapping ceylon.language so we need to return the ProducedTypes straight from the model we're compiling
        Module languageModule = modules.getLanguageModule();
        String simpleName = name.substring(name.lastIndexOf(".")+1);
        for(Package pkg : languageModule.getPackages()){
            Declaration member = pkg.getDirectMember(simpleName, null);
            if(member != null)
                return ((TypeDeclaration)member).getType();
        }
        throw new RuntimeException("Failed to look up given type in language module while bootstrapping: "+name);
    }

    protected static abstract class SourceDeclarationVisitor extends Visitor{
        
        abstract public void loadFromSource(Tree.Declaration decl);
        
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

        @Override
        public void visit(Tree.AttributeGetterDefinition that) {
            loadFromSource(that);
        }
    }

    public void printStats(){
        int loaded = 0;
        class Stats {
            int loaded, total;
        }
        Map<Package, Stats> loadedByPackage = new HashMap<Package, Stats>();
        for(Declaration decl : declarationsByName.values()){
            if(decl instanceof LazyElement){
                Package pkg = Util.getPackage(decl);
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
}
