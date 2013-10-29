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
package com.redhat.ceylon.compiler.java.test.issues;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.tools.Diagnostic;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.test.ErrorCollector;
import com.redhat.ceylon.compiler.java.tools.CeyloncTaskImpl;


public class IssuesTest_1000_1499 extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }
    
    @Override
    protected String transformDestDir(String name) {
        return name + "-1000-1499";
    }

    @Test
    public void testBug1000() {
        compile("bug10xx/Bug1000.ceylon");
    }
    
    @Test
    public void testBug1001() {
        compareWithJavaSource("bug10xx/Bug1001");
    }

    @Test
    public void testBug1007() {
        compareWithJavaSource("bug10xx/Bug1007");
    }

    @Test
    public void testBug1011() {
        compareWithJavaSource("bug10xx/Bug1011");
    }

    @Test
    public void testBug1016() {
        compareWithJavaSource("bug10xx/Bug1016");
    }
    
    @Test
    public void testBug1024() {
        compareWithJavaSource("bug10xx/Bug1024");
    }
    
    @Test
    public void testBug1026() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug10xx.bug1026", "bug10xx/Bug1026.ceylon");
    }
    
    @Test
    public void testBug1029() {
        compareWithJavaSource("bug10xx/Bug1029");
    }
    
    @Ignore("Disabled because of https://github.com/ceylon/ceylon-spec/issues/596")
    @Test
    public void testBug1036() {
        compareWithJavaSource("bug10xx/Bug1036");
    }
    
    @Test
    public void testBug1037() {
        compile("bug10xx/bug1037/Bug1037Java.java");
    }
    
    @Test
    public void testBug1041() {
        compile("bug10xx/bug1041/Bug1041Java.java");
        compareWithJavaSource("bug10xx/bug1041/Bug1041");
    }
    
    @Test
    public void testBug1042() {
        compareWithJavaSource("bug10xx/Bug1042");
    }
    
    @Test
    public void testBug1043() {
        compareWithJavaSource("bug10xx/Bug1043");
    }

    @Test
    public void testBug1059() {
        compareWithJavaSource("bug10xx/Bug1059");
    }
    
    @Test
    public void testBug1064() {
        compile("bug10xx/Bug1064.ceylon");
    }

    @Test
    public void testBug1067() {
        compareWithJavaSource("bug10xx/Bug1067");
    }
    
    @Test
    public void testBug1071() {
        compile("bug10xx/Bug1071.ceylon");
    }
    
    @Test
    public void testBug1079() {
        compareWithJavaSource("bug10xx/Bug1079");
    }

    @Test
    public void testBug1080() {
        compareWithJavaSource("bug10xx/Bug1080");
    }

    @Test
    public void testBug1083() {
        assertErrors("bug10xx/Bug1083",
                new CompilerError(24, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of BigInteger that accepts the given argument types ()"));
    }
    
    @Test
    public void testBug1089() {
        compareWithJavaSource("bug10xx/Bug1089");
        run("com.redhat.ceylon.compiler.java.test.issues.bug10xx.bug1089");
    }

    @Test
    public void testBug1095() {
        compareWithJavaSource("bug10xx/Bug1095");
    }
    
    @Test
    public void testBug1095B() {
        compareWithJavaSource("bug10xx/Bug1095B");
    }
    
    @Test
    public void testBug1106() {
        compareWithJavaSource("bug11xx/Bug1106");
    }
    
    @Test
    public void testBug1108() {
        compareWithJavaSource("bug11xx/Bug1108");
    }
    
    @Test
    public void testBug1113() {
        compareWithJavaSource("bug11xx/Bug1113");
    }

    @Test
    public void testBug1114() {
        compareWithJavaSource("bug11xx/Bug1114");
    }

    @Test
    public void testBug1116() {
        compareWithJavaSource("bug11xx/Bug1116");
    }

    @Test
    public void testBug1117() {
        compareWithJavaSource("bug11xx/Bug1117");
    }

    @Test
    public void testBug1119() {
        compareWithJavaSource("bug11xx/Bug1119");
    }

    @Test
    public void testBug1120() {
        compareWithJavaSource("bug11xx/Bug1120");
    }

    @Test
    public void testBug1124() {
        compareWithJavaSource("bug11xx/Bug1124");
    }

    @Test
    public void testBug1127() {
        compareWithJavaSource("bug11xx/Bug1127");
    }

    @Test
    public void testBug1134() {
        compareWithJavaSource("bug11xx/Bug1134");
    }

    @Test
    public void testBug1132() {
        compareWithJavaSource("bug11xx/Bug1132");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1132_testX", "bug11xx/Bug1132_2.ceylon");
    }

    @Test
    public void testBug1133() {
        compareWithJavaSource("bug11xx/Bug1133");
    }

    @Test
    public void testBug1135() {
        compareWithJavaSource("bug11xx/Bug1135");
    }
    
    @Test
    public void testBug1148() {
        compareWithJavaSource("bug11xx/Bug1148");
    }
    
    @Test
    public void testBug1151() {
        compareWithJavaSource("bug11xx/Bug1151");
        run("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1151_callsite");
    }
    
    @Test
    public void testBug1152() {
        compareWithJavaSource("bug11xx/Bug1152");
    }

    @Test
    public void testBug1153() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.Bug1153", "bug11xx/Bug1153.ceylon");
    }
    
    @Test
    public void testBug1154() {
        compareWithJavaSource("bug11xx/Bug1154");
    }

    @Test
    public void testBug1155() {
        //compareWithJavaSource("bug11xx/Bug1155");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1155", 
                "bug11xx/Bug1155.ceylon");
    }
    
    @Test
    public void testBug1155_B() {
        compareWithJavaSource("bug11xx/Bug1155_B");
    }
    
    @Test
    public void testBug1155_D() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1155_D", 
                "bug11xx/Bug1155_D.ceylon");
    }
    
    @Test
    public void testBug1156() {
        compile("bug11xx/Bug1156.java", "bug11xx/Bug1156.ceylon");
    }

    @Test
    public void testBug1157() {
        compareWithJavaSource("bug11xx/Bug1157");
    }
    
    @Test
    public void testBug1161() {
        compareWithJavaSource("bug11xx/Bug1161");
    }
    
    @Test
    public void testBug1165() {
        compareWithJavaSource("bug11xx/Bug1165");
    }
    
    @Test
    public void testBug1174() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1174_callsite", "bug11xx/Bug1174.ceylon");
    }
    
    @Test
    public void testBug1177() {
        compareWithJavaSource("bug11xx/Bug1177");
    }
    
    @Test
    public void testBug1180() {
        compile("bug11xx/Bug1180_1.ceylon");
        assertErrors("bug11xx/Bug1180_2",
                new CompilerError(25, "ambiguous reference to overloaded method or class: there must be exactly one overloaded declaration of ArrayList that accepts the given argument types ({Bug1180Person*})"),
                new CompilerError(25, "class alias may not alias overloaded class")
        );
    }
    
    @Test
    public void testBug1184() {
        compareWithJavaSource("bug11xx/Bug1184");
    }
    
    @Test
    public void testBug1185() {
        compareWithJavaSource("bug11xx/Bug1185");
        run("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1185");
        assertErrors("bug11xx/Bug1185_errors",
                new CompilerError(3, "literal outside representable range: 9223372036854775808 is too large to be represented as an Integer"),
                new CompilerError(4, "literal outside representable range: -9223372036854775809 is too large to be represented as an Integer"),
                new CompilerError(5, "invalid hexadecimal literal: #10000000000000000 has more than 64 bits"),
                new CompilerError(6, "invalid binary literal: $10000000000000000000000000000000000000000000000000000000000000000 has more than 64 bits")
        );
    }
    
    @Test
    public void testBug1188() {
        compareWithJavaSource("bug11xx/Bug1188");
    }
    
    @Test
    public void testBug1203_fail() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug12xx.bug1203", "bug12xx/Bug1203.ceylon");
    }
    
    @Test
    public void testBug1204() {
        compareWithJavaSource("bug12xx/Bug1204");
    }
    
    @Test
    public void testBug1206() {
        compareWithJavaSource("bug12xx/Bug1206");
    }

    @Test
    public void testBug1207() {
        compareWithJavaSource("bug12xx/Bug1207");
    }

    @Test
    public void testBug1208() {
        compareWithJavaSource("bug12xx/Bug1208");
    }

    @Test
    public void testBug1211() {
        compareWithJavaSource("bug12xx/Bug1211");
    }

    @Test
    public void testBug1212() {
        compile("bug12xx/Bug1212_1.ceylon");
        compile("bug12xx/Bug1212_2.ceylon");
    }

    @Test
    public void testBug1219() {
        compareWithJavaSource("bug12xx/Bug1219");
    }
    
    @Ignore("https://github.com/ceylon/ceylon-compiler/issues/1221")
    @Test
    public void testBug1221() {
        compareWithJavaSource("bug12xx/Bug1221");
    }

    @Test
    public void testBug1225() {
        compareWithJavaSource("bug12xx/Bug1225");
    }
    
    @Test
    public void testBug1227() {
        compareWithJavaSource("bug12xx/Bug1227");
    }

    @Test
    public void testBug1235() {
        compareWithJavaSource("bug12xx/Bug1235");
    }

    @Test
    public void testBug1236() {
        compareWithJavaSource("bug12xx/Bug1236");
    }

    @Test
    public void testBug1238() {
        compile("bug12xx/Bug1238.java");
        compareWithJavaSource("bug12xx/Bug1238");
    }

    @Test
    public void testBug1239() {
        compareWithJavaSource("bug12xx/Bug1239");
    }

    @Test
    public void testBug1240() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug12xx.bug1240_1", "bug12xx/Bug1240_1.ceylon");
        compareWithJavaSource("bug12xx/Bug1240");
        run("com.redhat.ceylon.compiler.java.test.issues.bug12xx.bug1240");
    }

    @Test
    public void testBug1242() {
        compareWithJavaSource("bug12xx/Bug1242");
    }

    @Test
    public void testBug1241() {
        compareWithJavaSource("bug12xx/Bug1241");
    }

    @Test
    public void testBug1243() {
        compareWithJavaSource("bug12xx/Bug1243");
    }
    
    @Test
    public void testBug1250() {
        compareWithJavaSource("bug12xx/Bug1250");
    }
    
    @Test
    public void testBug1251() {
        compareWithJavaSource("bug12xx/Bug1251");
    }
    
    @Test
    public void testBug1255() {
        assertErrors("bug12xx/Bug1255",
                new CompilerError(29, "spread argument is not iterable: {String*}? is not a subtype of Iterable"),
                new CompilerError(29, "iterable element type could not be inferred")
                );
    }
    
    @Test
    public void testBug1256() {
        // both at once
        compile("bug12xx/Bug1256_1.ceylon", "bug12xx/Bug1256_2.ceylon");
        // incrementally
        compile("bug12xx/Bug1256_1.ceylon");
        compile("bug12xx/Bug1256_2.ceylon");
    }

    @Test
    public void testBug1257() {
        compareWithJavaSource("bug12xx/Bug1257");
    }

    @Test
    public void testBug1263() {
        compareWithJavaSource("bug12xx/Bug1263");
    }
    
    @Test
    @Ignore("M6")
    public void testBug1270() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug12xx.bug1270", "bug12xx/Bug1270.ceylon");
    }
    
    @Test
    public void testBug1272() {
        compile("bug12xx/Bug1272.ceylon");
    }
    
    @Test
    public void testBug1284() {
        compareWithJavaSource("bug12xx/Bug1284");
    }
    
    @Test
    public void testBug1285() {
        compareWithJavaSource("bug12xx/Bug1285");
    }
    
    @Test
    public void testBug1286() {
        compareWithJavaSource("bug12xx/Bug1286");
    }
    
    @Ignore("https://github.com/ceylon/ceylon-compiler/issues/1287")
    @Test
    public void testBug1287() {
        compareWithJavaSource("bug12xx/Bug1287");
    }
    
    @Test
    public void testBug1288() {
        compareWithJavaSource("bug12xx/Bug1288");
    }
    
    @Test
    public void testBug1289() {
        compareWithJavaSource("bug12xx/Bug1289");
    }
    
    @Test
    public void testBug1291() {
        compareWithJavaSource("bug12xx/Bug1291");
    }
    
    @Test
    public void testBug1292() {
        compareWithJavaSource("bug12xx/Bug1292");
    }
    
    @Test
    public void testBug1293() {
        compareWithJavaSource("bug12xx/Bug1293");
    }
    
    @Test
    public void testBug1311() {
        compareWithJavaSource("bug13xx/Bug1311");
    }
    
    @Test
    public void testBug1313() {
        compareWithJavaSource("bug13xx/Bug1313");
    }
    
    @Ignore("https://github.com/ceylon/ceylon-compiler/issues/1315")
    @Test
    public void testBug1315() {
        compile("bug13xx/bug1315/bug1315_1.ceylon");
        compile("bug13xx/bug1315/bug1315_2.ceylon");
    }
    
    @Test
    public void testBug1316() throws IOException {
        String subPath = "bug13xx/bug1316/source";
        String srcPath = getPackagePath()+subPath;
        List<String> options = new LinkedList<String>();
        options.add("-src");
        options.add(srcPath);
        options.addAll(defaultOptions);
        options.add("-continue");
        ErrorCollector c = new ErrorCollector();
        CeyloncTaskImpl task = getCompilerTask(options, 
                c,
                Arrays.asList("okmodule"),
                subPath + "/dupdeclerr.ceylon",
                subPath + "/someok.ceylon");
        Boolean ret = task.call();
        assertFalse(ret);

        TreeSet<CompilerError> actualErrors = c.get(Diagnostic.Kind.ERROR);
        compareErrors(actualErrors,
                new CompilerError(-1, "Duplicate declaration error: run_ is declared twice: once in RegularFileObject[test/src/com/redhat/ceylon/compiler/java/test/issues/bug13xx/bug1316/source/dupdeclerr.ceylon] and again in: another file"),
                new CompilerError(22, "duplicate declaration name: run"));
        
        File carFile = getModuleArchive("default", null);
        assertTrue(carFile.exists());

        JarFile car = new JarFile(carFile);

        ZipEntry moduleClass = car.getEntry("foobar_.class");
        assertNotNull(moduleClass);
        car.close();

        carFile = getModuleArchive("okmodule", "1.0.0");
        assertTrue(carFile.exists());

        car = new JarFile(carFile);

        moduleClass = car.getEntry("okmodule/module_.class");
        assertNotNull(moduleClass);
        car.close();
    }

    @Test
    public void testBug1328() {
        compareWithJavaSource("bug13xx/Bug1328");
    }

    @Test
    public void testBug1330() {
        compareWithJavaSource("bug13xx/Bug1330");
    }

    @Test
    public void testBug1332() {
        compareWithJavaSource("bug13xx/Bug1332");
    }

    @Test
    public void testBug1334() {
        compareWithJavaSource("bug13xx/Bug1334");
    }

    @Test
    public void testBug1336() {
        compareWithJavaSource("bug13xx/Bug1336");
    }

    @Test
    public void testBug1339() {
        compareWithJavaSource("bug13xx/bug1339/Bug1339");
    }

    @Test
    public void testBug1341() {
        compareWithJavaSource("bug13xx/bug1341/Bug1341");
    }
    
    @Test
    public void testBug1342() {
        compile("bug13xx/bug1342_.java");
    }
    
    @Test
    public void testBug1345() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug13xx.bug1345", "bug13xx/Bug1345.ceylon");
    }
    
    @Test
    public void testBug1351() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug13xx.bug1351", "bug13xx/Bug1351.ceylon");
    }

    @Test
    public void testBug1353() {
        compareWithJavaSource("bug13xx/Bug1353");
    }
    
    @Test
    public void testBug1358() {
        compareWithJavaSource("bug13xx/Bug1358");
    }
    
    @Test
    public void testBug1360() {
        compareWithJavaSource("bug13xx/bug1360/Bug1360");
    }
    
    @Test
    public void testBug1365() {
        compareWithJavaSource("bug13xx/Bug1365");
    }
    
    @Test
    public void testBug1370() {
        compareWithJavaSource("bug13xx/Bug1370");
    }

    @Test
    public void testBug1372() {
        ErrorCollector c = new ErrorCollector();
        String base = getPackagePath()+"/bug13xx/bug1372";
        Boolean success = getCompilerTask(Arrays.asList("-src", base+"/testSrc"+File.pathSeparator+base+"/src"),
                c, Arrays.asList("org.myapp")).call();
        Assert.assertTrue(c.getAssertionFailureMessage(), success);
    }

    @Test
    @Ignore("For 1.1 - See https://github.com/ceylon/ceylon-compiler/issues/1375")
    public void testBug1375() {
        assertErrors("bug13xx/Bug1375",
                new CompilerError(22, "does not definitely return: x"),
                new CompilerError(28, "cannot find symbol\n  symbol:   method x()\n  location: variable x of type com.redhat.ceylon.compiler.java.test.issues.bug13xx.Bug1375")
        );
    }

    @Test
    public void testBug1380() {
        compareWithJavaSource("bug13xx/Bug1380");
    }

    @Test
    public void testBug1381() {
        compareWithJavaSource("bug13xx/Bug1381");
    }
    
    @Test
    public void testBug1382() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug13xx.bug1382", "bug13xx/Bug1382.ceylon");
    }
    
    @Test
    public void testBug1385() {
        compareWithJavaSource("bug13xx/Bug1385");
    }

    @Test
    public void testBug1388() {
        compareWithJavaSource("bug13xx/Bug1388");
    }

    @Test
    public void testBug1395() {
        compareWithJavaSource("bug13xx/Bug1395");
    }

    @Test
    public void testBug1402() {
        compareWithJavaSource("bug14xx/Bug1402");
    }

    @Test
    public void testBug1403() {
        compareWithJavaSource("bug14xx/Bug1403");
    }
}
