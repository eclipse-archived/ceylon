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

import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;
import com.redhat.ceylon.compiler.typechecker.TypeChecker;

public class IssuesTest extends CompilerTest {

    protected String getDestCar() {
        return getModuleArchive("com.redhat.ceylon.compiler.java.test.issues", "1").getPath();
    }
    
    @Test
    public void testBug41() throws IOException {
        compile("bug00xx/Bug41.ceylon");
        List<String> options = new ArrayList<String>();
        options.addAll(defaultOptions);
        options.add("-verbose");
        options.add("-cp");
        // If this test is failing, make sure you have done "ant publish"
        // of ceylon.language
        String repoDir = Repositories.get().getUserRepoDir().getCanonicalPath();
        options.add(dir+File.pathSeparator+getModuleArchive("ceylon.language", TypeChecker.LANGUAGE_MODULE_VERSION, repoDir));
        Boolean result = getCompilerTask(options, "bug00xx/Bug41_2.ceylon").call();
        Assert.assertEquals("Compilation worked", Boolean.TRUE, result);
    }

    @Test
    public void testBug111(){
        compareWithJavaSource("bug01xx/Bug111");
    }
    
    @Test
    public void testBug151(){
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug01xx.bug151", "bug01xx/Bug151.ceylon");
    }
    
    @Test
    public void testBug192(){
        compareWithJavaSource("bug01xx/Bug192");
    }

    @Test
    public void testBug193(){
        compareWithJavaSource("bug01xx/Bug193");
    }

    @Test
    public void testBug202(){
        compareWithJavaSource("bug02xx/Bug202");
    }
    
    @Test
    public void testBug224(){
        compareWithJavaSource("bug02xx/Bug224");
    }
    
    @Test
    public void testBug227(){
        compareWithJavaSource("bug02xx/Bug227");
    }
    
    @Test
    public void testBug233(){
        compile("bug02xx/Bug233_Java.java", "bug02xx/Bug233_Type.java");
        compareWithJavaSource("bug02xx/Bug233");
    }
    
    @Test
    public void testBug241(){
        compareWithJavaSource("bug02xx/Bug241");
    }
    
    @Test
    public void testBug242(){
        compareWithJavaSource("bug02xx/Bug242");
    }
    
    @Test
    public void testBug247(){
        compareWithJavaSource("bug02xx/Bug247");
    }

    @Test
    public void testBug248(){
        compareWithJavaSource("bug02xx/Bug248");
    }

    @Test
    public void testBug249(){
        compareWithJavaSource("bug02xx/Bug249");
    }

    @Test
    public void testBug253(){
        compareWithJavaSource("bug02xx/Bug253");
    }

    @Test
    public void testBug260(){
        compareWithJavaSource("bug02xx/Bug260");
    }

    @Test
    public void testBug261(){
        compareWithJavaSource("bug02xx/Bug261");
    }

    @Test
    public void testBug269(){
        compareWithJavaSource("bug02xx/Bug269");
    }

    @Test
    public void testBug270(){
        compareWithJavaSource("bug02xx/Bug270");
    }
    
    @Test
    public void testBug283() {
        compareWithJavaSource("bug02xx/Bug283");
    }
    
    @Test
    public void testBug291(){
        compareWithJavaSource("bug02xx/Bug291");
    }
    
    @Test
    public void testBug298(){
        compareWithJavaSource("bug02xx/Bug298");
    }
    
    @Test
    public void testBug299(){
        compareWithJavaSource("bug02xx/Bug299");
    }
    
    @Test
    public void testBug311(){
        compareWithJavaSource("bug03xx/assert/Bug311");
    }

    @Test
    public void testBug313(){
        compareWithJavaSource("bug03xx/Bug313");
    }

    @Test
    public void testBug323(){
        compareWithJavaSource("bug03xx/Bug323");
    }

    @Test
    public void testBug324(){
        compareWithJavaSource("bug03xx/Bug324");
    }

    @Test
    public void testBug327(){
        compareWithJavaSource("bug03xx/Bug327");
    }

    @Test
    public void testBug329(){
        compareWithJavaSource("bug03xx/Bug329");
    }

