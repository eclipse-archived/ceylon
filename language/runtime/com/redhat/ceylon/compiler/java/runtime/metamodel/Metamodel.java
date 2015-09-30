package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jboss.modules.ModuleLoader;

import ceylon.language.Annotated;
import ceylon.language.Anything;
import ceylon.language.Array;
import ceylon.language.Callable;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.null_;
import ceylon.language.sequence_;
import ceylon.language.meta.declaration.AnnotatedDeclaration;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.NestableDeclaration;
import ceylon.language.meta.declaration.OpenClassOrInterfaceType;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.FunctionModel;
import ceylon.language.meta.model.Generic;
import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.meta.model.InvocationException;
import ceylon.language.meta.model.TypeApplicationException;
import ceylon.language.meta.model.ValueModel;

import com.redhat.ceylon.common.runtime.CeylonModuleClassLoader;
import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.language.BooleanArray;
import com.redhat.ceylon.compiler.java.language.ByteArray;
import com.redhat.ceylon.compiler.java.language.CharArray;
import com.redhat.ceylon.compiler.java.language.DoubleArray;
import com.redhat.ceylon.compiler.java.language.FloatArray;
import com.redhat.ceylon.compiler.java.language.IntArray;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.language.LongArray;
import com.redhat.ceylon.compiler.java.language.ObjectArray;
import com.redhat.ceylon.compiler.java.language.ObjectArrayIterable;
import com.redhat.ceylon.compiler.java.language.ReifiedTypeError;
import com.redhat.ceylon.compiler.java.language.ShortArray;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModelLoader;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.cmr.ArtifactResult;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.loader.JvmBackendUtil;
import com.redhat.ceylon.model.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.model.loader.NamingBase;
import com.redhat.ceylon.model.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.model.loader.impl.reflect.mirror.ReflectionMethod;
import com.redhat.ceylon.model.loader.model.AnnotationProxyClass;
import com.redhat.ceylon.model.loader.model.FunctionOrValueInterface;
import com.redhat.ceylon.model.loader.model.JavaMethod;
import com.redhat.ceylon.model.loader.model.LazyClass;
import com.redhat.ceylon.model.loader.model.LazyClassAlias;
import com.redhat.ceylon.model.loader.model.LazyElement;
import com.redhat.ceylon.model.loader.model.LazyFunction;
import com.redhat.ceylon.model.loader.model.LazyInterface;
import com.redhat.ceylon.model.loader.model.LazyPackage;
import com.redhat.ceylon.model.loader.model.LazyTypeAlias;
import com.redhat.ceylon.model.loader.model.LazyValue;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.Functional;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.ModuleImport;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.NothingType;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.SiteVariance;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.TypedDeclaration;
import com.redhat.ceylon.model.typechecker.model.UnknownType;

import ceylon.language.Annotated;
import ceylon.language.Anything;
import ceylon.language.Array;
import ceylon.language.Callable;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.null_;
import ceylon.language.sequence_;
import ceylon.language.meta.declaration.AnnotatedDeclaration;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.NestableDeclaration;
import ceylon.language.meta.declaration.OpenClassOrInterfaceType;
import ceylon.language.meta.declaration.OpenType;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.FunctionModel;
import ceylon.language.meta.model.Generic;
import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.meta.model.InvocationException;
import ceylon.language.meta.model.TypeApplicationException;
import ceylon.language.meta.model.ValueModel;

public class Metamodel {

    private static RuntimeModuleManager moduleManager;
    
    // FIXME: this will need better thinking in terms of memory usage
    private static Map<java.lang.Class, ceylon.language.meta.declaration.NestableDeclaration> classToDeclaration
    = new HashMap<java.lang.Class, ceylon.language.meta.declaration.NestableDeclaration>();
    
    private static Map<com.redhat.ceylon.model.typechecker.model.Declaration, Object> typeCheckModelToRuntimeModel
        = new HashMap<com.redhat.ceylon.model.typechecker.model.Declaration, Object>();

    private static Map<com.redhat.ceylon.model.typechecker.model.Package, com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage> typeCheckPackagesToRuntimeModel
        = new HashMap<com.redhat.ceylon.model.typechecker.model.Package, com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage>();

    private static Map<com.redhat.ceylon.model.typechecker.model.Module, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule> typeCheckModulesToRuntimeModel
        = new HashMap<com.redhat.ceylon.model.typechecker.model.Module, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule>();

    private static Map<TypeDescriptor,Type> typeDescriptorToProducedType = new WeakHashMap<TypeDescriptor,Type>();

    private static final TypeDescriptor TD_ClosedTypeOfAnything
        = TypeDescriptor.klass(ceylon.language.meta.model.Type.class, ceylon.language.Anything.$TypeDescriptor$);
    private static final TypeDescriptor TD_ClosedTypeArgumentElement
        = TypeDescriptor.union(TD_ClosedTypeOfAnything, ceylon.language.meta.declaration.Variance.$TypeDescriptor$);
    private static final TypeDescriptor TD_ClosedTypeArgument
        = TypeDescriptor.tuple(false, false, -1, TD_ClosedTypeOfAnything, ceylon.language.meta.declaration.Variance.$TypeDescriptor$);
    public static final TypeDescriptor TD_OpenTypeArgumentElement
        = TypeDescriptor.union(ceylon.language.meta.declaration.OpenType.$TypeDescriptor$, ceylon.language.meta.declaration.Variance.$TypeDescriptor$);
    public static final TypeDescriptor TD_OpenTypeArgument
        = TypeDescriptor.tuple(false, false, -1, ceylon.language.meta.declaration.OpenType.$TypeDescriptor$, ceylon.language.meta.declaration.Variance.$TypeDescriptor$);
    
    static{
        resetModuleManager();
    }

    public static boolean loadModule(String name, String version, ArtifactResult result, ClassLoader classLoader){
        boolean hasLoaded = moduleManager.loadModule(name, version, result, classLoader);
        // notify any thread waiting for this monitor
        Object lock = getLock();
        synchronized(lock){
            lock.notifyAll();
        }
        return hasLoaded;
    }
    
    public static void resetModuleManager() {
        moduleManager = new RuntimeModuleManager();
        moduleManager.initCoreModules(new Modules());
        moduleManager.prepareForTypeChecking();
        classToDeclaration.clear();
        typeCheckModelToRuntimeModel.clear();
        typeCheckModulesToRuntimeModel.clear();
        typeCheckPackagesToRuntimeModel.clear();
        typeDescriptorToProducedType.clear();
    }
    
    // This is only used in tests
    public static RuntimeModuleManager getModuleManager(){
        return moduleManager;
    }

    public static Object getLock(){
        return moduleManager.getModelLoader().getLock();
    }
    
    public static TypeDescriptor getTypeDescriptor(Object instance) {
        if(instance == null)
            return null_.$TypeDescriptor$;
        else if(instance instanceof ReifiedType)
            return((ReifiedType) instance).$getType$();
        else
            return getJavaTypeDescriptor(instance.getClass());
    }
    
    public static TypeDescriptor getIteratedTypeDescriptor(TypeDescriptor td) {
        return getTypeDescriptorForProducedType(
                moduleManager.getModelLoader().getUnit().getIteratedType(
                        getProducedType(td)));
    }
    
    private static TypeDescriptor getJavaArrayTypeDescriptor(Class<?> klass) {
        if(klass == byte[].class)
            return ByteArray.$TypeDescriptor$;
        if(klass == short[].class)
            return ShortArray.$TypeDescriptor$;
        if(klass == int[].class)
            return IntArray.$TypeDescriptor$;
        if(klass == long[].class)
            return LongArray.$TypeDescriptor$;
        if(klass == float[].class)
            return FloatArray.$TypeDescriptor$;
        if(klass == double[].class)
            return DoubleArray.$TypeDescriptor$;
        if(klass == boolean[].class)
            return BooleanArray.$TypeDescriptor$;
        if(klass == char[].class)
            return CharArray.$TypeDescriptor$;
        TypeDescriptor componentType = getJavaTypeDescriptor(klass.getComponentType());
        return TypeDescriptor.klass(ObjectArray.class, componentType);
    }

    private static TypeDescriptor getJavaTypeDescriptor(Class<?> klass) {
        if(klass.isArray())
            return getJavaArrayTypeDescriptor(klass);
        // make sure java.lang.Object doesn't leak in the ceylon metamodel
        // TODO: what about Throwable/j.l.Exception/RuntimeException?
        if(klass == Object.class)
            return ceylon.language.Object.$TypeDescriptor$;
        if(klass.isMemberClass())
            return TypeDescriptor.member(getJavaTypeDescriptor(klass.getEnclosingClass()), TypeDescriptor.klass(klass));
        // FIXME: what about local or anonymous types?
        return TypeDescriptor.klass(klass);
    }
    
