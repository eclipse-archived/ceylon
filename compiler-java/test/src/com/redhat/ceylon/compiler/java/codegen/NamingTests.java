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
package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag.COMPANION;
import static com.redhat.ceylon.compiler.java.codegen.Naming.DeclNameFlag.QUALIFIED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.common.log.Logger;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.model.typechecker.model.Declaration;
import com.redhat.ceylon.model.typechecker.model.Scope;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;

public class NamingTests {
    
    private static final String PKGNAME = NamingTests.class.getPackage().getName();
    private static final String QUAL = "." + PKGNAME + ".";
    
    private static final class NullLogger implements Logger {
        @Override
        public void error(String str) {
            // Don't care
        }

        @Override
        public void warning(String str) {
            // Don't care
        }

        @Override
        public void info(String str) {
            // Don't care
        }

        @Override
        public void debug(String str) {
            // Don't care
        }
    }
    
    protected List<Declaration> getDecls(String resource) throws Exception {
        final String name = PKGNAME.replace('.','/') + "/" + resource;
        File file = new File("test/src", name);
        if (!file.exists()) {
            throw new RuntimeException("Unable to find resource " + name);
        }
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(new NullLogger(), false, 20000, java.net.Proxy.NO_PROXY);
        RepositoryManager repoManager = builder.buildRepository();
        VFS vfs = new VFS();
        Context context = new Context(repoManager, vfs);
        PhasedUnits pus = new PhasedUnits(context);
        // Make the module manager think we're looking at this package
        // even though there's no module descriptor
        pus.getModuleSourceMapper().push(PKGNAME);
        pus.parseUnit(vfs.getFromFile(file), vfs.getFromFile(new File("test-src")));
        final java.util.List<PhasedUnit> listOfUnits = pus.getPhasedUnits();
        
        PhasedUnit pu = listOfUnits.get(0);
        pu.validateTree();
        pu.scanDeclarations();
         
        pu.scanTypeDeclarations(); 
         
        pu.validateRefinement();
         
        pu.analyseTypes(); 
         
        pu.analyseFlow();
        return pu.getDeclarations();
    }
    
    protected Declaration findDecl(String resource, String declName) throws Exception {
        List<Declaration> members = getDecls(resource);
        Declaration found = null;
        outer: for (String name : declName.split("\\.")) {
            for (Declaration decl : members) {
                if (name.equals(decl.getName())) {
                    found = decl;
                    members = found.getMembers();
                    continue outer;
                }
            }
            break;
        }
        if (found == null) {
            throw new RuntimeException("Unable to find declaration");
        }
        return found;
    }
    
    protected TypeDeclaration findType(String resource, String declName) throws Exception {
        return (TypeDeclaration)findDecl(resource, declName);
    }

    private final Naming naming;
    public NamingTests () {
        super();
        com.sun.tools.javac.util.Context context = new com.sun.tools.javac.util.Context();
        new CeyloncFileManager(context, true, Charset.forName("UTF-8"));
        naming = new Naming(context) {
            @Override
            public String getLocalId(Scope d) {
                return "0";
            }
        };
    }
    
