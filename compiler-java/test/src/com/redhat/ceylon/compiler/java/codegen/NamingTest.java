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

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.compiler.java.tools.CeyloncFileManager;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Scope;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.sun.tools.javac.tree.TreeMaker;

public class NamingTest {
    
    private static final String PKGNAME = NamingTest.class.getPackage().getName();
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
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(new NullLogger(), false);
        RepositoryManager repoManager = builder.buildRepository();
        VFS vfs = new VFS();
        Context context = new Context(repoManager, vfs);
        PhasedUnits pus = new PhasedUnits(context);
        // Make the module manager think we're looking at this package
        // even though there's no module descriptor
        pus.getModuleManager().push(PKGNAME);
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
    
    protected TypeDeclaration findDecl(String resource, String declName) throws Exception {
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
        return (TypeDeclaration)found;
    }

    private final Naming naming;
    public NamingTest () {
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
        final TypeDeclaration decl = findDecl("C.ceylon", "C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals(QUAL + "C", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testCC() throws Exception {
        final TypeDeclaration decl = findDecl("CC.ceylon", "CC.C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals(QUAL + "CC.C", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testCI() throws Exception {
        final TypeDeclaration decl = findDecl("CI.ceylon", "CI.I");
        Assert.assertEquals("CI$I", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "CI$I", naming.declName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "CI.I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testCo() throws Exception {
        final TypeDeclaration decl = findDecl("Co.ceylon", "Co.o");
        Assert.assertEquals("o_", naming.declName(decl));
        Assert.assertEquals(QUAL + "Co.o_", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testI() throws Exception {
        final TypeDeclaration decl = findDecl("I.ceylon", "I");
        Assert.assertEquals("I", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "I", naming.declName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testIC() throws Exception {
        final TypeDeclaration decl = findDecl("IC.ceylon", "IC.C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals(QUAL + "IC$impl.C", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testII() throws Exception {
        final TypeDeclaration decl = findDecl("II.ceylon", "II.I");
        Assert.assertEquals("II$I", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "II$I", naming.declName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "II$impl.I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testIo() throws Exception {
        final TypeDeclaration decl = findDecl("Io.ceylon", "Io.o");
        Assert.assertEquals("o_", naming.declName(decl));
        Assert.assertEquals(QUAL + "Io$impl.o_", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testo() throws Exception {
        final TypeDeclaration decl = findDecl("o.ceylon", "o");
        Assert.assertEquals("o_", naming.declName(decl));
        Assert.assertEquals(QUAL + "o_", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testoC() throws Exception {
        final TypeDeclaration decl = findDecl("oC.ceylon", "oC.C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals(QUAL + "oC_.C", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testoI() throws Exception {
        final TypeDeclaration decl = findDecl("oI.ceylon", "oI.I");
        Assert.assertEquals("oI$I_", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "oI$I_", naming.declName(decl, QUALIFIED));
        Assert.assertEquals(QUAL + "oI_.I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testoo() throws Exception {
        final TypeDeclaration decl = findDecl("oo.ceylon", "oo.o");
        Assert.assertEquals("o_", naming.declName(decl));
        Assert.assertEquals(QUAL + "oo_.o_", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testfC() throws Exception {
        final TypeDeclaration decl = findDecl("fC.ceylon", "fC.C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals("C$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals("C", naming.declName(decl, QUALIFIED));
        Assert.assertEquals("C$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfCC() throws Exception {
        final TypeDeclaration decl = findDecl("fCC.ceylon", "fCC.CC.C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals("CC.C", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testfCI() throws Exception {
        final TypeDeclaration decl = findDecl("fCI.ceylon", "fCI.CI.I");
        Assert.assertEquals("fCI$0$CI$I_", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "fCI$0$CI$I_", naming.declName(decl, QUALIFIED));
        Assert.assertEquals("CI.I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfI() throws Exception {
        final TypeDeclaration decl = findDecl("fI.ceylon", "fI.I");
        Assert.assertEquals("fI$0$I_", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "fI$0$I_", naming.declName(decl, QUALIFIED));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfIC() throws Exception {
        final TypeDeclaration decl = findDecl("fIC.ceylon", "fIC.IC.C");
        Assert.assertEquals("C", naming.declName(decl));
        Assert.assertEquals("IC$impl.C", naming.declName(decl, QUALIFIED));
    }
    
    @Test
    public void testfII() throws Exception {
        final TypeDeclaration decl = findDecl("fII.ceylon", "fII.II.I");
        Assert.assertEquals("fII$0$II$I_", naming.declName(decl));
        Assert.assertEquals("I$impl", naming.declName(decl, COMPANION));
        Assert.assertEquals(QUAL + "fII$0$II$I_", naming.declName(decl, QUALIFIED));
        Assert.assertEquals("II$impl.I$impl", naming.declName(decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfo() throws Exception {
        final TypeDeclaration decl = findDecl("fo.ceylon", "fo.o");
        Assert.assertEquals("o_", naming.declName(decl));
        Assert.assertEquals("o$impl_", naming.declName(decl, COMPANION));
        Assert.assertEquals("o_", naming.declName(decl, QUALIFIED));
        Assert.assertEquals("o$impl_", naming.declName(decl, COMPANION, QUALIFIED));
    }

}