    /**
     * Is the given type one that is "completely reifiable"? 
     * That is, does it consist only of class or interface types. 
     * For example:
     * <table><tbody>
     * <tr><th>Type<th>                  <th>Result</th></tr>
     * <tr><td>{@code String}</td>       <td>true</td></tr>
     * <tr><td>{@code j.u.List}</td>     <td>false (generic declaration, raw application)</td></tr>
     * <tr><td>{@code j.u.List<String>}</td><td>true</td></tr>
     * <tr><td>{@code j.u.List<X>}</td>  <td>false (where X is a type parameter)</td></tr>
     * </tbody></table>
     */
    private static boolean reifiedGeneric(java.lang.reflect.Type t) {
        if (t instanceof Class
                && ((Class<?>)t).getTypeParameters().length == 0) {
            return true;
        } else if (t instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType)t;
            for (java.lang.reflect.Type t2 : pt.getActualTypeArguments()) {
                if (!reifiedGeneric(t2)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Does the type {@code o} "completely reify" the type {@code tested} 
     * in it's inheritance hierarchy?
     * @see #reifiedGeneric(java.lang.reflect.Type)
     */
    private static boolean reifiedByInheritance(Class<?> o, Class<?> tested) {
        int index = 0;
        // Try to avoid instantiation of ParameterizedTypes and their TypeVariables
        // by testing the Class instances for equality first. 
        for (Class<?> c : o.getInterfaces()) {
            if (c.equals(tested)
                    && reifiedGeneric(o.getGenericInterfaces()[index])) {
                return true;
            }
            if (reifiedByInheritance(c, tested)) {
                return true;
            }
            index++;
        }
        
        Class<?> sup = o.getSuperclass();
        if (sup != null) {
            if (sup.equals(tested)
                    && reifiedGeneric(o.getGenericSuperclass())) {
                return true;
            }
            if (reifiedByInheritance(sup, tested)) {
                return true;
            }
        }
        return false;
    }
    
    /** Implementation of {@code is} operator */
    public static boolean isReified(java.lang.Object instance, TypeDescriptor type){
        if (instance == null) {
            return type.containsNull();
        }
        
        TypeDescriptor instanceType = getTypeDescriptor(instance);
        if (instanceType == null) {
            return false;
        }
        if (instanceType == type) {
            return true;
        }
        
        boolean result = type.is(instanceType);
        
        if (!result
                && !(instance instanceof ReifiedType)// we lack reified types
                && type instanceof TypeDescriptor.Class// we're testing for a generic type
                && ((TypeDescriptor.Class)type).isGeneric()
                && ((TypeDescriptor.Class)type).getKlass().isInstance(instance)// the instance is an instance of the base type
                && !reifiedByInheritance(instance.getClass(), ((TypeDescriptor.Class)type).getKlass())// the type isn't reified by inheritance
                ) {
            // throw when asked if an instance of a Java class is
            // of a generic type and we don't have sufficient information to 
            // answer correctly.
            // note we do this only if the isSubtypeOf() test fails
            // that's so that we don't have to worry about type applications 
            // such as like <out Anything> which is true even in the absence 
            // of reified type arguments
            throw new ReifiedTypeError("Cannot determine whether " + instance.getClass() + " is a " + type);
        }
        
        return result;
    }

    public static Type getProducedType(Object instance) {
        TypeDescriptor instanceType = getTypeDescriptor(instance);
        if(instanceType == null)
            throw Metamodel.newModelError("Metamodel not yet supported for Java types");
        return getProducedType(instanceType);
    }

    public static Type getProducedType(TypeDescriptor reifiedType) {
        Type producedType;
        synchronized(getLock()){
            producedType = typeDescriptorToProducedType.get(reifiedType);
            if(producedType == null){
                producedType = reifiedType.toType(moduleManager);
                typeDescriptorToProducedType.put(reifiedType, producedType);
            }
        }
        return producedType;
    }

    public static ceylon.language.meta.model.Type<?> getAppliedMetamodel(TypeDescriptor typeDescriptor) {
        if(typeDescriptor == null)
            throw Metamodel.newModelError("Metamodel not yet supported for Java types");
        Type pt = getProducedType(typeDescriptor);
        return getAppliedMetamodel(pt);
    }
    
    public static <R> R getOrCreateMetamodel(com.redhat.ceylon.model.typechecker.model.Declaration declaration){
        synchronized(getLock()){
            Object ret = typeCheckModelToRuntimeModel.get(declaration);
            if(ret == null){
                // make sure its module is loaded
                com.redhat.ceylon.model.typechecker.model.Package pkg = getPackage(declaration);
                com.redhat.ceylon.model.typechecker.model.Module mod = pkg.getModule();
                getOrCreateMetamodel(mod);
                if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Class){
                    com.redhat.ceylon.model.typechecker.model.Class klass = (com.redhat.ceylon.model.typechecker.model.Class) declaration;
                    ret = klass.hasConstructors() || klass.hasEnumerated()?
                            new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassWithConstructors(klass) :
                            new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassWithInitializer(klass);
                }else if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Interface){
                    com.redhat.ceylon.model.typechecker.model.Interface interf = (com.redhat.ceylon.model.typechecker.model.Interface)declaration;
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeInterface(interf);
                }else if(declaration instanceof com.redhat.ceylon.model.typechecker.model.TypeAlias){
                    com.redhat.ceylon.model.typechecker.model.TypeAlias alias = (com.redhat.ceylon.model.typechecker.model.TypeAlias)declaration;
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeAliasDeclaration(alias);
                }else if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Function){
                    com.redhat.ceylon.model.typechecker.model.Function method = (com.redhat.ceylon.model.typechecker.model.Function)declaration;
                    if (method.getTypeDeclaration() instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                        ret = new FreeCallableConstructor(method, 
                                (com.redhat.ceylon.model.typechecker.model.Constructor)method.getTypeDeclaration());
                    } else {
                        ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeFunction(method);
                    }
                }else if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Value){
                    com.redhat.ceylon.model.typechecker.model.Value value = (com.redhat.ceylon.model.typechecker.model.Value)declaration;
                    if (value.getTypeDeclaration() instanceof com.redhat.ceylon.model.typechecker.model.Constructor) {
                        ret = new FreeValueConstructor(value, 
                                (com.redhat.ceylon.model.typechecker.model.Constructor)value.getTypeDeclaration());
                    } else {
                        
                        ret = value.isTransient() ? new FreeValue(value) : new FreeReference(value);
                    }
                }else if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Setter){
                    com.redhat.ceylon.model.typechecker.model.Setter value = (com.redhat.ceylon.model.typechecker.model.Setter)declaration;
                    ret = new FreeSetter(value);
                }else if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Constructor){
                    com.redhat.ceylon.model.typechecker.model.Constructor value = (com.redhat.ceylon.model.typechecker.model.Constructor)declaration;
                    Declaration functionOrValue = value.getContainer().getDirectMember(value.getName(), null, false);
                    if (functionOrValue instanceof com.redhat.ceylon.model.typechecker.model.Function) {
                        ret = new FreeCallableConstructor((com.redhat.ceylon.model.typechecker.model.Function)functionOrValue, value);
                    } else if (functionOrValue instanceof com.redhat.ceylon.model.typechecker.model.Value) {
                        ret = new FreeValueConstructor((com.redhat.ceylon.model.typechecker.model.Value)functionOrValue, value);
                    } else if (functionOrValue == null) {
                        ret = new FreeCallableConstructor(value, value);
                    }else {
                        throw Metamodel.newModelError("Declaration type not supported yet: "+declaration);
                    }
                }else{
                    throw Metamodel.newModelError("Declaration type not supported yet: "+declaration);
                }
                typeCheckModelToRuntimeModel.put(declaration, ret);
            }
            return (R)ret;
        }
    }

    public static boolean hasTypeParameters(com.redhat.ceylon.model.typechecker.model.TypedDeclaration model) {
        if(model instanceof com.redhat.ceylon.model.typechecker.model.Generic)
            return hasTypeParameters((com.redhat.ceylon.model.typechecker.model.Generic)model);
        if(model.getContainer() instanceof com.redhat.ceylon.model.typechecker.model.ClassOrInterface)
            return hasTypeParameters((com.redhat.ceylon.model.typechecker.model.ClassOrInterface)model.getContainer());
        return false;
    }
    
    public static boolean hasTypeParameters(com.redhat.ceylon.model.typechecker.model.Generic model) {
        if(!model.getTypeParameters().isEmpty())
            return true;
        Object container = ((com.redhat.ceylon.model.typechecker.model.Declaration)model).getContainer();
        if(container instanceof com.redhat.ceylon.model.typechecker.model.ClassOrInterface)
            return hasTypeParameters((com.redhat.ceylon.model.typechecker.model.ClassOrInterface) container);
        return false;
    }

    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage getOrCreateMetamodel(com.redhat.ceylon.model.typechecker.model.Package declaration){
        synchronized(getLock()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage ret = typeCheckPackagesToRuntimeModel.get(declaration);
            if(ret == null){
                // make sure its module is loaded
                com.redhat.ceylon.model.typechecker.model.Module mod = declaration.getModule();
                getOrCreateMetamodel(mod);

                ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage(declaration); 
                typeCheckPackagesToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule getOrCreateMetamodel(com.redhat.ceylon.model.typechecker.model.Module declaration){
        return getOrCreateMetamodel(declaration, null, false /* not optional */);
    }

    private static com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule getOrCreateMetamodel(com.redhat.ceylon.model.typechecker.model.Module declaration,
            Set<com.redhat.ceylon.model.typechecker.model.Module> visitedModules, boolean optional){
        synchronized(getLock()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule ret = typeCheckModulesToRuntimeModel.get(declaration);
            if(ret == null){
                // make sure it is loaded
                loadModule(declaration, visitedModules, optional);
                if(!declaration.isAvailable())
                    return null;
                ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule(declaration); 
                typeCheckModulesToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    private static void loadModule(com.redhat.ceylon.model.typechecker.model.Module declaration, 
            Set<com.redhat.ceylon.model.typechecker.model.Module> visitedModules, boolean optional) {
        // don't do if not running JBoss modules
        if(!isJBossModules()){
            return;
        }
        // we must use the context module loader, which is the one CeylonModuleLoader for every user module
        // if we use the language module loader it will be a LocalModuleLoader which doesn't know about the
        // module repos specified on the command-line.
        ModuleLoader moduleLoader = org.jboss.modules.Module.getContextModuleLoader();
        // no loading required for these
        if(JDKUtils.isJDKModule(declaration.getNameAsString())
                || JDKUtils.isOracleJDKModule(declaration.getNameAsString()))
            return;
        try {
            org.jboss.modules.Module jbossModule = moduleLoader.loadModule(ModuleIdentifier.create(declaration.getNameAsString(), declaration.getVersion()));
            org.jboss.modules.ModuleClassLoader cl = jbossModule.getClassLoader();
            
            // if we can force it loaded, let's do
            if(cl instanceof CeylonModuleClassLoader){
                // this can complete in another thread or this thread
                ((CeylonModuleClassLoader) cl).registerInMetaModel();
                if(!declaration.isAvailable()){
                    // perhaps it is being loaded in another thread, wait for it
                    Object lock = getLock();
                    synchronized(lock){
                        int tries = RuntimeModelLoader.MAX_JBOSS_MODULES_WAITS;
                        while(!declaration.isAvailable()){
                            try {
                                lock.wait(RuntimeModelLoader.JBOSS_MODULES_TIMEOUT);
                            } catch (InterruptedException e) {
                                throw Metamodel.newModelError("Interrupted");
                            }
                            if(tries-- < 0)
                                throw Metamodel.newModelError("JBoss modules failed to make module available: "+declaration.getNameAsString());
                        }
                    }
                }
            }// it was loaded via the bootstrap module loader and does not need forcing
            
            if(visitedModules == null)
                visitedModules = new HashSet<com.redhat.ceylon.model.typechecker.model.Module>();
            // do not visit this module again
            visitedModules.add(declaration);
            // make sure its imports are also loaded
            for(ModuleImport mi : declaration.getImports()){
                com.redhat.ceylon.model.typechecker.model.Module importedModule = mi.getModule();
                // make sure we don't run in circles
                if(importedModule != null && !visitedModules.contains(importedModule))
                    getOrCreateMetamodel(importedModule, visitedModules, mi.isOptional());
            }
        } catch (ModuleLoadException e) {
            // it's not an issue if we don't find the default module, it's always created but not always
            // present. Also not an issue for optional modules.
            if(!declaration.isDefault() && !optional)
                throw Metamodel.newModelError(e.toString());
        } catch (SecurityException e) {
            throw Metamodel.newModelError(e.toString());
        } catch (IllegalArgumentException e) {
            throw Metamodel.newModelError(e.toString());
        }
    }

    private static boolean isJBossModules() {
        return Metamodel.class.getClassLoader() instanceof org.jboss.modules.ModuleClassLoader;
    }

    public static ceylon.language.meta.declaration.OpenType getMetamodel(Type pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Class){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Constructor){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassType(pt.getQualifyingType());
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Interface){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeInterfaceType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.TypeParameter){
            com.redhat.ceylon.model.typechecker.model.TypeParameter tp = (com.redhat.ceylon.model.typechecker.model.TypeParameter) declaration;
            return new FreeTypeParameterType(tp);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.UnionType){
            return new FreeUnionType((com.redhat.ceylon.model.typechecker.model.UnionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.IntersectionType){
            return new FreeIntersectionType((com.redhat.ceylon.model.typechecker.model.IntersectionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.NothingType){
            return ceylon.language.meta.declaration.nothingType_.get_();
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.UnknownType){
            ((com.redhat.ceylon.model.typechecker.model.UnknownType)declaration).reportErrors();
        }
        throw Metamodel.newModelError("Declaration type not supported yet: "+declaration);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Sequential<? extends ceylon.language.meta.declaration.OpenType> getMetamodelSequential(List<Type> types) {
        if(types.isEmpty())
            return (Sequential<? extends ceylon.language.meta.declaration.OpenType>)(Sequential)empty_.get_();
        ceylon.language.meta.declaration.OpenType[] ret = new ceylon.language.meta.declaration.OpenType[types.size()];
        int i=0;
        TypeDescriptor td = TypeDescriptor.NothingType;
        for(Type pt : types){
            OpenType mm = Metamodel.getMetamodel(pt);
            td = TypeDescriptor.union(((ReifiedType)mm).$getType$());
            ret[i++] = mm;
        }
        return Util.sequentialWrapper(td, ret);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getAppliedMetamodelSequential(List<Type> types) {
        if(types.isEmpty())
            return (Sequential<? extends ceylon.language.meta.model.Type<? extends Object>>)(Sequential)empty_.get_();
        ceylon.language.meta.model.Type<?>[] ret = new ceylon.language.meta.model.Type[types.size()];
        int i=0;
        for(Type pt : types){
            ret[i++] = Metamodel.getAppliedMetamodel(pt);
        }
        return Util.sequentialWrapper(TypeDescriptor.klass(ceylon.language.meta.model.Type.class, Anything.$TypeDescriptor$), 
                                       ret);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> ceylon.language.meta.model.Type<T> getAppliedMetamodel(Type pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Constructor){
            com.redhat.ceylon.model.typechecker.model.Constructor ctorDeclaration = (com.redhat.ceylon.model.typechecker.model.Constructor)declaration;
            Type ctorType = pt;
            pt = pt.getExtendedType();
            declaration = pt.getDeclaration();
            TypeDescriptor reifiedArguments;
            if(!Decl.isEnumeratedConstructor(ctorDeclaration) && !declaration.isAnonymous() 
                    && !isLocalType(declaration))
                reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional)ctorDeclaration, ctorType);
            else
                reifiedArguments = TypeDescriptor.NothingType;
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);

            if(declaration.isToplevel() || isLocalType(declaration))
                return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass(reifiedType, reifiedArguments, pt, null, null);
            
            TypeDescriptor reifiedContainer = getTypeDescriptorForProducedType(pt.getQualifyingType());
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass(reifiedContainer, reifiedType, reifiedArguments, pt);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Class){
            // anonymous classes don't have parameter lists
            TypeDescriptor reifiedArguments;
            if(!declaration.isAnonymous() 
                    && !isLocalType(declaration))
                reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional)declaration, pt);
            else
                reifiedArguments = TypeDescriptor.NothingType;
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);

            if(declaration.isToplevel() || isLocalType(declaration))
                return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass(reifiedType, reifiedArguments, pt, null, null);
            
            TypeDescriptor reifiedContainer = getTypeDescriptorForProducedType(pt.getQualifyingType());
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass(reifiedContainer, reifiedType, reifiedArguments, pt);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Interface){
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);
            if(declaration.isToplevel() || isLocalType(declaration))
                return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedInterface<T>(reifiedType, pt, null, null);

            TypeDescriptor reifiedContainer = getTypeDescriptorForProducedType(pt.getQualifyingType());
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberInterface(reifiedContainer, reifiedType, pt);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.UnionType){
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);
            return new AppliedUnionType<T>(reifiedType, (com.redhat.ceylon.model.typechecker.model.UnionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.IntersectionType){
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);
            return new AppliedIntersectionType<T>(reifiedType, (com.redhat.ceylon.model.typechecker.model.IntersectionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.NothingType){
            return (ceylon.language.meta.model.Type<T>)ceylon.language.meta.model.nothingType_.get_();
        }
        throw Metamodel.newModelError("Declaration type not supported yet: "+declaration);
    }

    public static java.lang.Class<?> getJavaClass(com.redhat.ceylon.model.typechecker.model.Module module) {
        String className = module.getNameAsString() + "." + NamingBase.MODULE_DESCRIPTOR_CLASS_NAME;
        ReflectionClass classMirror = (ReflectionClass)moduleManager.getModelLoader().lookupClassMirror(module, className);
        return classMirror.klass;
        
    }
    
    public static java.lang.Class<?> getJavaClass(com.redhat.ceylon.model.typechecker.model.Package pkg) {
        String className = ((LazyPackage) pkg).getNameAsString()+ "." + NamingBase.PACKAGE_DESCRIPTOR_CLASS_NAME;
        ReflectionClass classMirror = (ReflectionClass)moduleManager.getModelLoader().lookupClassMirror(pkg.getModule(), className);
        return classMirror != null ? classMirror.klass : null;
    }
    
    public static java.lang.Class<?> getJavaClass(com.redhat.ceylon.model.typechecker.model.Declaration declaration) {
        if(declaration instanceof LazyClass){
            ReflectionClass classMirror = (ReflectionClass) ((LazyClass) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof LazyInterface){
            ReflectionClass classMirror = (ReflectionClass) ((LazyInterface) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof LazyFunction){
            ReflectionClass classMirror = (ReflectionClass) ((LazyFunction) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof LazyValue){
            ReflectionClass classMirror = (ReflectionClass) ((LazyValue) declaration).classMirror;
            return classMirror.klass;
        }
        if (declaration instanceof LazyClassAlias) {
            ReflectionClass classMirror = (ReflectionClass) ((LazyClassAlias) declaration).classMirror;
            return classMirror.klass;
        }
        if (declaration instanceof LazyTypeAlias) {
            ReflectionClass classMirror = (ReflectionClass) ((LazyTypeAlias) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof AnnotationProxyClass){
            return getJavaClass(((AnnotationProxyClass) declaration).iface);
        }
        if(declaration.getContainer() instanceof com.redhat.ceylon.model.typechecker.model.Declaration){
            return getJavaClass((com.redhat.ceylon.model.typechecker.model.Declaration)declaration.getContainer());
        }
        throw Metamodel.newModelError("Unsupported declaration type: " + declaration + " of type "+declaration.getClass());
    }
    
    public static java.lang.reflect.Constructor<?> getJavaConstructor(com.redhat.ceylon.model.typechecker.model.Constructor declaration) {
        return getJavaConstructor((com.redhat.ceylon.model.typechecker.model.Class)declaration.getContainer(), declaration.getName());
    }
    
    public static java.lang.reflect.Constructor<?> getJavaConstructor(com.redhat.ceylon.model.typechecker.model.Class clazz, String ctorName) {
        Constructor<?>[] ctors = getJavaClass(clazz).getDeclaredConstructors();
        for (java.lang.reflect.Constructor<?> ctor : ctors) {
            if (ctor.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            Name name = ctor.getAnnotation(Name.class);
            String n1 = name == null ? "" : name.value();
            String n2 = ctorName == null ? "" : ctorName;
            if (n1.equals(n2)) {
                return ctor;
            }
        }
        throw Metamodel.newModelError("Unsupported declaration type: " + clazz);
    }
    
    public static java.lang.reflect.Method getJavaInstantiator(com.redhat.ceylon.model.typechecker.model.Constructor declaration, String methodName) {
        com.redhat.ceylon.model.typechecker.model.Class cls = (com.redhat.ceylon.model.typechecker.model.Class)declaration.getContainer();
        Class<?> terJavaCls = getJavaClass((Declaration)cls.getContainer());
        java.lang.reflect.Method[] methods = terJavaCls.getDeclaredMethods();
        // the instantiator method is @Ignore'd and not @Name'd
        // So we search for the method:
        // * with the instantiator name
        // * with or without a constructor class name parameter and
        // * with the longest signature
        // This really is horrible.
        int numReified = cls.getTypeParameters().size();
        boolean defaultCtor = isDefaultConstructor(declaration);
        java.lang.reflect.Method longestSig = null;
        for (java.lang.reflect.Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] sig = method.getParameterTypes();
                if (longestSig == null 
                        || longestSig.getParameterTypes().length < sig.length) {
                    Class<?> possibleCtorNameCls;
                    if (sig.length > numReified) {
                        possibleCtorNameCls = sig[numReified];
                    } else {
                        possibleCtorNameCls = null;
                    }
                    boolean ctorNameParam = possibleCtorNameCls != null
                            && possibleCtorNameCls.getAnnotation(Ignore.class) != null;
                            //&& possibleCtorNameCls.getAnnotation(Name.class) != null
                            //&& declaration.equals(possibleCtorNameCls.getAnnotation(Name.class).value());
                    if (defaultCtor && !ctorNameParam) {
                        longestSig = method;
                    } else if (!defaultCtor && ctorNameParam){
                        longestSig = method;
                    }
                }
            }
        }
        if (longestSig == null) {
            throw Metamodel.newModelError("Unsupported declaration type: " + declaration);
        } else {
            return longestSig;
        }
    }
    
    /** 
     * Return all the Java constructors for the given named Ceylon constructor, 
     * including the overloaded ones.
     */
    public static List<java.lang.reflect.Constructor<?>> getJavaConstructors(
            com.redhat.ceylon.model.typechecker.model.Constructor declaration) {
        Class<?> javaClass = getJavaClass((com.redhat.ceylon.model.typechecker.model.Class)declaration.getContainer());
        ArrayList<java.lang.reflect.Constructor<?>> result = new ArrayList<java.lang.reflect.Constructor<?>>();
        // find the appropriate ultimate constructor
        java.lang.reflect.Constructor<?> ultimate = getJavaConstructor(declaration);
        result.add(ultimate);
        List<Parameter> parameters = declaration.getFirstParameterList().getParameters();
        Class<?>[] javapl = ultimate.getParameterTypes();
        // find all the overloads of the ultimate that we expect, 
        // according to the parameter list 
        for (int ii = parameters.size()-1, jj=javapl.length; ii >= 0; ii--, jj--) {
            Parameter p = parameters.get(ii);
            if (p.isDefaulted()
                    || (p.isSequenced() && !p.isAtLeastOne())) {
                Class<?>[] sig = Arrays.copyOfRange(javapl, 0, jj-1);
                try {
                    Constructor<?> overloaded = javaClass.getDeclaredConstructor(sig);
                    result.add(overloaded);
                } catch (NoSuchMethodException e) {
                    throw Metamodel.newModelError("Could not find overloaded constructor with signature " + Arrays.toString(sig), e);
                }
            }
        }
        return result;
    }
    
    public static List<java.lang.reflect.Method> getJavaInstantiators(
            com.redhat.ceylon.model.typechecker.model.Constructor declaration) {
        com.redhat.ceylon.model.typechecker.model.Class classModel = (com.redhat.ceylon.model.typechecker.model.Class)declaration.getContainer();
//        Class<?> javaClass = getJavaClass(classModel);
        Class<?> outerJavaClass = getJavaClass((Declaration)classModel.getContainer()); 
//        java.lang.reflect.Method[] ctors = outerJavaClass.getDeclaredMethods();
        ArrayList<java.lang.reflect.Method> result = new ArrayList<java.lang.reflect.Method>();
        // find the appropriate ultimate constructor
        String methodName = classModel.getName() + "$new$";
        java.lang.reflect.Method ultimate = getJavaInstantiator(declaration, methodName);
        result.add(ultimate);
        List<Parameter> parameters = declaration.getFirstParameterList().getParameters();
        Class<?>[] javapl = ultimate.getParameterTypes();
        // find all the overloads of the ultimate that we expect, 
        // according to the parameter list 
        for (int ii = parameters.size()-1, jj=javapl.length; ii >= 0; ii--, jj--) {
            Parameter p = parameters.get(ii);
            if (p.isDefaulted()
                    || (p.isSequenced() && !p.isAtLeastOne())) {
                Class<?>[] sig = Arrays.copyOfRange(javapl, 0, jj-1);
                try {
                    java.lang.reflect.Method overloaded = outerJavaClass.getDeclaredMethod(methodName, sig);
                    result.add(overloaded);
                } catch (NoSuchMethodException e) {
                    throw Metamodel.newModelError("Could not find overloaded constructor with signature " + Arrays.toString(sig), e);
                }
            }
        }
        return result;
    }
    
    public static boolean isDefaultConstructor(com.redhat.ceylon.model.typechecker.model.Constructor declaration) {
        return declaration.getName() == null || declaration.getName().isEmpty();
    }

    public static java.lang.reflect.Method getJavaMethod(com.redhat.ceylon.model.typechecker.model.Function declaration) {
        if(declaration instanceof JavaMethod){
            ReflectionMethod methodMirror = (ReflectionMethod) ((JavaMethod) declaration).mirror;
            return (java.lang.reflect.Method) methodMirror.method;
        }
        if(declaration instanceof LazyFunction){
            ReflectionMethod methodMirror = (ReflectionMethod) ((LazyFunction) declaration).getMethodMirror();
            return (java.lang.reflect.Method) methodMirror.method;
        }
        throw Metamodel.newModelError("Unsupported declaration type: " + declaration);
    }

    public static TypeDescriptor getTypeDescriptorForProducedType(com.redhat.ceylon.model.typechecker.model.Type type) {
        TypeDeclaration declaration = type.getDeclaration();
        if(type.isNothing()){
            return TypeDescriptor.NothingType;
        }
        if(type.isUnion()){
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getCaseTypes());
            return TypeDescriptor.union(tdArgs);
        }
        if(type.isIntersection()){
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getSatisfiedTypes());
            return TypeDescriptor.intersection(tdArgs);
        }
        if(declaration instanceof LazyClass){
            ReflectionClass classMirror = (ReflectionClass) ((LazyClass) declaration).classMirror;
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getTypeArgumentList());
            TypeDescriptor ret = TypeDescriptor.klass(classMirror.klass, tdArgs);
            if(type.getQualifyingType() != null)
                return TypeDescriptor.member(getTypeDescriptorForProducedType(type.getQualifyingType()), ret);
            return ret;
        }
        if(declaration instanceof LazyInterface){
            ReflectionClass classMirror = (ReflectionClass) ((LazyInterface) declaration).classMirror;
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getTypeArgumentList());
            TypeDescriptor ret = TypeDescriptor.klass(classMirror.klass, tdArgs);
            if(type.getQualifyingType() != null)
                return TypeDescriptor.member(getTypeDescriptorForProducedType(type.getQualifyingType()), ret);
            return ret;
        }
        if(declaration instanceof FunctionOrValueInterface){
            TypedDeclaration underlyingDeclaration = ((FunctionOrValueInterface) declaration).getUnderlyingDeclaration();
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getTypeArgumentList());
            TypeDescriptor ret;
            if(underlyingDeclaration.isToplevel()){
                ReflectionClass classMirror;
                // FIXME: this is not really true, but reflects what's in TypeDescriptor.functionOrValue where we do not
                // make any different, but this should not matter since we only care about container functions and their
                // type arguments
                if(underlyingDeclaration instanceof Setter)
                    underlyingDeclaration = ((Setter) underlyingDeclaration).getGetter();
                if(underlyingDeclaration instanceof LazyValue)
                    classMirror = (ReflectionClass) ((LazyValue) underlyingDeclaration).classMirror;
                else if(underlyingDeclaration instanceof LazyFunction)
                    classMirror = (ReflectionClass) ((LazyFunction) underlyingDeclaration).classMirror;
                else
                    throw Metamodel.newModelError("Unsupported underlying declaration type: " + underlyingDeclaration);
                ret = TypeDescriptor.functionOrValue(classMirror.klass, tdArgs);
            }else
                ret = TypeDescriptor.functionOrValue(underlyingDeclaration.getPrefixedName(), tdArgs);
            if(type.getQualifyingType() != null)
                return TypeDescriptor.member(getTypeDescriptorForProducedType(type.getQualifyingType()), ret);
            return ret;
        }
        if(declaration instanceof UnknownType){
            ((UnknownType) declaration).reportErrors();
        }
        throw Metamodel.newModelError("Unsupported declaration type: " + (declaration == null ? "null" : declaration.getClass()));
    }

    public static TypeDescriptor[] getTypeDescriptors(Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        Iterator<? extends ceylon.language.meta.model.Type<?>> iterator = types.iterator();
        Object it;
        TypeDescriptor[] ret = new TypeDescriptor[(int) types.getSize()];
        int i=0;
        while((it = iterator.next()) != finished_.get_()){
            ceylon.language.meta.model.Type<?> annotationType = (ceylon.language.meta.model.Type<?>)it;
            ret[i++] = getTypeDescriptor(annotationType);
        }
        return ret;
    }

    @SuppressWarnings("rawtypes")
    public static TypeDescriptor getTypeDescriptor(ceylon.language.meta.model.Type<?> appliedType) {
        if(appliedType instanceof AppliedClass){
            return ((AppliedClass) appliedType).$reifiedType;
        }
        if(appliedType instanceof AppliedInterface){
            return ((AppliedInterface) appliedType).$reifiedType;
        }
        if(appliedType instanceof AppliedUnionType){
            return ((AppliedUnionType) appliedType).$reifiedUnion;
        }
        if(appliedType instanceof AppliedIntersectionType){
            return ((AppliedIntersectionType) appliedType).$reifiedIntersection;
        }
        if(appliedType == ceylon.language.meta.model.nothingType_.get_())
            return TypeDescriptor.NothingType;
        throw Metamodel.newModelError("Unsupported type: " + appliedType);
    }

    private static TypeDescriptor[] getTypeDescriptorsForProducedTypes(List<Type> args) {
        TypeDescriptor[] tdArgs = new TypeDescriptor[args.size()];
        for(int i=0;i<tdArgs.length;i++){
            tdArgs[i] = getTypeDescriptorForProducedType(args.get(i));
        }
        return tdArgs;
    }

    public static ceylon.language.meta.declaration.FunctionDeclaration getMetamodel(Function method) {
        // find its container
        Scope container = method.getContainer();
        if(container instanceof com.redhat.ceylon.model.typechecker.model.ClassOrInterface){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface classOrInterface = (FreeClassOrInterface) getOrCreateMetamodel((com.redhat.ceylon.model.typechecker.model.ClassOrInterface) container);
            // now find the method
            ceylon.language.meta.declaration.FunctionDeclaration ret = classOrInterface.findMethod(method.getName());
            if(ret == null)
                throw Metamodel.newModelError("Failed to find method "+method.getName()+" in "+container);
            return ret;
        }
        if(container instanceof com.redhat.ceylon.model.typechecker.model.Package){
            ceylon.language.meta.declaration.Package pkg = getOrCreateMetamodel((com.redhat.ceylon.model.typechecker.model.Package) container);
            ceylon.language.meta.declaration.FunctionDeclaration ret = pkg.getFunction(method.getName());
            if(ret == null)
                throw Metamodel.newModelError("Failed to find method "+method.getName()+" in "+container);
            return ret;
        }
        throw Metamodel.newModelError("Unsupported method container for "+method.getName()+": "+container);
    }

    public static com.redhat.ceylon.model.typechecker.model.Type getModel(ceylon.language.meta.declaration.OpenType pt) {
        if(pt instanceof FreeClassOrInterfaceType)
            return ((FreeClassOrInterfaceType)pt).producedType;
        throw Metamodel.newModelError("Unsupported produced type: " + pt);
    }

    public static com.redhat.ceylon.model.typechecker.model.Type getModel(ceylon.language.meta.model.Type<?> pt) {
        if(pt instanceof AppliedClassOrInterface)
            return ((AppliedClassOrInterface<?>)pt).producedType;
        if(pt instanceof AppliedUnionType<?>)
            return ((AppliedUnionType<?>)pt).model;
        if(pt instanceof AppliedIntersectionType<?>)
            return ((AppliedIntersectionType<?>)pt).model;
        if(pt instanceof ceylon.language.meta.model.nothingType_)
            return new NothingType(moduleManager.getModelLoader().getUnit()).getType();
            
        throw Metamodel.newModelError("Unsupported applied produced type: " + pt);
    }

    public static com.redhat.ceylon.model.typechecker.model.Package getPackage(com.redhat.ceylon.model.typechecker.model.Declaration declaration) {
        Scope scope = declaration.getContainer();
        while(scope != null && scope instanceof com.redhat.ceylon.model.typechecker.model.Package == false)
            scope = scope.getContainer();
        if(scope == null)
            throw Metamodel.newModelError("Declaration with no package: "+declaration);
        return (com.redhat.ceylon.model.typechecker.model.Package)scope;
    }


    public static java.util.List<com.redhat.ceylon.model.typechecker.model.Type> getProducedTypes(Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        Iterator<?> iterator = types.iterator();
        Object it;
        List<com.redhat.ceylon.model.typechecker.model.Type> producedTypes = new LinkedList<com.redhat.ceylon.model.typechecker.model.Type>();
        while((it = iterator.next()) != finished_.get_()){
            ceylon.language.meta.model.Type<?> pt = (ceylon.language.meta.model.Type<?>) it;
            com.redhat.ceylon.model.typechecker.model.Type modelPt = Metamodel.getModel(pt);
            producedTypes.add(modelPt);
        }
        return producedTypes;
    }
    
    /**
     * returns the java.lang.Class of the given the Ceylon metamodel of 
     * an annotation class.
     */
    public static 
    <Value extends java.lang.annotation.Annotation, 
    Values, 
    ProgramElement extends Annotated>
    Class<?> getReflectedAnnotationClass(
            ClassOrInterface<? extends java.lang.annotation.Annotation> annotationType) {
        FreeClassOrInterface freeClass;
        if (annotationType instanceof AppliedClassOrInterface) {
            freeClass = (FreeClassOrInterface)((AppliedClassOrInterface<?>)annotationType).getDeclaration();
        } else {
            freeClass = (FreeClassOrInterface)annotationType;
        }
        final Class<?> refAnnotationClass = getJavaClass(freeClass.declaration);
        return refAnnotationClass;
    }
    
    private static <A extends java.lang.annotation.Annotation> void addAnnotation(
            Annotated annotated, 
            ArrayList<A> ceylonAnnotations,
            java.lang.annotation.Annotation jAnnotation,
            Predicates.Predicate<A> pred) {
        Class<? extends java.lang.annotation.Annotation> jAnnotationType = jAnnotation.annotationType();
        if (pred != null && pred instanceof Predicates.AnnotationPredicate && !((Predicates.AnnotationPredicate<A>)pred).shouldInstantiate(jAnnotationType)) {
            return;
        }
        if (jAnnotationType.getAnnotation(Ceylon.class) == null) {
            // It's a Java annotation
            addProxyCeylonAnnotation(annotated, ceylonAnnotations, jAnnotation);
            return;
        }
        if (jAnnotationType.getName().endsWith("$annotations$")) {
            java.lang.annotation.Annotation[] jAnnotations;
            try {
                jAnnotations = (java.lang.annotation.Annotation[])jAnnotationType.getMethod("value").invoke(jAnnotation);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {/* aka ReflectiveOperationException */
                throw Metamodel.newModelError("While unwrapping a sequenced annotation of element " + annotated, e);
            }
            for (java.lang.annotation.Annotation wrapped : jAnnotations) {
                addAnnotation(annotated, ceylonAnnotations, wrapped, pred);
            }
        } else {
            // Find the annotation class
            java.lang.Class<A> annotationClass = getAnnotationClass(jAnnotationType, annotated);
            
            // Invoke it with the jAnnotation as the only argument
            try {
                Constructor<A> constructor = annotationClass.getDeclaredConstructor(jAnnotationType);
                constructor.setAccessible(true);
                A cAnnotation = constructor.newInstance(jAnnotation);
                if (pred.accept(cAnnotation)) {
                    ceylonAnnotations.add(cAnnotation);
                }
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {/* aka ReflectiveOperationException */
                throw Metamodel.newModelError("While reflectively instantiating " + annotationClass + " on element " + annotated, e);
            } 
        }
    }
    /** 
     * Gets the {@code java.lang.Class} of the Ceylon annotation class, 
     * given the {@code java.lang.Class} of a Java annotation type
     */
    protected static <A extends Annotation> Class<A> getAnnotationClass(
            Class<? extends java.lang.annotation.Annotation> jAnnotationType,
            Annotated annotated) {
        String annotationName = jAnnotationType.getName();
        if (!annotationName.endsWith("$annotation$")) {
            throw Metamodel.newModelError("Annotation has invalid name: "+annotationName);
        }
        String className = annotationName.substring(0, annotationName.length() - "$annotation$".length());
        java.lang.Class<A> annotationClass;
        try {
            annotationClass = (Class<A>)Class.forName(className, false, jAnnotationType.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw Metamodel.newModelError("Unable to find annotation class " + className + " for annotation type " + annotationName + " on element "+ annotated, e);
        }
        return annotationClass;
    }
    
    protected static <AT extends java.lang.annotation.Annotation> Class<AT> getJavaAnnotationClass(
            Class<? extends Annotation> ceylonAnnotationClass) {
        if(ceylonAnnotationClass == ceylon.language.Annotation.class
                || ceylonAnnotationClass == ceylon.language.ConstrainedAnnotation.class)
            return (Class<AT>) java.lang.annotation.Annotation.class;
        String suffix;
        if (ceylon.language.SequencedAnnotation.class.isAssignableFrom(ceylonAnnotationClass)) {
            suffix = "$annotations$";
        } else {
            suffix = "$annotation$";
        }
        String classname = ceylonAnnotationClass.getName() + suffix;
        try {
            return (Class)Class.forName(classname , false, ceylonAnnotationClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw Metamodel.newModelError("Unable to find annotation class " + classname + " for annotation type " + ceylonAnnotationClass, e);
        }
    }
    
    private static void addProxyCeylonAnnotation(
            Annotated annotated, ArrayList<? extends java.lang.annotation.Annotation> ceylonAnnotations,
            java.lang.annotation.Annotation jAnnotation) {
        Class<? extends java.lang.annotation.Annotation> jAnnotationType = jAnnotation.annotationType();
        // we add java.lang.Deprecated on top of our own annotation, so ignore it
        if(jAnnotationType == java.lang.Deprecated.class)
            return;
        // ignore all our metadata annotations
        if(jAnnotationType.getName().startsWith("com.redhat.ceylon.compiler.java.metadata."))
            return;
        // seriously, wtf?
        ((ArrayList)ceylonAnnotations).add(jAnnotation);
    }
    
    public static <A extends java.lang.annotation.Annotation> Sequential<? extends A> annotations(
            TypeDescriptor $reifiedValues,
            Annotated annotated) {
        // TODO If the annotated is not a valid target for the annotationType
        // we can return empty immediately
        Predicates.Predicate<A> predicate = Predicates.isAnnotationOfType($reifiedValues);
        return annotations($reifiedValues, annotated, predicate);
    }

    @SuppressWarnings("unchecked")
    public static <A extends java.lang.annotation.Annotation> Sequential<? extends A> annotations(TypeDescriptor $reifiedValues,
            Annotated annotated, Predicates.Predicate<A> predicate) {
        java.lang.annotation.Annotation[] jAnnotations = ((AnnotationBearing)annotated).$getJavaAnnotations$();
        if (jAnnotations == null) {
            throw Metamodel.newModelError("Unable to find java.lang.reflect.AnnotatedElement for " + annotated);
        }
        
        // TODO Fix initial size estimate when query for OptionalAnnotation
        ArrayList<A> ceylonAnnotations = new ArrayList<A>(jAnnotations.length);
        for (java.lang.annotation.Annotation jAnnotation: jAnnotations) {
            addAnnotation(annotated, ceylonAnnotations, jAnnotation, predicate);
        }
        java.lang.annotation.Annotation[] array = ceylonAnnotations.toArray(new java.lang.annotation.Annotation[0]);
		return new ObjectArrayIterable<A>($reifiedValues, (A[]) array).sequence();
    }

    public static String getJavaMethodName(Functional method) {
        // FIXME: introduce a damn interface for getRealName()
        if(method instanceof JavaMethod)
            return ((JavaMethod)method).getRealName();
        else if(method instanceof LazyFunction){
            return ((LazyFunction)method).getRealMethodName();
        }else
            throw Metamodel.newModelError("Function declaration type not supported yet: "+method);
    }

    public static int getFirstDefaultedParameter(List<Parameter> parameters) {
        int i = 0;
        for(Parameter param : parameters){
            if(param.isDefaulted() || 
                    (param.isSequenced() && !param.isAtLeastOne())){
                return i;
            }
            i++;
        }
        return -1;
    }

    public static int getVariadicParameter(List<Parameter> parameters) {
        int i = 0;
        for(Parameter param : parameters){
            if(param.isSequenced()){
                return i;
            }
            i++;
        }
        return -1;
    }
    
    public static Sequential<? extends ceylon.language.meta.declaration.Module> getModuleList() {
        // FIXME: this probably needs synchronisation to avoid new modules loaded during traversal
        Set<com.redhat.ceylon.model.typechecker.model.Module> modules = moduleManager.getModules().getListOfModules();
        com.redhat.ceylon.model.typechecker.model.Module[] view = new com.redhat.ceylon.model.typechecker.model.Module[modules.size()];
        modules.toArray(view);
        ceylon.language.meta.declaration.Module[] array = new ceylon.language.meta.declaration.Module[view.length];
        int i=0;
        for(com.redhat.ceylon.model.typechecker.model.Module module : view){
            FreeModule mod = getOrCreateMetamodel(module, null, true); // optional means don't throw if it's not available
            // skip unavailable modules
            if(mod != null)
                array[i++] = mod;
        }
        ObjectArrayIterable<ceylon.language.meta.declaration.Module> iterable = 
        		new ObjectArrayIterable<ceylon.language.meta.declaration.Module>(ceylon.language.meta.declaration.Module.$TypeDescriptor$, array);
        return iterable.take(i).sequence();
    }

    /**
     * Used by c.l.meta.modules.find, which accepts null
     */
    public static ceylon.language.meta.declaration.Module findLoadedModule(String name, String version) {
        // FIXME: this probably needs synchronisation to avoid new modules loaded during traversal
        com.redhat.ceylon.model.typechecker.model.Module module = moduleManager.findLoadedModule(name, version);
        // consider it optional to get null rather than exception
        return module != null ? getOrCreateMetamodel(module, null, true) : null;
    }

    /**
     * Used by c.l.meta.modules.find, which accepts null
     */
    public static Module getDefaultModule() {
        com.redhat.ceylon.model.typechecker.model.Module module = moduleManager.getModules().getDefaultModule();
        // consider it optional to get null rather than exception
        return module != null ? getOrCreateMetamodel(module, null, true) : null;
    }

    public static List<Type> getParameterProducedTypes(List<Parameter> parameters, Reference producedReference) {
        List<Type> parameterProducedTypes = new ArrayList<Type>(parameters.size());
        for(Parameter parameter : parameters){
            Type ft = producedReference.getTypedParameter(parameter).getFullType();
            parameterProducedTypes.add(ft);
        }
        return parameterProducedTypes;
    }
    
    public static boolean isCeylon(com.redhat.ceylon.model.typechecker.model.ClassOrInterface declaration){
        return JvmBackendUtil.isCeylon(declaration);
    }

    public static TypeDescriptor getTypeDescriptorForArguments(com.redhat.ceylon.model.typechecker.model.Unit unit, 
            com.redhat.ceylon.model.typechecker.model.Functional decl, 
            Reference producedReference) {
        if(!decl.getParameterLists().isEmpty()){
            List<Parameter> parameters = decl.getFirstParameterList().getParameters();
            com.redhat.ceylon.model.typechecker.model.Type tupleType 
            = unit.getParameterTypesAsTupleType(parameters, producedReference);
            return Metamodel.getTypeDescriptorForProducedType(tupleType);
        }else{
            return TypeDescriptor.NothingType;
        }
    }

    public static Type getProducedTypeForArguments(com.redhat.ceylon.model.typechecker.model.Unit unit, 
            com.redhat.ceylon.model.typechecker.model.Functional decl, 
            Reference producedReference) {
        
        if(!decl.getParameterLists().isEmpty()){
            List<Parameter> parameters = decl.getFirstParameterList().getParameters();
            return unit.getParameterTypesAsTupleType(parameters, producedReference);
        }else{
            return new NothingType(unit).getType();
        }
    }

    /**
     * This is also used by generated code in the JVM compiler, for type declaration literals.
     * In theory this can only be used for ClassOrInterface or TypeAlias.
     */
    public static ceylon.language.meta.declaration.NestableDeclaration getOrCreateMetamodel(java.lang.Class<?> klass){
        synchronized(getLock()){
            ceylon.language.meta.declaration.NestableDeclaration result = classToDeclaration.get(klass);
            if (result == null) {
                // FIXME: is this really enough?
                String typeName = klass.getName();
                com.redhat.ceylon.model.typechecker.model.Module module = moduleManager.findModuleForClass(klass);
                com.redhat.ceylon.model.typechecker.model.TypeDeclaration decl = 
                        (com.redhat.ceylon.model.typechecker.model.TypeDeclaration) 
                            moduleManager.getModelLoader().getDeclaration(module, typeName, DeclarationType.TYPE);
                result = (ceylon.language.meta.declaration.NestableDeclaration) getOrCreateMetamodel(decl);
                classToDeclaration.put(klass, result);
            }
            return result;
        }
    }

    public static TypeDescriptor getTypeDescriptorForFunction(Reference appliedFunction) {
        return getTypeDescriptorForProducedType(getFunctionReturnType(appliedFunction));
    }
    
    public static Type getFunctionReturnType(Reference appliedFunction) {
        // pull the return type out of the Callable
        Type fullType = appliedFunction.getFullType();
        return fullType.getTypeArgumentList().get(0);
    }

    public static com.redhat.ceylon.model.typechecker.model.Parameter getParameterFromTypedDeclaration(com.redhat.ceylon.model.typechecker.model.TypedDeclaration declaration) {
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.FunctionOrValue)
            return ((com.redhat.ceylon.model.typechecker.model.FunctionOrValue) declaration).getInitializerParameter();
        return null;
    }
    
    /**
     * Called when an annotation class is instantiated via an annotation 
     * constructor or annotation callsite to convert the String representation
     * of a Declaration literal back into the corresponding Declaration.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ceylon.language.meta.declaration.Declaration> T parseMetamodelReference(String ref/*, java.lang.Class<?> klass*/) {
        DeclarationParser parser = new DeclarationParser();
        return (T)parser.ref(ref);
    }
    
    /**
     * Called when an annotation class is instantiated via an annotation 
     * constructor or annotation callsite to convert an array of String representations
     * of Declaration literals back into a Sequential of Declarations.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ceylon.language.meta.declaration.Declaration> Sequential<T> parseMetamodelReferences(TypeDescriptor $reifiedElement, String[] refs) {
        DeclarationParser parser = new DeclarationParser();
        ceylon.language.meta.declaration.Declaration[] array = new ceylon.language.meta.declaration.Declaration[refs.length];
        for (int ii = 0; ii < refs.length; ii++) {
            array[ii] = (T)parser.ref(refs[ii]);
        }
        return Util.<T>sequentialWrapper($reifiedElement, (T[])array);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T parseEnumerationReference(java.lang.Class<T> klass) {
        FreeClassOrInterface decl = (FreeClassOrInterface)getOrCreateMetamodel(klass);
        String getterName = NamingBase.getGetterName(decl.declaration);
        try {
            java.lang.reflect.Method method = klass.getDeclaredMethod(getterName);
            method.setAccessible(true);
            return (T)method.invoke(null);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {/* aka ReflectiveOperationException */
            throw Metamodel.newModelError(e.toString());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> Sequential<? extends T> parseEnumerationReferences(TypeDescriptor $reifiedElement, java.lang.Class<?>[] refs) {
        Object[] array = new Object[refs.length];
        for (int ii = 0; ii < refs.length; ii++) {
            array[ii] = parseEnumerationReference(refs[ii]);
        }
        return new ObjectArrayIterable<T>($reifiedElement, (T[]) array).sequence();
    }

    public static Sequential<? extends ceylon.language.meta.declaration.TypeParameter> getTypeParameters(com.redhat.ceylon.model.typechecker.model.Generic declaration) {
        List<com.redhat.ceylon.model.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.meta.declaration.TypeParameter[] typeParametersArray = new ceylon.language.meta.declaration.TypeParameter[typeParameters.size()];
        int i=0;
        for(com.redhat.ceylon.model.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        return Util.sequentialWrapper(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor$, typeParametersArray);
    }

    @SuppressWarnings("hiding")
    public static <DeclarationType extends ceylon.language.meta.declaration.Declaration>
        DeclarationType findDeclarationByName(Sequential<? extends DeclarationType> declarations, String name) {
        Iterator<? extends DeclarationType> iterator = declarations.iterator();
        Object it;
        while((it = iterator.next()) != finished_.get_()){
            @SuppressWarnings("unchecked")
            DeclarationType tp = (DeclarationType) it;
            if(tp.getName().equals(name))
                return tp;
        }
        return null;
    }

    public static AnnotatedDeclaration getContainer(Declaration declaration) {
        Scope container = declaration.getContainer();
        if(container instanceof com.redhat.ceylon.model.typechecker.model.Declaration)
            return Metamodel.getOrCreateMetamodel((com.redhat.ceylon.model.typechecker.model.Declaration)container);
        if(container instanceof com.redhat.ceylon.model.typechecker.model.Package)
            return Metamodel.getOrCreateMetamodel((com.redhat.ceylon.model.typechecker.model.Package)container);
        // FIXME: can that happen?
        throw Metamodel.newModelError("Illegal container type: "+container);
    }

    public static boolean isLocalType(com.redhat.ceylon.model.typechecker.model.TypeDeclaration decl) {
        return ((LazyElement)decl).isLocal();
    }

    public static ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> 
        getTypeArguments(ceylon.language.meta.declaration.GenericDeclaration declaration, Reference appliedFunction) {
        
        java.util.Map<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.model.Type<?>> typeArguments 
            = new LinkedHashMap<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.model.Type<?>>();
        Iterator<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.model.typechecker.model.TypeParameter, com.redhat.ceylon.model.typechecker.model.Type> ptArguments 
        = appliedFunction.getTypeArguments();
        while((it = typeParameters.next()) != finished_.get_()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter) it;
            com.redhat.ceylon.model.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.model.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.model.typechecker.model.Type ptArg = ptArguments.get(tpDecl);
            ceylon.language.meta.model.Type<?> ptArgWrapped = Metamodel.getAppliedMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        return new InternalMap<ceylon.language.meta.declaration.TypeParameter, 
                               ceylon.language.meta.model.Type<?>>(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor$, 
                                                                   TD_ClosedTypeOfAnything, 
                                                                   typeArguments);
    }

    public static ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.Sequence<? extends Object>> 
        getTypeArgumentWithVariances(ceylon.language.meta.declaration.GenericDeclaration declaration, Reference appliedFunction) {
    
        java.util.Map<ceylon.language.meta.declaration.TypeParameter, ceylon.language.Sequence<? extends Object>> typeArguments 
            = new LinkedHashMap<ceylon.language.meta.declaration.TypeParameter, ceylon.language.Sequence<? extends Object>>();
        Iterator<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.model.typechecker.model.TypeParameter, com.redhat.ceylon.model.typechecker.model.Type> ptArguments 
            = appliedFunction.getTypeArguments();
        Map<TypeParameter, SiteVariance> varianceOverrides = appliedFunction instanceof com.redhat.ceylon.model.typechecker.model.Type
                ? ((com.redhat.ceylon.model.typechecker.model.Type)appliedFunction).getVarianceOverrides() : null;
        while((it = typeParameters.next()) != finished_.get_()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter) it;
            com.redhat.ceylon.model.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.model.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.model.typechecker.model.Type ptArg = ptArguments.get(tpDecl);
            ceylon.language.meta.model.Type<?> ptArgWrapped = Metamodel.getAppliedMetamodel(ptArg);
            ceylon.language.meta.declaration.Variance variance = modelVarianceToMetaModel(varianceOverrides, tpDecl);
            ceylon.language.Sequence<? extends Object> tuple = ceylon.language.Tuple.instance(TD_ClosedTypeArgumentElement, new Object[]{ptArgWrapped, variance});
            typeArguments.put(tp, tuple);
        }
        return new InternalMap<ceylon.language.meta.declaration.TypeParameter, 
                               ceylon.language.Sequence<?>>(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor$, 
                                                            TD_ClosedTypeArgument, 
                                                            typeArguments);
    }

    public static ceylon.language.meta.declaration.Variance modelVarianceToMetaModel(Map<TypeParameter, SiteVariance> varianceOverrides, TypeParameter tpDecl) {
        if(varianceOverrides == null)
            return getModelVariance(tpDecl);
        SiteVariance useSiteVariance = varianceOverrides.get(tpDecl);
        if(useSiteVariance == null)
            return ceylon.language.meta.declaration.invariant_.get_();
        switch(useSiteVariance){
        case IN:
            return ceylon.language.meta.declaration.contravariant_.get_(); 
        case OUT:
            return ceylon.language.meta.declaration.covariant_.get_(); 
        }
        // should not happen
        return ceylon.language.meta.declaration.invariant_.get_();
    }

    public static String toTypeString(ceylon.language.meta.declaration.NestableDeclaration declaration, 
            ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, 
                                ? extends ceylon.language.Sequence<? extends Object>> typeArguments){
        StringBuffer string = new StringBuffer();
        string.append(declaration.getName());
        if(declaration instanceof ceylon.language.meta.declaration.GenericDeclaration)
            addTypeArguments(string, (ceylon.language.meta.declaration.GenericDeclaration)declaration, typeArguments);
        java.lang.Object container = declaration.getContainer();
        while(container != null){
            if(container instanceof Package)
                return ((Package)container).getName() + "::" + string;
            StringBuffer string2 = new StringBuffer(((NestableDeclaration)container).getName());
            if(container instanceof ceylon.language.meta.declaration.GenericDeclaration)
                addTypeArguments(string2, (ceylon.language.meta.declaration.GenericDeclaration)container, typeArguments);
            string2.append(".");
            string.insert(0, string2.toString());
            container = ((NestableDeclaration)container).getContainer();
        }
        return string.toString();
    }
    
    /**
     * @param <TypeOrOpenType> Either a Type (appending for a Model) or an 
     * OpenType (appending for a Declaration)
     */
    private static void addTypeArguments(StringBuffer string, ceylon.language.meta.declaration.GenericDeclaration declaration,
            ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.Sequence<? extends Object>> typeArguments) {
        if(!declaration.getTypeParameterDeclarations().getEmpty()){
            string.append("<");
            Iterator<?> iterator = declaration.getTypeParameterDeclarations().iterator();
            Object it;
            boolean once = true;
            while((it = iterator.next()) != finished_.get_()){
                if(once)
                    once = false;
                else
                    string.append(",");
                ceylon.language.meta.declaration.TypeParameter tpDecl = (ceylon.language.meta.declaration.TypeParameter) it;
                ceylon.language.Sequence<?> tuple = typeArguments != null ? typeArguments.get(tpDecl) : null;
                Object val = tuple.getFromFirst(0);
                ceylon.language.meta.declaration.Variance variance = (ceylon.language.meta.declaration.Variance)tuple.getFromFirst(1);
                if(variance == ceylon.language.meta.declaration.contravariant_.get_())
                    string.append("in ");
                else if(variance == ceylon.language.meta.declaration.covariant_.get_())
                    string.append("out ");
                
                if (val instanceof ceylon.language.meta.model.Type) {
                    string.append(val);
                } else if (val instanceof ceylon.language.meta.declaration.OpenTypeVariable) {
                    string.append(((ceylon.language.meta.declaration.OpenTypeVariable)val).getDeclaration().getQualifiedName());
                } else if (val instanceof ceylon.language.meta.declaration.OpenClassOrInterfaceType) {
                    string.append(((ceylon.language.meta.declaration.OpenClassOrInterfaceType)val).getDeclaration().getQualifiedName());
                } else if (val instanceof ceylon.language.meta.declaration.OpenType) {
                    string.append(val);
                } else {
                    string.append("##Missing##");
                }
            }
            string.append(">");
        }
    }

    public static String toTypeString(ceylon.language.meta.model.Model model){
        StringBuffer string = new StringBuffer();
        ceylon.language.meta.model.Type<?> container = model.getContainer();
        if (model instanceof AppliedCallableConstructor) {
            string.append(((AppliedCallableConstructor<?,?>)model).appliedClass.toString()).append(".");
        } else if (model instanceof AppliedValueConstructor) {
            string.append(((AppliedValueConstructor<?>)model).clazz.toString()).append(".");
        } else if (model instanceof AppliedCallableMemberConstructor) {
            string.append(((AppliedCallableMemberConstructor<?,?,?>)model).clazz.toString()).append(".");
        } else if (model instanceof AppliedValueMemberConstructor) {
            string.append(((AppliedValueMemberConstructor<?,?>)model).clazz.toString()).append(".");
        } else if(container == null){
            string.append(model.getDeclaration().getContainingPackage().getName()).append("::");
        } else if(container instanceof ceylon.language.meta.model.ClassOrInterface<?>){
            string.append(container.toString()).append(".");
        } else {
            string.append("<").append(container.toString()).append(">.");
        }
        
        string.append(model.getDeclaration().getName());
        if(model instanceof ceylon.language.meta.model.Generic)
            addTypeArguments(string, (ceylon.language.meta.declaration.GenericDeclaration) model.getDeclaration(), 
                    ((ceylon.language.meta.model.Generic)model).getTypeArgumentWithVariances());
        return string.toString();
    }

    public static void checkTypeArguments(Type qualifyingType, Declaration declaration, List<Type> typeArguments) {
        if(declaration instanceof com.redhat.ceylon.model.typechecker.model.Generic){
            List<com.redhat.ceylon.model.typechecker.model.TypeParameter> typeParameters = ((com.redhat.ceylon.model.typechecker.model.Generic) declaration).getTypeParameters();
            if(typeParameters.size() < typeArguments.size())
                throw new TypeApplicationException("Too many type arguments provided: "+typeArguments.size()+", but only accepts "+typeParameters.size());
            int min = 0;
            for (TypeParameter tp: typeParameters) { 
                if (!tp.isDefaulted()) min++;
            }
            if(typeArguments.size() < min){
                String requires = (min == typeParameters.size()) ? "exactly" : "at least";
                throw new TypeApplicationException("Not enough type arguments provided: "+typeArguments.size()+", but requires "+requires+" "+min);
            }
            for(int i=0;i<typeArguments.size();i++){
                Type typeArgument = typeArguments.get(i);
                com.redhat.ceylon.model.typechecker.model.TypeParameter typeParameter = typeParameters.get(i);
                for (Type st: typeParameter.getSatisfiedTypes()) {
                    Type sts = st.appliedType(qualifyingType, declaration, typeArguments, null);
                    if (!typeArgument.isSubtypeOf(sts)) {
                        throw new TypeApplicationException("Type argument "+i+": "+typeArgument.asQualifiedString()
                                +" does not conform to upper bound constraint: "+sts.asQualifiedString()
                                +" of type parameter "+typeParameter.getQualifiedNameString());
                    }
                }
                if(!ModelUtil.argumentSatisfiesEnumeratedConstraint(qualifyingType, declaration, typeArguments, typeArgument, typeParameter)){
                    throw new TypeApplicationException("Type argument "+i+": "+typeArgument.asQualifiedString()
                            +" does not conform to enumerated constraints "
                            +" of type parameter "+typeParameter.getQualifiedNameString());
                }
            }
        }else{
            if(!typeArguments.isEmpty())
                throw new TypeApplicationException("Declaration does not accept type arguments");
        }
    }

    public static boolean isTypeOf(Type producedType, Object instance) {
        Type instanceType = Metamodel.getProducedType(instance);
        return instanceType.isSubtypeOf(producedType);
    }

    public static boolean isSuperTypeOf(Type a, ceylon.language.meta.model.Type<? extends Object> type) {
        Type b = Metamodel.getModel(type);
        return a.isSupertypeOf(b);
    }

    public static boolean isSubTypeOf(Type a, ceylon.language.meta.model.Type<? extends Object> type) {
        Type b = Metamodel.getModel(type);
        return a.isSubtypeOf(b);
    }

    public static boolean isExactly(Type a, ceylon.language.meta.model.Type<? extends Object> type) {
        Type b = Metamodel.getModel(type);
        return a.isExactly(b);
    }
    
    public static ceylon.language.meta.model.Type<?> union(
            ceylon.language.meta.model.Type<? extends Object> typeX, 
            ceylon.language.meta.model.Type<? extends Object> typeY) {
        Type x = Metamodel.getModel(typeX);
        Type y = Metamodel.getModel(typeY);
        Type unionType = com.redhat.ceylon.model.typechecker.model.ModelUtil.unionType(x, y, moduleManager.getModelLoader().getUnit());
        return getAppliedMetamodel(unionType);
    }
    
    public static ceylon.language.meta.model.Type<?> intersection(
            ceylon.language.meta.model.Type<? extends Object> typeX, 
            ceylon.language.meta.model.Type<? extends Object> typeY) {
        Type x = Metamodel.getModel(typeX);
        Type y = Metamodel.getModel(typeY);
        Type intersectionType = com.redhat.ceylon.model.typechecker.model.ModelUtil.intersectionType(x, y, moduleManager.getModelLoader().getUnit());
        return getAppliedMetamodel(intersectionType);
    }

    public static void checkReifiedTypeArgument(String methodName, String className, Variance variance, Type appliedType, TypeDescriptor $reifiedType) {
        Type expectedReifiedType = Metamodel.getProducedType($reifiedType);
        boolean check = checkReifiedTypeArgument(variance, appliedType, expectedReifiedType);
        if(!check){
            String appliedTypeString = appliedType.asString();
            String expectedReifiedTypeString = expectedReifiedType.asString();
            String appliedString = className.replace("$1", appliedTypeString);
            String expectedString = className.replace("$1", expectedReifiedTypeString);
            throw new IncompatibleTypeException("Incompatible type: actual type of applied declaration is "+appliedString
                    +" is not compatible with expected type: "+expectedString+". Try passing the type argument explicitly with: "
                    +methodName+"<"+appliedTypeString+">()");
        }
    }

    public static void checkReifiedTypeArgument(String methodName, String className, 
            Variance variance1, Type appliedType1, TypeDescriptor $reifiedType1,
            Variance variance2, Type appliedType2, TypeDescriptor $reifiedType2) {
        Type expectedReifiedType1 = Metamodel.getProducedType($reifiedType1);
        Type expectedReifiedType2 = Metamodel.getProducedType($reifiedType2);
        boolean check1 = checkReifiedTypeArgument(variance1, appliedType1, expectedReifiedType1);
        boolean check2 = checkReifiedTypeArgument(variance2, appliedType2, expectedReifiedType2);
        if(!check1 || !check2){
            String appliedTypeString1 = appliedType1.asString();
            String expectedReifiedTypeString1 = expectedReifiedType1.asString();
            String appliedTypeString2 = appliedType2.asString();
            String expectedReifiedTypeString2 = expectedReifiedType2.asString();
            String appliedString = className.replace("$1", appliedTypeString1).replace("$2", appliedTypeString2);
            String expectedString = className.replace("$1", expectedReifiedTypeString1).replace("$2", expectedReifiedTypeString2);
            throw new IncompatibleTypeException("Incompatible type: actual type of applied declaration is "+appliedString
                    +" is not compatible with expected type: "+expectedString+". Try passing the type argument explicitly with: "
                    +methodName+"<"+appliedTypeString1+","+appliedTypeString2+">()");
        }
    }

    public static void checkReifiedTypeArgument(String methodName, String className, 
            Variance variance1, Type appliedType1, TypeDescriptor $reifiedType1,
            Variance variance2, Type appliedType2, TypeDescriptor $reifiedType2,
            Variance variance3, Type appliedType3, TypeDescriptor $reifiedType3) {
        Type expectedReifiedType1 = Metamodel.getProducedType($reifiedType1);
        Type expectedReifiedType2 = Metamodel.getProducedType($reifiedType2);
        Type expectedReifiedType3 = Metamodel.getProducedType($reifiedType3);
        boolean check1 = checkReifiedTypeArgument(variance1, appliedType1, expectedReifiedType1);
        boolean check2 = checkReifiedTypeArgument(variance2, appliedType2, expectedReifiedType2);
        boolean check3 = checkReifiedTypeArgument(variance3, appliedType3, expectedReifiedType3);
        if(!check1 || !check2 || !check3){
            String appliedTypeString1 = appliedType1.asString();
            String expectedReifiedTypeString1 = expectedReifiedType1.asString();
            String appliedTypeString2 = appliedType2.asString();
            String expectedReifiedTypeString2 = expectedReifiedType2.asString();
            String appliedTypeString3 = appliedType3.asString();
            String expectedReifiedTypeString3 = expectedReifiedType3.asString();
            String appliedString = className.replace("$1", appliedTypeString1).replace("$2", appliedTypeString2).replace("$3", appliedTypeString3);
            String expectedString = className.replace("$1", expectedReifiedTypeString1).replace("$2", expectedReifiedTypeString2).replace("$3", expectedReifiedTypeString3);
            throw new IncompatibleTypeException("Incompatible type: actual type of applied declaration is "+appliedString
                    +" is not compatible with expected type: "+expectedString+". Try passing the type argument explicitly with: "
                    +methodName+"<"+appliedTypeString1+","+appliedTypeString2+","+appliedTypeString3+">()");
        }
    }

    private static boolean checkReifiedTypeArgument(Variance variance, Type appliedType, Type expectedReifiedType) {
        switch(variance){
        case IN: return appliedType.isSupertypeOf(expectedReifiedType);
        case OUT: return appliedType.isSubtypeOf(expectedReifiedType);
        case NONE: return appliedType.isExactly(expectedReifiedType);
        default:
            throw Metamodel.newModelError("Invalid variance: "+variance);
        }
    }

    public static void checkQualifyingType(Type qualifyingType, Declaration declaration) {
        Scope container = declaration.getContainer();
        if(container instanceof TypeDeclaration == false)
            throw new IncompatibleTypeException("Declaration container is not a type: "+container);
        TypeDeclaration typeDecl = (TypeDeclaration) container;
        Type supertype = qualifyingType.getSupertype(typeDecl);
        if(supertype == null)
            throw new IncompatibleTypeException("Invalid container type: "+qualifyingType+" is not a subtype of "+typeDecl);
    }
    
    public static <Return> Return namedApply(Callable<? extends Return> function,
            DefaultValueProvider defaultValueProvider, 
            com.redhat.ceylon.model.typechecker.model.Functional declaration,
            ceylon.language.Iterable<? extends ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object>,? extends java.lang.Object> arguments, 
            List<Type> parameterProducedTypes){
        // FIXME: throw for Java declarations
        
        java.util.Map<java.lang.String,java.lang.Object> argumentMap = collectArguments(arguments);
        
        java.util.List<Parameter> parameters = declaration.getFirstParameterList().getParameters();
        
        // store the values in an array
        Array<java.lang.Object> values = new Array<java.lang.Object>(Anything.$TypeDescriptor$, parameters.size(), (java.lang.Object) null);
        int parameterIndex = 0;
        for(Parameter parameter : parameters){
            // get the parameter value and remove it so we can keep track of those we used
            java.lang.Object value;
            if(argumentMap.containsKey(parameter.getName())){
                value = argumentMap.remove(parameter.getName());
                // we have a value: check the type
                Type argumentType = Metamodel.getProducedType(value);
                Type parameterType = parameterProducedTypes.get(parameterIndex);
                if(!argumentType.isSubtypeOf(parameterType))
                    throw new ceylon.language.meta.model.IncompatibleTypeException("Invalid argument "+parameter.getName()+", expected type "+parameterType+" but got "+argumentType);
            }else{
                // make sure it has a default value
                if(!parameter.isDefaulted())
                    throw new InvocationException("Missing value for non-defaulted parameter "+parameter.getName());
                // we need to fetch the default value
                value = defaultValueProvider.getDefaultParameterValue(parameter, values, parameterIndex);
                argumentMap.remove(parameter.getName());
            }
            values.set(parameterIndex++, value);
        }
        // do we have extra unknown/unused parameters left?
        if(!argumentMap.isEmpty()){
            for(String name : argumentMap.keySet()){
                throw new InvocationException("No such parameter "+name);
            }
        }
        // FIXME: don't we need to spread any variadic param?
        
        // now do a regular invocation
        Sequential<? extends Object> argumentSequence = values.sequence();
        // we can trust any variadic or pseudo-variadic since we checked parameter by parameter (no spreading possible)
        return Util.apply(function, argumentSequence, null);
    }
    
    private static Map<String, Object> collectArguments(ceylon.language.Iterable<? extends ceylon.language.Entry<? extends ceylon.language.String,? extends Object>,? extends Object> arguments) {
        java.util.Map<java.lang.String,java.lang.Object> args = new java.util.HashMap<>();
        ceylon.language.Iterator<? extends ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object>> iterator 
                = arguments.iterator();
        java.lang.Object elem; 
        while((elem = iterator.next()) != finished_.get_()){
            @SuppressWarnings("unchecked")
            ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object> entry 
                = (ceylon.language.Entry<? extends ceylon.language.String,? extends java.lang.Object>)elem;
            args.put(entry.getKey().toString(), entry.getItem());
        }
        return args;
    }

    public static <Return> Return apply(Callable<? extends Return> function,
            Sequential<?> arguments, 
            List<Type> parameterProducedTypes,
            int firstDefaulted, int variadicIndex){
        int argumentCount = Util.toInt(arguments.getSize());
        int parameters = parameterProducedTypes.size();
        
        // check minimum
        if(firstDefaulted == -1){
            if(argumentCount < parameters)
                throw new InvocationException("Not enough arguments to function. Expected "+parameters+" but got only "+argumentCount);
        }else if(argumentCount < firstDefaulted)
            throw new InvocationException("Not enough arguments to function. Expected at least "+firstDefaulted+" but got only "+argumentCount);
        
        // check maximum
        if(variadicIndex == -1){
            if(argumentCount > parameters)
                throw new InvocationException("To many arguments to function. Expected at most "+parameters+" but got "+argumentCount);
        }// if we're variadic we accept any number
        
        // now check their types
        Iterator<?> it = arguments.iterator();
        Object arg;
        int i = 0;
        Type variadicElement = null;
        if(variadicIndex != -1)
            // it must be a Sequential<T>
            variadicElement = parameterProducedTypes.get(variadicIndex).getTypeArgumentList().get(0);
        while((arg = it.next()) != finished_.get_()){
            Type parameterType = variadicIndex == -1 || i < variadicIndex ?
                    // normal param
                    parameterProducedTypes.get(i)
                    // variadic param
                    : variadicElement;
            Type argumentType = Metamodel.getProducedType(arg);
            if(!argumentType.isSubtypeOf(parameterType))
                throw new IncompatibleTypeException("Invalid argument "+i+", expected type "+parameterType+" but got "+argumentType);
            i++;
        }
        // they are all good, let's call it
        TypeDescriptor variadicElementType = variadicElement != null ? Metamodel.getTypeDescriptorForProducedType(variadicElement) : null;
        return Util.apply(function, arguments, variadicElementType);
    }
    
    public static <K,C>K bind(ceylon.language.meta.model.Qualified<K,C> member, Type containerType, Object container){
        if(container == null)
            throw new IncompatibleTypeException("Invalid container "+container+", expected type "+containerType+" but got ceylon.language::Null");
        Type argumentType = Metamodel.getProducedType(container);
        if(!argumentType.isSubtypeOf(containerType))
            throw new IncompatibleTypeException("Invalid container "+container+", expected type "+containerType+" but got "+argumentType);
        return member.$call$(container);
    }

    public static int hashCode(FreeNestableDeclaration decl, String type) {
        int result = 1;
        java.lang.Object container = decl.getContainer();
        result = 37 * result + type.hashCode();
        result = 37 * result + (container == null ? 0 : container.hashCode());
        result = 37 * result + (decl.getQualifier() == null ? 0 : decl.getQualifier().hashCode());
        result = 37 * result + decl.getName().hashCode();
        return result;
    }

    public static boolean equalsForSameType(FreeNestableDeclaration a, FreeNestableDeclaration b) {
        if(!Util.eq(a.getContainer(), b.getContainer()))
            return false;
        if(!Util.eq(a.getQualifier(), b.getQualifier()))
            return false;
        return a.getName().equals(b.getName());
    }

    public static RuntimeException newModelError(String string) {
        return newModelError(string, null);
    }

    public static RuntimeException newModelError(String string, Throwable cause) {
        // we throw in rethrow in order not to have to declare the error everywhere
        Util.rethrow(new ModelError(string, cause));
        // we don't return any thing since we throw
        return null;
    }
    
    /**
     * Used by the generated JVM compiler code
     */
    public static ceylon.language.meta.declaration.Module checkModule(ceylon.language.meta.declaration.Module module, String name, String version){
        if(module == null){
            String spec;
            if(version == null)
                spec = name;
            else
                spec = name + "/" + version;
            throw new ceylon.language.AssertionError("Module "+spec+" is not available");
        }
        return module;
    }
    
    public static Object getCompanionInstance(java.lang.Object instance, com.redhat.ceylon.model.typechecker.model.Interface iface) {
        if (instance == null) {
            return null;
        }
        try {
            java.lang.reflect.Method implAccessor = instance.getClass().getMethod("$" + 
        iface.getQualifiedNameString().replace('.', '$').replace("::", "$") + "$impl");
            implAccessor.setAccessible(true);
            return implAccessor.invoke(instance);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<?>> getTypeArgumentList(Generic generic) {
        Object sequence = sequence_.sequence(
                TypeDescriptor.klass(ceylon.language.meta.model.Type.class, Anything.$TypeDescriptor$), Null.$TypeDescriptor$, 
                generic.getTypeArguments().getItems());
        return (Sequential)(sequence != null ? sequence : empty_.get_());
    }

    public static ceylon.language.Sequential<? extends ceylon.language.meta.declaration.OpenType> getTypeArgumentList(OpenClassOrInterfaceType generic) {
        Object sequence = sequence_.sequence(
                OpenType.$TypeDescriptor$, Null.$TypeDescriptor$, 
                generic.getTypeArguments().getItems());
        return (Sequential)(sequence != null ? sequence : empty_.get_());
    }

    public static ceylon.language.Sequential<? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVarianceList(Generic generic) {
        Object sequence = sequence_.sequence(
                TD_ClosedTypeArgument, Null.$TypeDescriptor$, 
                generic.getTypeArgumentWithVariances().getItems());
        return (Sequential)(sequence != null ? sequence : empty_.get_());
    }

    public static ceylon.language.Sequential<? extends ceylon.language.Sequence<? extends Object>> getTypeArgumentWithVarianceList(OpenClassOrInterfaceType generic) {
        Object sequence = sequence_.sequence(
                TD_OpenTypeArgument, Null.$TypeDescriptor$, 
                generic.getTypeArgumentWithVariances().getItems());
        return (Sequential)(sequence != null ? sequence : empty_.get_());
    }

    public static boolean isAnnotated(TypeDescriptor reifed$AnnotationType,
            AnnotationBearing annotated) {
        return annotated.$isAnnotated$(getJavaAnnotationClass((Class)((TypeDescriptor.Class)reifed$AnnotationType).getKlass()));
    }

    public static ceylon.language.meta.declaration.Variance getModelVariance(TypeParameter declaration) {
        if(declaration.isInvariant())
            return ceylon.language.meta.declaration.invariant_.get_();
        if(declaration.isCovariant())
            return ceylon.language.meta.declaration.covariant_.get_();
        if(declaration.isContravariant())
            return ceylon.language.meta.declaration.contravariant_.get_();
        throw Metamodel.newModelError("Underlying declaration is neither invariant, covariant nor contravariant");
    }
    
    static boolean hasAllAnnotations(AnnotatedDeclaration decl, TypeDescriptor[] annotationTypeDescriptors) {
        for(TypeDescriptor annotationTypeDescriptor : annotationTypeDescriptors){
            if(decl.annotations(annotationTypeDescriptor).getEmpty()){
                // skip this declaration
                return false;
            }
        }
        return true;
    }
    
    static <Type> Sequential getConstructors(
            AppliedClassOrInterface<Type> cls,
            boolean justShared,
            boolean callableConstructors,
            TypeDescriptor $reified$Arguments,
            ceylon.language.Sequential<? extends ceylon.language.meta.model.Type<? extends java.lang.annotation.Annotation>> annotations) {
        ArrayList<Object> ctors = new ArrayList<>();
        com.redhat.ceylon.model.typechecker.model.Type reifiedArguments = $reified$Arguments == null ? null : Metamodel.getProducedType($reified$Arguments);
        TypeDescriptor[] annotationTypeDescriptors = Metamodel.getTypeDescriptors(annotations);
        if (cls.declaration instanceof FreeClassWithInitializer) {
            
            Reference producedReference = cls.declaration.declaration.appliedReference(cls.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
            com.redhat.ceylon.model.typechecker.model.Type argumentsType = Metamodel.getProducedTypeForArguments(
                    cls.declaration.declaration.getUnit(), 
                    (Functional) cls.declaration.declaration, 
                    producedReference);
            if(reifiedArguments.isSubtypeOf(argumentsType)) {
                if(hasAllAnnotations(((FreeClassWithInitializer)cls.declaration).getDefaultConstructor(), annotationTypeDescriptors)) {
                    // TODO test for arguments too
                    //ctors.add(new AppliedInitializer((AppliedClass)cls));
                    ctors.add(((ClassModel<?, ?>)cls).getDefaultConstructor());
                }
            }
        } else {
            for (ceylon.language.meta.declaration.Declaration d : ((FreeClass)cls.declaration).constructors()) {
                Declaration dd = null;
                AnnotatedDeclaration annotated;
                if (d instanceof FreeCallableConstructor
                        && callableConstructors) {
                    dd = ((FreeCallableConstructor)d).declaration;
                    annotated = (CallableConstructorDeclaration)d;
                } else if (d instanceof FreeValueConstructor
                        && !callableConstructors) {
                    dd = ((FreeValueConstructor)d).declaration;
                    annotated = (ValueConstructorDeclaration)d;
                } else {
                    continue;
                }
                // ATM this is an AND WRT annotation types: all must be present
                if(!hasAllAnnotations(annotated, annotationTypeDescriptors))
                    continue;
                if (dd instanceof Functional 
                        && reifiedArguments != null) {
                    // CallableConstructor need a check on the <Arguments>
                    Reference producedReference = dd.appliedReference(cls.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
                    com.redhat.ceylon.model.typechecker.model.Type argumentsType = Metamodel.getProducedTypeForArguments(
                            dd.getUnit(), 
                            (Functional) dd, 
                            producedReference);
                    if(!reifiedArguments.isSubtypeOf(argumentsType))
                        continue;
                }
                // ATM this is an AND WRT annotation types: all must be present
                if(!Metamodel.hasAllAnnotations((AnnotatedDeclaration)d, annotationTypeDescriptors))
                    continue;
                if (dd instanceof Functional 
                        && reifiedArguments != null) {
                    // CallableConstructor need a check on the <Arguments>
                    Reference producedReference = dd.appliedReference(cls.producedType, Collections.<com.redhat.ceylon.model.typechecker.model.Type>emptyList());
                    com.redhat.ceylon.model.typechecker.model.Type argumentsType = Metamodel.getProducedTypeForArguments(
                            dd.getUnit(), 
                            (Functional) dd, 
                            producedReference);
                    if(!reifiedArguments.isSubtypeOf(argumentsType))
                        continue;
                }
                if (!justShared || (d instanceof NestableDeclaration
                        && ((NestableDeclaration)d).getShared())) {
                    Object ctor;
                    if (cls instanceof AppliedClass<?,?>) {
                        ctor = ((AppliedClass<?,?>)cls).getDeclaredConstructor(TypeDescriptor.NothingType, d.getName());
                    } else {//if (cls instanceof AppliedMemberClass<?,?,?>) {
                        ctor = ((AppliedMemberClass<?,?,?>)cls).getDeclaredConstructor(TypeDescriptor.NothingType, d.getName());
                    }
                    ctors.add(ctor);
                }
            }
        }
        
        Object[] array = ctors.toArray(new Object[ctors.size()]);
        ObjectArrayIterable<ceylon.language.meta.declaration.Declaration> iterable = 
                new ObjectArrayIterable<ceylon.language.meta.declaration.Declaration>(
                        TypeDescriptor.union(
                                TypeDescriptor.klass(FunctionModel.class, cls.$reifiedType, TypeDescriptor.NothingType),
                                TypeDescriptor.klass(ValueModel.class, cls.$reifiedType, TypeDescriptor.NothingType)),
                        (Object[]) array);
        return (ceylon.language.Sequential) iterable.sequence();
    }
}