    // we can't test this anymore now that the typechecker chokes on `is Foo<X>`, so we wait for reified
    // generics to test it back
    @Ignore("M5: needs reified generics")
    @Test
    public void testBug330(){
        // compile them both at the same time
        compile("bug03xx/Bug330_1.ceylon", "bug03xx/Bug330_2.ceylon");
        // compile them individually, loading the other half from the .car
        compile("bug03xx/Bug330_1.ceylon");
        compile("bug03xx/Bug330_2.ceylon");
    }

    @Test
    public void testBug353(){
        // compile them both at the same time
        compile("bug03xx/Bug353_1.ceylon", "bug03xx/Bug353_2.ceylon");
    }

    @Test
    public void testBug366(){
        compareWithJavaSource("bug03xx/Bug366");
    }

    @Test
    public void testBug399(){
        compile("bug03xx/Bug399.ceylon");
    }

    @Test
    public void testBug404(){
        compareWithJavaSource("bug04xx/Bug404");
    }

    @Test
    public void testBug406(){
        compareWithJavaSource("bug04xx/Bug406");
    }

    @Test
    public void testBug407(){
        // make sure we don't get an NPE error
        assertErrors("bug04xx/Bug407", new CompilerError(25, "specified expression must be assignable to declared type: Set<Map<String,Integer>.Entry<String,Integer>> is not assignable to {unknown...}"));
    }

    @Test
    public void testBug410(){
        compareWithJavaSource("bug04xx/Bug410");
    }
    
    @Test
    public void testBug420(){
        compile("bug04xx/Bug420.ceylon");
    }

    @Test
    public void testBug441(){
        compareWithJavaSource("bug04xx/Bug441");
    }
    
    @Test
    public void testBug445(){
        compareWithJavaSource("bug04xx/Bug445");
    }

    @Test
    public void testBug446(){
        compareWithJavaSource("bug04xx/Bug446");
    }

    @Test
    public void testBug457(){
        compareWithJavaSource("bug04xx/Bug457");
    }

    @Test
    public void testBug458(){
        compile("bug04xx/bug458/a/module.ceylon", "bug04xx/bug458/a/package.ceylon", "bug04xx/bug458/a/a.ceylon");
        compile("bug04xx/bug458/b/module.ceylon", "bug04xx/bug458/b/b.ceylon");
    }

    @Test
    public void testBug475(){
        compareWithJavaSource("bug04xx/Bug475");
    }

    @Test
    public void testBug476(){
        compareWithJavaSource("bug04xx/Bug476");
    }

    @Test
    public void testBug477(){
        compareWithJavaSource("bug04xx/Bug477");
    }

    @Test
    public void testBug479(){
        compareWithJavaSource("bug04xx/Bug479");
    }

    @Test
    public void testBug490(){
        compareWithJavaSource("bug04xx/Bug490");
    }
    
    @Test
    public void testBug493(){
        compareWithJavaSource("bug04xx/bug493/module");
    }

    @Test
    public void testBug494(){
        compareWithJavaSource("bug04xx/Bug494");
    }
    
    @Test
    public void testBug504(){
        compile("bug05xx/Bug504.ceylon");
    }
    
    @Test
    public void testBug508(){
        compareWithJavaSource("bug05xx/Bug508");
    }
    
    @Test
    public void testBug509(){
        compareWithJavaSource("bug05xx/Bug509");
    }
    
    @Test
    public void testBug510(){
        compareWithJavaSource("bug05xx/bug510/Bug510");
    }

    @Test
    public void testBug517(){
        compareWithJavaSource("bug05xx/Bug517");
    }

    @Test
    public void testBug518(){
        compareWithJavaSource("bug05xx/Bug518");
    }

    @Test
    public void testBug526(){
        compareWithJavaSource("bug05xx/Bug526");
    }

    @Test
    public void testBug533(){
        compareWithJavaSource("bug05xx/Bug533");
    }
    
    @Test
    public void testBug540(){
        compareWithJavaSource("bug05xx/Bug540");
    }
    
    @Test
    public void testBug544(){
        compareWithJavaSource("bug05xx/Bug544");
    }
    
    @Test
    public void testBug548() throws IOException{
        compile("bug05xx/bug548/Bug548_1.ceylon");
        compile("bug05xx/bug548/Bug548_2.ceylon");
    }
    
    @Test
    public void testBug552(){
        compareWithJavaSource("bug05xx/Bug552");
    }
    
