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

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;

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
                new CompilerError(24, "ambiguous reference to overloaded method or class: BigInteger"));
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
    public void testBug1156() {
        compile("bug11xx/Bug1156.java", "bug11xx/Bug1156.ceylon");
    }

    @Ignore("To resolve for M6: https://github.com/ceylon/ceylon-compiler/issues/1155")
    @Test
    public void testBug1155() {
        //compareWithJavaSource("bug11xx/Bug1155");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1155", 
                "bug11xx/Bug1155.ceylon");
    }
    
    @Ignore("To resolve for M6: https://github.com/ceylon/ceylon-compiler/issues/1157")
    @Test
    public void testBug1157() {
        compareWithJavaSource("bug11xx/Bug1157");
    }
    
    @Test
    public void testBug1161() {
        compareWithJavaSource("bug11xx/Bug1161");
    }
    
    @Ignore("To resolve for M6: https://github.com/ceylon/ceylon-compiler/issues/1165")
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
    public void testBug1184() {
        compareWithJavaSource("bug11xx/Bug1184");
    }
    
    @Test
    public void testBug1188() {
        compareWithJavaSource("bug11xx/Bug1188");
    }
    
    @Test
    public void testBug1203_fail() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug11xx.bug1203", "bug11xx/Bug1203.ceylon");
    }
    
    @Test
    public void testBug1204() {
        compareWithJavaSource("bug11xx/Bug1204");
    }
    
    @Test
    public void testBug1206() {
        compareWithJavaSource("bug11xx/Bug1206");
    }

    @Test
    public void testBug1207() {
        compareWithJavaSource("bug11xx/Bug1207");
    }
}
