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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.java.util.Util;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;

public class IssuesTest extends CompilerTest {
    
    @Test
    public void testBug41(){
        compile("Bug41.ceylon");
        List<String> options = new ArrayList<String>();
        options.addAll(defaultOptions);
        options.add("-verbose");
        options.add("-cp");
        // If this test is failing, make sure you have done "ant publish"
        // of ceylon.language
        options.add(dir+File.pathSeparator+getModuleArchive("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, Util.getHomeRepository()));
        Boolean result = getCompilerTask(options, "Bug41_2.ceylon").call();
        Assert.assertEquals("Compilation worked", Boolean.TRUE, result);
    }

    @Test
    public void testBug111(){
        compareWithJavaSource("Bug111");
    }
    
    @Test
    public void testBug151(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug151", "Bug151.ceylon");
    }
    
    @Test
    public void testBug192(){
        compareWithJavaSource("Bug192");
    }

    @Test
    public void testBug193(){
        compareWithJavaSource("Bug193");
    }

    @Test
    public void testBug224(){
        compareWithJavaSource("Bug224");
    }
    
    @Test
    public void testBug227(){
        compareWithJavaSource("Bug227");
    }
    
    @Test
    public void testBug233(){
        compile("Bug233_Java.java", "Bug233_Type.java");
        compareWithJavaSource("Bug233");
    }
    
    @Test
    public void testBug241(){
        compareWithJavaSource("Bug241");
    }
    
    @Test
    public void testBug242(){
        compareWithJavaSource("Bug242");
    }
    
    @Test
    public void testBug247(){
        compareWithJavaSource("Bug247");
    }

    @Test
    public void testBug248(){
        compareWithJavaSource("Bug248");
    }

    @Test
    public void testBug249(){
        compareWithJavaSource("Bug249");
    }

    @Test
    public void testBug253(){
        compareWithJavaSource("Bug253");
    }

    @Test
    public void testBug260(){
        compareWithJavaSource("Bug260");
    }

    @Test
    public void testBug261(){
        compareWithJavaSource("Bug261");
    }

    @Test
    public void testBug269(){
        compareWithJavaSource("Bug269");
    }

    @Test
    public void testBug270(){
        compareWithJavaSource("Bug270");
    }
    
    @Test
    public void testBug283() {
        compareWithJavaSource("Bug283");
    }
    
    @Test
    public void testBug291(){
        compareWithJavaSource("Bug291");
    }
    
    @Test
    public void testBug298(){
        compareWithJavaSource("Bug298");
    }
    
    @Test
    public void testBug299(){
        compareWithJavaSource("Bug299");
    }
    
    @Test
    public void testBug311(){
        compareWithJavaSource("assert/Bug311");
    }

    @Test
    public void testBug313(){
        compareWithJavaSource("Bug313");
    }

    @Test
    public void testBug323(){
        compareWithJavaSource("Bug323");
    }

    @Test
    public void testBug324(){
        compareWithJavaSource("Bug324");
    }

    @Test
    public void testBug327(){
        compareWithJavaSource("Bug327");
    }

    @Test
    public void testBug329(){
        compareWithJavaSource("Bug329");
    }

    // we can't test this anymore now that the typechecker chokes on `is Foo<X>`, so we wait for reified
    // generics to test it back
    @Ignore("M5")
    @Test
    public void testBug330(){
        // compile them both at the same time
        compile("Bug330_1.ceylon", "Bug330_2.ceylon");
        // compile them individually, loading the other half from the .car
        compile("Bug330_1.ceylon");
        compile("Bug330_2.ceylon");
    }

    @Test
    public void testBug353(){
        // compile them both at the same time
        compile("Bug353_1.ceylon", "Bug353_2.ceylon");
    }

    @Test
    public void testBug366(){
        compareWithJavaSource("Bug366");
    }

    @Test
    public void testBug399(){
        compile("Bug399.ceylon");
    }

    @Test
    public void testBug404(){
        compareWithJavaSource("Bug404");
    }

    @Test
    public void testBug406(){
        compareWithJavaSource("Bug406");
    }

    @Test
    public void testBug407(){
        // make sure we don't get an NPE error
        assertErrors("Bug407", new CompilerError(25, "specified expression must be assignable to declared type: Set<Map<String,Integer>.Entry<String,Integer>> is not assignable to Iterable<unknown>"));
    }

    @Test
    public void testBug410(){
        compareWithJavaSource("Bug410");
    }

    @Test
    public void testBug441(){
        compareWithJavaSource("Bug441");
    }
    
    @Test
    public void testBug445(){
        compareWithJavaSource("Bug445");
    }

    @Test
    public void testBug446(){
        compareWithJavaSource("Bug446");
    }

    @Test
    public void testBug457(){
        compareWithJavaSource("Bug457");
    }

    @Test
    public void testBug458(){
        compile("bug458/a/module.ceylon", "bug458/a/package.ceylon", "bug458/a/a.ceylon");
        compile("bug458/b/module.ceylon", "bug458/b/b.ceylon");
    }

    @Test
    public void testBug475(){
        compareWithJavaSource("Bug475");
    }

    @Test
    public void testBug476(){
        compareWithJavaSource("Bug476");
    }

    @Test
    public void testBug477(){
        compareWithJavaSource("Bug477");
    }

    @Test
    public void testBug479(){
        compareWithJavaSource("Bug479");
    }

    @Test
    public void testBug490(){
        compareWithJavaSource("Bug490");
    }
    
    @Test
    public void testBug493(){
        compareWithJavaSource("bug493/module");
    }

    @Test
    public void testBug494(){
        compareWithJavaSource("Bug494");
    }
    
    @Test
    public void testBug504(){
        compile("Bug504.ceylon");
    }
    
    @Test
    public void testBug508(){
        compareWithJavaSource("Bug508");
    }
    
    @Test
    public void testBug509(){
        compareWithJavaSource("Bug509");
    }
    
    @Test
    public void testBug510(){
        compareWithJavaSource("bug510/Bug510");
    }

    @Test
    public void testBug517(){
        compareWithJavaSource("Bug517");
    }

    @Test
    public void testBug518(){
        compareWithJavaSource("Bug518");
    }

    @Test
    public void testBug526(){
        compareWithJavaSource("Bug526");
    }

    @Test
    public void testBug533(){
        compareWithJavaSource("Bug533");
    }
    
    @Test
    public void testBug540(){
        compareWithJavaSource("Bug540");
    }
    
    @Test
    public void testBug544(){
        compareWithJavaSource("Bug544");
    }
    
    @Test
    public void testBug548() throws IOException{
        compile("bug548/Bug548_1.ceylon");
        compile("bug548/Bug548_2.ceylon");
    }
    
    @Test
    public void testBug552(){
        compareWithJavaSource("Bug552");
    }
    
    @Test
    public void testBug568(){
        compareWithJavaSource("Bug568");
    }

    @Test
    public void testBug569(){
        compile("bug569/module.ceylon", "bug569/Foo.ceylon", "bug569/z/Bar.ceylon");
    }
    
    @Test
    public void testBug586(){
        compareWithJavaSource("Bug586");
    }
    
    @Test
    public void testBug588(){
        compareWithJavaSource("Bug588");
    }
    
    @Test
    public void testBug589(){
        compareWithJavaSource("Bug589");
    }
    
    @Test
    public void testBug591(){
        compile("bug591/Bug591_1.ceylon", "bug591/Bug591_2.ceylon");
        compile("bug591/Bug591_2.ceylon");
    }

    @Test
    public void testBug592(){
        compilesWithoutWarnings("Bug592.ceylon");
    }

    @Test
    public void testBug593(){
        assertErrors("Bug593",
                new CompilerError(27, "argument must be assignable to parameter arg1 of newFileSystem: HashMap<String,Object> is not assignable to Map<String,Object>?"));
    }

    @Test
    public void testBug594(){
        compareWithJavaSource("Bug594");
    }

    @Test
    public void testBug597(){
        compareWithJavaSource("Bug597");
    }

    @Test
    public void testBug601(){
        compareWithJavaSource("Bug601");
    }
    
    @Test
    public void testBug604(){
        compareWithJavaSource("Bug604");
    }
    
    @Test
    public void testBug605(){
        compareWithJavaSource("bug605/Bug605");
    }
    
    @Test
    public void testBug606(){
        compareWithJavaSource("Bug606");
    }
    
    @Test
    public void testBug607(){
        compareWithJavaSource("Bug607");
    }
    
    @Test
    public void testBug608(){
        compareWithJavaSource("Bug608");
    }
    
    @Test
    public void testBug609(){
        compareWithJavaSource("bug609/Bug609");
    }
    
    @Test
    public void testBug615(){
        compareWithJavaSource("Bug615");
    }

    @Test
    public void testBug616(){
        compareWithJavaSource("Bug616");
    }

    @Test
    public void testBug620(){
        compareWithJavaSource("Bug620");
    }

    @Test
    public void testBug623(){
        compareWithJavaSource("Bug623");
    }
    
    @Test
    public void testBug626(){
        compareWithJavaSource("Bug626");
    }

    @Test
    public void testBug627(){
        compareWithJavaSource("Bug627");
    }

    @Test
    public void testBug630(){
        compareWithJavaSource("Bug630a");
        compareWithJavaSource("Bug630b");
    }

    @Test
    public void testBug639(){
        compareWithJavaSource("Bug639");
    }

    @Test
    public void testBug633(){
        compareWithJavaSource("Bug633");
    }

    @Test
    public void testBug640(){
        compareWithJavaSource("bug640/Bug640");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug640.bug640", "bug640/Bug640.ceylon");
    }

    @Test
    public void testBug641(){
        compareWithJavaSource("Bug641");
    }

    @Test
    public void testBug646(){
        compareWithJavaSource("Bug646");
    }

    @Test
    public void testBug648(){
        compareWithJavaSource("Bug648");
    }
    
    @Test
    public void testBug655(){
        compareWithJavaSource("Bug655");
    }
    
    @Test
    public void testBug657(){
        compareWithJavaSource("Bug657");
    }
    
    @Test
    public void testBug660(){
        compareWithJavaSource("Bug660");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug660", "Bug660.ceylon");
    }

    @Test
    public void testBug667_fail(){
        compareWithJavaSource("Bug667");
    }

    @Test
    public void testBug669() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug669_testAssertEquals", "Bug669.ceylon");
    }
    
    @Test
    public void testBug671(){
        compareWithJavaSource("Bug671");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug671", "Bug671.ceylon");
    }
    
    @Test
    public void testBug673() {
        compareWithJavaSource("Bug673");
    }
    
    @Test 
    public void testBug674() {
        compareWithJavaSource("Bug674");
    }
    
    @Test
    public void testBug675(){
        compareWithJavaSource("Bug675");
    }
    
    @Test
    public void testBug676() {
        compareWithJavaSource("Bug676");
    }
    
    @Test
    public void testBug687() {
        compareWithJavaSource("Bug687");
    }
    
    @Test
    public void testBug689() {
        compile("Bug689_ModelLoader.ceylon");
        compareWithJavaSource("Bug689");
    }

    @Test
    public void testBug690_fail() {
        compile("Bug690_2.ceylon", "Bug690_1.ceylon");
    }
    
    @Test
    public void testBug693_fail() {
        compareWithJavaSource("Bug693");
    }
    
    @Test
    public void testBug699() {
        compareWithJavaSource("Bug699");
    }

    @Test
    public void testBug709() {
        compareWithJavaSource("Bug709");
    }
    
    @Test
    public void testBug711(){
        compile("bug711/InterfaceWithGetter.java", "bug711/ClassWithGetterAndSetter.java");
        compareWithJavaSource("bug711/Bug711");
    }

    @Test
    public void testBug715() {
        compile("Bug715.ceylon");
    }

    @Test
    public void testBug731() {
        compile("Bug731.ceylon");
    }
}

