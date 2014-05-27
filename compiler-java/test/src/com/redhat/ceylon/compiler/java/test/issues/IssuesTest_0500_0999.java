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

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.test.CompilerError;
import com.redhat.ceylon.compiler.java.test.CompilerTest;

public class IssuesTest_0500_0999 extends CompilerTest {

    @Override
    protected ModuleWithArtifact getDestModuleWithArtifact(){
        return new ModuleWithArtifact("com.redhat.ceylon.compiler.java.test.issues", "1");
    }

    @Override
    protected String transformDestDir(String name) {
        return name + "-0500-0999";
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
    public void testBug509NoOpt(){
        compareWithJavaSourceNoOpt("bug05xx/Bug509");
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
    public void testBug568NoOpt(){
        compareWithJavaSourceNoOpt("bug05xx/Bug568");
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
    public void testBug588NoOpt(){
        compareWithJavaSourceNoOpt("bug05xx/Bug588");
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
    public void testBug616NoOpt(){
        compareWithJavaSourceNoOpt("bug06xx/Bug616");
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
    public void testBug641(){
        compareWithJavaSource("bug06xx/Bug641");
    }

    @Test
    public void testBug646(){
        compareWithJavaSource("bug06xx/Bug646");
    }
    
    @Test
    public void testBug647(){
        compareWithJavaSource("bug06xx/Bug647");
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
    public void testBug675NoOpt(){
        compareWithJavaSourceNoOpt("bug06xx/Bug675");
    }
    
    @Test
    public void testBug676() {
        compareWithJavaSource("bug06xx/Bug676");
    }
    
    @Test
    public void testBug687() {
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

    @Test
    public void testBug706() {
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
        compareWithJavaSource("bug07xx/Bug713");
    }
    
    @Ignore("M6: https://github.com/ceylon/ceylon-compiler/issues/714")
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
        assertErrors("bug07xx/Bug751", new CompilerError(20, "package not found in imported modules: laknsd.askduyasjd (add module import to module descriptor of com.redhat.ceylon.compiler.java.test.issues)"));
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
    
    @Ignore("Disabled because of https://github.com/ceylon/ceylon-spec/issues/596")
    @Test
    public void testBug866() {
        compareWithJavaSource("bug08xx/Bug866");
    }

    @Test
    public void testBug867() {
        compareWithJavaSource("bug08xx/Bug867");
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
    public void testBug895() {
        compareWithJavaSource("bug08xx/Bug895");
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
    public void testBug903() {
        compareWithJavaSource("bug09xx/Bug903");
    }

    @Test
    public void testBug904() {
        compareWithJavaSource("bug09xx/Bug904");
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
    public void testBug931() {
        compareWithJavaSource("bug09xx/Bug931");
    }

    @Test
    public void testBug934() {
        compareWithJavaSource("bug09xx/Bug934");
    }
    
    @Test
    public void testBug934NoOpt() {
        compareWithJavaSourceNoOpt("bug09xx/Bug934");
    }

    @Test
    public void testBug935() {
        compareWithJavaSource("bug09xx/Bug935");
    }
    
    @Test
    public void testBug935NoOpt() {
        compareWithJavaSourceNoOpt("bug09xx/Bug935");
    }

    @Test
    public void testBug940() {
        compareWithJavaSource("bug09xx/Bug940");
    }

    @Test
    public void testBug949() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug949", "bug09xx/Bug949.ceylon");
    }

    @Test
    public void testBug950() {
        assertErrors("bug09xx/Bug950",
                new CompilerError(34, "member foo is inherited ambiguously by Bug950_Bottom from Bug950_Left and another subtype of Bug950_Top and so must be refined by Bug950_Bottom"),
                new CompilerError(34, "may not inherit two declarations with the same name unless redefined in subclass: foo is defined by supertypes Bug950_Left and Bug950_Right"));

    }

    @Test
    public void testBug953() {
        compareWithJavaSource("bug09xx/Bug953");
    }
    
    @Test
    public void testBug954() {
        compareWithJavaSource("bug09xx/Bug954");
    }
    
    @Test
    public void testBug955() {
        compareWithJavaSource("bug09xx/Bug955");
    }

    @Test
    public void testBug963() {
        compareWithJavaSource("bug09xx/Bug963");
    }
    
    @Test
    public void testBug965() {
        compareWithJavaSource("bug09xx/Bug965");
    }
    
    @Test
    public void testBug966() {
        compareWithJavaSource("bug09xx/Bug966");
    }
    
    @Test
    public void testBug973() {
        compareWithJavaSource("bug09xx/Bug973");
    }

    @Test
    public void testBug974() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug09xx.Bug974", "bug09xx/Bug974.ceylon");
    }
    
    @Test
    public void testBug975() {
        compareWithJavaSource("bug09xx/Bug975_1");
        compareWithJavaSource("bug09xx/Bug975_2");
    }

    @Test
    public void testBug976() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug976", "bug09xx/Bug976.ceylon");
    }

    @Test
    public void testBug978() {
        compareWithJavaSource("bug09xx/Bug978");
    }

    @Test
    public void testBug979() {
        compareWithJavaSource("bug09xx/Bug979");
    }

    @Test
    public void testBug985() {
        compareWithJavaSource("bug09xx/Bug985");
    }
    
    @Test
    public void testBug986() {
        compileAndRun("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug986", "bug09xx/Bug986.ceylon");
    }
    
    @Test
    public void testBug991() {
        compareWithJavaSource("bug09xx/Bug991");
    }

    @Test
    public void testBug993() {
        compareWithJavaSource("bug09xx/Bug993");
    }

    @Test
    public void testBug994() {
        compareWithJavaSource("bug09xx/Bug994");
    }

    @Test
    public void testBug995() {
        compareWithJavaSource("bug09xx/Bug995");
    }

    @Test
    public void testBug996() {
        compareWithJavaSource("bug09xx/Bug996");
        run("com.redhat.ceylon.compiler.java.test.issues.bug09xx.bug996");
    }

    @Test
    public void testBug997() {
        compareWithJavaSource("bug09xx/Bug997");
    }

    @Test
    public void testBug998() {
        compareWithJavaSource("bug09xx/Bug998");
    }

    @Test
    public void testBug999() {
        compareWithJavaSource("bug09xx/Bug999");
    }
}

