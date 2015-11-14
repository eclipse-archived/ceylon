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

package com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024b;

public class Bug2024B<T>
// FIXED:
//    <T extends com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant>
// FIXED:
// <T extends Bug2024B<? extends com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant>>
// FIXED:
// extends com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant
{
    // FIXED:
    //    com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    // this works
    //    Object i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    // this works
    //    {
    //        com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    }
    // this works
    //  static {
    //      com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //  }
    // FIXED:
    //    com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant m(){
    //        return null;
    //    }
    // FIXED:
    Bug2024B<com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant> m(){
        return null;
    }

    // FIXED:
    //    void m(com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i){
    //    }

    // FIXED:
    //    Bug2024B(com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i){
    //    }

    // this works
    //    void m(){
    //        com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant(); 
    //    }

    // this works
    //    class C{
    //        com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //    }

    // this works
    //    void m(){
    //        class C{
    //            com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant i = new com.redhat.ceylon.compiler.java.test.issues.bug20xx.bug2024a.Instant();
    //        }
    //    }
}
