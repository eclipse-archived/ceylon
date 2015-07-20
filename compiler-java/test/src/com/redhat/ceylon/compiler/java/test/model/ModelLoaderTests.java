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
package com.redhat.ceylon.compiler.java.test.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.common.Backend;
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.java.codegen.CeylonTransformer;
import com.redhat.ceylon.compiler.java.codegen.ClassTransformer;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.codegen.ExpressionTransformer;
import com.redhat.ceylon.compiler.java.codegen.StatementTransformer;
import com.redhat.ceylon.compiler.java.loader.CeylonEnter;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModelLoader;
import com.redhat.ceylon.compiler.java.runtime.model.RuntimeModuleManager;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTests;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.ClosableVirtualFile;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.model.cmr.JDKUtils;
import com.redhat.ceylon.model.loader.AbstractModelLoader;
import com.redhat.ceylon.model.loader.ModelLoader;
import com.redhat.ceylon.model.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.model.loader.impl.reflect.mirror.ReflectionUtils;
import com.redhat.ceylon.model.loader.model.LazyElement;
import com.redhat.ceylon.model.typechecker.model.Annotation;
import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Interface;
import com.redhat.ceylon.model.typechecker.model.Function;
import com.redhat.ceylon.model.typechecker.model.FunctionOrValue;
import com.redhat.ceylon.model.typechecker.model.ModelUtil;
import com.redhat.ceylon.model.typechecker.model.Module;
import com.redhat.ceylon.model.typechecker.model.Modules;
import com.redhat.ceylon.model.typechecker.model.Package;
import com.redhat.ceylon.model.typechecker.model.Parameter;
import com.redhat.ceylon.model.typechecker.model.ParameterList;
import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.Setter;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.TypeParameter;
import com.redhat.ceylon.model.typechecker.model.Value;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskEvent.Kind;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.util.Context;

@RunWith(Parameterized.class)
public class ModelLoaderTests extends CompilerTests {
    
    @Parameters
    public static Iterable<Object[]> testParameters() {
        return Arrays.asList(new Object[]{true}, new Object[]{false});
    }
    
    private final boolean simpleAnnotationModels;
    
    public ModelLoaderTests(boolean simpleAnnotationModels) {
        this.simpleAnnotationModels = simpleAnnotationModels;
    }
    