    @Test
    public void testC() throws Exception {
        final TypeDeclaration decl = findType("C.ceylon", "C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.C", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testCC() throws Exception {
        final TypeDeclaration decl = findType("CC.ceylon", "CC.C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "CC.C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.CC.C", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testCI() throws Exception {
        final TypeDeclaration decl = findType("CI.ceylon", "CI.I");
        Assert.assertEquals("CI$I", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "CI$I", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "CI.I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.CI$I", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testCo() throws Exception {
        final TypeDeclaration decl = findType("Co.ceylon", "Co.o");
        Assert.assertEquals("o_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "Co.o_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.Co.o_.get_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testI() throws Exception {
        final TypeDeclaration decl = findType("I.ceylon", "I");
        Assert.assertEquals("I", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "I", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.I", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testIC() throws Exception {
        final TypeDeclaration decl = findType("IC.ceylon", "IC.C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "IC$impl.C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.IC$impl.C", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testII() throws Exception {
        final TypeDeclaration decl = findType("II.ceylon", "II.I");
        Assert.assertEquals("II$I", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "II$I", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "II$impl.I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
    
        assertEquals("com.redhat.ceylon.compiler.java.codegen.II$I", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testIo() throws Exception {
        final TypeDeclaration decl = findType("Io.ceylon", "Io.o");
        Assert.assertEquals("o_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "Io$impl.o_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.Io$impl.o_.get_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testo() throws Exception {
        final TypeDeclaration decl = findType("o.ceylon", "o");
        Assert.assertEquals("o_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "o_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.o_.get_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testoC() throws Exception {
        final TypeDeclaration decl = findType("oC.ceylon", "oC.C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "oC_.C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.oC_.C", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testoI() throws Exception {
        final TypeDeclaration decl = findType("oI.ceylon", "oI.I");
        Assert.assertEquals("oI$I_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "oI$I_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "oI_.I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.oI$I_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testoo() throws Exception {
        final TypeDeclaration decl = findType("oo.ceylon", "oo.o");
        Assert.assertEquals("o_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals(QUAL + "oo_.o_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        
        assertEquals("com.redhat.ceylon.compiler.java.codegen.oo_.o_.get_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testf() throws Exception {
        final Declaration decl = findDecl("f.ceylon", "f");
        assertEquals("com.redhat.ceylon.compiler.java.codegen.f_.f", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testfC() throws Exception {
        final TypeDeclaration decl = findType("fC.ceylon", "fC.C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("C$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals("C$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testfCC() throws Exception {
        final TypeDeclaration decl = findType("fCC.ceylon", "fCC.CC.C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("CC.C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testfCI() throws Exception {
        final TypeDeclaration decl = findType("fCI.ceylon", "fCI.CI.I");
        Assert.assertEquals("fCI$CI$I_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "fCI$CI$I_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals("CI.I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testfI() throws Exception {
        final TypeDeclaration decl = findType("fI.ceylon", "fI.I");
        Assert.assertEquals("fI$I_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "fI$I_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testfIC() throws Exception {
        final TypeDeclaration decl = findType("fIC.ceylon", "fIC.IC.C");
        Assert.assertEquals("C", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("IC$impl.C", naming.makeTypeDeclarationName(decl, QUALIFIED));
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testfII() throws Exception {
        final TypeDeclaration decl = findType("fII.ceylon", "fII.II.I");
        Assert.assertEquals("fII$II$I_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("I$impl", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals(QUAL + "fII$II$I_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals("II$impl.I$impl", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testfo() throws Exception {
        final TypeDeclaration decl = findType("fo.ceylon", "fo.o");
        Assert.assertEquals("o_", naming.makeTypeDeclarationName(decl));
        Assert.assertEquals("o$impl_", naming.makeTypeDeclarationName(decl, COMPANION));
        Assert.assertEquals("o_", naming.makeTypeDeclarationName(decl, QUALIFIED));
        Assert.assertEquals("o$impl_", naming.makeTypeDeclarationName(decl, COMPANION, QUALIFIED));
        try {
            CodegenUtil.getJavaNameOfDeclaration(decl);
            fail();
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void testv() throws Exception {
        final Declaration decl = findDecl("v.ceylon", "v");
        assertEquals("com.redhat.ceylon.compiler.java.codegen.v_.get_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testg() throws Exception {
        final Declaration decl = findDecl("g.ceylon", "g");
        assertEquals("com.redhat.ceylon.compiler.java.codegen.g_.get_", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testCm() throws Exception {
        final Declaration decl = findDecl("Cm.ceylon", "Cm.m");
        assertEquals("com.redhat.ceylon.compiler.java.codegen.Cm.m", CodegenUtil.getJavaNameOfDeclaration(decl));
    }
    
    @Test
    public void testCa() throws Exception {
        final Declaration decl = findDecl("Ca.ceylon", "Ca.a");
        assertEquals("com.redhat.ceylon.compiler.java.codegen.Ca.getA", CodegenUtil.getJavaNameOfDeclaration(decl));
    }

    @Test
    public void testAmbiguousAttributeNames(){
        assertFalse("", Naming.isAmbiguousGetterName(""));
        assertFalse("my", Naming.isAmbiguousGetterName("my"));
        assertFalse("myR", Naming.isAmbiguousGetterName("myR"));
        assertFalse("myURL", Naming.isAmbiguousGetterName("myURL"));
        assertFalse("myURLInput", Naming.isAmbiguousGetterName("myURLInput"));
        assertFalse("myURLInput", Naming.isAmbiguousGetterName("myURLInput"));
        assertTrue("mR", Naming.isAmbiguousGetterName("mR"));
        assertTrue("mURL", Naming.isAmbiguousGetterName("mURL"));
        assertTrue("mURLInput", Naming.isAmbiguousGetterName("mURLInput"));
        assertTrue("mURLInput", Naming.isAmbiguousGetterName("mURLInput"));
    }
}
