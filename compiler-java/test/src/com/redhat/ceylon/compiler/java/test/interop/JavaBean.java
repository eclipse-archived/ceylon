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
package com.redhat.ceylon.compiler.java.test.interop;

public class JavaBean {
    public boolean getBooleanWithGet(){ return false; }
    public void setBooleanWithGet(boolean b){}

    public boolean isBooleanWithIs(){ return false; }
    public void setBooleanWithIs(boolean b){}
    
    public boolean oldStyle(){ return false; }
    public void setOldStyle(boolean b){}
    
    public String getURL(){ return null; }
    public void setURL(String url){}

    public String getURLEncoderForHTML(){ return null; }
    public void setURLEncoderForHTML(String url){}
    
    public boolean getConfusedProperty(){ return false; }
    public void setConfusedProperty(String str){}
    
    public long getÉpardaud(){ throw new RuntimeException("I am not a number, I am a free man!"); }
    public void setÉpardaud(long l){}
    
    public long getConfusedAttr1(){ return 1; }
    // we pick this one because it matches the setter
    public boolean isConfusedAttr1() { return false; }
    public void setConfusedAttr1(boolean b){}

    // we pick this one because it matches the setter 
    public long getConfusedAttr2(){ return 1; }
    public boolean isConfusedAttr2() { return false; }
    public void setConfusedAttr2(long l){}

    public boolean getConfusedAttr3(){ return false; }
    // we pick this one because for booleans it's preferred 
    public boolean isConfusedAttr3() { return false; }
    // we pick that one too
    public void setConfusedAttr3(boolean b){}
    public void setConfusedAttr3(long l){}

    // we pick this one because we prefer getters otherwise 
    public long getConfusedAttr4(){ return 1; }
    public boolean isConfusedAttr4() { return false; }
    public void setConfusedAttr4(boolean b){}
    // we pick that one too
    public void setConfusedAttr4(long l){}
}