    @Test
    public void testBug568(){
        compareWithJavaSource("bug05xx/Bug568");
    }

    @Test
    public void testBug569(){
        compile("bug05xx/bug569/module.ceylon", "bug05xx/bug569/Foo.ceylon", "bug05xx/bug569/z/Bar.ceylon");
    }
    
    @Test
    public void testBug586(){
        compareWithJavaSource("bug05xx/Bug586");
    }
    
    @Test
    public void testBug588(){
        compareWithJavaSource("bug05xx/Bug588");
    }
    
    @Test
    public void testBug589(){
        compareWithJavaSource("bug05xx/Bug589");
    }
    
    @Test
    public void testBug591(){
        compile("bug05xx/bug591/Bug591_1.ceylon", "bug05xx/bug591/Bug591_2.ceylon");
        compile("bug05xx/bug591/Bug591_2.ceylon");
    }

    @Test
    public void testBug592(){
        compilesWithoutWarnings("bug05xx/Bug592.ceylon");
    }

    @Test
    public void testBug593(){
        assertErrors("bug05xx/Bug593",
                new CompilerError(27, "argument must be assignable to parameter arg1 of newFileSystem: HashMap<String,Object> is not assignable to Map<String,Object>?"));
    }

    @Test
    public void testBug594(){
        compareWithJavaSource("bug05xx/Bug594");
    }

    @Test
    public void testBug597(){
        compareWithJavaSource("bug05xx/Bug597");
    }

    @Test
    public void testBug601(){
        compareWithJavaSource("bug06xx/Bug601");
    }
    
    @Test
    public void testBug604(){
        compareWithJavaSource("bug06xx/Bug604");
    }
    
    @Test
    public void testBug605(){
        compareWithJavaSource("bug06xx/bug605/Bug605");
    }
    
    @Test
    public void testBug606(){
        compareWithJavaSource("bug06xx/Bug606");
    }
    
    @Test
    public void testBug607(){
        compareWithJavaSource("bug06xx/Bug607");
    }
    
    @Test
    public void testBug608(){
        compareWithJavaSource("bug06xx/Bug608");
    }
    
    @Test
    public void testBug609(){
        compareWithJavaSource("bug06xx/bug609/Bug609");
    }
    
    @Test
    public void testBug615(){
        compareWithJavaSource("bug06xx/Bug615");
    }

    @Test
    public void testBug616(){
        compareWithJavaSource("bug06xx/Bug616");
    }

    @Test
    public void testBug620(){
        compareWithJavaSource("bug06xx/Bug620");
    }

    @Test
    public void testBug623(){
        compareWithJavaSource("bug06xx/Bug623");
    }
    
    @Test
    public void testBug626(){
        compareWithJavaSource("bug06xx/Bug626");
    }

    @Test
    public void testBug627(){
        compareWithJavaSource("bug06xx/Bug627");
    }

    @Test
    public void testBug630(){
        compareWithJavaSource("bug06xx/Bug630a");
        compareWithJavaSource("bug06xx/Bug630b");
    }

    @Test
    public void testBug639(){
        compareWithJavaSource("bug06xx/Bug639");
    }

    @Test
    public void testBug633(){
        compareWithJavaSource("bug06xx/Bug633");
    }

    @Test
    public void testBug640(){
        compareWithJavaSource("bug06xx/bug640/Bug640");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug06xx.bug640.bug640", "bug06xx/bug640/Bug640.ceylon");
    }

    @Test
    public void testBug641(){
        compareWithJavaSource("bug06xx/Bug641");
    }

    @Test
    public void testBug646(){
        compareWithJavaSource("bug06xx/Bug646");
    }

    @Test
    public void testBug648(){
        compareWithJavaSource("bug06xx/Bug648");
    }
    
    @Test
    public void testBug655(){
        compareWithJavaSource("bug06xx/Bug655");
    }
    
    @Test
    public void testBug657(){
        compareWithJavaSource("bug06xx/Bug657");
    }
    
    @Test
    public void testBug660(){
        compareWithJavaSource("bug06xx/Bug660");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug06xx.bug660", "bug06xx/Bug660.ceylon");
    }

    @Test
    public void testBug667(){
        compareWithJavaSource("bug06xx/Bug667");
    }

    @Test
    public void testBug668(){
        compareWithJavaSource("bug06xx/Bug668");
    }

