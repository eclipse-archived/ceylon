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
package org.eclipse.ceylon.compiler.java.test.interop.access;

public class JavaBug2019 {
    public String getPubProt() { // or package private
        return "";
    }
    protected void setPubProt(String prop) {
    }
    
    public String getPubDef() { // or package private
        return "";
    }
    void setPubDef(String prop) {
    }
    
    public String getPubPriv() { // or package private
        return "";
    }
    private void setPubPriv(String prop) {
    }
    
    
    protected String getProtPub() { // or package private
        return "";
    }
    public void setProtPub(String prop) {
    }
    
    protected String getProtDef() { // or package private
        return "";
    }
    void setProtDef(String prop) {
    }
    
    protected String getProtPriv() { // or package private
        return "";
    }
    private void setProtPriv(String prop) {
    }
    
    String getDefPub() { // or package private
        return "";
    }
    public void setDefPub(String prop) {
    }
    
    String getDefProt() { // or package private
        return "";
    }
    protected void setDefProt(String prop) {
    }
    
    String getDefPriv() { // or package private
        return "";
    }
    private void setDefPriv(String prop) {
    }
    
    
    private String getPrivPub() { // or package private
        return "";
    }
    public void setPrivPub(String prop) {
    }
    
    private String getPrivProt() { // or package private
        return "";
    }
    protected void setPrivProt(String prop) {
    }
    
    private String getPrivDef() { // or package private
        return "";
    }
    void setPrivDef(String prop) {
    }
}
