package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ceylon.language.ArraySequence;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.SequenceBuilder;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;
import ceylon.language.Annotated;
import ceylon.language.meta.model.ClassOrInterface;
import ceylon.language.meta.model.IncompatibleTypeException;
import ceylon.language.ConstrainedAnnotation;
import ceylon.language.meta.model.TypeApplicationException;
import ceylon.language.meta.declaration.AnnotatedDeclaration;
import ceylon.language.meta.declaration.Module;
import ceylon.language.meta.declaration.Package;
import ceylon.language.meta.declaration.NestableDeclaration;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.codegen.Naming;
import com.redhat.ceylon.compiler.java.language.InternalMap;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionMethod;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyClassAlias;
import com.redhat.ceylon.compiler.loader.model.LazyElement;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyPackage;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.analyzer.ExpressionVisitor;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Functional;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

public class Metamodel {

    private static RuntimeModuleManager moduleManager;
    
    // FIXME: this will need better thinking in terms of memory usage
    private static Map<com.redhat.ceylon.compiler.typechecker.model.Declaration, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeNestableDeclaration> typeCheckModelToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.Declaration, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeNestableDeclaration>();

    private static Map<com.redhat.ceylon.compiler.typechecker.model.Package, com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage> typeCheckPackagesToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.Package, com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage>();

    private static Map<com.redhat.ceylon.compiler.typechecker.model.Module, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule> typeCheckModulesToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.Module, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule>();

    static{
        resetModuleManager();
    }

    public static void loadModule(String name, String version, ArtifactResult result, ClassLoader classLoader){
        moduleManager.loadModule(name, version, result, classLoader);
    }
    
    public static void resetModuleManager() {
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(new Logger(){

            @Override
            public void error(String str) {
                System.err.println("ERROR: "+str);
            }

            @Override
            public void warning(String str) {
                System.err.println("WARN: "+str);
            }

            @Override
            public void info(String str) {
                System.err.println("INFO: "+str);
            }

            @Override
            public void debug(String str) {
                System.err.println("DEBUG: "+str);
            }
            
        }, false);
        RepositoryManager repoManager = builder.buildRepository();
        VFS vfs = new VFS();
        Context context = new Context(repoManager, vfs);
        moduleManager = new RuntimeModuleManager(context);
        moduleManager.initCoreModules();
        moduleManager.prepareForTypeChecking();
        typeCheckModelToRuntimeModel.clear();
        typeCheckModulesToRuntimeModel.clear();
        typeCheckPackagesToRuntimeModel.clear();
    }

    public static Object getLock(){
        return moduleManager.getModelLoader();
    }
    
    public static TypeDescriptor getTypeDescriptor(Object instance) {
        if(instance == null)
            return Null.$TypeDescriptor;
        else if(instance instanceof ReifiedType)
            return((ReifiedType) instance).$getType();
        else
            return null; // FIXME: interop?
    }
    public static boolean isReified(java.lang.Object o, TypeDescriptor type){
        TypeDescriptor instanceType = getTypeDescriptor(o);
        if(instanceType == null)
            return false; // FIXME: interop?
        return instanceType.toProducedType(moduleManager).isSubtypeOf(type.toProducedType(moduleManager));
    }

    public static ProducedType getProducedType(Object instance) {
        TypeDescriptor instanceType = getTypeDescriptor(instance);
        if(instanceType == null)
            throw new RuntimeException("Metamodel not yet supported for Java types");
        return getProducedType(instanceType);
    }

    public static ProducedType getProducedType(TypeDescriptor reifiedType) {
        return reifiedType.toProducedType(moduleManager);
    }