    @Test
    public void testBug669() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug06xx.bug669_testAssertEquals", "bug06xx/Bug669.ceylon");
    }
    
    @Test
    public void testBug671(){
        compareWithJavaSource("bug06xx/Bug671");
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug06xx.bug671", "bug06xx/Bug671.ceylon");
    }
    
    @Test
    public void testBug673() {
        compareWithJavaSource("bug06xx/Bug673");
    }
    
    @Test 
    public void testBug674() {
        compareWithJavaSource("bug06xx/Bug674");
    }
    
    @Test
    public void testBug675(){
        compareWithJavaSource("bug06xx/Bug675");
    }
    
    @Test
    public void testBug676() {
        compareWithJavaSource("bug06xx/Bug676");
    }
    
    // fails pending figuring out https://github.com/ceylon/ceylon-spec/issues/465
    @Test
    public void testBug687_fail() {
        compareWithJavaSource("bug06xx/Bug687");
    }
    
    @Test
    public void testBug689() {
        compile("bug06xx/Bug689_ModelLoader.ceylon");
        compareWithJavaSource("bug06xx/Bug689");
    }

    @Test
    public void testBug690() {
        compile("bug06xx/Bug690_2.ceylon", "bug06xx/Bug690_1.ceylon");
    }
    
    @Test
    public void testBug693() {
        compareWithJavaSource("bug06xx/Bug693");
    }
    
    @Test
    public void testBug699() {
        compareWithJavaSource("bug06xx/Bug699");
    }

    @Test
    public void testBug702() {
        compile("bug07xx/Bug702.ceylon");
    }

    @Ignore("M5: https://github.com/ceylon/ceylon-compiler/issues/706")
    @Test
    public void testBug706_fail() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug07xx.bug706", "bug07xx/Bug706.ceylon");
    }
    
    @Test
    public void testBug708() {
        compareWithJavaSource("bug07xx/Bug708");
        compareWithJavaSource("bug07xx/Bug708_2");
    }
    
    @Test
    public void testBug709() {
        compareWithJavaSource("bug07xx/Bug709");
    }
    
    @Test
    public void testBug711(){
        compile("bug07xx/bug711/InterfaceWithGetter.java", "bug07xx/bug711/ClassWithGetterAndSetter.java");
        compareWithJavaSource("bug07xx/bug711/Bug711");
    }

    @Test
    public void testBug712() {
        compile("bug07xx/Bug712.ceylon");
    }
    
    @Test
    public void testBug713() {
        assertErrors("bug07xx/Bug713",
                new CompilerError(26, "member does not have the same number of parameters as the member it refines: getComponentAt"));
    }
    
    @Test
    public void testBug714_fail() {
        compareWithJavaSource("bug07xx/Bug714");
    }
    
    @Test
    public void testBug715() {
        compile("bug07xx/Bug715.ceylon");
    }
    
    @Test
    public void testBug722() {
        compile("bug07xx/Bug722.ceylon");
    }
    
    @Test
    public void testBug724() {
        compile("bug07xx/Bug724.ceylon");
    }

    @Test
    public void testBug730() {
        compile("bug07xx/Bug730.ceylon");
    }
    
    @Test
    public void testBug731() {
        compile("bug07xx/Bug731.ceylon");
    }

    @Test
    public void testBug747() {
        compile("bug07xx/Bug747Java.java");
        compareWithJavaSource("bug07xx/Bug747");
    }

    @Test
    public void testBug751() {
        assertErrors("bug07xx/Bug751", new CompilerError(20, "package not found in dependent modules: laknsd.askduyasjd"));
    }

    @Test
    public void testBug774() {
        compareWithJavaSource("bug07xx/Bug774");
    }

    @Test
    public void testBug776() {
        compile("bug07xx/Bug776.ceylon");
    }

    @Test
    public void testBug781() {
        compareWithJavaSource("bug07xx/Bug781");
    }

    @Test
    public void testBug782() {
        compile("bug07xx/Bug782.ceylon");
    }

    @Test
    public void testBug784() {
        compile("bug07xx/Bug784.ceylon");
    }

    @Test
    public void testBug785() {
        compile("bug07xx/Bug785.ceylon");
    }

    @Test
    public void testBug796() {
        compile("bug07xx/Bug796_1.ceylon");
        compile("bug07xx/Bug796_2.ceylon");
    }

    @Test
    public void testBug797() {
        compile("bug07xx/Bug797.ceylon");
    }

    @Test
    public void testBug799() {
        compareWithJavaSource("bug07xx/Bug799");
    }

    @Test
    public void testBug801() {
        compareWithJavaSource("bug08xx/Bug801");
    }

    @Test
    public void testBug803() {
        compareWithJavaSource("bug08xx/Bug803");
    }
    
    @Test
    public void testBug816() {
        compareWithJavaSource("bug08xx/Bug816");
    }

    @Test
    public void testBug817() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug08xx.bug817.bug817", "bug08xx/bug817/NullArray.java", "bug08xx/bug817/Bug817.ceylon");
    }

    @Test
    public void testBug821() {
        compareWithJavaSource("bug08xx/Bug821");
    }
    
    @Test
    public void testBug823() {
        compareWithJavaSource("bug08xx/Bug823");
    }
    
    @Test
    public void testBug824() {
        compareWithJavaSource("bug08xx/Bug824");
    }

    @Test
    public void testBug825() {
        compile("bug08xx/Bug825.ceylon");
        compile("bug08xx/Bug825.ceylon");
    }
    
    @Test
    public void testBug832() {
        compareWithJavaSource("bug08xx/Bug832");
    }
    

    @Test
    public void testBug834() {
        compile("bug08xx/Bug834.ceylon");
        compile("bug08xx/Bug834.ceylon");
    }
    
    @Test
    public void testBug837() {
        compareWithJavaSource("bug08xx/Bug837");
    }
    
    @Test
    public void testBug843() {
        compareWithJavaSource("bug08xx/Bug843");
    }
    
    @Test
    public void testBug844() {
        compareWithJavaSource("bug08xx/Bug844");
    }

    @Test
    public void testBug850(){
        compile("bug08xx/bug850/Bug850.java");
        compareWithJavaSource("bug08xx/bug850/Bug850");
    }
    
    @Test
    public void testBug854(){
        compile("bug08xx/bug854/Bug854.java");
        compareWithJavaSource("bug08xx/bug854/Bug854");
    }
    
    @Test
    public void testBug866() {
        compareWithJavaSource("bug08xx/Bug866");
    }

    @Test
    public void testBug870() {
        compareWithJavaSource("bug08xx/Bug870");
    }

    @Test
    public void testBug871() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug08xx.bug871", "bug08xx/Bug871.ceylon");
    }

    @Test
    public void testBug872() {
        compareWithJavaSource("bug08xx/Bug872");
    }

    @Test
    public void testBug889() {
        compareWithJavaSource("bug08xx/Bug889");
        compareWithJavaSource("bug08xx/Bug889_2");
    }
    
    @Test
    public void testBug891() {
        compareWithJavaSource("bug08xx/Bug891");
    }

    @Test
    public void testBug893() {
        compareWithJavaSource("bug08xx/Bug893");
    }

    @Test
    public void testBug897() {
        compareWithJavaSource("bug08xx/Bug897");
    }

    @Test
    public void testBug898() {
        compareWithJavaSource("bug08xx/Bug898");
    }

    @Test
    public void testBug899() {
        compareWithJavaSource("bug08xx/Bug899");
    }

    @Test
    public void testBug902() {
        compareWithJavaSource("bug09xx/Bug902");
    }
    
    @Test
    public void testBug903_fail() {
        compareWithJavaSource("bug09xx/Bug903");
    }

    @Test
    public void testBug911() {
        compareWithJavaSource("bug09xx/Bug911");
    }

    @Test
    public void testBug919() {
        compareWithJavaSource("bug09xx/Bug919");
    }

    @Test
    public void testBug920() {
        compareWithJavaSource("bug09xx/Bug920");
    }

    @Test
    public void testBug929_fail() {
        compile("bug09xx/Bug929.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug929_1");
        run("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug929_2");
        run("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug929_3");
    }
    
    @Test
    public void testBug929_super_fail() {
        compile("bug09xx/Bug929_super.ceylon");
        run("com.redhat.ceylon.compiler.java.test.issues.bug09xx.Bug929_superSub");
    }

    @Test
    public void testBug931() {
        compareWithJavaSource("bug09xx/Bug931");
    }
}

