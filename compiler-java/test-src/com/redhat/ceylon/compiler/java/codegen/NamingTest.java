package com.redhat.ceylon.compiler.java.codegen;

import static com.redhat.ceylon.compiler.java.codegen.CodegenUtil.declName;
import static com.redhat.ceylon.compiler.java.codegen.CodegenUtil.NameFlag.COMPANION;
import static com.redhat.ceylon.compiler.java.codegen.CodegenUtil.NameFlag.QUALIFIED;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.cmr.api.Logger;
import com.redhat.ceylon.cmr.api.RepositoryManager;
import com.redhat.ceylon.cmr.api.RepositoryManagerBuilder;
import com.redhat.ceylon.compiler.java.codegen.LocalId;
import com.redhat.ceylon.compiler.typechecker.context.Context;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnit;
import com.redhat.ceylon.compiler.typechecker.context.PhasedUnits;
import com.redhat.ceylon.compiler.typechecker.io.VFS;
import com.redhat.ceylon.compiler.typechecker.model.Declaration;
import com.redhat.ceylon.compiler.typechecker.model.Scope;

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
        File file = new File("test-src", name);
        if (!file.exists()) {
            throw new RuntimeException("Unable to find resource " + name);
        }
        RepositoryManagerBuilder builder = new RepositoryManagerBuilder(new NullLogger());
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

    private static final LocalId lid = new LocalId() {
        @Override
        public String getLocalId(Scope d) {
            return "0";
        }
    };
    
    @Test
    public void testC() throws Exception {
        final Declaration decl = findDecl("C.ceylon", "C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testCC() throws Exception {
        final Declaration decl = findDecl("CC.ceylon", "CC.C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "CC.C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "CC.C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testCI() throws Exception {
        final Declaration decl = findDecl("CI.ceylon", "CI.I");
        Assert.assertEquals("CI$I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "CI$I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "CI.I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testCo() throws Exception {
        final Declaration decl = findDecl("Co.ceylon", "Co.o");
        Assert.assertEquals("o", declName(lid, decl));
        Assert.assertEquals("o$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "Co.o", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "Co.o$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testI() throws Exception {
        final Declaration decl = findDecl("I.ceylon", "I");
        Assert.assertEquals("I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testIC() throws Exception {
        final Declaration decl = findDecl("IC.ceylon", "IC.C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "IC$impl.C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "IC$impl.C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testII() throws Exception {
        final Declaration decl = findDecl("II.ceylon", "II.I");
        Assert.assertEquals("II$I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "II$I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "II$impl.I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testIo() throws Exception {
        final Declaration decl = findDecl("Io.ceylon", "Io.o");
        Assert.assertEquals("o", declName(lid, decl));
        Assert.assertEquals("o$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "Io$impl.o", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "Io$impl.o$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testo() throws Exception {
        final Declaration decl = findDecl("o.ceylon", "o");
        Assert.assertEquals("o", declName(lid, decl));
        Assert.assertEquals("o$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "o", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "o$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testoC() throws Exception {
        final Declaration decl = findDecl("oC.ceylon", "oC.C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "oC.C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "oC.C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testoI() throws Exception {
        final Declaration decl = findDecl("oI.ceylon", "oI.I");
        Assert.assertEquals("oI$I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "oI$I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "oI.I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testoo() throws Exception {
        final Declaration decl = findDecl("oo.ceylon", "oo.o");
        Assert.assertEquals("o", declName(lid, decl));
        Assert.assertEquals("o$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "oo.o", declName(lid, decl, QUALIFIED));
        Assert.assertEquals(QUAL + "oo.o$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfC() throws Exception {
        final Declaration decl = findDecl("fC.ceylon", "fC.C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals("C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfCC() throws Exception {
        final Declaration decl = findDecl("fCC.ceylon", "fCC.CC.C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals("CC.C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("CC.C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfCI() throws Exception {
        final Declaration decl = findDecl("fCI.ceylon", "fCI.CI.I");
        Assert.assertEquals("fCI$0$CI$I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "fCI$0$CI$I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("CI.I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfI() throws Exception {
        final Declaration decl = findDecl("fI.ceylon", "fI.I");
        Assert.assertEquals("fI$0$I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "fI$0$I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfIC() throws Exception {
        final Declaration decl = findDecl("fIC.ceylon", "fIC.IC.C");
        Assert.assertEquals("C", declName(lid, decl));
        Assert.assertEquals("C$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals("IC$impl.C", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("IC$impl.C$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfII() throws Exception {
        final Declaration decl = findDecl("fII.ceylon", "fII.II.I");
        Assert.assertEquals("fII$0$II$I", declName(lid, decl));
        Assert.assertEquals("I$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals(QUAL + "fII$0$II$I", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("II$impl.I$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }
    
    @Test
    public void testfo() throws Exception {
        final Declaration decl = findDecl("fo.ceylon", "fo.o");
        Assert.assertEquals("o", declName(lid, decl));
        Assert.assertEquals("o$impl", declName(lid, decl, COMPANION));
        Assert.assertEquals("o", declName(lid, decl, QUALIFIED));
        Assert.assertEquals("o$impl", declName(lid, decl, COMPANION, QUALIFIED));
    }

}