    public static ceylon.language.meta.model.Type<?> getAppliedMetamodel(TypeDescriptor typeDescriptor) {
        if(typeDescriptor == null)
            throw new RuntimeException("Metamodel not yet supported for Java types");
        ProducedType pt = typeDescriptor.toProducedType(moduleManager);
        return getAppliedMetamodel(pt);
    }
    
    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreeNestableDeclaration getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration){
        synchronized(getLock()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeNestableDeclaration ret = typeCheckModelToRuntimeModel.get(declaration);
            if(ret == null){
                if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
                    com.redhat.ceylon.compiler.typechecker.model.Class klass = (com.redhat.ceylon.compiler.typechecker.model.Class) declaration;
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClass(klass);
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
                    com.redhat.ceylon.compiler.typechecker.model.Interface interf = (com.redhat.ceylon.compiler.typechecker.model.Interface)declaration;
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeInterface(interf);
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.TypeAlias){
                    com.redhat.ceylon.compiler.typechecker.model.TypeAlias alias = (com.redhat.ceylon.compiler.typechecker.model.TypeAlias)declaration;
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeAliasDeclaration(alias);
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Method){
                    com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration method = (com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration)declaration;
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeFunction(method);
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value){
                    com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration value = (com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration)declaration;
                    if (value.isVariable()) {
                        ret = new FreeVariable(value);
                    } else {
                        ret = new FreeAttribute(value);
                    }
                }else{
                    throw new RuntimeException("Declaration type not supported yet: "+declaration);
                }
                typeCheckModelToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static boolean hasTypeParameters(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration model) {
        if(model instanceof com.redhat.ceylon.compiler.typechecker.model.Generic)
            return hasTypeParameters((com.redhat.ceylon.compiler.typechecker.model.Generic)model);
        if(model.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)
            return hasTypeParameters((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)model.getContainer());
        return false;
    }
    
    public static boolean hasTypeParameters(com.redhat.ceylon.compiler.typechecker.model.Generic model) {
        if(!model.getTypeParameters().isEmpty())
            return true;
        Object container = ((com.redhat.ceylon.compiler.typechecker.model.Declaration)model).getContainer();
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface)
            return hasTypeParameters((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) container);
        return false;
    }

    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.Package declaration){
        synchronized(getLock()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage ret = typeCheckPackagesToRuntimeModel.get(declaration);
            if(ret == null){
                ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage(declaration); 
                typeCheckPackagesToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.Module declaration){
        synchronized(getLock()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule ret = typeCheckModulesToRuntimeModel.get(declaration);
            if(ret == null){
                ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule(declaration); 
                typeCheckModulesToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static ceylon.language.meta.declaration.OpenType getMetamodel(ProducedType pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeInterfaceType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.TypeParameter){
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) declaration;
            return new FreeTypeParameterType(tp);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType){
            return new FreeUnionType((com.redhat.ceylon.compiler.typechecker.model.UnionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType){
            return new FreeIntersectionType((com.redhat.ceylon.compiler.typechecker.model.IntersectionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType){
            return ceylon.language.meta.declaration.nothingType_.get_();
        }
        throw new RuntimeException("Declaration type not supported yet: "+declaration);
    }

    public static Sequential<? extends ceylon.language.meta.declaration.OpenType> getMetamodelSequential(List<ProducedType> types) {
        if(types.isEmpty())
            return (Sequential)empty_.get_();
        ceylon.language.meta.declaration.OpenType[] ret = new ceylon.language.meta.declaration.OpenType[types.size()];
        int i=0;
        for(ProducedType pt : types){
            ret[i++] = Metamodel.getMetamodel(pt);
        }
        return Util.sequentialInstance(ceylon.language.meta.declaration.OpenType.$TypeDescriptor, ret);
    }

    public static <T> ceylon.language.meta.model.Type<T> getAppliedMetamodel(ProducedType pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
            // anonymous classes don't have parameter lists
            TypeDescriptor reifiedArguments;
            if(!declaration.isAnonymous() && !isLocalType((com.redhat.ceylon.compiler.typechecker.model.Class)declaration))
                reifiedArguments = Metamodel.getTypeDescriptorForArguments(declaration.getUnit(), (Functional)declaration, pt);
            else
                reifiedArguments = TypeDescriptor.NothingType;
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);

            if(declaration.isToplevel())
                return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass(reifiedType, reifiedArguments, pt, null, null);
            
            TypeDescriptor reifiedContainer = getTypeDescriptorForProducedType(pt.getQualifyingType());
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass(reifiedContainer, reifiedType, reifiedArguments, pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);
            if(declaration.isToplevel())
                return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedInterface<T>(reifiedType, pt, null, null);

            TypeDescriptor reifiedContainer = getTypeDescriptorForProducedType(pt.getQualifyingType());
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberInterface(reifiedContainer, reifiedType, pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType){
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);
            return new AppliedUnionType<T>(reifiedType, (com.redhat.ceylon.compiler.typechecker.model.UnionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType){
            TypeDescriptor reifiedType = getTypeDescriptorForProducedType(pt);
            return new AppliedIntersectionType<T>(reifiedType, (com.redhat.ceylon.compiler.typechecker.model.IntersectionType)declaration);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType){
            return (ceylon.language.meta.model.Type<T>)ceylon.language.meta.model.nothingType_.get_();
        }
        throw new RuntimeException("Declaration type not supported yet: "+declaration);
    }

    public static java.lang.Class<?> getJavaClass(com.redhat.ceylon.compiler.typechecker.model.Module module) {
        
        String className = module.getNameAsString() + ".module_";
        ReflectionClass classMirror = (ReflectionClass)moduleManager.getModelLoader().lookupClassMirror(module, className);
        return classMirror.klass;
        
    }
    
    public static java.lang.Class<?> getJavaClass(com.redhat.ceylon.compiler.typechecker.model.Package pkg) {
        String className = ((LazyPackage) pkg).getNameAsString()+ ".package_";
        ReflectionClass classMirror = (ReflectionClass)moduleManager.getModelLoader().lookupClassMirror(pkg.getModule(), className);
        return classMirror != null ? classMirror.klass : null;
    }
    
    public static java.lang.Class<?> getJavaClass(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        if(declaration instanceof LazyClass){
            ReflectionClass classMirror = (ReflectionClass) ((LazyClass) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof LazyInterface){
            ReflectionClass classMirror = (ReflectionClass) ((LazyInterface) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof LazyMethod){
            ReflectionClass classMirror = (ReflectionClass) ((LazyMethod) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration instanceof LazyValue){
            ReflectionClass classMirror = (ReflectionClass) ((LazyValue) declaration).classMirror;
            return classMirror.klass;
        }
        if(declaration.getContainer() instanceof com.redhat.ceylon.compiler.typechecker.model.Declaration){
            return getJavaClass((com.redhat.ceylon.compiler.typechecker.model.Declaration)declaration.getContainer());
        }
        if (declaration instanceof LazyClassAlias) {
            ReflectionClass classMirror = (ReflectionClass) ((LazyClassAlias) declaration).classMirror;
            return classMirror.klass;
        }
        throw new RuntimeException("Unsupported declaration type: " + declaration);
    }

    public static java.lang.reflect.Method getJavaMethod(com.redhat.ceylon.compiler.typechecker.model.Method declaration) {
        if(declaration instanceof JavaMethod){
            ReflectionMethod methodMirror = (ReflectionMethod) ((JavaMethod) declaration).mirror;
            return (java.lang.reflect.Method) methodMirror.method;
        }
        if(declaration instanceof LazyMethod){
            ReflectionMethod methodMirror = (ReflectionMethod) ((LazyMethod) declaration).getMethodMirror();
            return (java.lang.reflect.Method) methodMirror.method;
        }
        throw new RuntimeException("Unsupported declaration type: " + declaration);
    }

    public static TypeDescriptor getTypeDescriptorForProducedType(com.redhat.ceylon.compiler.typechecker.model.ProducedType type) {
        TypeDeclaration declaration = type.getDeclaration();
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
        if(declaration instanceof NothingType){
            return TypeDescriptor.NothingType;
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType){
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getCaseTypes());
            return TypeDescriptor.union(tdArgs);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType){
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getSatisfiedTypes());
            return TypeDescriptor.intersection(tdArgs);
        }
        throw new RuntimeException("Unsupported declaration type: " + declaration);
    }

    private static TypeDescriptor[] getTypeDescriptorsForProducedTypes(List<ProducedType> args) {
        TypeDescriptor[] tdArgs = new TypeDescriptor[args.size()];
        for(int i=0;i<tdArgs.length;i++){
            tdArgs[i] = getTypeDescriptorForProducedType(args.get(i));
        }
        return tdArgs;
    }

    public static ceylon.language.meta.declaration.FunctionDeclaration getMetamodel(Method method) {
        // find its container
        Scope container = method.getContainer();
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface classOrInterface = (FreeClassOrInterface) getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) container);
            // now find the method
            ceylon.language.meta.declaration.FunctionDeclaration ret = classOrInterface.findMethod(method.getName());
            if(ret == null)
                throw new RuntimeException("Failed to find method "+method.getName()+" in "+container);
            return ret;
        }
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Package){
            ceylon.language.meta.declaration.Package pkg = getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.Package) container);
            ceylon.language.meta.declaration.FunctionDeclaration ret = pkg.getFunction(method.getName());
            if(ret == null)
                throw new RuntimeException("Failed to find method "+method.getName()+" in "+container);
            return ret;
        }
        throw new RuntimeException("Unsupported method container for "+method.getName()+": "+container);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.ProducedType getModel(ceylon.language.meta.declaration.OpenType pt) {
        if(pt instanceof FreeClassOrInterfaceType)
            return ((FreeClassOrInterfaceType)pt).producedType;
        throw new RuntimeException("Unsupported produced type: " + pt);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.ProducedType getModel(ceylon.language.meta.model.Type<?> pt) {
        if(pt instanceof AppliedClassOrInterface)
            return ((AppliedClassOrInterface<?>)pt).producedType;
        if(pt instanceof AppliedUnionType<?>)
            return ((AppliedUnionType<?>)pt).model;
        if(pt instanceof AppliedIntersectionType<?>)
            return ((AppliedIntersectionType<?>)pt).model;
        if(pt instanceof ceylon.language.meta.model.nothingType_)
            return new NothingType(moduleManager.getModelLoader().getUnit()).getType();
            
        throw new RuntimeException("Unsupported applied produced type: " + pt);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.Package getPackage(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        Scope scope = declaration.getContainer();
        while(scope != null && scope instanceof com.redhat.ceylon.compiler.typechecker.model.Package == false)
            scope = scope.getContainer();
        if(scope == null)
            throw new RuntimeException("Declaration with no package: "+declaration);
        return (com.redhat.ceylon.compiler.typechecker.model.Package)scope;
    }


    public static java.util.List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> getProducedTypes(Sequential<? extends ceylon.language.meta.model.Type<?>> types) {
        Iterator<?> iterator = types.iterator();
        Object it;
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        while((it = iterator.next()) != finished_.get_()){
            ceylon.language.meta.model.Type<?> pt = (ceylon.language.meta.model.Type<?>) it;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType modelPt = Metamodel.getModel(pt);
            producedTypes.add(modelPt);
        }
        return producedTypes;
    }
    
    /**
     * returns the java.lang.Class of the given the Ceylon metamodel of 
     * an annotation class.
     */
    public static 
    <Value extends ConstrainedAnnotation<? extends Value, ? extends Values, ? super ProgramElement>, 
    Values, 
    ProgramElement extends Annotated>
    Class<?> getReflectedAnnotationClass(
            ClassOrInterface<? extends ConstrainedAnnotation<? extends Value, ? extends Values, ? super ProgramElement>> annotationType) {
        FreeClassOrInterface freeClass;
        if (annotationType instanceof AppliedClassOrInterface) {
            freeClass = (FreeClassOrInterface)(annotationType.getDeclaration());
        } else {
            freeClass = (FreeClassOrInterface)annotationType;
        }
        final Class<?> refAnnotationClass = getJavaClass(freeClass.declaration);
        return refAnnotationClass;
    }
    
    private static <A extends ceylon.language.Annotation<? extends A>> void addAnnotation(
            SequenceBuilder<A> ceylonAnnotations,
            java.lang.annotation.Annotation jAnnotation,
            Predicates.Predicate<A> pred) {
        Class<? extends java.lang.annotation.Annotation> jAnnotationType = jAnnotation.annotationType();
        if (pred != null && pred instanceof Predicates.AnnotationPredicate && !((Predicates.AnnotationPredicate<A>)pred).shouldInstantiate(jAnnotationType)) {
            return;
        }
        if (jAnnotationType.getAnnotation(Ceylon.class) == null) {
            // It's a Java annotation
            addProxyCeylonAnnotation(ceylonAnnotations, jAnnotation);
            return;
        }
        if (jAnnotationType.getName().endsWith("$annotations")) {
            java.lang.annotation.Annotation[] jAnnotations;
            try {
                jAnnotations = (java.lang.annotation.Annotation[])jAnnotationType.getMethod("value").invoke(jAnnotation);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("While unwrapping a sequenced annotation", e);
            }
            for (java.lang.annotation.Annotation wrapped : jAnnotations) {
                addAnnotation(ceylonAnnotations, wrapped, pred);
            }
        } else {
            // Find the annotation class
            String annotationName = jAnnotationType.getName();
            if (!annotationName.endsWith("$annotation")) {
                throw new RuntimeException();
            }
            String className = annotationName.substring(0, annotationName.length() - "$annotation".length());
            java.lang.Class<A> annotationClass;
            try {
                annotationClass = (java.lang.Class<A>)Class.forName(className, false, jAnnotationType.getClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find annotation class " + className + " for annotation type " + annotationName, e);
            }
            
            // Invoke it with the jAnnotation as the only argument
            try {
                Constructor<A> constructor = annotationClass.getDeclaredConstructor(jAnnotationType);
                constructor.setAccessible(true);
                A cAnnotation = constructor.newInstance(jAnnotation);
                if (pred.accept(cAnnotation)) {
                    ceylonAnnotations.append(cAnnotation);
                }
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("While reflectively instantiating " + annotationClass, e);
            } 
        }
    }
    
    private static void addProxyCeylonAnnotation(
            SequenceBuilder<? extends ceylon.language.Annotation<?>> ceylonAnnotations,
            java.lang.annotation.Annotation jAnnotation) {
        Class<? extends java.lang.annotation.Annotation> jAnnotationType = jAnnotation.annotationType();
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, java.lang.reflect.Method method,
                    Object[] args) throws Throwable {
                // TODO Auto-generated method stub
                // 
                return null;
            }
        };
        java.lang.reflect.Proxy.newProxyInstance(jAnnotationType.getClassLoader(), 
                new Class[]{jAnnotationType, ceylon.language.Annotation.class}, 
                handler);
    }
    
    public static <A extends ceylon.language.Annotation<? extends A>> Sequential<? extends A> annotations(
            TypeDescriptor $reifiedValues,
            Annotated annotated) {
        // TODO If the annotated is not a valid target for the annotationType
        // we can return empty immediately
        Predicates.Predicate<A> predicate = Predicates.isAnnotationOfType($reifiedValues);
        return annotations($reifiedValues, annotated, predicate);
    }

    public static <A extends ceylon.language.Annotation<? extends A>> Sequential<? extends A> annotations(TypeDescriptor $reifiedValues,
            Annotated annotated, Predicates.Predicate<A> predicate) {
        java.lang.annotation.Annotation[] jAnnotations = ((AnnotationBearing)annotated).$getJavaAnnotations();
        if (jAnnotations == null) {
            throw new RuntimeException("Unable to find java.lang.reflect.AnnotatedElement for " + annotated);
        }
        
        // TODO Fix initial size estimate when query for OptionalAnnotation
        SequenceBuilder<A> ceylonAnnotations = new SequenceBuilder<A>($reifiedValues, jAnnotations.length);
        for (java.lang.annotation.Annotation jAnnotation: jAnnotations) {
            addAnnotation(ceylonAnnotations, jAnnotation, predicate);
        }
        return ceylonAnnotations.getSequence();
    }

    public static String getJavaMethodName(Method method) {
        // FIXME: introduce a damn interface for getRealName()
        if(method instanceof JavaMethod)
            return ((JavaMethod)method).getRealName();
        else if(method instanceof LazyMethod){
            return ((LazyMethod)method).getRealMethodName();
        }else
            throw new RuntimeException("Function declaration type not supported yet: "+method);
    }

    public static int getFirstDefaultedParameter(List<Parameter> parameters) {
        int i = 0;
        for(Parameter param : parameters){
            if(param.isDefaulted()){
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
        Set<com.redhat.ceylon.compiler.typechecker.model.Module> modules = moduleManager.getContext().getModules().getListOfModules();
        ceylon.language.meta.declaration.Module[] array = new ceylon.language.meta.declaration.Module[modules.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.Module module : modules){
            array[i++] = getOrCreateMetamodel(module);
        }
        return Util.sequentialInstance(Module.$TypeDescriptor, array);
    }

    public static ceylon.language.meta.declaration.Module findLoadedModule(String name, String version) {
        // FIXME: this probably needs synchronisation to avoid new modules loaded during traversal
        com.redhat.ceylon.compiler.typechecker.model.Module module = moduleManager.findLoadedModule(name, version);
        return module != null ? getOrCreateMetamodel(module) : null;
    }

    public static Module getDefaultModule() {
        com.redhat.ceylon.compiler.typechecker.model.Module module = moduleManager.getContext().getModules().getDefaultModule();
        return module != null ? getOrCreateMetamodel(module) : null;
    }

    public static List<ProducedType> getParameterProducedTypes(List<Parameter> parameters, ProducedReference producedReference) {
        List<ProducedType> parameterProducedTypes = new ArrayList<ProducedType>(parameters.size());
        for(Parameter parameter : parameters){
            ProducedType ft = producedReference.getTypedParameter(parameter).getFullType();
            parameterProducedTypes.add(ft);
        }
        return parameterProducedTypes;
    }
    
    public static boolean isCeylon(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration){
        if(declaration instanceof LazyClass)
            return ((LazyClass) declaration).isCeylon();
        if(declaration instanceof LazyInterface)
            return ((LazyInterface) declaration).isCeylon();
        throw new RuntimeException("Declaration type not supported: "+declaration);
    }

    public static TypeDescriptor getTypeDescriptorForArguments(com.redhat.ceylon.compiler.typechecker.model.Unit unit, 
            com.redhat.ceylon.compiler.typechecker.model.Functional decl, 
            ProducedReference producedReference) {
        
        if(!decl.getParameterLists().isEmpty()){
            List<Parameter> parameters = decl.getParameterLists().get(0).getParameters();
            com.redhat.ceylon.compiler.typechecker.model.ProducedType tupleType 
            = unit.getParameterTypesAsTupleType(parameters, producedReference);
            return Metamodel.getTypeDescriptorForProducedType(tupleType);
        }else{
            return TypeDescriptor.NothingType;
        }
    }

    public static ProducedType getProducedTypeForArguments(com.redhat.ceylon.compiler.typechecker.model.Unit unit, 
            com.redhat.ceylon.compiler.typechecker.model.Functional decl, 
            ProducedReference producedReference) {
        
        if(!decl.getParameterLists().isEmpty()){
            List<Parameter> parameters = decl.getParameterLists().get(0).getParameters();
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
        // FIXME: is this really enough?
        String typeName = klass.getName();
        com.redhat.ceylon.compiler.typechecker.model.Module module = moduleManager.findModuleForClass(klass);
        com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration decl = 
                (com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration) 
                    moduleManager.getModelLoader().getDeclaration(module, typeName, DeclarationType.TYPE);
        return (ceylon.language.meta.declaration.NestableDeclaration) getOrCreateMetamodel(decl);
    }

    public static TypeDescriptor getTypeDescriptorForFunction(ProducedReference appliedFunction) {
        return getTypeDescriptorForProducedType(getFunctionReturnType(appliedFunction));
    }
    
    public static ProducedType getFunctionReturnType(ProducedReference appliedFunction) {
        // pull the return type out of the Callable
        ProducedType fullType = appliedFunction.getFullType();
        return fullType.getTypeArgumentList().get(0);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.Parameter getParameterFromTypedDeclaration(com.redhat.ceylon.compiler.typechecker.model.TypedDeclaration declaration) {
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.MethodOrValue)
            return ((com.redhat.ceylon.compiler.typechecker.model.MethodOrValue) declaration).getInitializerParameter();
        return null;
    }
    
    /**
     * Called when an annotation class is instantiated via an annotation 
     * constructor or annotation callsite to convert the String representation
     * of a Declaration literal back into the corresponding Declaration.
     */
    public static <T extends ceylon.language.meta.declaration.Declaration> T parseMetamodelReference(String ref/*, java.lang.Class<?> klass*/) {
        DeclarationParser parser = new DeclarationParser();
        return (T)parser.ref(ref);
    }
    
    /**
     * Called when an annotation class is instantiated via an annotation 
     * constructor or annotation callsite to convert an array of String representations
     * of Declaration literals back into a Sequential of Declarations.
     */
    public static <T extends ceylon.language.meta.declaration.Declaration> Sequential<T> parseMetamodelReferences(TypeDescriptor $reifiedElement, String[] refs) {
        DeclarationParser parser = new DeclarationParser();
        ceylon.language.meta.declaration.Declaration[] array = new ceylon.language.meta.declaration.Declaration[refs.length];
        for (int ii = 0; ii < refs.length; ii++) {
            array[ii] = (T)parser.ref(refs[ii]);
        }
        return ArraySequence.instance($reifiedElement, array);
    }
    
    public static <T> T parseEnumerationReference(java.lang.Class<T> klass) {
        FreeClassOrInterface decl = (FreeClassOrInterface)getOrCreateMetamodel(klass);
        String getterName = Naming.getGetterName(decl.declaration);
        try {
            java.lang.reflect.Method method = klass.getMethod(getterName);
            return (T)method.invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> Sequential<T> parseEnumerationReferences(TypeDescriptor $reifiedElement, java.lang.Class[] refs) {
        Object[] array = new Object[refs.length];
        for (int ii = 0; ii < refs.length; ii++) {
            array[ii] = parseEnumerationReference(refs[ii]);
        }
        return ArraySequence.instance($reifiedElement, array);
    }

    public static Sequential<? extends ceylon.language.meta.declaration.TypeParameter> getTypeParameters(com.redhat.ceylon.compiler.typechecker.model.Generic declaration) {
        List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = declaration.getTypeParameters();
        ceylon.language.meta.declaration.TypeParameter[] typeParametersArray = new ceylon.language.meta.declaration.TypeParameter[typeParameters.size()];
        int i=0;
        for(com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp : typeParameters){
            typeParametersArray[i++] = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter(tp);
        }
        return Util.sequentialInstance(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor, typeParametersArray);
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
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Declaration)
            return Metamodel.getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.Declaration)container);
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.Package)
            return Metamodel.getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.Package)container);
        // FIXME: can that happen?
        throw new RuntimeException("Illegal container type: "+container);
    }

    public static boolean isLocalType(com.redhat.ceylon.compiler.typechecker.model.Class decl) {
        return ((LazyElement)decl).isLocal();
    }

    public static ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> 
        getTypeArguments(ceylon.language.meta.declaration.GenericDeclaration declaration, ProducedReference appliedFunction) {
        
        java.util.Map<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.model.Type<?>> typeArguments 
            = new LinkedHashMap<ceylon.language.meta.declaration.TypeParameter, ceylon.language.meta.model.Type<?>>();
        Iterator<? extends ceylon.language.meta.declaration.TypeParameter> typeParameters = declaration.getTypeParameterDeclarations().iterator();
        Object it;
        java.util.Map<com.redhat.ceylon.compiler.typechecker.model.TypeParameter, com.redhat.ceylon.compiler.typechecker.model.ProducedType> ptArguments 
        = appliedFunction.getTypeArguments();
        while((it = typeParameters.next()) != finished_.get_()){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter tp = (com.redhat.ceylon.compiler.java.runtime.metamodel.FreeTypeParameter) it;
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tpDecl = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) tp.declaration;
            com.redhat.ceylon.compiler.typechecker.model.ProducedType ptArg = ptArguments.get(tpDecl);
            ceylon.language.meta.model.Type<?> ptArgWrapped = Metamodel.getAppliedMetamodel(ptArg);
            typeArguments.put(tp, ptArgWrapped);
        }
        return new InternalMap<ceylon.language.meta.declaration.TypeParameter, 
                               ceylon.language.meta.model.Type<?>>(ceylon.language.meta.declaration.TypeParameter.$TypeDescriptor, 
                                                              TypeDescriptor.klass(ceylon.language.meta.model.Type.class, ceylon.language.Anything.$TypeDescriptor), 
                                                              typeArguments);
    }
    
    public static String toTypeString(ceylon.language.meta.declaration.NestableDeclaration declaration, 
            ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends java.lang.Object> typeArguments){
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
    
    private static void addTypeArguments(StringBuffer string, ceylon.language.meta.declaration.GenericDeclaration declaration,
            ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends java.lang.Object> typeArguments) {
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
                Object val = typeArguments != null ? typeArguments.get(tpDecl) : null;
                string.append(val != null ? val : "##Missing##");
            }
            string.append(">");
        }
    }

    public static String toTypeString(ceylon.language.meta.model.Model model){
        StringBuffer string = new StringBuffer();
        ceylon.language.meta.model.Type<?> container = model.getContainer();
        if(container == null){
            string.append(model.getDeclaration().getContainingPackage().getName()).append("::");
        }else if(container instanceof ceylon.language.meta.model.ClassOrInterface<?>){
            string.append(container.toString()).append(".");
        }else{
            string.append("<").append(container.toString()).append(">.");
        }
        string.append(model.getDeclaration().getName());
        if(model instanceof ceylon.language.meta.model.Generic)
            addTypeArguments(string, (ceylon.language.meta.declaration.GenericDeclaration) model.getDeclaration(), ((ceylon.language.meta.model.Generic)model).getTypeArguments());
        return string.toString();
    }

    public static void checkTypeArguments(ProducedType qualifyingType, Declaration declaration, List<ProducedType> typeArguments) {
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Generic){
            List<com.redhat.ceylon.compiler.typechecker.model.TypeParameter> typeParameters = ((com.redhat.ceylon.compiler.typechecker.model.Generic) declaration).getTypeParameters();
            if(typeParameters.size() < typeArguments.size())
                throw new TypeApplicationException("Too many type arguments provided: "+typeArguments.size()+", but only accepts "+typeParameters.size());
            int min = 0;
            for (TypeParameter tp: typeParameters) { 
                if (!tp.isDefaulted()) min++;
            }
            if(typeArguments.size() < min)
                throw new TypeApplicationException("Not enough type arguments provided: "+typeArguments.size()+", but requires at least "+min);
            for(int i=0;i<typeArguments.size();i++){
                ProducedType typeArgument = typeArguments.get(i);
                com.redhat.ceylon.compiler.typechecker.model.TypeParameter typeParameter = typeParameters.get(i);
                for (ProducedType st: typeParameter.getSatisfiedTypes()) {
                    ProducedType sts = st.getProducedType(qualifyingType, declaration, typeArguments);
                    if (!typeArgument.isSubtypeOf(sts)) {
                        throw new TypeApplicationException("Type argument "+i+": "+typeArgument.getProducedTypeQualifiedName()
                                +" does not conform to upper bound constraint: "+sts.getProducedTypeQualifiedName()
                                +" of type parameter "+typeParameter.getQualifiedNameString());
                    }
                }
                if(!ExpressionVisitor.argumentSatisfiesEnumeratedConstraint(qualifyingType, declaration, typeArguments, typeArgument, typeParameter)){
                    throw new TypeApplicationException("Type argument "+i+": "+typeArgument.getProducedTypeQualifiedName()
                            +" does not conform to enumerated constraints "
                            +" of type parameter "+typeParameter.getQualifiedNameString());
                }
            }
        }else{
            if(!typeArguments.isEmpty())
                throw new TypeApplicationException("Declaration does not accept type arguments");
        }
    }

    public static boolean isTypeOf(ProducedType producedType, Object instance) {
        ProducedType instanceType = Metamodel.getProducedType(instance);
        return instanceType.isSubtypeOf(producedType);
    }

    public static boolean isSuperTypeOf(ProducedType a, ceylon.language.meta.model.Type<? extends Object> type) {
        ProducedType b = Metamodel.getModel(type);
        return a.isSupertypeOf(b);
    }

    public static boolean isSubTypeOf(ProducedType a, ceylon.language.meta.model.Type<? extends Object> type) {
        ProducedType b = Metamodel.getModel(type);
        return a.isSubtypeOf(b);
    }

    public static boolean isExactly(ProducedType a, ceylon.language.meta.model.Type<? extends Object> type) {
        ProducedType b = Metamodel.getModel(type);
        return a.isExactly(b);
    }

    public static void checkReifiedTypeArgument(String methodName, String className, Variance variance, ProducedType appliedType, TypeDescriptor $reifiedType) {
        ProducedType expectedReifiedType = Metamodel.getProducedType($reifiedType);
        boolean check = checkReifiedTypeArgument(variance, appliedType, expectedReifiedType);
        if(!check){
            String appliedTypeString = appliedType.getProducedTypeName();
            String expectedReifiedTypeString = expectedReifiedType.getProducedTypeName();
            String appliedString = className.replace("$1", appliedTypeString);
            String expectedString = className.replace("$1", expectedReifiedTypeString);
            throw new IncompatibleTypeException("Incompatible type: actual type of applied declaration is "+appliedString
                    +" is not compatible with expected type: "+expectedString+". Try passing the type argument explicitly with: "
                    +methodName+"<"+appliedTypeString+">()");
        }
    }

    public static void checkReifiedTypeArgument(String methodName, String className, 
            Variance variance1, ProducedType appliedType1, TypeDescriptor $reifiedType1,
            Variance variance2, ProducedType appliedType2, TypeDescriptor $reifiedType2) {
        ProducedType expectedReifiedType1 = Metamodel.getProducedType($reifiedType1);
        ProducedType expectedReifiedType2 = Metamodel.getProducedType($reifiedType2);
        boolean check1 = checkReifiedTypeArgument(variance1, appliedType1, expectedReifiedType1);
        boolean check2 = checkReifiedTypeArgument(variance2, appliedType2, expectedReifiedType2);
        if(!check1 || !check2){
            String appliedTypeString1 = appliedType1.getProducedTypeName();
            String expectedReifiedTypeString1 = expectedReifiedType1.getProducedTypeName();
            String appliedTypeString2 = appliedType2.getProducedTypeName();
            String expectedReifiedTypeString2 = expectedReifiedType2.getProducedTypeName();
            String appliedString = className.replace("$1", appliedTypeString1).replace("$2", appliedTypeString2);
            String expectedString = className.replace("$1", expectedReifiedTypeString1).replace("$2", expectedReifiedTypeString2);
            throw new IncompatibleTypeException("Incompatible type: actual type of applied declaration is "+appliedString
                    +" is not compatible with expected type: "+expectedString+". Try passing the type argument explicitly with: "
                    +methodName+"<"+appliedTypeString1+","+appliedTypeString2+">()");
        }
    }

    public static void checkReifiedTypeArgument(String methodName, String className, 
            Variance variance1, ProducedType appliedType1, TypeDescriptor $reifiedType1,
            Variance variance2, ProducedType appliedType2, TypeDescriptor $reifiedType2,
            Variance variance3, ProducedType appliedType3, TypeDescriptor $reifiedType3) {
        ProducedType expectedReifiedType1 = Metamodel.getProducedType($reifiedType1);
        ProducedType expectedReifiedType2 = Metamodel.getProducedType($reifiedType2);
        ProducedType expectedReifiedType3 = Metamodel.getProducedType($reifiedType3);
        boolean check1 = checkReifiedTypeArgument(variance1, appliedType1, expectedReifiedType1);
        boolean check2 = checkReifiedTypeArgument(variance2, appliedType2, expectedReifiedType2);
        boolean check3 = checkReifiedTypeArgument(variance3, appliedType3, expectedReifiedType3);
        if(!check1 || !check2 || !check3){
            String appliedTypeString1 = appliedType1.getProducedTypeName();
            String expectedReifiedTypeString1 = expectedReifiedType1.getProducedTypeName();
            String appliedTypeString2 = appliedType2.getProducedTypeName();
            String expectedReifiedTypeString2 = expectedReifiedType2.getProducedTypeName();
            String appliedTypeString3 = appliedType3.getProducedTypeName();
            String expectedReifiedTypeString3 = expectedReifiedType3.getProducedTypeName();
            String appliedString = className.replace("$1", appliedTypeString1).replace("$2", appliedTypeString2).replace("$3", appliedTypeString3);
            String expectedString = className.replace("$1", expectedReifiedTypeString1).replace("$2", expectedReifiedTypeString2).replace("$3", expectedReifiedTypeString3);
            throw new IncompatibleTypeException("Incompatible type: actual type of applied declaration is "+appliedString
                    +" is not compatible with expected type: "+expectedString+". Try passing the type argument explicitly with: "
                    +methodName+"<"+appliedTypeString1+","+appliedTypeString2+","+appliedTypeString3+">()");
        }
    }

    private static boolean checkReifiedTypeArgument(Variance variance, ProducedType appliedType, ProducedType expectedReifiedType) {
        switch(variance){
        case IN: return appliedType.isSupertypeOf(expectedReifiedType);
        case OUT: return appliedType.isSubtypeOf(expectedReifiedType);
        case NONE: return appliedType.isExactly(expectedReifiedType);
        default:
            throw new RuntimeException("Invalid variance: "+variance);
        }
    }

    public static void checkQualifyingType(ProducedType qualifyingType, Declaration declaration) {
        Scope container = declaration.getContainer();
        if(container instanceof TypeDeclaration == false)
            throw new IncompatibleTypeException("Declaration container is not a type: "+container);
        TypeDeclaration typeDecl = (TypeDeclaration) container;
        ProducedType supertype = qualifyingType.getSupertype(typeDecl);
        if(supertype == null)
            throw new IncompatibleTypeException("Invalid container type: "+qualifyingType+" is not a subtype of "+typeDecl);
    }
}