    protected static String getQualifiedPrefixedName(Declaration decl){
        String name = Decl.className(decl);
        String prefix;
        if(decl instanceof ClassOrInterface)
            prefix = "C";
        else if(Decl.isValue(decl))
            prefix = "V";
        else if(Decl.isGetter(decl))
            prefix = "G";
        else if(decl instanceof Setter)
            prefix = "S";
        else if(decl instanceof Function)
            prefix = "M";
        else
            throw new RuntimeException("Don't know how to prefix decl: "+decl);
        return prefix + name;
    }
    
    
    protected void verifyCompilerClassLoading(String ceylon) {
        verifyCompilerClassLoading(ceylon, new ModelComparison());
    }
    protected void verifyCompilerClassLoading(String ceylon, final ModelComparison modelCompare){
        // now compile the ceylon decl file
        CeyloncTaskImpl task = getCompilerTask(ceylon);
        // get the context to grab the phased units
        Context context = task.getContext();
        if (simpleAnnotationModels) {
            CeylonEnter.instance(context);
            ExpressionTransformer.getInstance(context).simpleAnnotationModels = true;
            CeylonTransformer.getInstance(context).simpleAnnotationModels = true;
            StatementTransformer.getInstance(context).simpleAnnotationModels = true;
            ClassTransformer.getInstance(context).simpleAnnotationModels = true;
        }

        Boolean success = task.call();
        
        Assert.assertTrue(success);

        PhasedUnits phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        
        // find out what was in that file
        Assert.assertEquals(2, phasedUnits.getPhasedUnits().size());
        PhasedUnit one = phasedUnits.getPhasedUnits().get(0);
        PhasedUnit two = phasedUnits.getPhasedUnits().get(1);
        PhasedUnit phasedUnit = one.getUnitFile().getName().endsWith("module.ceylon")
                ? two : one;
        final Map<String,Declaration> decls = new HashMap<String,Declaration>();
        for(Declaration decl : phasedUnit.getUnit().getDeclarations()){
            if(decl.isToplevel()){
                decls.put(getQualifiedPrefixedName(decl), decl);
            }
        }

        // now compile the ceylon usage file
        // remove the extension, make lowercase and add "test"
        String testfile = ceylon.substring(0, ceylon.length()-7).toLowerCase()+"test.ceylon";
        JavacTaskImpl task2 = getCompilerTask(testfile);
        // get the context to grab the declarations
        final Context context2 = task2.getContext();
        
        // check the declarations after Enter but before the compilation is done otherwise we can't load lazy
        // declarations from the jar anymore because we've overridden the jar and the javac jar index is corrupted
        class Listener implements TaskListener{
            @Override
            public void started(TaskEvent e) {
            }

            @Override
            public void finished(TaskEvent e) {
                if(e.getKind() == Kind.ENTER){
                    AbstractModelLoader modelLoader = CeylonModelLoader.instance(context2);
                    Modules modules = LanguageCompiler.getCeylonContextInstance(context2).getModules();
                    // now see if we can find our declarations
                    compareDeclarations(modelCompare, decls, modelLoader, modules);
                }
            }
        }
        Listener listener = new Listener();
        task2.setTaskListener(listener);

        success = task2.call();
        Assert.assertTrue("Compilation failed", success);
        
        // now check with the runtime model loader too
        String module = moduleForJavaModelLoading();
        String version = "1";
        ModuleWithArtifact moduleWithArtifact = new ModuleWithArtifact(module, version);
        synchronized(RUN_LOCK){
            // this initialises the metamodel, even if we don't use the resulting ClassLoader
            URLClassLoader classLoader;
            try {
                classLoader = getClassLoader("runtime model loader tests", moduleWithArtifact);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            try{
                RuntimeModuleManager moduleManager = Metamodel.getModuleManager();
                RuntimeModelLoader modelLoader = moduleManager.getModelLoader();
                Modules modules = moduleManager.getModules();
                // now see if we can find our declarations
                compareDeclarations(modelCompare, decls, modelLoader, modules);
            }finally{
                try {
                    classLoader.close();
                } catch (IOException e) {
                    // ignore
                    e.printStackTrace();
                }
            }
        }
    }

    private void compareDeclarations(final ModelComparison modelCompare, final Map<String, Declaration> decls, AbstractModelLoader modelLoader, Modules modules) {
        Module testModule = getTestModule(modules);
        for(Entry<String, Declaration> entry : decls.entrySet()){
            String quotedQualifiedName = entry.getKey().substring(1);
            Declaration modelDeclaration = modelLoader.getDeclaration(testModule, quotedQualifiedName, 
                    Decl.isValue(entry.getValue()) ? DeclarationType.VALUE : DeclarationType.TYPE);
            Assert.assertNotNull(modelDeclaration);
            // make sure we loaded them exactly the same
            modelCompare.compareDeclarations(entry.getValue().getQualifiedNameString(), entry.getValue(), modelDeclaration);
        }
    }

    private Module getTestModule(Modules modules) {
        for(Module mod : modules.getListOfModules()){
            if(mod.getNameAsString().equals(moduleForJavaModelLoading()))
                return mod;
        }
        Assert.fail("Could not find module "+moduleForJavaModelLoading());
        return null;
    }

    protected void verifyCompilerClassLoading(String ceylon, final RunnableTest test){
        verifyCompilerClassLoading(ceylon, test, defaultOptions);
    }
    
    protected void verifyCompilerClassLoading(String ceylon, final RunnableTest test, List<String> options){
        // now compile the ceylon usage file
        JavacTaskImpl task2 = getCompilerTask(options, ceylon);
        // get the context to grab the declarations
        final Context context2 = task2.getContext();
        
        // check the declarations after Enter but before the compilation is done otherwise we can'tload lazy
        // declarations from the jar anymore because we've overridden the jar and the javac jar index is corrupted
        class Listener implements TaskListener{
            @Override
            public void started(TaskEvent e) {
            }

            @Override
            public void finished(TaskEvent e) {
                if(e.getKind() == Kind.ENTER){
                    CeylonModelLoader modelLoader = (CeylonModelLoader) CeylonModelLoader.instance(context2);
                    test.test(modelLoader);
                }
            }
        }
        Listener listener = new Listener();
        task2.setTaskListener(listener);

        try{
            Boolean success = task2.call();
            Assert.assertTrue("Compilation failed", success);
        }catch(AssertionError x){
            throw x;
        }catch(Throwable x){
            // make sure we unwrap it
            while(x.getCause() != null)
                x = x.getCause();
            if(x instanceof Error)
                throw (Error)x;
            if(x instanceof RuntimeException)
                throw (RuntimeException)x;
            throw new RuntimeException(x);
        }
    }
    
    protected void verifyRuntimeClassLoading(RunnableTest test) {
        RepositoryManager repoManager = CeylonUtils.repoManager()
                .buildManager();
        VFS vfs = new VFS();
        com.redhat.ceylon.compiler.typechecker.context.Context context = new com.redhat.ceylon.compiler.typechecker.context.Context(repoManager, vfs);
        RuntimeModuleManager moduleManager = new RuntimeModuleManager();
        context.setModules(new Modules());
        moduleManager.initCoreModules(new Modules());
        moduleManager.loadModule(AbstractModelLoader.CEYLON_LANGUAGE, Versions.CEYLON_VERSION_NUMBER, repoManager.getArtifactResult("ceylon.language", Versions.CEYLON_VERSION_NUMBER), 
                getClass().getClassLoader());
        RuntimeModelLoader modelLoader = moduleManager.getModelLoader();
        modelLoader.setupWithNoStandardModules();
        modelLoader.loadStandardModules();
        test.test(modelLoader);
    }
    
    static protected class ModelComparison {
        private boolean isUltimatelyVisible(Declaration d) {
            if (d instanceof FunctionOrValue && 
                    ((FunctionOrValue)d).isParameter()) {
                Scope container = d.getContainer();
                if (container instanceof Declaration) {
                    return isUltimatelyVisible((Declaration)container);
                }
            }
            return d.isShared();
        }

        public void compareDeclarations(String name, Declaration validDeclaration, Declaration modelDeclaration) {
            if (validDeclaration.isNativeHeader()) {
                // It's a native header, best to compare against its JVM implementation
                Declaration impl = ModelUtil.getNativeDeclaration(validDeclaration, Backend.Java);
                if (impl != null) {
                    validDeclaration = impl;
                }
            }
            compareDeclarations(name, validDeclaration, modelDeclaration, false);
        }
        
        public void compareDeclarations(String name, Declaration validDeclaration, Declaration modelDeclaration, boolean skipAnnotations) {
            if(alreadyCompared(validDeclaration, modelDeclaration) || validDeclaration instanceof LazyElement)
                return;
            Assert.assertNotNull("Missing model declararion for: "+name, modelDeclaration);
            // check that we have a unit
            Assert.assertNotNull("Missing Unit: "+modelDeclaration.getQualifiedNameString(), modelDeclaration.getUnit());
            Assert.assertNotNull("Invalid Unit", modelDeclaration.getUnit().getPackage());
            // let's not check java stuff for now, due to missing types in the jdk's private methods
            if(name.startsWith("java."))
                return;
            // only compare parameter names for public methods
            if(!(validDeclaration instanceof FunctionOrValue) 
                    || !((FunctionOrValue)validDeclaration).isParameter() 
                    || isUltimatelyVisible(validDeclaration)) {
                Assert.assertEquals(name+" [name]", validDeclaration.getQualifiedNameString(), modelDeclaration.getQualifiedNameString());
            }
            Assert.assertEquals(name+" [shared]", validDeclaration.isShared(), modelDeclaration.isShared());
            Assert.assertEquals(name+" [annotation]", validDeclaration.isAnnotation(), modelDeclaration.isAnnotation());
            // if they're not shared, stop at making sure they are the same type of object
            if(!validDeclaration.isShared()
                    && !isUltimatelyVisible(validDeclaration)
                    && !(validDeclaration instanceof TypeParameter)){
                boolean sameType = validDeclaration.getClass().isAssignableFrom(modelDeclaration.getClass());
                // we may replace Getter or Setter with Value, no harm done
                sameType |= validDeclaration instanceof Value && modelDeclaration instanceof Value;
                sameType |= validDeclaration instanceof Setter && modelDeclaration instanceof Value;
                Assert.assertTrue(name+" [type] " + validDeclaration + " is not the same as " + modelDeclaration, sameType);
                return;
            }
            if(!skipAnnotations)
                compareAnnotations(validDeclaration, modelDeclaration);
            // check containers
            compareContainers(validDeclaration, modelDeclaration);
            compareScopes(validDeclaration, modelDeclaration);
            // full check
            if(validDeclaration instanceof ClassOrInterface){
                Assert.assertTrue(name+" [ClassOrInterface]", modelDeclaration instanceof ClassOrInterface);
                compareClassOrInterfaceDeclarations((ClassOrInterface)validDeclaration, (ClassOrInterface)modelDeclaration);
            } else if(Decl.isConstructor(validDeclaration)){
                Assert.assertTrue(name+" [Constructor]", Decl.isConstructor(modelDeclaration));
            } else if(validDeclaration instanceof Function){
                Assert.assertTrue(name+" [Method]", modelDeclaration instanceof Function);
                compareMethodDeclarations((Function)validDeclaration, (Function)modelDeclaration);
            }else if(validDeclaration instanceof Value || validDeclaration instanceof Setter){
                Assert.assertTrue(name+" [Attribute]", modelDeclaration instanceof Value);
                compareAttributeDeclarations((FunctionOrValue)validDeclaration, (Value)modelDeclaration);
            }else if(validDeclaration instanceof TypeParameter){
                Assert.assertTrue(name+" [TypeParameter]", modelDeclaration instanceof TypeParameter);
                compareTypeParameters((TypeParameter)validDeclaration, (TypeParameter)modelDeclaration);
            }
        }
        
        protected void compareContainers(Declaration validDeclaration, Declaration modelDeclaration) {
            String name = validDeclaration.getQualifiedNameString();
            Scope validContainer = validDeclaration.getContainer();
            Scope modelContainer = modelDeclaration.getContainer();
            if(validContainer instanceof Declaration){
                Assert.assertTrue(name+" [Container is Declaration]", modelContainer instanceof Declaration);
                compareDeclarations(name+" [container]", (Declaration)validContainer, (Declaration)modelContainer);
            }else{
                Assert.assertTrue(name+" [Container is not Declaration]", modelContainer instanceof Declaration == false);
            }
        }
    
        protected void compareScopes(Declaration validDeclaration, Declaration modelDeclaration) {
            String name = validDeclaration.getQualifiedNameString();
            Scope validContainer = validDeclaration.getContainer();
            Scope modelContainer = modelDeclaration.getContainer();
            if(validContainer instanceof Declaration){
                Assert.assertTrue(name+" [Scope is Declaration]", modelContainer instanceof Declaration);
                compareDeclarations(name+" [scope]", (Declaration)validContainer, (Declaration)modelContainer);
            }else{
                Assert.assertTrue(name+" [Scope is not Declaration]", modelContainer instanceof Declaration == false);
            }
        }
        
        protected void compareAnnotations(Declaration validDeclaration, Declaration modelDeclaration) {
            // let's not compare setter annotations
            if(validDeclaration instanceof Setter)
                return;
            String name = validDeclaration.getQualifiedNameString();
            Comparator<Annotation> cmp = new Comparator<Annotation>() {
                public int compare(Annotation a, Annotation b) {
                    return a.getName().compareTo(b.getName());
                }
            };
            Set<Annotation> validAnnotations = new TreeSet<Annotation>(cmp);
            validAnnotations.addAll(validDeclaration.getAnnotations());
            Set<Annotation> modelAnnotations = new TreeSet<Annotation>(cmp);
            modelAnnotations.addAll(modelDeclaration.getAnnotations());
            Assert.assertEquals(name+" [annotation count]", validAnnotations.size(), modelAnnotations.size());
            Iterator<Annotation> validIter = validAnnotations.iterator();
            Iterator<Annotation> modelIter = modelAnnotations.iterator();
            while(validIter.hasNext() || modelIter.hasNext()){
                compareAnnotation(name, validIter.next(), modelIter.next());
            }
        }
    
        protected void compareAnnotation(String element, Annotation validAnnotation, Annotation modelAnnotation) {
            Assert.assertEquals(element+ " [annotation name]", validAnnotation.getName(), modelAnnotation.getName());
            String name = element+"@"+validAnnotation.getName();
            List<String> validPositionalArguments = validAnnotation.getPositionalArguments();
            List<String> modelPositionalArguments = modelAnnotation.getPositionalArguments();
            Assert.assertEquals(name+ " [annotation argument size]", validPositionalArguments.size(), modelPositionalArguments.size());
            for(int i=0;i<validPositionalArguments.size();i++){
                Assert.assertEquals(name+ " [annotation argument "+i+"]", validPositionalArguments.get(i), modelPositionalArguments.get(i));
            }
            Map<String, String> validNamedArguments = validAnnotation.getNamedArguments();
            Map<String, String> modelNamedArguments = modelAnnotation.getNamedArguments();
            Assert.assertEquals(name+ " [annotation named argument size]", validNamedArguments.size(), modelNamedArguments.size());
            for(Entry<String,String> validEntry : validNamedArguments.entrySet()){
                String modelValue = modelNamedArguments.get(validEntry.getKey());
                Assert.assertEquals(name+ " [annotation named argument "+validEntry.getKey()+"]", validEntry.getValue(), modelValue);
            }
        }
    
        private Map<Integer, Set<Integer>> alreadyCompared = new HashMap<Integer, Set<Integer>>();
        
        private boolean alreadyCompared(Declaration validDeclaration, Declaration modelDeclaration) {
            int hashCode = System.identityHashCode(modelDeclaration);
            Set<Integer> comparedDeclarations = alreadyCompared.get(hashCode);
            if(comparedDeclarations == null){
                comparedDeclarations = new HashSet<Integer>();
                alreadyCompared.put(hashCode, comparedDeclarations);
            }
            return !comparedDeclarations.add(System.identityHashCode(validDeclaration));
        }
    
        protected void compareTypeParameters(TypeParameter validDeclaration, TypeParameter modelDeclaration) {
            String name = validDeclaration.getContainer().toString()+"<"+validDeclaration.getName()+">";
            Assert.assertEquals(name+" [Contravariant]", validDeclaration.isContravariant(), modelDeclaration.isContravariant());
            Assert.assertEquals(name+" [Covariant]", validDeclaration.isCovariant(), modelDeclaration.isCovariant());
            Assert.assertEquals(name+" [SelfType]", validDeclaration.isSelfType(), modelDeclaration.isSelfType());
            Assert.assertEquals(name+" [Defaulted]", validDeclaration.isDefaulted(), modelDeclaration.isDefaulted());
            if (validDeclaration.getDeclaration() != null && modelDeclaration.getDeclaration() != null) {
                compareDeclarations(name+" [type param]", validDeclaration.getDeclaration(), modelDeclaration.getDeclaration());
            } else if (!(validDeclaration.getDeclaration() == null && modelDeclaration.getDeclaration() == null)) {
                Assert.fail("[Declaration] one has declaration the other not");
            }
            if (validDeclaration.getSelfTypedDeclaration() != null && modelDeclaration.getSelfTypedDeclaration() != null) {
                compareDeclarations(name+" [type param self typed]", validDeclaration.getSelfTypedDeclaration(), modelDeclaration.getSelfTypedDeclaration());
            } else if (!(validDeclaration.getSelfTypedDeclaration() == null && modelDeclaration.getSelfTypedDeclaration() == null)) {
                Assert.fail("[SelfType] one has self typed declaration the other not");
            }
            if (validDeclaration.getDefaultTypeArgument() != null && modelDeclaration.getDefaultTypeArgument() != null) {
                compareDeclarations(name+" [type param default]", validDeclaration.getDefaultTypeArgument().getDeclaration(), modelDeclaration.getDefaultTypeArgument().getDeclaration());
            } else if (!(validDeclaration.getDefaultTypeArgument() == null && modelDeclaration.getDefaultTypeArgument() == null)) {
                Assert.fail("[DefaultTypeArgument] one has default type argument the other not");
            }
            compareSatisfiedTypes(name, validDeclaration.getSatisfiedTypes(), modelDeclaration.getSatisfiedTypes());
            compareCaseTypes(name, validDeclaration.getCaseTypes(), modelDeclaration.getCaseTypes());
        }
    
        protected void compareClassOrInterfaceDeclarations(ClassOrInterface validDeclaration, ClassOrInterface modelDeclaration) {
            String name = validDeclaration.getQualifiedNameString();
            Assert.assertEquals(name+" [abstract]", validDeclaration.isAbstract(), modelDeclaration.isAbstract());
            Assert.assertEquals(name+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
            Assert.assertEquals(name+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
            Assert.assertEquals(name+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
            Assert.assertEquals(name+" [sealed]", validDeclaration.isSealed(), modelDeclaration.isSealed());
            Assert.assertEquals(name+" [dynamic]", validDeclaration.isDynamic(), modelDeclaration.isDynamic());
            // extended type
            if(validDeclaration.getExtendedType() == null)
                Assert.assertTrue(name+" [null supertype]", modelDeclaration.getExtendedType() == null);
            else
                compareDeclarations(name+" [supertype]", validDeclaration.getExtendedType().getDeclaration(), modelDeclaration.getExtendedType()==null ? null : modelDeclaration.getExtendedType().getDeclaration());
            // satisfied types!
            compareSatisfiedTypes(name, validDeclaration.getSatisfiedTypes(), modelDeclaration.getSatisfiedTypes());
            // case types
            compareCaseTypes(name, validDeclaration.getCaseTypes(), modelDeclaration.getCaseTypes());
            // work on type parameters
            compareTypeParameters(name, validDeclaration.getTypeParameters(), modelDeclaration.getTypeParameters());
            // tests specific to classes
            if(validDeclaration instanceof Class){
                Assert.assertTrue(name+" [is class]", modelDeclaration instanceof Class);
                // self type
                compareSelfTypes((Class)validDeclaration, (Class)modelDeclaration, name);
                // parameters
                compareParameterLists(validDeclaration.getQualifiedNameString(), 
                        ((Class)validDeclaration).getParameterLists(), 
                        ((Class)modelDeclaration).getParameterLists());
                // final
                Assert.assertEquals(name+" [is final]", validDeclaration.isFinal(), modelDeclaration.isFinal());
            }else{
                // tests specific to interfaces
                Assert.assertTrue(name+" [is interface]", modelDeclaration instanceof Interface);
            }
            // make sure it has every member required
            for(Declaration validMember : validDeclaration.getMembers()){
                // skip non-shared members
                if(!validMember.isShared())
                    continue;
                Declaration modelMember = lookupMember(modelDeclaration, validMember);
                Assert.assertNotNull(validMember.getClass().getSimpleName() + " " + validMember.getQualifiedNameString()+" ["+validMember.getDeclarationKind()+"] not found in loaded model", modelMember);
                compareDeclarations(modelMember.getQualifiedNameString(), validMember, modelMember);
            }
            // and not more
            for(Declaration modelMember : modelDeclaration.getMembers()){
                // skip non-shared members
                if(!modelMember.isShared())
                    continue;
                Declaration validMember = lookupMember(validDeclaration, modelMember);
                if (validMember == null && validDeclaration.isNative()) {
                    Declaration hdr = ModelUtil.getNativeHeader(validDeclaration);
                    if (hdr instanceof ClassOrInterface) {
                        validMember = lookupMember((ClassOrInterface)hdr, modelMember);
                    }
                }
                Assert.assertNotNull(modelMember.getQualifiedNameString()+" [extra member] encountered in loaded model", validMember);
            }
        }
    
        protected void compareCaseTypes(String name,
                List<Type> validTypeDeclarations,
                List<Type> modelTypeDeclarations) {
            if(validTypeDeclarations != null){
                Assert.assertNotNull(name+ " [null case types]", modelTypeDeclarations);
            }else{
                Assert.assertNull(name+ " [non-null case types]", modelTypeDeclarations);
                return;
            }
            Assert.assertEquals(name+ " [case types count]", validTypeDeclarations.size(), modelTypeDeclarations.size());
            for(int i=0;i<validTypeDeclarations.size();i++){
                TypeDeclaration validTypeDeclaration = validTypeDeclarations.get(i).getDeclaration();
                TypeDeclaration modelTypeDeclaration = modelTypeDeclarations.get(i).getDeclaration();
                compareDeclarations(name+ " [case types]", validTypeDeclaration, modelTypeDeclaration);
            }
        }
    
        protected void compareSelfTypes(Class validDeclaration,
                Class modelDeclaration, String name) {
            if(validDeclaration.getSelfType() == null)
                Assert.assertTrue(name+" [null self type]", modelDeclaration.getSelfType() == null);
            else{
                Type validSelfType = validDeclaration.getSelfType();
                Type modelSelfType =  modelDeclaration.getSelfType();
                Assert.assertNotNull(name+" [non-null self type]", modelSelfType);
                // self types are always type parameters so they must have a declaration
                compareDeclarations(name+" [non-null self type]", validSelfType.getDeclaration(), modelSelfType.getDeclaration());
            }
        }
        
        private Declaration lookupMember(ClassOrInterface container, Declaration referenceMember) {
            String name = referenceMember.getName();
            for(Declaration member : container.getMembers()){
                if ((referenceMember instanceof Constructor && member instanceof FunctionOrValue)
                        || (referenceMember instanceof FunctionOrValue && member instanceof Constructor)) {
                    continue;
                }
                if (name == null 
                        && member.getName() == null) {
                    return member;
                } else if(member.getName() != null 
                        && member.getName().equals(name)){
                    // we have a special case if we're asking for a Value and we find a Class, it means it's an "object"'s
                    // class with the same name so we ignore it
                    if(Decl.isValue(referenceMember) && (member instanceof Class
                                                         || (member instanceof Value && ((Value)member).isParameter())))
                        continue;
                    // the opposite is also true
                    if((referenceMember instanceof Class
                        || referenceMember instanceof Value && ((Value)referenceMember).isParameter())
                            && Decl.isValue(member))
                        continue;
                    // otherwise we found it
                    return member;
                }
            }
            // not found
            return null;
        }
    
        protected void compareSatisfiedTypes(String name, List<Type> validTypeDeclarations, List<Type> modelTypeDeclarations) {
            Assert.assertEquals(name+ " [Satisfied types count]", validTypeDeclarations.size(), modelTypeDeclarations.size());
            for(int i=0;i<validTypeDeclarations.size();i++){
                TypeDeclaration validTypeDeclaration = validTypeDeclarations.get(i).getDeclaration();
                TypeDeclaration modelTypeDeclaration = modelTypeDeclarations.get(i).getDeclaration();
                compareDeclarations(name+ " [Satisfied types]", validTypeDeclaration, modelTypeDeclaration);
            }
        }
    
        protected void compareParameterLists(String name, List<ParameterList> validParameterLists, List<ParameterList> modelParameterLists) {
            Assert.assertEquals(name+" [param lists count]", validParameterLists.size(), modelParameterLists.size());
            for(int i=0;i<validParameterLists.size();i++){
                List<Parameter> validParameterList = validParameterLists.get(i).getParameters();
                List<Parameter> modelParameterList = modelParameterLists.get(i).getParameters();
                compareParameterList(name, i, validParameterList,
                        modelParameterList);
            }
        }
    
        protected void compareParameterList(String name, int i,
                List<Parameter> validParameterList,
                List<Parameter> modelParameterList) {
            Assert.assertEquals(name+" [param lists "+i+" count]", 
                    validParameterList.size(), modelParameterList.size());
            for(int p=0;p<validParameterList.size();p++){
                Parameter validParameter = validParameterList.get(p);
                Parameter modelParameter = modelParameterList.get(p);
                Assert.assertEquals(name+" [param "+validParameter.getName()+" name]", 
                        validParameter.getName(), modelParameter.getName());
                Assert.assertEquals(name+" [param "+validParameter.getName()+" declaredAnything]", 
                        validParameter.isDeclaredAnything(), modelParameter.isDeclaredAnything());
                Assert.assertEquals(name+" [param "+validParameter.getName()+" sequenced]", 
                        validParameter.isSequenced(), modelParameter.isSequenced());
                Assert.assertEquals(name+" [param "+validParameter.getName()+" defaulted]", 
                        validParameter.isDefaulted(), modelParameter.isDefaulted());
                // make sure they have the same name and type
                compareDeclarations(name+" [param "+i+", "+p+"]", validParameter.getModel(), modelParameter.getModel(), i > 0);
            }
        }
    
        protected void compareMethodDeclarations(Function validDeclaration, Function modelDeclaration) {
            String name = validDeclaration.getQualifiedNameString();
            Assert.assertEquals(name+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
            Assert.assertEquals(name+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
            Assert.assertEquals(name+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
            List<ParameterList> validParameterLists = validDeclaration.getParameterLists();
            List<ParameterList> modelParameterLists = modelDeclaration.getParameterLists();

            // only check for void for non-MPL, since those actually return Callable
            if(validParameterLists.size() == 1){
                Assert.assertEquals(name+" [declaredVoid]", validDeclaration.isDeclaredVoid(), modelDeclaration.isDeclaredVoid());
            }

            // make sure it has every parameter list required
            compareParameterLists(name, validParameterLists, modelParameterLists);
            // now same for return type
            compareDeclarations(name + " [return type]", validDeclaration.getType().getDeclaration(), modelDeclaration.getType().getDeclaration());
            // work on type parameters
            compareTypeParameters(name, validDeclaration.getTypeParameters(), modelDeclaration.getTypeParameters());
        }
    
        protected void compareTypeParameters(String name, List<TypeParameter> validTypeParameters, List<TypeParameter> modelTypeParameters) {
            Assert.assertEquals(name+" [type parameter count]", validTypeParameters.size(), modelTypeParameters.size());
            for(int i=0;i<validTypeParameters.size();i++){
                TypeParameter validTypeParameter = validTypeParameters.get(i);
                TypeParameter modelTypeParameter = modelTypeParameters.get(i);
                compareDeclarations(name+" [type param "+i+"]", validTypeParameter, modelTypeParameter);
            }
        }
    
        protected void compareAttributeDeclarations(FunctionOrValue validDeclaration, Value modelDeclaration) {
            // let's not check Setters since their corresponding Getter is checked already
            if(validDeclaration instanceof Setter)
                return;
            // make sure the flags are the same
            String name = validDeclaration.getQualifiedNameString();
            Assert.assertEquals(name+" [variable]", validDeclaration.isVariable(), modelDeclaration.isVariable());
            Assert.assertEquals(name+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
            Assert.assertEquals(name+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
            Assert.assertEquals(name+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
            Assert.assertEquals(name+" [late]", validDeclaration.isLate(), modelDeclaration.isLate());
            if (compareTransientness(validDeclaration)) {
                Assert.assertEquals(name+" [transient]", validDeclaration.isTransient(), modelDeclaration.isTransient());
            }
            // compare the types
            compareDeclarations(name+" [type]", validDeclaration.getType().getDeclaration(), modelDeclaration.getType().getDeclaration());
        }

        protected boolean compareTransientness(FunctionOrValue validDeclaration) {
            return true;
        }
    }// class ModelComparison
	@Test
	public void loadClass(){
		verifyCompilerClassLoading("Klass.ceylon");
	}

    @Test
    public void loadClassWithMethods(){
        verifyCompilerClassLoading("KlassWithMethods.ceylon");
    }
    
    @Test
    public void loadVariadic(){
        compile("Variadic.ceylon");
        assertErrors("variadictest",
                new CompilerError(7, "missing argument to required parameter 'String+ seq' of 'VariadicPlus'")
        );
    }
    
    @Test
    public void loadMultipleParameterList(){
        verifyCompilerClassLoading("MultipleParameterList.ceylon");
    }
    
    @Test
    public void loadFunctionalParameter(){
        verifyCompilerClassLoading("FunctionalParameter.ceylon");
    }
    
    @Test
    public void functionalParameterParameterNames(){
        compile("FunctionalParameterParameterNames.ceylon");
        try {
            verifyCompilerClassLoading("functionalparameterparameternamestest.ceylon", new RunnableTest() {
                
                @Override
                public void test(ModelLoader loader) {
                    Module mod = loader.getLoadedModule(moduleForJavaModelLoading(), moduleVersionForJavaModelLoading());
                    Assert.assertNotNull(mod);
                    Package p = mod.getDirectPackage(packageForJavaModelLoading());
                    Assert.assertNotNull(p);
                    Declaration fpClass = p.getDirectMember("FunctionalParameterParameterNames", Collections.<Type>emptyList(), false);
                    Assert.assertNotNull(fpClass);
                    { // functionalParameter
                        Function fp = (Function)fpClass.getDirectMember("functionalParameter", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(String))", typeName(fp));
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertTrue(paramF.isDeclaredAnything());
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertTrue(modelF.isDeclaredVoid());
                        Assert.assertEquals("Anything(String)", typeName(modelF));
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Parameter paramS = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String", typeName(modelS));
                    }
                    { // callableValueParameter
                        Function fp = (Function)fpClass.getDirectMember("callableValueParameter", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(String))", typeName(fp));
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertFalse(paramF.isDeclaredAnything());
                        Assert.assertTrue(paramF.getModel() instanceof Value);
                        Value modelF = (Value)paramF.getModel();
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals("Anything(String)", typeName(modelF));
                    }
                    { // functionalParameterNested
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterNested", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(Anything(String)))", typeName(fp));
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.isDeclaredAnything());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertEquals("Anything(Anything(String))", typeName(modelF));
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertTrue(modelF.isDeclaredVoid());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Parameter paramF2 = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertTrue(paramF2.isDeclaredAnything());
                        Assert.assertEquals("f2", paramF2.getName());
                        Assert.assertTrue(paramF2.getModel() instanceof Function);
                        Function modelF2 = (Function)paramF2.getModel();
                        Assert.assertEquals("Anything(String)", typeName(modelF2));
                        Assert.assertEquals("f2", modelF2.getName());
                        Assert.assertTrue(modelF2.isDeclaredVoid());
                        Assert.assertEquals(1, modelF2.getParameterLists().size());
                        Assert.assertEquals(1, modelF2.getParameterLists().get(0).getParameters().size());
                        Parameter paramS = modelF2.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String", typeName(modelS));
                    }
                    { // functionalParameterNested2
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterNested2", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(Anything(String, Anything(Boolean, Integer))))", typeName(fp));
                        
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals("Anything(Anything(String, Anything(Boolean, Integer)))", typeName(modelF));
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramF2 = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f2", paramF2.getName());
                        Assert.assertTrue(paramF2.getModel() instanceof Function);
                        Function modelF2 = (Function)paramF2.getModel();
                        Assert.assertEquals("Anything(String, Anything(Boolean, Integer))", typeName(modelF2));
                        Assert.assertEquals("f2", modelF2.getName());
                        Assert.assertEquals(1, modelF2.getParameterLists().size());
                        Assert.assertEquals(2, modelF2.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramS = modelF2.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("String", typeName(modelS));
                        Assert.assertEquals("s", modelS.getName());
                        
                        Parameter paramF3 = modelF2.getParameterLists().get(0).getParameters().get(1);
                        Assert.assertEquals("f3", paramF3.getName());
                        Assert.assertTrue(paramF3.getModel() instanceof Function);
                        Function modelF3 = (Function)paramF3.getModel();
                        Assert.assertEquals("Anything(Boolean, Integer)", typeName(modelF3));
                        Assert.assertEquals("f3", modelF3.getName());
                        Assert.assertEquals(1, modelF3.getParameterLists().size());
                        Assert.assertEquals(2, modelF3.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramB1 = modelF3.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("b1", paramB1.getName());
                        Assert.assertTrue(paramB1.getModel() instanceof Value);
                        Value modelB1 = (Value)paramB1.getModel();
                        Assert.assertEquals("Boolean", typeName(modelB1));
                        Assert.assertEquals("b1", modelB1.getName());
                        
                        Parameter paramI2 = modelF3.getParameterLists().get(0).getParameters().get(1);
                        Assert.assertEquals("i2", paramI2.getName());
                        Assert.assertTrue(paramI2.getModel() instanceof Value);
                        Value modelI2 = (Value)paramI2.getModel();
                        Assert.assertEquals("i2", modelI2.getName());
                        Assert.assertEquals("Integer", typeName(modelI2));
                    }
                    { // functionalParameterMpl
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterMpl", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(Integer)(String))", typeName(fp));
                        
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertTrue(paramF.isDeclaredAnything());
                        Assert.assertEquals("mpl", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertTrue(modelF.isDeclaredVoid());
                        Assert.assertEquals("Anything(Integer)(String)", typeName(modelF));
                        Assert.assertEquals("mpl", modelF.getName());
                        Assert.assertEquals(2, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(1).getParameters().size());
                        
                        Parameter paramS = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String", typeName(modelS));
                        
                        Parameter paramS2 = modelF.getParameterLists().get(1).getParameters().get(0);
                        Assert.assertEquals("i2", paramS2.getName());
                        Assert.assertTrue(paramS2.getModel() instanceof Value);
                        Value modelS2 = (Value)paramS2.getModel();
                        Assert.assertEquals("i2", modelS2.getName());
                        Assert.assertEquals("Integer", typeName(modelS2));
                    }
                    { // functionalParameterMpl2
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterMpl2", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramMpl = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("mpl", paramMpl.getName());
                        Assert.assertTrue(paramMpl.getModel() instanceof Function);
                        Function modelMpl = (Function)paramMpl.getModel();
                        Assert.assertEquals("mpl", modelMpl.getName());
                        Assert.assertEquals(2, modelMpl.getParameterLists().size());
                        Assert.assertEquals(1, modelMpl.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals(1, modelMpl.getParameterLists().get(1).getParameters().size());
                        
                        Parameter paramS = modelMpl.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String", typeName(modelS));
                        
                        Parameter paramF = modelMpl.getParameterLists().get(1).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(2, modelF.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramB1 = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("b1", paramB1.getName());
                        Assert.assertTrue(paramB1.getModel() instanceof Value);
                        Value modelB1 = (Value)paramB1.getModel();
                        Assert.assertEquals("b1", modelB1.getName());
                        Assert.assertEquals("Boolean", typeName(modelB1));
                        
                        Parameter paramI2 = modelF.getParameterLists().get(0).getParameters().get(1);
                        Assert.assertEquals("i2", paramI2.getName());
                        Assert.assertTrue(paramI2.getModel() instanceof Value);
                        Value modelI2 = (Value)paramI2.getModel();
                        Assert.assertEquals("i2", modelI2.getName());
                        Assert.assertEquals("Integer", typeName(modelI2));
                        
                    }
                    { // functionalParameterMpl3
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterMpl3", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramMpl = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("mpl", paramMpl.getName());
                        Assert.assertTrue(paramMpl.getModel() instanceof Function);
                        Function modelMpl = (Function)paramMpl.getModel();
                        Assert.assertEquals("mpl", modelMpl.getName());
                        Assert.assertEquals(2, modelMpl.getParameterLists().size());
                        Assert.assertEquals(1, modelMpl.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals(1, modelMpl.getParameterLists().get(1).getParameters().size());
                        
                        Parameter paramF = modelMpl.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(2, modelF.getParameterLists().get(0).getParameters().size());
                        
                        Parameter paramB1 = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("b1", paramB1.getName());
                        Assert.assertTrue(paramB1.getModel() instanceof Value);
                        Value modelB1 = (Value)paramB1.getModel();
                        Assert.assertEquals("b1", modelB1.getName());
                        Assert.assertEquals("Boolean", typeName(modelB1));
                        
                        Parameter paramI2 = modelF.getParameterLists().get(0).getParameters().get(1);
                        Assert.assertEquals("i2", paramI2.getName());
                        Assert.assertTrue(paramI2.getModel() instanceof Value);
                        Value modelI2 = (Value)paramI2.getModel();
                        Assert.assertEquals("i2", modelI2.getName());
                        Assert.assertEquals("Integer", typeName(modelI2));
                        
                        Parameter paramS = modelMpl.getParameterLists().get(1).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String", typeName(modelS));
                    }
                    { // functionalParameterReturningCallable
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterReturningCallable", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals("Anything()", modelF.getType().asString());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Parameter paramS = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String", modelS.getType().asString());
                    }
                    { // functionalParameterReturningCallable
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterTakingCallable", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals("Anything", modelF.getType().asString());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Parameter paramF2 = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("f2", paramF2.getName());
                        Assert.assertTrue(paramF2.getModel() instanceof Value);
                        Value modelF2 = (Value)paramF2.getModel();
                        Assert.assertEquals("f2", modelF2.getName());
                        Assert.assertEquals("Anything(String)", modelF2.getType().asString());
                    }
                    { // functionalParameterVariadicStar
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterVariadicStar", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(String*))", typeName(fp));
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertTrue(paramF.isDeclaredAnything());
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertTrue(modelF.isDeclaredVoid());
                        Assert.assertEquals("Anything(String*)", typeName(modelF));
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Parameter paramS = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.isSequenced());
                        Assert.assertFalse(paramS.isAtLeastOne());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("String[]", typeName(modelS));
                    }
                    { // functionalParameterVariadicPlus
                        Function fp = (Function)fpClass.getDirectMember("functionalParameterVariadicPlus", null, false);
                        Assert.assertNotNull(fp);
                        Assert.assertEquals(1, fp.getParameterLists().size());
                        Assert.assertEquals(1, fp.getParameterLists().get(0).getParameters().size());
                        Assert.assertEquals("Anything(Anything(String+))", typeName(fp));
                        Parameter paramF = fp.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertTrue(paramF.isDeclaredAnything());
                        Assert.assertEquals("f", paramF.getName());
                        Assert.assertTrue(paramF.getModel() instanceof Function);
                        Function modelF = (Function)paramF.getModel();
                        Assert.assertTrue(modelF.isDeclaredVoid());
                        Assert.assertEquals("Anything(String+)", typeName(modelF));
                        Assert.assertEquals("f", modelF.getName());
                        Assert.assertEquals(1, modelF.getParameterLists().size());
                        Assert.assertEquals(1, modelF.getParameterLists().get(0).getParameters().size());
                        Parameter paramS = modelF.getParameterLists().get(0).getParameters().get(0);
                        Assert.assertEquals("s", paramS.getName());
                        Assert.assertTrue(paramS.isSequenced());
                        Assert.assertTrue(paramS.isAtLeastOne());
                        Assert.assertTrue(paramS.getModel() instanceof Value);
                        Value modelS = (Value)paramS.getModel();
                        Assert.assertEquals("s", modelS.getName());
                        Assert.assertEquals("[String+]", typeName(modelS));
                    }
                }

                private String typeName(FunctionOrValue fp) {
                    if (fp instanceof Function) {
                        return fp.appliedTypedReference(null, Collections.<Type>emptyList()).getFullType().asString();
                    } else if (fp instanceof Value) {
                        return fp.getType().asString();
                    }
                    return null;
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof org.junit.ComparisonFailure) {
                throw (org.junit.ComparisonFailure)e.getCause();
            } else if (e.getCause() instanceof java.lang.AssertionError) {
                throw (java.lang.AssertionError)e.getCause();
            }
            throw e;
        }
    }
    
    @Test
    public void loadInnerClass(){
        verifyCompilerClassLoading("InnerClass.ceylon");
    }
    
    @Test
    public void loadInnerInterface(){
        verifyCompilerClassLoading("InnerInterface.ceylon");
    }

    @Test
    public void loadClassWithAttributes(){
        verifyCompilerClassLoading("KlassWithAttributes.ceylon");
    }

    @Test
    public void loadClassWithAttributeAndConflictingMethods(){
        verifyCompilerClassLoading("KlassWithAttributeAndConflictingMethods.ceylon");
    }
    
    @Test
    public void loadClassSingletonConstructors(){
        verifyCompilerClassLoading("SingletonConstructors.ceylon");
    }

    @Test
    public void loadTypeParameters(){
        verifyCompilerClassLoading("TypeParameters.ceylon");
    }

    @Test
    public void loadTypeParameterResolving(){
        verifyCompilerClassLoading("TypeParameterResolving.ceylon");
    }

    @Test
    public void loadToplevelMethods(){
        verifyCompilerClassLoading("ToplevelMethods.ceylon");
    }

    @Test
    public void loadToplevelAttributes(){
        verifyCompilerClassLoading("ToplevelAttributes.ceylon");
    }

    @Test
    public void loadToplevelObjects(){
        verifyCompilerClassLoading("ToplevelObjects.ceylon");
    }

    @Test
    public void loadErasedTypes(){
        verifyCompilerClassLoading("ErasedTypes.ceylon");
    }

    @Test
    public void loadDocAnnotations(){
        verifyCompilerClassLoading("DocAnnotations.ceylon");
    }

    @Test
    public void loadLocalDeclarations(){
        verifyCompilerClassLoading("LocalDeclarations.ceylon");
    }

    @Test
    public void loadDefaultValues(){
        verifyCompilerClassLoading("DefaultValues.ceylon");
    }

    @Test
    public void loadJavaKeywords(){
        verifyCompilerClassLoading("JavaKeywords.ceylon");
    }

    @Test
    public void loadAnnotations(){
        verifyCompilerClassLoading("Annotations.ceylon");
    }

    @Test
    public void loadGettersWithUnderscores(){
        verifyCompilerClassLoading("GettersWithUnderscores.ceylon");
    }

    @Test
    public void loadCaseTypes(){
        verifyCompilerClassLoading("CaseTypes.ceylon");
    }

    @Test
    public void loadSelfType(){
        verifyCompilerClassLoading("SelfType.ceylon");
    }
    
    @Test
    public void testTypeParserUsingSourceModel(){
        compile("A.ceylon", "B.ceylon");
        compile("A.ceylon");
    }

    @Test
    public void loadFormalClasses(){
        verifyCompilerClassLoading("FormalClasses.ceylon");
    }
    
    @Test
    public void parallelLoader(){
        // whatever test, doesn't matter
        verifyCompilerClassLoading("Any.ceylon", new RunnableTest(){
            @Override
            public void test(final ModelLoader loader) {
                // now walk it in threads
                List<Callable<Object>> tasks = new ArrayList<Callable<Object>>(10);
                // make a runnable that walks the whole jdk model
                Callable<Object> runnable = new Callable<Object>(){
                    @Override
                    public Object call() throws Exception {
                        System.err.println("Starting work from thread " + Thread.currentThread().getName());
                        // walk every jdk package
                        for(String moduleName : JDKUtils.getJDKModuleNames()) {
                            // load the module
                            Module mod = loader.getLoadedModule(moduleName, JDKUtils.jdk.version);
                            Assert.assertNotNull(mod);
                            for (String pkgName : JDKUtils.getJDKPackagesByModule(moduleName)) {
                                Package p = mod.getDirectPackage(pkgName);
                                Assert.assertNotNull(p);
                                for(Declaration decl : p.getMembers()){
                                    // that causes model loading
                                    decl.getQualifiedNameString();
                                }
                            }
                        }
                        System.err.println("Done work from thread " + Thread.currentThread().getName());
                        return null;
                    }
                };
                // make ten tasks with the same runnable
                for(int i=0;i<10;i++){
                    tasks.add(runnable);
                }
                // create an executor
                ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(10));
                try {
                    // run them all
                    List<Future<Object>> futures = executor.invokeAll(tasks);
                    // and wait for them all to be done
                    for(Future<Object> f : futures){
                        f.get();
                    }
                    executor.shutdown();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
                // [Model loader: 4488(loaded)/4945(total) declarations] is what we expect if it works out
                // now print the stats
                if(loader instanceof AbstractModelLoader){
                    ((AbstractModelLoader)loader).printStats();
                }
            }
        }, Arrays.asList("-verbose:loader"));
    }

    @Ignore("This is the single-threaded version of parallelLoader that loads the JDK entirely to benchmark the model loader")
    @Test
    public void jdkModelLoaderSpeedTest(){
        // whatever test, doesn't matter
        verifyCompilerClassLoading("Any.ceylon", new RunnableTest(){
            @Override
            public void test(ModelLoader loader) {
                // walk every jdk package
                for (String moduleName : JDKUtils.getJDKModuleNames()) {
                    Module mod = loader.getLoadedModule(moduleName, JDKUtils.jdk.version);
                    Assert.assertNotNull(mod);
                    for(String pkgName : JDKUtils.getJDKPackagesByModule(moduleName)){
                        Package p = mod.getDirectPackage(pkgName);
                        Assert.assertNotNull(p);
                        for(Declaration decl : p.getMembers()){
                            // that causes model loading
                            decl.getQualifiedNameString();
                        }
                    }
                }
                // [Model loader: 4488(loaded)/4945(total) declarations] is what we expect if it works out
                // now print the stats
                if(loader instanceof AbstractModelLoader){
                    ((AbstractModelLoader)loader).printStats();
                }
            }
        }, Arrays.asList("-verbose:loader"));
    }

    protected String moduleForJavaModelLoading() {
        return packageForJavaModelLoading();
    }
    
    protected String moduleVersionForJavaModelLoading(){
        return "1";
    }
    
    protected String packageForJavaModelLoading() {
        return "com.redhat.ceylon.compiler.java.test.model";
    }

    @Test
    public void javaModelLoading(){
        compile("JavaType.java");
        verifyCompilerClassLoading("Java.ceylon", new RunnableTest(){
            @Override
            public void test(ModelLoader loader) {
                Module mod = loader.getLoadedModule(moduleForJavaModelLoading(), moduleVersionForJavaModelLoading());
                Assert.assertNotNull(mod);
                Package p = mod.getDirectPackage(packageForJavaModelLoading());
                Assert.assertNotNull(p);
                Declaration javaType = p.getDirectMember("JavaType", null, false);
                Assert.assertNotNull(javaType);
                
                // check the method which returns a java list
                Function javaListMethod = (Function) javaType.getDirectMember("javaList", null, false);
                Assert.assertNotNull(javaListMethod);
                Assert.assertEquals("List<out Object>", javaListMethod.getType().asString());
                Parameter javaListParam = javaListMethod.getParameterLists().get(0).getParameters().get(0);
                Assert.assertNotNull(javaListParam);
                Assert.assertEquals("List<out Object>?", javaListParam.getType().asString());

                // check the method which returns a Ceylon list
                Function ceylonListMethod = (Function) javaType.getDirectMember("ceylonList", null, false);
                Assert.assertNotNull(ceylonListMethod);
                Assert.assertEquals("List<Object>", ceylonListMethod.getType().asString());
                Parameter ceylonListParam = ceylonListMethod.getParameterLists().get(0).getParameters().get(0);
                Assert.assertNotNull(ceylonListParam);
                Assert.assertEquals("List<Object>?", ceylonListParam.getType().asString());

                Function equalsMethod = (Function) javaType.getDirectMember("equals", null, false);
                Assert.assertNotNull(equalsMethod);
                Assert.assertTrue(equalsMethod.isActual());
                Assert.assertTrue(equalsMethod.getRefinedDeclaration() != equalsMethod);

                Value hashAttribute = (Value) javaType.getDirectMember("hash", null, false);
                Assert.assertNotNull(hashAttribute);
                Assert.assertTrue(hashAttribute.isActual());
                Assert.assertTrue(hashAttribute.getRefinedDeclaration() != hashAttribute);

                Value stringAttribute = (Value) javaType.getDirectMember("string", null, false);
                Assert.assertNotNull(stringAttribute);
                Assert.assertTrue(stringAttribute.isActual());
                Assert.assertTrue(stringAttribute.getRefinedDeclaration() != stringAttribute);

                Function cloneMethod = (Function) javaType.getDirectMember("clone", null, false);
                Assert.assertNotNull(cloneMethod);
                Assert.assertFalse(cloneMethod.isActual());
                Assert.assertTrue(cloneMethod.getRefinedDeclaration() == cloneMethod);

                Function finalizeMethod = (Function) javaType.getDirectMember("finalize", null, false);
                Assert.assertNotNull(finalizeMethod);
                Assert.assertFalse(finalizeMethod.isActual());
                Assert.assertTrue(finalizeMethod.getRefinedDeclaration() == finalizeMethod);
}
        });
    }
    
    @Test
    public void javaDeprecated(){
        compile("JavaDeprecated.java");
        verifyCompilerClassLoading("JavaDeprecated.ceylon", new RunnableTest(){
            @Override
            public void test(ModelLoader loader) {
                Module mod = loader.getLoadedModule(moduleForJavaModelLoading(), moduleVersionForJavaModelLoading());
                Assert.assertNotNull(mod);
                Package p = mod.getDirectPackage(packageForJavaModelLoading());
                Assert.assertNotNull(p);
                Declaration javaDep = p.getDirectMember("JavaDeprecated", null, false);
                Assert.assertNotNull(javaDep);
                Assert.assertEquals(javaDep.toString(), 1, numDeprecated(javaDep));
                
                Function javaMethod = (Function) javaDep.getDirectMember("m", null, false);
                Assert.assertNotNull(javaMethod);
                Assert.assertEquals(javaMethod.toString(), 1, numDeprecated(javaMethod));
                
                Parameter javaParameter = javaMethod.getParameterLists().get(0).getParameters().get(0);
                Assert.assertNotNull(javaParameter);
                Assert.assertEquals(javaParameter.toString(), 1, numDeprecated(javaParameter.getModel()));
                
                Value javaField = (Value) javaDep.getDirectMember("s", null, false);
                Assert.assertNotNull(javaField);
                Assert.assertEquals(javaField.toString(), 1, numDeprecated(javaField));
                
                // Check there is only 1 model Annotation, when annotation with ceylon's deprecated... 
                javaMethod = (Function) javaDep.getDirectMember("ceylonDeprecation", null, false);
                Assert.assertNotNull(javaMethod);
                Assert.assertEquals(javaMethod.toString(), 1, numDeprecated(javaMethod));
                
                // ... or both the ceylon and java deprecated
                javaMethod = (Function) javaDep.getDirectMember("bothDeprecation", null, false);
                Assert.assertNotNull(javaMethod);
                Assert.assertEquals(javaMethod.toString(), 1, numDeprecated(javaMethod));
            }

            private int numDeprecated(Declaration javaDep) {
                int num = 0;
                for (Annotation a : javaDep.getAnnotations()) {
                    if ("deprecated".equals(a.getName())) {
                        num++;
                    }
                }
                return num;
            }
        });
    }
    
    @Test
    public void bogusModelAnnotationsTopLevelAttribute(){
        compile("bogusTopLevelAttributeNoGetter_.java", "bogusTopLevelAttributeMissingType_.java", "bogusTopLevelAttributeInvalidType_.java");
        assertErrors("bogusTopLevelAttributeUser",
                new CompilerError(2, "Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"
                        +"   Error while resolving toplevel attribute com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeNoGetter: getter method missing"),
                new CompilerError(-1, "Error while resolving type of toplevel attribute for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeMissingType: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while parsing type of toplevel attribute for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeInvalidType: Expecting word but got AND"),
                new CompilerError(3, "could not determine type of function or value reference: 'bogusTopLevelAttributeNoGetter': Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"+
                        "   Error while resolving toplevel attribute com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeNoGetter: getter method missing"),
                new CompilerError(4, "could not determine type of function or value reference: 'bogusTopLevelAttributeMissingType': Error while resolving type of toplevel attribute for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeMissingType: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(5, "could not determine type of function or value reference: 'bogusTopLevelAttributeInvalidType': Error while parsing type of toplevel attribute for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeInvalidType: Expecting word but got AND")
                );
    }

    @Test
    public void bogusModelAnnotationsTopLevelMethod(){
        compile("bogusTopLevelMethodNoMethod_.java", "bogusTopLevelMethodMissingType_.java", "bogusTopLevelMethodInvalidType_.java", "bogusTopLevelMethodNotStatic_.java");
        assertErrors("bogusTopLevelMethodUser",
                new CompilerError(2, "Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"
                        +"   Error while resolving toplevel method com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodNoMethod: static method missing"),
                new CompilerError(2, "Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"
                        +"   Error while resolving toplevel method com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodNotStatic: method is not static"),
                new CompilerError(2, "Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"
                        +"   Error while resolving type of toplevel method for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodMissingType:\n"
                        +"   Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(2, "Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"
                        +"   Error while parsing type of toplevel method for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodInvalidType:\n"
                        +"   Expecting word but got AND"),
                new CompilerError(4, "could not determine type of function or value reference: 'bogusTopLevelMethodNotStatic': Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"+
                        "   Error while resolving toplevel method com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodNotStatic: method is not static"),
                new CompilerError(5, "could not determine type of function or value reference: 'bogusTopLevelMethodMissingType': Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n"+
                        "   Error while resolving type of toplevel method for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodMissingType:\n"+
                        "   Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(6, "could not determine type of function or value reference: 'bogusTopLevelMethodInvalidType': Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n" + 
                        "   Error while parsing type of toplevel method for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodInvalidType:\n" + 
                        "   Expecting word but got AND")
        );

    }
    
    @Test
    public void bogusModelAnnotationsTopLevelClass(){
        compile("BogusTopLevelClass.java", "BogusTopLevelClass2.java");
        assertErrors("bogusTopLevelClassUser",
                new CompilerError(-1, "Constructor for 'com.redhat.ceylon.compiler.java.test.model.BogusTopLevelClass' should take 1 reified type arguments (TypeDescriptor) but has '0': skipping constructor."),
                new CompilerError(-1, "Invalid type signature for self type of com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: com.redhat.ceylon.compiler.java.test.model::MissingType is not a type parameter"),
                new CompilerError(2, "Error while loading the com.redhat.ceylon.compiler.java.test.model/1 module:\n   Error while resolving type of extended type for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass:\n   Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                // FIXME: I wish I knew how to get rid of that one...
                new CompilerError(3, "constructor BogusTopLevelClass in class com.redhat.ceylon.compiler.java.test.model.BogusTopLevelClass<T> cannot be applied to given types;\n  required: no arguments\n  found: com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor\n  reason: actual and formal argument lists differ in length")
        );
    }

    interface I<T,X,B extends Number> {
        void foo();
        void params(int i);
        void parameterised(T t);
        void parameterised(T t, String s);
        void parameterised2(X t);
        <M> void mparameterised();
        void parameterisedB(B b);
    }
    interface I2<B extends Number> {
        void parameterisedB(B b);
    }
    class RawC implements I2 {
        @Override
        public void parameterisedB(Number b) {}
    }
    class C<T,B extends Number> implements I<Integer,T,B> {
        @Override
        public void foo() {}
        // this one does not override anything 
        public void bar() {}
        @Override
        public void params(int i){}
        // overrides a generic method
        @Override
        public void parameterised(Integer t) {}
        // overrides a generic method
        @Override
        public void parameterised(Integer t, String s) {}
        // overrides a generic method
        @Override
        public void parameterised2(T t) {}
        // this one does not override anything 
        public void parameterised(String t) {}
        @Override
        public void mparameterised() {}
        // overrides a generic method with bounded type
        @Override
        public void parameterisedB(B b) {}
        
    }
    class C2 extends C<String, Integer> {
        public void params(){}
        public void parameterised(Integer t, int i) {}
    }
    class Container<O> {
        class Inner<I> {
            public void method(O o, I i){}
        }
    }
    class ContainerSubClass extends Container<Integer> {
        class Inner extends Container<Integer>.Inner<String> {
            @Override
            public void method(Integer o, String i) {}
        }
    }
    class Visibility {
        protected void prot(){}
        void pkg(){}
        private void priv(){}
    }
    class VisibilitySubClass extends Visibility {
        protected void prot(){}
        void pkg(){}
        private void priv(){}
    }
    interface Channel {}
    interface WebSocketChannel extends  Channel {}
    
    public abstract class AbstractReceiveListener implements ChannelListener<WebSocketChannel> {
        @Override
        public void handleEvent(WebSocketChannel channel) {}
    }
    public interface ChannelListener<T extends Channel> {
        void handleEvent(T channel);
    }
    class HiddenObjectMethods {
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }

    @Test
    public void testReflectionOverriding() throws NoSuchMethodException, SecurityException{
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("foo")));
        Assert.assertFalse(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("bar")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("params", int.class)));
        Assert.assertFalse(ReflectionUtils.isOverridingMethod(C2.class.getDeclaredMethod("params")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("parameterised", Integer.class)));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("parameterised", Integer.class, String.class)));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("parameterised2", Object.class)));
        Assert.assertFalse(ReflectionUtils.isOverridingMethod(C2.class.getDeclaredMethod("parameterised", Integer.class, int.class)));
        Assert.assertFalse(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("parameterised", String.class)));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("mparameterised")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(C.class.getDeclaredMethod("parameterisedB", Number.class)));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(RawC.class.getDeclaredMethod("parameterisedB", Number.class)));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(ContainerSubClass.Inner.class.getDeclaredMethod("method", Integer.class, String.class)));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(VisibilitySubClass.class.getDeclaredMethod("prot")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(VisibilitySubClass.class.getDeclaredMethod("pkg")));
        Assert.assertFalse(ReflectionUtils.isOverridingMethod(VisibilitySubClass.class.getDeclaredMethod("priv")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(AbstractReceiveListener.class.getDeclaredMethod("handleEvent", WebSocketChannel.class)));

        Assert.assertFalse(ReflectionUtils.isOverridingMethod(HiddenObjectMethods.class.getDeclaredMethod("finalize")));
        Assert.assertFalse(ReflectionUtils.isOverridingMethod(HiddenObjectMethods.class.getDeclaredMethod("clone")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(HiddenObjectMethods.class.getDeclaredMethod("toString")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(HiddenObjectMethods.class.getDeclaredMethod("hashCode")));
        Assert.assertTrue(ReflectionUtils.isOverridingMethod(HiddenObjectMethods.class.getDeclaredMethod("equals", Object.class)));
    }
    
    @Test
    public void loadPublicJavaClassWithPackageConstructor(){
        compile("PublicJavaClassWithPackageConstructor.java");
        compile("publicjavaclasswithpackageconstructortest.ceylon");
    }

    @Test
    public void loadJavaContainer(){
        compile("JavaContainer1.java", "JavaContainer2.java");
        compile("JavaContainerTest.ceylon");
    }
    
    private static final ClosableVirtualFile getLatestZippedLanguageSourceFile() {
        VFS vfs = new VFS();
        File langDir = new File("../ceylon-dist/dist/repo/ceylon/language");
        if (!langDir.exists()) {
            System.err.println("Unable to test language module, not found in repository: " + langDir);
            System.exit(-1);
        }
        String[] versions = langDir.list();
        Arrays.sort(versions);
        String version = versions[versions.length-1]; //last
        return vfs.getFromZipFile( new File(langDir, version + "/ceylon.language-" + version + ".src") );
    }
    
    /**
     * When testing the annotations on the native declarations in the language module
     * we need to wory around a few bugs
     */
    class OtherModelCompare extends ModelComparison {
        @Override
        protected void compareAnnotations(Declaration validDeclaration, Declaration modelDeclaration) {
            // do nothing, until ceylon/ceylon-compiler#1231 is fixed
        }
    }
    
    @Test
    public void compareNativeRuntimeWithJavaRuntime() {
        // parse the ceylon sources from the language module and 
        // build a map of all the native declarations
        final Map<String, Declaration> nativeFromSource = new HashMap<String, Declaration>();
        
        ClosableVirtualFile latestZippedLanguageSourceFile = getLatestZippedLanguageSourceFile();
        try {
            TypeCheckerBuilder typeCheckerBuilder = new TypeCheckerBuilder()
                    .verbose(false)
                    .addSrcDirectory(latestZippedLanguageSourceFile);
            TypeChecker typeChecker = typeCheckerBuilder.getTypeChecker();
            typeChecker.process();
            for (PhasedUnit pu : typeChecker.getPhasedUnits().getPhasedUnits()) {
                for (Declaration d : pu.getDeclarations()) {
                    if (d.isNativeHeader() && d.isToplevel()) {
                        String qualifiedNameString = d.getQualifiedNameString();
                        String key = d.getDeclarationKind()+":"+qualifiedNameString;
                        Declaration prev = nativeFromSource.put(key, d);
                        if (prev != null) {
                            Assert.fail("Two declarations with the same key " + key + ": " + d + " and: " + prev);
                        }
                    }
                }
            }
        } finally {
            latestZippedLanguageSourceFile.close();
        }
        System.out.println(nativeFromSource);
        
        // now compile something (it doesn't matter what, we just need 
        // to get our hands on from-binary models for the language module) 
        RunnableTest tester = new RunnableTest() {
            
            @Override
            public void test(ModelLoader loader) {
                OtherModelCompare comparer = new OtherModelCompare();
                Module binaryLangMod = loader.getLoadedModule(AbstractModelLoader.CEYLON_LANGUAGE, Versions.CEYLON_VERSION_NUMBER);
                for (Map.Entry<String, Declaration> entry : nativeFromSource.entrySet()) {
                    System.out.println(entry.getKey());
                    Declaration source = entry.getValue();
                    ModelLoader.DeclarationType dt = null;
                    switch (source.getDeclarationKind()) {
                    case TYPE:
                    case TYPE_PARAMETER:
                        dt = DeclarationType.TYPE;
                        break;
                    case MEMBER:
                    case SETTER:
                        dt = DeclarationType.VALUE;
                        break;
                    }
                    // Ensure the package is loaded
                    binaryLangMod.getDirectPackage(source.getQualifiedNameString().replaceAll("::.*", ""));
                    Declaration binary = loader.getDeclaration(binaryLangMod, 
                            source.getQualifiedNameString().replace("::", "."), 
                            dt);
                    comparer.compareDeclarations(source.getQualifiedNameString(), source, binary);
                }
            }
        };
        verifyCompilerClassLoading("Any.ceylon", tester, defaultOptions);
        verifyRuntimeClassLoading(tester);
    }
}
