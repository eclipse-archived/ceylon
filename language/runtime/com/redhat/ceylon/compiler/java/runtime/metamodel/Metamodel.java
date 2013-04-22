package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ceylon.language.Null;

import com.redhat.ceylon.cmr.api.ArtifactResult;
import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.loader.impl.reflect.mirror.ReflectionClass;
import com.redhat.ceylon.compiler.loader.model.LazyClass;
import com.redhat.ceylon.compiler.loader.model.LazyInterface;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.NothingType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

public class Metamodel {

    private static RuntimeModuleManager moduleManager;
    
    static{
        resetModuleManager();
    }

    // FIXME: this will need better thinking in terms of memory usage
    private static Map<com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface, com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface> typeCheckModelToRuntimeModel
        = new HashMap<com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface, com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface>();

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
            
        });
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

    public static ceylon.language.metamodel.AppliedProducedType getAppliedMetamodel(TypeDescriptor typeDescriptor) {
        if(typeDescriptor == null)
            throw new RuntimeException("Metamodel not yet supported for Java types");
        ProducedType pt = typeDescriptor.toProducedType(moduleManager);
        return getAppliedMetamodel(pt);
    }
    
    public static com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface getOrCreateMetamodel(com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface declaration){
        synchronized(typeCheckModelToRuntimeModel){
            com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface ret = typeCheckModelToRuntimeModel.get(declaration);
            if(ret == null){
                if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.Class((com.redhat.ceylon.compiler.typechecker.model.Class)declaration); 
                }else if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
                    ret = new com.redhat.ceylon.compiler.java.runtime.metamodel.Interface((com.redhat.ceylon.compiler.typechecker.model.Interface)declaration);
                }
                typeCheckModelToRuntimeModel.put(declaration, ret);
            }
            return ret;
        }
    }
    
    public static ceylon.language.metamodel.ProducedType getMetamodel(ProducedType pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.ClassType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Interface){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.InterfaceType(pt);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.TypeParameter){
            com.redhat.ceylon.compiler.typechecker.model.TypeParameter tp = (TypeParameter) declaration;
            return new TypeParameterType(tp);
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.UnionType){
            return new UnionType(declaration.getCaseTypes());
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.IntersectionType){
            return new IntersectionType(declaration.getSatisfiedTypes());
        }
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.NothingType){
            return ceylon.language.metamodel.nothingType_.getNothingType$();
        }
        throw new RuntimeException("Declaration type not supported yet: "+declaration);
    }

    public static ceylon.language.metamodel.AppliedProducedType getAppliedMetamodel(ProducedType pt) {
        TypeDeclaration declaration = pt.getDeclaration();
        if(declaration instanceof com.redhat.ceylon.compiler.typechecker.model.Class){
            return new com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClassType(pt);
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
            return ceylon.language.metamodel.appliedNothingType_.getAppliedNothingType$();
        }
        throw new RuntimeException("Declaration type not supported yet: "+declaration);
    }

    // FIXME: this is just wrong because types are not applied
    public static TypeDescriptor getTypeDescriptorForDeclaration(com.redhat.ceylon.compiler.typechecker.model.Declaration declaration) {
        if(declaration instanceof LazyClass){
            ReflectionClass classMirror = (ReflectionClass) ((LazyClass) declaration).classMirror;
            return TypeDescriptor.klass(classMirror.klass);
        }
        if(declaration instanceof LazyInterface){
            ReflectionClass classMirror = (ReflectionClass) ((LazyInterface) declaration).classMirror;
            return TypeDescriptor.klass(classMirror.klass);
        }
        throw new RuntimeException("Unsupported declaration type: " + declaration);
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
        throw new RuntimeException("Unsupported declaration type: " + declaration);
    }

    public static TypeDescriptor getTypeDescriptorForProducedType(com.redhat.ceylon.compiler.typechecker.model.ProducedType type) {
        TypeDeclaration declaration = type.getDeclaration();
        if(declaration instanceof LazyClass){
            ReflectionClass classMirror = (ReflectionClass) ((LazyClass) declaration).classMirror;
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getTypeArgumentList());
            return TypeDescriptor.klass(classMirror.klass, tdArgs);
        }
        if(declaration instanceof LazyInterface){
            ReflectionClass classMirror = (ReflectionClass) ((LazyInterface) declaration).classMirror;
            TypeDescriptor[] tdArgs = getTypeDescriptorsForProducedTypes(type.getTypeArgumentList());
            return TypeDescriptor.klass(classMirror.klass, tdArgs);
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

    public static Function getMetamodel(Method method) {
        // find its container
        Scope container = method.getContainer();
        if(container instanceof com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface){
            com.redhat.ceylon.compiler.java.runtime.metamodel.ClassOrInterface classOrInterface = getOrCreateMetamodel((com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface) container);
            // now find the method
            Function ret = classOrInterface.findMethod(method.getName());
            if(ret == null)
                throw new RuntimeException("Failed to find method "+method.getName()+" in "+container);
            return ret;
        }
        throw new RuntimeException("Unsupported method container for "+method.getName()+": "+container);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.ProducedType getModel(ceylon.language.metamodel.ProducedType pt) {
        if(pt instanceof ClassOrInterfaceType)
            return ((ClassOrInterfaceType)pt).producedType;
        throw new RuntimeException("Unsupported produced type: " + pt);
    }

    public static com.redhat.ceylon.compiler.typechecker.model.ProducedType getModel(ceylon.language.metamodel.AppliedProducedType pt) {
        if(pt instanceof AppliedClassOrInterfaceType)
            return ((AppliedClassOrInterfaceType)pt).producedType;
        throw new RuntimeException("Unsupported applied produced type: " + pt);
    }
}
