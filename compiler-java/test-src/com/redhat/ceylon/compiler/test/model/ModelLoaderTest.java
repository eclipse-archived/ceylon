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
package com.redhat.ceylon.compiler.test.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.loader.CeylonModelLoader;
import com.redhat.ceylon.compiler.loader.ModelLoader.DeclarationType;
import com.redhat.ceylon.compiler.test.CompilerTest;
import com.redhat.ceylon.compiler.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Getter;
import com.redhat.ceylon.compiler.typechecker.model.Method;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Parameter;
import com.redhat.ceylon.compiler.typechecker.model.ParameterList;
import com.redhat.ceylon.compiler.typechecker.model.Setter;
import com.redhat.ceylon.compiler.typechecker.model.Value;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TaskEvent.Kind;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Symbol.TypeSymbol;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Types;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;
import com.sun.tools.javac.zip.ZipFileIndex;

public class ModelLoaderTest extends CompilerTest {
    
    private Set<Declaration> validDeclarations = new HashSet<Declaration>();
    
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
                decls.put(Util.getQualifiedPrefixedName(decl), decl);
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
                    CeylonModelLoader modelLoader = CeylonModelLoader.instance(context2);
                    // now see if we can find our declarations
                    for(Entry<String, Declaration> entry : decls.entrySet()){
                        Declaration modelDeclaration = modelLoader.getDeclaration(entry.getKey().substring(1), 
                                entry.getValue() instanceof Value ? DeclarationType.VALUE : DeclarationType.TYPE);
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

    private void compareDeclarations(Declaration validDeclaration, Declaration modelDeclaration) {
        if(!validDeclarations.add(validDeclaration))
            return;
        // check that we have a unit
        Assert.assertNotNull("Missing Unit: "+modelDeclaration.getQualifiedNameString(), modelDeclaration.getUnit());
        Assert.assertNotNull("Invalid Unit", modelDeclaration.getUnit().getPackage());
        // let's not check java stuff for now, due to missing types in the jdk's private methods
        if(validDeclaration.getQualifiedNameString().startsWith("java."))
            return;
        // only compare parameter names for public methods
        if(!(validDeclaration instanceof Parameter) || validDeclaration.isShared())
            Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [name]", validDeclaration.getQualifiedNameString(), modelDeclaration.getQualifiedNameString());
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [shared]", validDeclaration.isShared(), modelDeclaration.isShared());
        if(validDeclaration instanceof Class){
            Assert.assertTrue("[Class]", modelDeclaration instanceof Class);
            compareClassDeclarations((Class)validDeclaration, (Class)modelDeclaration);
        }else if(validDeclaration instanceof Method){
            Assert.assertTrue("[Method]", modelDeclaration instanceof Method);
            compareMethodDeclarations((Method)validDeclaration, (Method)modelDeclaration);
        }else if(validDeclaration instanceof Value || validDeclaration instanceof Getter || validDeclaration instanceof Setter){
            Assert.assertTrue("[Attribute]", modelDeclaration instanceof Value);
            compareAttributeDeclarations((MethodOrValue)validDeclaration, (Value)modelDeclaration);
        }
    }
    
    private void compareClassDeclarations(Class validDeclaration, Class modelDeclaration) {
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [abstract]", validDeclaration.isAbstract(), modelDeclaration.isAbstract());
        if(validDeclaration.getExtendedTypeDeclaration() == null)
            Assert.assertTrue(validDeclaration.getQualifiedNameString()+" [null supertype]", modelDeclaration.getExtendedTypeDeclaration() == null);
        else
            compareDeclarations(validDeclaration.getExtendedTypeDeclaration(), modelDeclaration.getExtendedTypeDeclaration());
        compareParameterLists(validDeclaration.getQualifiedNameString(), validDeclaration.getParameterLists(), modelDeclaration.getParameterLists());
        // make sure it has every member required
        for(Declaration validMember : validDeclaration.getMembers()){
            Declaration modelMember = modelDeclaration.getMemberOrParameter(validMember.getName());
            Assert.assertNotNull(validMember.getQualifiedNameString()+" [member] not found in loaded model", modelMember);
            compareDeclarations(validMember, modelMember);
        }
        // and not more
        for(Declaration modelMember : modelDeclaration.getMembers()){
            Declaration validMember = validDeclaration.getMemberOrParameter(modelMember.getName());
            Assert.assertNotNull(modelMember.getQualifiedNameString()+" [extra member] encountered in loaded model", validMember);
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
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [formal]", validDeclaration.isFormal(), modelDeclaration.isFormal());
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [actual]", validDeclaration.isActual(), modelDeclaration.isActual());
        Assert.assertEquals(validDeclaration.getQualifiedNameString()+" [default]", validDeclaration.isDefault(), modelDeclaration.isDefault());
        // make sure it has every parameter list required
        List<ParameterList> validParameterLists = validDeclaration.getParameterLists();
        List<ParameterList> modelParameterLists = modelDeclaration.getParameterLists();
        compareParameterLists(validDeclaration.getQualifiedNameString(), validParameterLists, modelParameterLists);
        // now same for return type
        compareDeclarations(validDeclaration.getType().getDeclaration(), modelDeclaration.getType().getDeclaration());
    }

    private void compareAttributeDeclarations(MethodOrValue validDeclaration, Value modelDeclaration) {
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
    public void loadClassWithAttributes(){
        verifyClassLoading("KlassWithAttributes.ceylon");
    }

    @Test
    public void loadTypeParameters(){
        verifyClassLoading("TypeParameters.ceylon");
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
}
