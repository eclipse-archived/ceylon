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
import com.redhat.ceylon.common.Versions;
import com.redhat.ceylon.compiler.Options;
import com.redhat.ceylon.compiler.js.JsCompiler;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;
import com.redhat.ceylon.compiler.typechecker.TypeCheckerBuilder;
import com.redhat.ceylon.compiler.typechecker.model.ClassOrInterface;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.MethodOrValue;
import com.redhat.ceylon.compiler.typechecker.model.Module;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.TypeParameter;
import com.redhat.ceylon.compiler.typechecker.model.Value;

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
                "build/runtime/ceylon/language/%s/ceylon.language-%<s.js", Versions.CEYLON_VERSION_NUMBER));
        java.io.File dstlangmod = new java.io.File("build/test/test_modules/ceylon/language/" + Versions.CEYLON_VERSION_NUMBER);
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
        repoman = CeylonUtils.repoManager()
                .cwd(options.getCwd())
                .systemRepo(options.getSystemRepo())
                .userRepos(options.getRepos())
                .outRepo(options.getOutDir())
                .buildManager();
        //Create a typechecker to compile the test module
        System.out.println("Compiling pass 1");
        TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);
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
        srclang = srcmod.getDirectPackage("ceylon.language");
        jslang = jsmod.getDirectPackage("ceylon.language");
    }

    private static void loadJsModel() {
        if (jstc == null) {
            System.out.println("Pass 2: Loading model from JS");
            final RepositoryManager repoman = CeylonUtils.repoManager()
                    .cwd(options.getCwd())
                    .systemRepo(options.getSystemRepo())
                    .userRepos(options.getRepos())
                    .outRepo(options.getOutDir())
                    .buildManager();
            TypeCheckerBuilder tcb = new TypeCheckerBuilder().usageWarnings(false);//.verbose(true);
            tcb.moduleManagerFactory(new JsModuleManagerFactory((String)null));
            tcb.addSrcDirectory(new java.io.File("src/test/resources/loader/pass2"));
            tcb.setRepositoryManager(repoman);
            jstc = tcb.getTypeChecker();
            jstc.process();
        }
    }

    private void compareMembers(Declaration m1, Declaration m2) {
        if (m1 instanceof MethodOrValue) {
            return;
        } else if (m1.getMembers() == null) {
            Assert.assertNull(m2.getMembers());
            return;
        }
        if (m1.getMembers().size() != m2.getMembers().size()) {
            System.out.println(m1 + " (src) tiene " + m1.getMembers());
            System.out.println(m2 + " (js)  tiene " + m2.getMembers());
        }
        Assert.assertEquals(m1 + " member count", m1.getMembers().size(), m2.getMembers().size());
        for (Declaration d0 : m1.getMembers()) {
            String n = d0.getQualifiedNameString();
            if (n.indexOf('.') < 0) {
                n += "(" + d0.getContainer().getQualifiedNameString() + ")";
            }
            Declaration d1 = findMatchingDeclaration(d0, m2.getMembers());
            Assert.assertNotNull(n + " not found in " + m2, d1);
            Assert.assertEquals(n + " declaration kinds differ", d0.getDeclarationKind(), d1.getDeclarationKind());
            if (d0.getContainer() == null) {
                Assert.assertNull(d1.getContainer());
            } else {
                Assert.assertEquals(d0.getContainer().getQualifiedNameString(), d1.getContainer().getQualifiedNameString());
            }
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
        compareTypes(a.getType(), b.getType(), null);
        compareTypes(a.getSelfType(), b.getSelfType(), null);
        if (a.getSatisfiedTypes() != null) {
            Assert.assertNotNull(b.getSatisfiedTypes());
            Assert.assertEquals("satisfied types for " + n,
                    a.getSatisfiedTypes().size(), b.getSatisfiedTypes().size());
            Iterator<ProducedType> bsats = b.getSatisfiedTypes().iterator();
            for (ProducedType satA : a.getSatisfiedTypes()) {
                ProducedType satB = bsats.next();
                compareTypes(satA, satB, null);
            }
        }
    }

    private Declaration findMatchingDeclaration(Declaration src, java.util.List<Declaration> members) {
        for (Declaration d : members) {
            if (src.getName().equals(d.getName()) && src.getClass().isInstance(d)) {
                return d;
            }
        }
        return null;
    }

    @Test
    public void compareLanguageModules() {
        Assert.assertNotNull("langmod from source", srcmod);
        Assert.assertNotNull("langmod from js", jsmod);
        Assert.assertNotNull("clpack from source", srclang);
        Assert.assertNotNull("clpack from js", jslang);
        Assert.assertEquals(srclang.getMembers().size(), jslang.getMembers().size());
        for (Declaration d0 : srclang.getMembers()) {
            Declaration d1 = findMatchingDeclaration(d0, jslang.getMembers());
            Assert.assertNotNull(d0.getName() + " not found in js", d1);
            if (d0 instanceof ClassOrInterface && d1 instanceof Value) {
                d1 = ((Value)d1).getTypeDeclaration();
            }
            Assert.assertEquals(d0 + " wrong class!", d0.getClass(), d1.getClass());
            Assert.assertEquals(d0 + " wrong kind " + d1, d0.getDeclarationKind(), d1.getDeclarationKind());
            compareMembers(d0, d1);
        }
    }

    @Test
    public void tmptest() {
        System.out.println("-----------------------");
        ClassOrInterface d0 = (ClassOrInterface)srclang.getDirectMember("Iterable", null, false);
        Assert.assertNotNull("ContainerWithFirstElement from srclang", d0);
        ClassOrInterface d1 = (ClassOrInterface)jslang.getDirectMember("Iterable", null, false);
        Assert.assertNotNull("ContainerWithFirstElement from jslang", d1);
        ProducedType seq0 = null, seq1 = null;
        for (ProducedType pt : d0.getSatisfiedTypes()) {
            System.out.println(d0 + " satisfies " + pt);
            if (pt.getProducedTypeName().startsWith("Null[]")) {
                seq0 = pt;
                break;
            }
        }
        for (ProducedType pt : d1.getSatisfiedTypes()) {
            if (pt.getProducedTypeName().startsWith("Null[]")) {
                seq1 = pt;
                break;
            }
        }
        compareTypes(seq0, seq1, new ArrayList<String>());
        System.out.println("src " + seq0 + " - js " + seq1);
        compareTypeDeclarations(d0, d1);
        MethodOrValue m0 = (MethodOrValue)d0.getDirectMember("last", null, false);
        MethodOrValue m1 = (MethodOrValue)d1.getDirectMember("last", null, false);
        System.out.println("Iterable.last " + m0 + " vs " + m1);
        System.out.println("refined member " + d0.getRefinedMember("last", null, false).getContainer() + " vs " + d1.getRefinedMember("last", null, false).getContainer());
        System.out.println("last is transient? " + m0.isTransient() + " vs " + m1.isTransient());
        System.out.println("refined " + m0.getRefinedDeclaration().getContainer() + " vs " + m1.getRefinedDeclaration().getContainer());
    }

    public void compareTypes(ProducedType t0, ProducedType t1, ArrayList<String> stack) {
        if (stack == null) {
            stack = new ArrayList<String>();
        }
        if (t0 == null) {
            Assert.assertNull("not null " + t1, t1);
            return;
        } else {
            Assert.assertNotNull(t1);
        }
        Assert.assertEquals("stack: " + stack, t0.getProducedTypeName(), t1.getProducedTypeName());
        Assert.assertEquals(t0.getUnderlyingType(), t1.getUnderlyingType());
        Assert.assertEquals(t0.getProducedTypeQualifiedName(), t1.getProducedTypeQualifiedName());
        Assert.assertEquals(t0.isFunctional(), t1.isFunctional());
        Assert.assertEquals(t0.isRaw(), t1.isRaw());
        Assert.assertEquals(t0.isWellDefined(), t1.isWellDefined());
        final String t0ptqn = t0.getProducedTypeQualifiedName();
        stack.add(t0ptqn);
        if ("ceylon.language::Anything".equals(t0ptqn) || "ceylon.language::Null".equals(t0ptqn) || "ceylon.language::Basic".equals(t0ptqn) || "ceylon.language::Null".equals(t0ptqn)) {
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
                final String s0ppp = e.getValue().getProducedTypeQualifiedName();
                if (stack.contains(s0ppp)) {
                    Assert.assertEquals(s0ppp, tparm.getProducedTypeQualifiedName());
                } else {
                    compareTypes(e.getValue(), tparm, stack);
                }
            }
        }
        //Case types
        if (t0.getCaseTypes() == null) {
            Assert.assertNull(t1.getCaseTypes());
        } else {
            Iterator<ProducedType> cases = t1.getCaseTypes().iterator();
            for (ProducedType c0 : t0.getCaseTypes()) {
                ProducedType c1 = cases.next();
                compareTypes(c0, c1, stack);
            }
        }
        if (t0.getDeclaration().getSelfType() == null) {
            Assert.assertNull(t1.getDeclaration().getSelfType());
        } else {
            compareTypes(t0.getDeclaration().getSelfType(), t1.getDeclaration().getSelfType(), stack);
        }
        if (t0.getSupertypes() == null) {
            Assert.assertNull(t1.getSupertypes());
        } else {
            if (t0.getSupertypes().size() != t1.getSupertypes().size()) {
                System.out.println("SRC " + t0ptqn + "(" + t0.getDeclaration().getContainer().getQualifiedNameString() + ") supers " + t0.getSupertypes());
                System.out.println("JS  " + t1.getProducedTypeQualifiedName() + "(" + t1.getDeclaration().getContainer().getQualifiedNameString() + ") supers " + t1.getSupertypes());
            }
            Assert.assertEquals("supertypes differ for " + t0 + ": " + t0.getSupertypes() + " vs " + t1.getSupertypes(),
                    t0.getSupertypes().size(), t1.getSupertypes().size());
            Iterator<ProducedType> supers = t1.getSupertypes().iterator();
            for (ProducedType s0 : t0.getSupertypes()) {
                ProducedType s1 = supers.next();
                if (s0 == t0) {
                    Assert.assertTrue(s1 == t1);
                } else {
                    final String s0ppp = s0.getProducedTypeQualifiedName();
                    if (stack.contains(s0ppp)) {
                        Assert.assertEquals(s0ppp, s1.getProducedTypeQualifiedName());
                    } else {
                        compareTypes(s0, s1, stack);
                    }
                }
            }
        }
    }

}
