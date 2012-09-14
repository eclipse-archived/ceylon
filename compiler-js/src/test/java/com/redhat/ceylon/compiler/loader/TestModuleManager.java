package com.redhat.ceylon.compiler.loader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.ceylon.CeylonUtils;
import com.redhat.ceylon.cmr.impl.JULLogger;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.SimpleJsonEncoder;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;

public class TestModuleManager {

    private static TypeChecker tc;
    private static TypeChecker jstc;
    private static RepositoryManager repoman;
    private static Options options;
    private static Module srcmod;
    private static Module jsmod;
    private static com.redhat.ceylon.compiler.typechecker.model.Package  srclang;
    private static com.redhat.ceylon.compiler.typechecker.model.Package  jslang;

    @BeforeClass
    public static void setup() throws IOException {
        //Copy language module to destination
        java.io.File srclangmod = new java.io.File(String.format(
                "build/runtime/ceylon/language/%s/ceylon.language-%<s.js", JsCompiler.VERSION));
        java.io.File dstlangmod = new java.io.File("build/test/test_modules/ceylon/language/" + JsCompiler.VERSION);
        System.out.printf("Copying %s to %s%n", srclangmod, dstlangmod);
        dstlangmod.mkdirs();
        java.io.BufferedReader lmreader = new java.io.BufferedReader(new java.io.FileReader(srclangmod));
        java.io.Writer lmwriter = new java.io.FileWriter(new java.io.File(dstlangmod, srclangmod.getName()));
        String line;
        while ((line = lmreader.readLine()) != null) {
            lmwriter.write(line);
            lmwriter.write('\n');
        }
        lmreader.close();
        lmwriter.close();
        ArrayList<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("src/test/resources/loader/pass1/m1/test.ceylon",
                "-rep", "build/test/test_modules", "-out", "build/test/test_modules",
                "-src", "src/test/resources/loader/pass1"));
        options = Options.parse(args);
        repoman = CeylonUtils.makeRepositoryManager(
                options.getSystemRepo(), options.getRepos(),
                options.getOutDir(), new JULLogger());
        //Create a typechecker to compile the test module
        System.out.println("Compiling pass 1 (with js model loader now)");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
        tcb.moduleManagerFactory(new JsModuleManagerFactory());
        tcb.addSrcDirectory(new java.io.File("src/test/resources/loader/pass1"));
        tcb.setRepositoryManager(repoman);
        tc = tcb.getTypeChecker();
        tc.process();
        JsCompiler compiler = new JsCompiler(tc, options);
        compiler.stopOnErrors(false);
        compiler.generate();
        loadJsModel();
        srcmod = tc.getPhasedUnits().getPhasedUnits().get(0).getPackage().getModule().getLanguageModule();
        jsmod = jstc.getPhasedUnits().getPhasedUnits().get(0).getPackage().getModule().getLanguageModule();
    }

    private static void loadJsModel() {
        if (jstc == null) {
            System.out.println("Pass 2: Loading model from JS");
            final RepositoryManager repoman = CeylonUtils.makeRepositoryManager(
                    options.getSystemRepo(), options.getRepos(),
                    options.getOutDir(), new JULLogger());
            TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);//.verbose(true);
            tcb.moduleManagerFactory(new JsModuleManagerFactory());
            tcb.addSrcDirectory(new java.io.File("src/test/resources/loader/pass2"));
            tcb.setRepositoryManager(repoman);
            jstc = tcb.getTypeChecker();
            jstc.process();
        }
    }

    @Test
    public void testSameModels() throws java.io.IOException {
        MetamodelGenerator mmg = new MetamodelGenerator(jsmod);
        mmg.encodeClass((com.redhat.ceylon.compiler.typechecker.model.Class)jslang.getDirectMember("Integer", null));
        SimpleJsonEncoder json = new SimpleJsonEncoder();
        java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(System.out);
        writer.write("$$metamodel$$=");
        json.encode(mmg.getModel(), writer);
        writer.write(";\n");
        writer.flush();
    }

    private void compareMembers(Declaration m1, Declaration m2) {
        if (m1.getMembers() == null) {
            Assert.assertNull(m2.getMembers());
        } else {
            Assert.assertEquals(m1.getMembers().size(), m2.getMembers().size());
        }
        Iterator<Declaration> ds2 = m2.getMembers().iterator();
        for (Declaration d0 : m1.getMembers()) {
            String n = d0.getQualifiedNameString();
            if (n.indexOf('.') < 0) {
                n += "(" + d0.getContainer().getQualifiedNameString() + ")";
            }
            Declaration d1 = ds2.next();
            Assert.assertNotNull(n + " not found in js ", d1);
            Assert.assertEquals(n + " declaration kinds differ", d0.getDeclarationKind(), d1.getDeclarationKind());
            Assert.assertEquals(d0.getContainer().getQualifiedNameString(), d1.getContainer().getQualifiedNameString());
            if (d0 instanceof TypeDeclaration) {
                Assert.assertEquals(d0.getClass(), d1.getClass());
                compareTypeDeclarations((TypeDeclaration)d0, (TypeDeclaration)d1);
            }
            compareMembers(d0, d1);
        }
    }

    private void compareTypeDeclarations(TypeDeclaration a, TypeDeclaration b) {
        String n = a.getQualifiedNameString();
        Assert.assertEquals(n, b.getQualifiedNameString());
        if (n.indexOf('.') < 0) {
            n = a + "(" + a.getContainer().getQualifiedNameString() + ")";
        }
        Assert.assertEquals("strings differ ", a.toString(), b.toString());
        Assert.assertEquals(a.isActual(), b.isActual());
        Assert.assertEquals(a.isAnonymous(), b.isAnonymous());
        Assert.assertEquals(a.isDefault(), b.isDefault());
        Assert.assertEquals(a.isFormal(), b.isFormal());
        Assert.assertEquals(a.isShared(), b.isShared());
        Assert.assertEquals(a.isClassMember(), b.isClassMember());
        Assert.assertEquals(a.isClassOrInterfaceMember(), b.isClassOrInterfaceMember());
        compareTypes(a.getType(), b.getType());
        compareTypes(a.getSelfType(), b.getSelfType());
        if (a.getSatisfiedTypes() != null) {
            Assert.assertNotNull(b.getSatisfiedTypes());
            Assert.assertEquals("satisfied types for " + n,
                    a.getSatisfiedTypes().size(), b.getSatisfiedTypes().size());
            Iterator<ProducedType> bsats = b.getSatisfiedTypes().iterator();
            for (ProducedType satA : a.getSatisfiedTypes()) {
                ProducedType satB = bsats.next();
                compareTypes(satA, satB);
            }
        }
    }

    @Test
    public void compareLanguageModules() {
        Assert.assertNotNull("langmod from source", srcmod);
        Assert.assertNotNull("langmod from js", jsmod);
        srclang = srcmod.getDirectPackage("ceylon.language");
        jslang = jsmod.getDirectPackage("ceylon.language");
        Assert.assertNotNull("clpack from source", srclang);
        Assert.assertNotNull("clpack from js", jslang);
        for (Declaration d0 : srclang.getMembers()) {
            Declaration d1 = jslang.getDirectMember(d0.getName(), null);
            Assert.assertNotNull(d0.getName() + " not found in js", d1);
            Assert.assertEquals("declaration kinds differ", d0.getDeclarationKind(), d1.getDeclarationKind());
            compareMembers(d0, d1);
        }
    }

    @Test
    public void testScalar() {
        System.out.println("-----------------------");
        ClassOrInterface d0 = (ClassOrInterface)srclang.getDirectMember("Ordinal", null);
        Assert.assertNotNull("Scalar from srclang", d0);
        ClassOrInterface d1 = (ClassOrInterface)jslang.getDirectMember("Ordinal", null);
        Assert.assertNotNull("Scalar from jslang", d1);
        compareTypes(d0.getType(), d1.getType());
        compareTypes(d0.getSelfType(), d1.getSelfType());
        Iterator<TypeParameter> parms1 = d1.getTypeParameters().iterator();
        for (TypeParameter p0 : d0.getTypeParameters()) {
            TypeParameter p1 = parms1.next();
            Assert.assertEquals(p0.getQualifiedNameString(), p1.getQualifiedNameString());
            compareTypes(p0.getSelfType(), p1.getSelfType());
            Iterator<ProducedType> sats1 = p1.getSatisfiedTypes().iterator();
            for (ProducedType sat0 : p0.getSatisfiedTypes()) {
                ProducedType sat1 = sats1.next();
                compareTypes(sat0, sat1);
            }
        }
        compareTypeDeclarations(d0, d1);
    }

    public void compareTypes(ProducedType t0, ProducedType t1) {
        if (t0 == null) {
            Assert.assertNull("not null " + t1, t1);
            return;
        } else {
            Assert.assertNotNull(t1);
        }
        Assert.assertEquals(t0.getProducedTypeName(), t1.getProducedTypeName());
        Assert.assertEquals(t0.getUnderlyingType(), t1.getUnderlyingType());
        Assert.assertEquals(t0.getProducedTypeQualifiedName(), t1.getProducedTypeQualifiedName());
        Assert.assertEquals(t0.isCallable(), t1.isCallable());
        Assert.assertEquals(t0.isFunctional(), t1.isFunctional());
        Assert.assertEquals(t0.isRaw(), t1.isRaw());
        Assert.assertEquals(t0.isWellDefined(), t1.isWellDefined());
        if ("ceylon.language.Void".equals(t0.getProducedTypeQualifiedName())) {
            return;
        }
        //Type arguments
        if (t0.getTypeArguments() == null) {
            Assert.assertNull(t1.getTypeArguments());
        } else {
            Map<TypeParameter, ProducedType> parms1 = t1.getTypeArguments();
            for (Map.Entry<TypeParameter, ProducedType> e : t0.getTypeArguments().entrySet()) {
                ProducedType tparm = parms1.get(e.getKey());
                Assert.assertNotNull(tparm);
                compareTypes(e.getValue(), tparm);
            }
        }
        //Case types
        if (t0.getCaseTypes() == null) {
            Assert.assertNull(t1.getCaseTypes());
        } else if (!"ceylon.language.Nothing".equals(t0.getProducedTypeQualifiedName())) {
            Iterator<ProducedType> cases = t1.getCaseTypes().iterator();
            for (ProducedType c0 : t0.getCaseTypes()) {
                ProducedType c1 = cases.next();
                compareTypes(c0, c1);
            }
        }
        if (t0.getSupertypes() == null) {
            Assert.assertNull(t1.getSupertypes());
        } else {
            Assert.assertEquals("supertypes differ for " + t0, t0.getSupertypes().size(), t1.getSupertypes().size());
            Iterator<ProducedType> supers = t1.getSupertypes().iterator();
            for (ProducedType s0 : t0.getSupertypes()) {
                ProducedType s1 = supers.next();
                if (s0 == t0) {
                    Assert.assertTrue(s1 == t1);
                } else {
                    compareTypes(s0, s1);
                }
            }
        }
    }

}
