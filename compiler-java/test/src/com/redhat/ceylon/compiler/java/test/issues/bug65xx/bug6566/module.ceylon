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
native("jvm")
module com.redhat.ceylon.compiler.java.test.issues.bug65xx.bug6566 "1" {
    import java.base "8";
    import maven:"org.netbeans.api:org-netbeans-modules-editor-mimelookup" "RELEASE81";
    import maven:"org.netbeans.modules:org-netbeans-modules-editor-errorstripe" "RELEASE81";
    import maven:"org.netbeans.api:org-netbeans-modules-csl-api" "RELEASE81";
    import maven:"org.netbeans.api:org-openide-util" "RELEASE81";
    import maven:"org.netbeans.api:org-openide-util-lookup" "RELEASE81";
    import maven:"org.netbeans.api:org-netbeans-modules-lexer" "RELEASE81";
    import maven:"org.netbeans.api:org-openide-awt" "RELEASE81";
    import ceylon.collection "1.3.0";
}