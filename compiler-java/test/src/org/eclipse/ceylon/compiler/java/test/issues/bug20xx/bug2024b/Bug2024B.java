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

package org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024b;

public class Bug2024B<T>
// FIXED:
//    <T extends org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant>
// FIXED:
// <T extends Bug2024B<? extends org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant>>
// FIXED:
// extends org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant
{
    // FIXED:
    //    org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    // this works
    //    Object i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    // this works
    //    {
    //        org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    }
    // this works
    //  static {
    //      org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //  }
    // FIXED:
    //    org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant m(){
    //        return null;
    //    }
    // FIXED:
    Bug2024B<org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant> m(){
        return null;
    }

    // FIXED:
    //    void m(org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i){
    //    }

    // FIXED:
    //    Bug2024B(org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i){
    //    }

    // this works
    //    void m(){
    //        org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant(); 
    //    }

    // this works
    //    class C{
    //        org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    }

    // this works
    //    void m(){
    //        class C{
    //            org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new org.eclipse.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //        }
    //    }
}
