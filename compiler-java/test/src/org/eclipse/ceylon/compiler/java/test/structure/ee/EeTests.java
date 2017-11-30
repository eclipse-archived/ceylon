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
package org.eclipse.ceylon.compiler.java.test.structure.ee;

import org.eclipse.ceylon.compiler.java.test.CompilerTests;
import org.junit.Test;

public class EeTests extends CompilerTests {

    
    @Test
    public void testNoFinalMethods() {
        compareWithJavaSource("NoFinalMethods");
    }
    
    @Test
    public void testPublicImplicitCtor() {
        compareWithJavaSource("PublicImplicitCtor");
    }
    
    @Test
    public void testUncheckedLate() {
        compareWithJavaSource("UncheckedLate");
    }
    
    @Test
    public void testJavaBoxes() {
        compareWithJavaSource("JavaBoxes");
    }
    
    @Test
    public void testJavaCollectionBoxes() {
        compareWithJavaSource("JavaCollectionBoxes");
    }
    
    @Test
    public void testEeModeEnabling() {
        compareWithJavaSource("enabledModule/Class");
        compareWithJavaSource("enabledModuleMaven/Class");
        compareWithJavaSource("enabledPackage/Class.src",
                "enabledPackage/Class.ceylon",
                "enabledPackage/package.ceylon");
        compareWithJavaSource("enabledClass/enabledClass");
    }
    
    @Test
    public void testEntityExample() {
        // don't want no stinkin' warnings about late and things not being properly initialized
        compilesWithoutWarnings("EntityExample.ceylon");
        compareWithJavaSource("EntityExample");
    }
}
