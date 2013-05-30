package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.SequenceBuilder;
import ceylon.language.Sequential;
import ceylon.language.finished_;
import ceylon.language.metamodel.Annotated;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.ConstrainedAnnotation;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.model.JavaMethod;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.loader.model.LazyMethod;
import com.redhat.ceylon.compiler.loader.model.LazyPackage;
import com.redhat.ceylon.compiler.loader.model.LazyValue;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

public class Metamodel {

    private static RuntimeModuleManager moduleManager;
    
    static{
        resetModuleManager();
    }

    // FIXME: this will need better thinking in terms of memory usage
    private static Map<com.redhat.ceylon.compiler.typechecker.model.Declaration, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeDeclaration> typeCheckModelToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.Declaration, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeDeclaration>();

    private static Map<com.redhat.ceylon.compiler.typechecker.model.Package, com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage> typeCheckPackagesToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.Package, com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage>();

    private static Map<com.redhat.ceylon.compiler.typechecker.model.Module, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule> typeCheckModulesToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.Module, com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule>();

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
        return instanceType.toProducedType(moduleManager);
    }

    public static ceylon.language.metamodel.Type getAppliedMetamodel(TypeDescriptor typeDescriptor) {
        if(typeDescriptor == null)
            throw new RuntimeException("Metamodel not yet supported for Java types");
        ProducedType pt = typeDescriptor.toProducedType(moduleManager);
        return getAppliedMetamodel(pt);
    }
    
    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreeDeclaration getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration){
        synchronized(typeCheckModelToRuntimeModel){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeDeclaration ret = typeCheckModelToRuntimeModel.get(declaration);
            if(ret == null){
                if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClass((com.redhat.ceylon.compiler.typechecker.model.Class)declaration); 
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeInterface((com.redhat.ceylon.compiler.typechecker.model.Interface)declaration);
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Method){
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeFunction((com.redhat.ceylon.compiler.typechecker.model.Method)declaration);
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Value){
                    ret = FreeAttribute.instance((com.redhat.ceylon.compiler.typechecker.model.Value)declaration);
                }else{
                    throw new RuntimeException("Declaration type not supported yet: "+declaration);
                }
                typeCheckModelToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.Package declaration){
        synchronized(typeCheckPackagesToRuntimeModel){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage ret = typeCheckPackagesToRuntimeModel.get(declaration);
            if(ret == null){
                ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreePackage(declaration); 
                typeCheckPackagesToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.Module declaration){
        synchronized(typeCheckModulesToRuntimeModel){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule ret = typeCheckModulesToRuntimeModel.get(declaration);
            if(ret == null){
                ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeModule(declaration); 
                typeCheckModulesToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }

    public static ceylon.language.metamodel.declaration.OpenType getMetamodel(ProducedType pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.FreeParameterisedType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.TypeParameter){
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp = (com.redhat.ceylon.compiler.typechecker.model.TypeParameter) declaration;
            return new FreeTypeParameterType(tp);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType){
            return new FreeUnionType(declaration.getCaseTypes());
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType){
            return new FreeIntersectionType(declaration.getSatisfiedTypes());
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType){
            return ceylon.language.metamodel.declaration.nothingType_.$get();
        }
        throw new RuntimeException("Declaration type not supported yet: "+declaration);
    }

    public static ceylon.language.metamodel.Type getAppliedMetamodel(ProducedType pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
            // FIXME: this null is most likely just wrong
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClassType(pt, null);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedInterfaceType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType){
            return new AppliedUnionType(declaration.getCaseTypes());
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType){
            return new AppliedIntersectionType(declaration.getSatisfiedTypes());
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType){
            return ceylon.language.metamodel.nothingType_.$get();
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

    public static FreeFunction getMetamodel(Method method) {
        // find its container
        Scope container = method.getContainer();
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
            com.redhat.ceylon.compiler.java.runtime.metamodel.FreeClassOrInterface classOrInterface = (FreeClassOrInterface) getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) container);
            // now find the method
            FreeFunction ret = classOrInterface.findMethod(method.getName());
            if(ret == null)
                throw new RuntimeException("Failed to find method "+method.getName()+" in "+container);
            return ret;
        }
        throw new RuntimeException("Unsupported method container for "+method.getName()+": "+container);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.ProducedType getModel(ceylon.language.metamodel.declaration.OpenType pt) {
        if(pt instanceof FreeParameterisedType)
            return ((FreeParameterisedType)pt).producedType;
        throw new RuntimeException("Unsupported produced type: " + pt);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.ProducedType getModel(ceylon.language.metamodel.Type pt) {
        if(pt instanceof AppliedClassOrInterfaceType)
            return ((AppliedClassOrInterfaceType)pt).producedType;
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


    public static java.util.List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> getProducedTypes(Sequential<? extends ceylon.language.metamodel.Type> types) {
        Iterator<?> iterator = types.iterator();
        Object it;
        List<com.redhat.ceylon.compiler.typechecker.model.ProducedType> producedTypes = new LinkedList<com.redhat.ceylon.compiler.typechecker.model.ProducedType>();
        while((it = iterator.next()) != finished_.$get()){
            ceylon.language.metamodel.Type pt = (ceylon.language.metamodel.Type) it;
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
        if (annotationType instanceof AppliedClassOrInterfaceType) {
            freeClass = (FreeClassOrInterface)(annotationType.getDeclaration());
        } else {
            freeClass = (FreeClassOrInterface)annotationType;
        }
        final Class<?> refAnnotationClass = getJavaClass(freeClass.declaration);
        return refAnnotationClass;
    }
    
    private static <A extends ceylon.language.metamodel.Annotation<A>> void addAnnotation(
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
                Constructor<A> constructor = annotationClass.getConstructor(jAnnotationType);
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
            SequenceBuilder<? extends ceylon.language.metamodel.Annotation> ceylonAnnotations,
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
                new Class[]{jAnnotationType, ceylon.language.metamodel.Annotation.class}, 
                handler);
    }
    
    public static <A extends ceylon.language.metamodel.Annotation<A>> Sequential<? extends A> annotations(
            TypeDescriptor $reifiedValues,
            Annotated annotated) {
        // TODO If the annotated is not a valid target for the annotationType
        // we can return empty immediately
        Predicates.Predicate<A> predicate = Predicates.isAnnotationOfType($reifiedValues);
        return annotations($reifiedValues, annotated, predicate);
    }

    public static <A extends ceylon.language.metamodel.Annotation<A>> Sequential<? extends A> annotations(TypeDescriptor $reifiedValues,
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
}
