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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.JDKUtils;
import com.redhat.ceylon.compiler.java.codegen.Decl;
import com.redhat.ceylon.compiler.java.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.java.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.loader.AbstractModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.loader.model.LazyElement;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Annotation;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Interface;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.Modules;
import com.redhat.ceylon.compiler.typechecker.model.Package;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.typechecker.model.ValueParameter;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskEvent.Kind;
import com.sun.source.util.TaskListener;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.util.Context;

public class ModelLoaderTest extends CompilerTest {
    
    private Map<Integer, Set<Integer>> alreadyCompared = new HashMap<Integer, Set<Integer>>();
    
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
        else if(decl instanceof Method)
            prefix = "M";
        else
            throw new RuntimeException("Don't know how to prefix decl: "+decl);
        return prefix + name;
    }
    
    protected void verifyClassLoading(String ceylon){
        // now compile the ceylon decl file
        CeyloncTaskImpl task = getCompilerTask(ceylon);
        // get the context to grab the phased units
        Context context = task.getContext();

        Boolean success = task.call();
        
        Assert.assertTrue(success);

        PhasedUnits phasedUnits = LanguageCompiler.getPhasedUnitsInstance(context);
        
        // find out what was in that file
        Assert.assertEquals(1, phasedUnits.getPhasedUnits().size());
        PhasedUnit phasedUnit = phasedUnits.getPhasedUnits().get(0);
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
        
        // check the declarations after Enter but before the compilation is done otherwise we can'tload lazy
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
                    Module defaultModule = modules.getDefaultModule();
                    // now see if we can find our declarations
                    for(Entry<String, Declaration> entry : decls.entrySet()){
                        String quotedQualifiedName = entry.getKey().substring(1);
                        Declaration modelDeclaration = modelLoader.getDeclaration(defaultModule, quotedQualifiedName, 
                                Decl.isValue(entry.getValue()) ? DeclarationType.VALUE : DeclarationType.TYPE);
                        Assert.assertNotNull(modelDeclaration);
                        // make sure we loaded them exactly the same
                        compareDeclarations(entry.getValue(), modelDeclaration);
                    }
                }
            }
        }
        Listener listener = new Listener();
        task2.setTaskListener(listener);

        success = task2.call();
        Assert.assertTrue("Compilation failed", success);
    }

    protected void verifyClassLoading(String ceylon, final RunnableTest test){
        verifyClassLoading(ceylon, test, defaultOptions);
    }
    
    protected void verifyClassLoading(String ceylon, final RunnableTest test, List<String> options){
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

        Boolean success = task2.call();
        Assert.assertTrue("Compilation failed", success);
    }

    protected void compareDeclarations(Declaration validDeclaration, Declaration modelDeclaration) {
        if(alreadyCompared(validDeclaration, modelDeclaration) || validDeclaration instanceof LazyElement)
            return;
        String name = validDeclaration.getQualifiedNameString();
        Assert.assertNotNull("Missing model declararion for: "+name, modelDeclaration);
        // check that we have a unit
        Assert.assertNotNull("Missing Unit: "+modelDeclaration.getQualifiedNameString(), modelDeclaration.getUnit());
        Assert.assertNotNull("Invalid Unit", modelDeclaration.getUnit().getPackage());
        // let's not check java stuff for now, due to missing types in the jdk's private methods
        if(name.startsWith("java."))
            return;
        // only compare parameter names for public methods
        if(!(validDeclaration instanceof Parameter) || validDeclaration.isShared())
            Assert.assertEquals(name+" [name]", validDeclaration.getQualifiedNameString(), modelDeclaration.getQualifiedNameString());
        Assert.assertEquals(name+" [shared]", validDeclaration.isShared(), modelDeclaration.isShared());
        // if they're not shared, stop at making sure they are the same type of object
        if(!validDeclaration.isShared() && !(validDeclaration instanceof TypeParameter)){
            boolean sameType = validDeclaration.getClass().isAssignableFrom(modelDeclaration.getClass());
            // we may replace Getter or Setter with Value, no harm done
            sameType |= validDeclaration instanceof Value && modelDeclaration instanceof Value;
            sameType |= validDeclaration instanceof Setter && modelDeclaration instanceof Value;
            Assert.assertTrue(name+" [type]", sameType);
            return;
        }
        compareAnnotations(validDeclaration, modelDeclaration);
        // check containers
        compareContainers(validDeclaration, modelDeclaration);
        // full check
        if(validDeclaration instanceof ClassOrInterface){
            Assert.assertTrue(name+" [ClassOrInterface]", modelDeclaration instanceof ClassOrInterface);
            compareClassOrInterfaceDeclarations((ClassOrInterface)validDeclaration, (ClassOrInterface)modelDeclaration);
        }else if(validDeclaration instanceof Method){
            Assert.assertTrue(name+" [Method]", modelDeclaration instanceof Method);
            compareMethodDeclarations((Method)validDeclaration, (Method)modelDeclaration);
        }else if(validDeclaration instanceof Value || validDeclaration instanceof Setter){
            Assert.assertTrue(name+" [Attribute]", modelDeclaration instanceof Value);
            compareAttributeDeclarations((MethodOrValue)validDeclaration, (Value)modelDeclaration);
        }else if(validDeclaration instanceof TypeParameter){
            Assert.assertTrue(name+" [TypeParameter]", modelDeclaration instanceof TypeParameter);
            compareTypeParameters((TypeParameter)validDeclaration, (TypeParameter)modelDeclaration);
        }
    }
    
    private void compareContainers(Declaration validDeclaration, Declaration modelDeclaration) {
        String name = validDeclaration.getQualifiedNameString();
        Scope validContainer = validDeclaration.getContainer();
        Scope modelContainer = modelDeclaration.getContainer();
        if(validContainer instanceof Declaration){
            Assert.assertTrue(name+" [Container is Declaration]", modelContainer instanceof Declaration);
            compareDeclarations((Declaration)validContainer, (Declaration)modelContainer);
        }else{
            Assert.assertTrue(name+" [Container is not Declaration]", modelContainer instanceof Declaration == false);
        }
    }

    private void compareAnnotations(Declaration validDeclaration, Declaration modelDeclaration) {
        // let's not compare setter annotations
        if(validDeclaration instanceof Setter)
            return;
        String name = validDeclaration.getQualifiedNameString();
        List<Annotation> validAnnotations = validDeclaration.getAnnotations();
        List<Annotation> modelAnnotations = modelDeclaration.getAnnotations();
        Assert.assertEquals(name+" [annotation count]", validAnnotations.size(), modelAnnotations.size());
        for(int i=0;i<validAnnotations.size();i++){
            compareAnnotation(name, validAnnotations.get(i), modelAnnotations.get(i));
        }
    }

    private void compareAnnotation(String element, Annotation validAnnotation, Annotation modelAnnotation) {
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

    private boolean alreadyCompared(Declaration validDeclaration, Declaration modelDeclaration) {
        int hashCode = System.identityHashCode(modelDeclaration);
        Set<Integer> comparedDeclarations = alreadyCompared.get(hashCode);
        if(comparedDeclarations == null){
            comparedDeclarations = new HashSet<Integer>();
            alreadyCompared.put(hashCode, comparedDeclarations);
        }
        return !comparedDeclarations.add(System.identityHashCode(validDeclaration));
    }

    private void compareTypeParameters(TypeParameter validDeclaration, TypeParameter modelDeclaration) {
        String name = validDeclaration.getContainer().toString()+"<"+validDeclaration.getName()+">";
        Assert.assertEquals("[Contravariant]", validDeclaration.isContravariant(), modelDeclaration.isContravariant());
        Assert.assertEquals("[Covariant]", validDeclaration.isCovariant(), modelDeclaration.isCovariant());
        Assert.assertEquals("[SelfType]", validDeclaration.isSelfType(), modelDeclaration.isSelfType());
        Assert.assertEquals("[Defaulted]", validDeclaration.isDefaulted(), modelDeclaration.isDefaulted());
        if (validDeclaration.getDeclaration() != null && modelDeclaration.getDeclaration() != null) {
            compareDeclarations(validDeclaration.getDeclaration(), modelDeclaration.getDeclaration());
        } else if (!(validDeclaration.getDeclaration() == null && modelDeclaration.getDeclaration() == null)) {
            Assert.fail("[Declaration] one has declaration the other not");
        }
        if (validDeclaration.getSelfTypedDeclaration() != null && modelDeclaration.getSelfTypedDeclaration() != null) {
            compareDeclarations(validDeclaration.getSelfTypedDeclaration(), modelDeclaration.getSelfTypedDeclaration());
        } else if (!(validDeclaration.getSelfTypedDeclaration() == null && modelDeclaration.getSelfTypedDeclaration() == null)) {
            Assert.fail("[SelfType] one has self typed declaration the other not");
        }
        if (validDeclaration.getDefaultTypeArgument() != null && modelDeclaration.getDefaultTypeArgument() != null) {
            compareDeclarations(validDeclaration.getDefaultTypeArgument().getDeclaration(), modelDeclaration.getDefaultTypeArgument().getDeclaration());
        } else if (!(validDeclaration.getDefaultTypeArgument() == null && modelDeclaration.getDefaultTypeArgument() == null)) {
            Assert.fail("[DefaultTypeArgument] one has default type argument the other not");
        }
        compareSatisfiedTypes(name, validDeclaration.getSatisfiedTypeDeclarations(), modelDeclaration.getSatisfiedTypeDeclarations());
        compareCaseTypes(name, validDeclaration.getCaseTypeDeclarations(), modelDeclaration.getCaseTypeDeclarations());
    }

    private void compareClassOrInterfaceDeclarations(ClassOrInterface validDeclaration, ClassOrInterface modelDeclaration) {
        String name = validDeclaration.getQualifiedNameString();
        Assert.assertEquals(name+" [abstract]", validDeclaration.isAbstract(), modelDeclaration.isAbstract());
        Assert.assertEquals(name+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
        Assert.assertEquals(name+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
        Assert.assertEquals(name+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
        // extended type
        if(validDeclaration.getExtendedTypeDeclaration() == null)
            Assert.assertTrue(name+" [null supertype]", modelDeclaration.getExtendedTypeDeclaration() == null);
        else
            compareDeclarations(validDeclaration.getExtendedTypeDeclaration(), modelDeclaration.getExtendedTypeDeclaration());
        // satisfied types!
        compareSatisfiedTypes(name, validDeclaration.getSatisfiedTypeDeclarations(), modelDeclaration.getSatisfiedTypeDeclarations());
        // case types
        compareCaseTypes(name, validDeclaration.getCaseTypeDeclarations(), modelDeclaration.getCaseTypeDeclarations());
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
            Assert.assertNotNull(validMember.getQualifiedNameString()+" [member] not found in loaded model", modelMember);
            compareDeclarations(validMember, modelMember);
        }
        // and not more
        for(Declaration modelMember : modelDeclaration.getMembers()){
            // skip non-shared members
            if(!modelMember.isShared())
                continue;
            Declaration validMember = lookupMember(validDeclaration, modelMember);
            Assert.assertNotNull(modelMember.getQualifiedNameString()+" [extra member] encountered in loaded model", validMember);
        }
    }

    private void compareCaseTypes(String name,
            List<TypeDeclaration> validTypeDeclarations,
            List<TypeDeclaration> modelTypeDeclarations) {
        if(validTypeDeclarations != null){
            Assert.assertNotNull(name+ " [null case types]", modelTypeDeclarations);
        }else{
            Assert.assertNull(name+ " [non-null case types]", modelTypeDeclarations);
            return;
        }
        Assert.assertEquals(name+ " [case types count]", validTypeDeclarations.size(), modelTypeDeclarations.size());
        for(int i=0;i<validTypeDeclarations.size();i++){
            TypeDeclaration validTypeDeclaration = validTypeDeclarations.get(i);
            TypeDeclaration modelTypeDeclaration = modelTypeDeclarations.get(i);
            compareDeclarations(validTypeDeclaration, modelTypeDeclaration);
        }
    }

    private void compareSelfTypes(Class validDeclaration,
            Class modelDeclaration, String name) {
        if(validDeclaration.getSelfType() == null)
            Assert.assertTrue(name+" [null self type]", modelDeclaration.getSelfType() == null);
        else{
            ProducedType validSelfType = validDeclaration.getSelfType();
            ProducedType modelSelfType =  modelDeclaration.getSelfType();
            Assert.assertNotNull(name+" [non-null self type]", modelSelfType);
            // self types are always type parameters so they must have a declaration
            compareDeclarations(validSelfType.getDeclaration(), modelSelfType.getDeclaration());
        }
    }
    
    private Declaration lookupMember(ClassOrInterface container, Declaration referenceMember) {
        String name = referenceMember.getName();
        for(Declaration member : container.getMembers()){
            if(member.getName() != null 
                    && member.getName().equals(name)){
                // we have a special case if we're asking for a Value and we find a Class, it means it's an "object"'s
                // class with the same name so we ignore it
                if(Decl.isValue(referenceMember) && (member instanceof Class
                                                     || member instanceof ValueParameter))
                    continue;
                // the opposite is also true
                if((referenceMember instanceof Class
                    || referenceMember instanceof ValueParameter)
                        && Decl.isValue(member))
                    continue;
                // otherwise we found it
                return member;
            }
        }
        // not found
        return null;
    }

    private void compareSatisfiedTypes(String name, List<TypeDeclaration> validTypeDeclarations, List<TypeDeclaration> modelTypeDeclarations) {
        Assert.assertEquals(name+ " [Satisfied types count]", validTypeDeclarations.size(), modelTypeDeclarations.size());
        for(int i=0;i<validTypeDeclarations.size();i++){
            TypeDeclaration validTypeDeclaration = validTypeDeclarations.get(i);
            TypeDeclaration modelTypeDeclaration = modelTypeDeclarations.get(i);
            compareDeclarations(validTypeDeclaration, modelTypeDeclaration);
        }
    }

    private void compareParameterLists(String name, List<ParameterList> validParameterLists, List<ParameterList> modelParameterLists) {
        Assert.assertEquals(name+" [param lists count]", validParameterLists.size(), modelParameterLists.size());
        for(int i=0;i<validParameterLists.size();i++){
            List<Parameter> validParameterList = validParameterLists.get(i).getParameters();
            List<Parameter> modelParameterList = modelParameterLists.get(i).getParameters();
            Assert.assertEquals(name+" [param lists "+i+" count]", 
                    validParameterList.size(), modelParameterList.size());
            for(int p=0;p<validParameterList.size();p++){
                Parameter validParameter = validParameterList.get(i);
                Parameter modelParameter = modelParameterList.get(i);
                Assert.assertEquals(name+" [param "+validParameter.getName()+" sequenced]", 
                        validParameter.isSequenced(), modelParameter.isSequenced());
                Assert.assertEquals(name+" [param "+validParameter.getName()+" defaulted]", 
                        validParameter.isDefaulted(), modelParameter.isDefaulted());
                // make sure they have the same name and type
                compareDeclarations(validParameter, modelParameter);
            }
        }
    }

    private void compareMethodDeclarations(Method validDeclaration, Method modelDeclaration) {
        String name = validDeclaration.getQualifiedNameString();
        Assert.assertEquals(name+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
        Assert.assertEquals(name+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
        Assert.assertEquals(name+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
        // make sure it has every parameter list required
        List<ParameterList> validParameterLists = validDeclaration.getParameterLists();
        List<ParameterList> modelParameterLists = modelDeclaration.getParameterLists();
        compareParameterLists(name, validParameterLists, modelParameterLists);
        // now same for return type
        compareDeclarations(validDeclaration.getType().getDeclaration(), modelDeclaration.getType().getDeclaration());
        // work on type parameters
        compareTypeParameters(name, validDeclaration.getTypeParameters(), modelDeclaration.getTypeParameters());
    }

    private void compareTypeParameters(String name, List<TypeParameter> validTypeParameters, List<TypeParameter> modelTypeParameters) {
        Assert.assertEquals(name+" [type parameter count]", validTypeParameters.size(), modelTypeParameters.size());
        for(int i=0;i<validTypeParameters.size();i++){
            TypeParameter validTypeParameter = validTypeParameters.get(i);
            TypeParameter modelTypeParameter = modelTypeParameters.get(i);
            compareDeclarations(validTypeParameter, modelTypeParameter);
        }
    }

    private void compareAttributeDeclarations(MethodOrValue validDeclaration, Value modelDeclaration) {
        // let's not check Setters since their corresponding Getter is checked already
        if(validDeclaration instanceof Setter)
            return;
        // make sure the flags are the same
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [variable]", validDeclaration.isVariable(), modelDeclaration.isVariable());
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
        // compare the types
        compareDeclarations(validDeclaration.getType().getDeclaration(), modelDeclaration.getType().getDeclaration());
    }

	@Test
	public void loadClass(){
		verifyClassLoading("Klass.ceylon");
	}

    @Test
    public void loadClassWithMethods(){
        verifyClassLoading("KlassWithMethods.ceylon");
    }

    @Test
    public void loadInnerClass(){
        verifyClassLoading("InnerClass.ceylon");
    }
    
    @Test
    public void loadInnerInterface(){
        verifyClassLoading("InnerInterface.ceylon");
    }

    @Test
    public void loadClassWithAttributes(){
        verifyClassLoading("KlassWithAttributes.ceylon");
    }

    @Test
    public void loadClassWithAttributeAndConflictingMethods(){
        verifyClassLoading("KlassWithAttributeAndConflictingMethods.ceylon");
    }

    @Test
    public void loadTypeParameters(){
        verifyClassLoading("TypeParameters.ceylon");
    }

    @Test
    public void loadTypeParameterResolving(){
        verifyClassLoading("TypeParameterResolving.ceylon");
    }

    @Test
    public void loadToplevelMethods(){
        verifyClassLoading("ToplevelMethods.ceylon");
    }

    @Test
    public void loadToplevelAttributes(){
        verifyClassLoading("ToplevelAttributes.ceylon");
    }

    @Test
    public void loadToplevelObjects(){
        verifyClassLoading("ToplevelObjects.ceylon");
    }

    @Test
    public void loadErasedTypes(){
        verifyClassLoading("ErasedTypes.ceylon");
    }

    @Test
    public void loadDocAnnotations(){
        verifyClassLoading("DocAnnotations.ceylon");
    }

    @Test
    public void loadLocalDeclarations(){
        verifyClassLoading("LocalDeclarations.ceylon");
    }

    @Test
    public void loadDefaultValues(){
        verifyClassLoading("DefaultValues.ceylon");
    }

    @Test
    public void loadJavaKeywords(){
        verifyClassLoading("JavaKeywords.ceylon");
    }

    @Test
    public void loadGettersWithUnderscores(){
        verifyClassLoading("GettersWithUnderscores.ceylon");
    }

    @Test
    public void loadCaseTypes(){
        verifyClassLoading("CaseTypes.ceylon");
    }

    @Test
    public void loadSelfType(){
        verifyClassLoading("SelfType.ceylon");
    }
    
    @Test
    public void testTypeParserUsingSourceModel(){
        compile("A.ceylon", "B.ceylon");
        compile("A.ceylon");
    }

    @Test
    public void loadFormalClasses(){
        verifyClassLoading("FormalClasses.ceylon");
    }
    
    @Test
    public void parallelLoader(){
        // whatever test, doesn't matter
        verifyClassLoading("Any.ceylon", new RunnableTest(){
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
                            Module mod = loader.getLoadedModule(moduleName);
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
        verifyClassLoading("Any.ceylon", new RunnableTest(){
            @Override
            public void test(ModelLoader loader) {
                // walk every jdk package
                for (String moduleName : JDKUtils.getJDKModuleNames()) {
                    Module mod = loader.getLoadedModule(moduleName);
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
        return Module.DEFAULT_MODULE_NAME;
    }
    
    protected String packageForJavaModelLoading() {
        return "com.redhat.ceylon.compiler.java.test.model";
    }

    @Test
    public void javaModelLoading(){
        compile("JavaType.java");
        verifyClassLoading("Java.ceylon", new RunnableTest(){
            @Override
            public void test(ModelLoader loader) {
                Module mod = loader.getLoadedModule(moduleForJavaModelLoading());
                Assert.assertNotNull(mod);
                Package p = mod.getDirectPackage(packageForJavaModelLoading());
                Assert.assertNotNull(p);
                Declaration javaType = p.getDirectMember("JavaType", null, false);
                Assert.assertNotNull(javaType);
                
                // check the method which returns a java list
                Method javaListMethod = (Method) javaType.getDirectMember("javaList", null, false);
                Assert.assertNotNull(javaListMethod);
                Assert.assertEquals("Object", javaListMethod.getType().getProducedTypeName());
                Parameter javaListParam = javaListMethod.getParameterLists().get(0).getParameters().get(0);
                Assert.assertNotNull(javaListParam);
                Assert.assertEquals("List<Object>?", javaListParam.getType().getProducedTypeName());

                // check the method which returns a Ceylon list
                Method ceylonListMethod = (Method) javaType.getDirectMember("ceylonList", null, false);
                Assert.assertNotNull(ceylonListMethod);
                Assert.assertEquals("List<Object>", ceylonListMethod.getType().getProducedTypeName());
                Parameter ceylonListParam = ceylonListMethod.getParameterLists().get(0).getParameters().get(0);
                Assert.assertNotNull(ceylonListParam);
                Assert.assertEquals("List<Object>?", ceylonListParam.getType().getProducedTypeName());
}
        }, Collections.EMPTY_LIST);
    }
    
    @Test
    public void javaDeprecated(){
        compile("JavaDeprecated.java");
        verifyClassLoading("JavaDeprecated.ceylon", new RunnableTest(){
            @Override
            public void test(ModelLoader loader) {
                Module mod = loader.getLoadedModule(moduleForJavaModelLoading());
                Assert.assertNotNull(mod);
                Package p = mod.getDirectPackage(packageForJavaModelLoading());
                Assert.assertNotNull(p);
                Declaration javaDep = p.getDirectMember("JavaDeprecated", null, false);
                Assert.assertNotNull(javaDep);
                Assert.assertEquals(javaDep.toString(), 1, numDeprecated(javaDep));
                
                Method javaMethod = (Method) javaDep.getDirectMember("m", null, false);
                Assert.assertNotNull(javaMethod);
                Assert.assertEquals(javaMethod.toString(), 1, numDeprecated(javaMethod));
                
                Parameter javaParameter = javaMethod.getParameterLists().get(0).getParameters().get(0);
                Assert.assertNotNull(javaParameter);
                Assert.assertEquals(javaParameter.toString(), 1, numDeprecated(javaParameter));
                
                Value javaField = (Value) javaDep.getDirectMember("s", null, false);
                Assert.assertNotNull(javaField);
                Assert.assertEquals(javaField.toString(), 1, numDeprecated(javaField));
                
                // Check there is only 1 model Annotation, when annotation with ceylon's deprecated... 
                javaMethod = (Method) javaDep.getDirectMember("ceylonDeprecation", null, false);
                Assert.assertNotNull(javaMethod);
                Assert.assertEquals(javaMethod.toString(), 1, numDeprecated(javaMethod));
                
                // ... or both the ceylon and java deprecated
                javaMethod = (Method) javaDep.getDirectMember("bothDeprecation", null, false);
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
                new CompilerError(-1, "Error while resolving toplevel attribute com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeNoGetter: getter method missing"),
                new CompilerError(-1, "Error while resolving type of toplevel attribute for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeMissingType: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while parsing type of toplevel attribute for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelAttributeInvalidType: Expecting word but got AND"),
                new CompilerError(3, "could not determine type of function or value reference: bogusTopLevelAttributeNoGetter"),
                new CompilerError(4, "could not determine type of function or value reference: bogusTopLevelAttributeMissingType"),
                new CompilerError(5, "could not determine type of function or value reference: bogusTopLevelAttributeInvalidType")
                );
    }

    @Test
    public void bogusModelAnnotationsTopLevelMethod(){
        compile("bogusTopLevelMethodNoMethod_.java", "bogusTopLevelMethodMissingType_.java", "bogusTopLevelMethodInvalidType_.java");
        assertErrors("bogusTopLevelMethodUser",
                new CompilerError(-1, "Error while resolving toplevel method com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodNoMethod: static method missing"),
                new CompilerError(-1, "Error while resolving toplevel method com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodNotStatic: method is not static"),
                new CompilerError(-1, "Error while resolving type of toplevel method for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodMissingType: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while parsing type of toplevel method for com.redhat.ceylon.compiler.java.test.model::bogusTopLevelMethodInvalidType: Expecting word but got AND"),
                // FIXME: this is not great
                new CompilerError(3, "function has no parameter list: bogusTopLevelMethodNoMethod"),
                new CompilerError(3, "could not determine type of function or value reference: bogusTopLevelMethodNoMethod"),
                // FIXME: this is not great
                new CompilerError(4, "function has no parameter list: bogusTopLevelMethodNotStatic"),
                new CompilerError(4, "could not determine type of function or value reference: bogusTopLevelMethodNotStatic"),
                new CompilerError(5, "could not determine type of function or value reference: bogusTopLevelMethodMissingType"),
                new CompilerError(6, "could not determine type of function or value reference: bogusTopLevelMethodInvalidType")
        );

    }
    
    @Test
    public void bogusModelAnnotationsTopLevelClass(){
        compile("BogusTopLevelClass.java", "BogusTopLevelClass2.java");
        assertErrors("bogusTopLevelClassUser",
                new CompilerError(-1, "Constructor for 'com.redhat.ceylon.compiler.java.test.model.BogusTopLevelClass' should take 1 reified type arguments (TypeDescriptor) but has '0': skipping constructor."),
                new CompilerError(-1, "Error while resolving type of getter 'getter' for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of parameter 'arg0' of method 'method' for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass.method: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of method 'method' for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of setter 'setSetter' for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of parameter 'arg0' of method 'setSetter' for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass.setSetter: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of extended type for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of self type for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Invalid type signature for self type of com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: com.redhat.ceylon.compiler.java.test.model::MissingType is not a type parameter"),
                new CompilerError(-1, "Error while resolving type of type parameter 'T' satisfied types for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of type parameter 'T' case types for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of type parameter 'T' defaultValue for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Error while resolving type of case types for com.redhat.ceylon.compiler.java.test.model::BogusTopLevelClass2: Could not find type 'com.redhat.ceylon.compiler.java.test.model.MissingType'"),
                new CompilerError(-1, "Method 'com.redhat.ceylon.compiler.java.test.model.BogusTopLevelClass.params' should take 1 reified type arguments (TypeDescriptor) but has '0': method is invalid."),

                // FIXME: I wish I knew how to get rid of that one...
                new CompilerError(3, "constructor BogusTopLevelClass in class com.redhat.ceylon.compiler.java.test.model.BogusTopLevelClass<T> cannot be applied to given types;\n  required: no arguments\n  found: com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor\n  reason: actual and formal argument lists differ in length")
        );
    }
}
