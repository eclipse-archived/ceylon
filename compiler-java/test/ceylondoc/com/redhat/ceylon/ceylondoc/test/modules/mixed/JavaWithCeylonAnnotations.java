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
package com.redhat.ceylon.ceylondoc.test.modules.mixed;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

import ceylon.language.DocAnnotation$annotation$;

@Ceylon(major = 8)
public class JavaWithCeylonAnnotations {
    
    @DocAnnotation$annotation$(description = "foo1")
    @TypeInfo("ceylon.language::Boolean")
    public boolean foo() {
        return true;
    }
    
    @DocAnnotation$annotation$(description = "foo2")
    @TypeInfo("ceylon.language::Boolean")
    public boolean foo(
            final @TypeInfo("ceylon.language::Integer") @Name("p1") @DocAnnotation$annotation$(description = "p1") long p1) {
        return true;
    }
    
    @DocAnnotation$annotation$(description = "foo3")
    @TypeInfo("ceylon.language::Boolean")
    public boolean foo(
            final @TypeInfo("ceylon.language::Integer") @Name("p1") @DocAnnotation$annotation$(description = "p1") long p1,
            final @TypeInfo("ceylon.language::String") @Name("p2") @DocAnnotation$annotation$(description = "p2") ceylon.language.String p2) {
        return true;
    }

}