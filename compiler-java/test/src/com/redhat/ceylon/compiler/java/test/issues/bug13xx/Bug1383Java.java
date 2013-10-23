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

package com.redhat.ceylon.compiler.java.test.issues.bug13xx;

public class Bug1383Java {
    public boolean hash;
    public boolean string;
    public boolean equals;
    
    public boolean hash(){ return true; }
    public boolean hash(String param){ return true; }
    public boolean string(){ return true; }
    public boolean string(String param){ return true; }
    public boolean equals(){ return true; }
    public boolean equals(String param){ return true; }
    
    public boolean getHash(){ return true; }
    public void setHash(boolean b){}

    public boolean getString(){ return true; }
    public void setString(boolean b){}

    public boolean getEquals(){ return true; }
    public void setEquals(boolean b){}
}
