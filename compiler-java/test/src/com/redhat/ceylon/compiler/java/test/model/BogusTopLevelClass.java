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

package com.redhat.ceylon.compiler.java.test.model;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 6)
@com.redhat.ceylon.compiler.java.metadata.Class(extendsType = "com.redhat.ceylon.compiler.java.test.model::MissingType")
@CaseTypes(of = "com.redhat.ceylon.compiler.java.test.model::MissingType")
@TypeParameters(@TypeParameter(value = "T", 
                               caseTypes = "com.redhat.ceylon.compiler.java.test.model::MissingType", 
                               satisfies = "com.redhat.ceylon.compiler.java.test.model::MissingType",
                               defaultValue = "com.redhat.ceylon.compiler.java.test.model::MissingType"))
public class BogusTopLevelClass<T> {
    @TypeInfo("com.redhat.ceylon.compiler.java.test.model::MissingType")
    public Object getGetter(){
        return null;
    }

    public long getSetter(){ return 1; } 
    
    public void setSetter(@TypeInfo("com.redhat.ceylon.compiler.java.test.model::MissingType") Object arg){
    }

    @TypeInfo("com.redhat.ceylon.compiler.java.test.model::MissingType")
    public Object method(@TypeInfo("com.redhat.ceylon.compiler.java.test.model::MissingType") Object arg){
        return null;
    }
    
    public <T> void params(){}
}
