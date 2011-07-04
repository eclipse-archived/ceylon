package com.redhat.ceylon.compiler.test.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Test;

import com.redhat.ceylon.compiler.codegen.CeylonModelLoader;
import com.redhat.ceylon.compiler.test.CompilerTest;
import com.redhat.ceylon.compiler.tools.CeyloncTaskImpl;
import com.redhat.ceylon.compiler.tools.LanguageCompiler;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.model.Class;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.util.Util;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.util.Context;

public class ModelLoaderTest extends CompilerTest {
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
        Map<String,Declaration> decls = new HashMap<String,Declaration>();
        for(Declaration decl : phasedUnit.getUnit().getDeclarations()){
            decls.put(Util.getQualifiedName(decl), decl);
        }
        
        // now compile the ceylon usage file
        // remove the extension, make lowercase and add "test"
        String testfile = ceylon.substring(0, ceylon.length()-7).toLowerCase()+"test.ceylon";
        JavacTaskImpl task2 = getCompilerTask(testfile);
        // get the context to grab the declarations
        Context context2 = task2.getContext();
        success = task2.call();
        Assert.assertTrue(success);
        
        CeylonModelLoader modelLoader = CeylonModelLoader.instance(context2);
        // now see if we can find our declarations
        for(Entry<String, Declaration> entry : decls.entrySet()){
            ProducedType producedType = modelLoader.getType(entry.getKey(), null);
            Assert.assertNotNull(producedType);
            TypeDeclaration modelDeclaration = producedType.getDeclaration();
            // make sure we loaded them exactly the same
            compareDeclarations(entry.getValue(), modelDeclaration);
        }
    }

    private void compareDeclarations(Declaration validDeclaration, Declaration modelDeclaration) {
        Assert.assertEquals(validDeclaration.getQualifiedName()+" [name]", validDeclaration.getName(), modelDeclaration.getName());
        Assert.assertEquals(validDeclaration.getQualifiedName()+" [shared]", validDeclaration.isShared(), modelDeclaration.isShared());
        if(validDeclaration instanceof Class){
            Assert.assertTrue("[Class]", modelDeclaration instanceof Class);
            compareClassDeclarations((Class)validDeclaration, (Class)modelDeclaration);
        }
    }
    private void compareClassDeclarations(Class validDeclaration, Class modelDeclaration) {
        Assert.assertEquals(validDeclaration.getQualifiedName()+" [abstract]", validDeclaration.isAbstract(), modelDeclaration.isAbstract());
        if(validDeclaration.getExtendedTypeDeclaration() == null)
            Assert.assertTrue(validDeclaration.getQualifiedName()+" [null supertype]", modelDeclaration.getExtendedTypeDeclaration() == null);
        else
            compareDeclarations(validDeclaration.getExtendedTypeDeclaration(), modelDeclaration.getExtendedTypeDeclaration());
    }

	@Test
	public void loadClass(){
		verifyClassLoading("Klass.ceylon");
	}

    @Test
    public void loadClassWithMethods(){
        verifyClassLoading("KlassWithMethods.ceylon");
    }
}
