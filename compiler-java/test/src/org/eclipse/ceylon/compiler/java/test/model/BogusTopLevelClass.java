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

package org.eclipse.ceylon.compiler.java.test.model;

import org.eclipse.ceylon.compiler.java.metadata.CaseTypes;
import org.eclipse.ceylon.compiler.java.metadata.Ceylon;
import org.eclipse.ceylon.compiler.java.metadata.TypeInfo;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameter;
import org.eclipse.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 8)
@org.eclipse.ceylon.compiler.java.metadata.Class(extendsType = "org.eclipse.ceylon.compiler.java.test.model::MissingType")
@CaseTypes(of = "org.eclipse.ceylon.compiler.java.test.model::MissingType")
@TypeParameters(@TypeParameter(value = "T", 
                               caseTypes = "org.eclipse.ceylon.compiler.java.test.model::MissingType", 
                               satisfies = "org.eclipse.ceylon.compiler.java.test.model::MissingType",
                               defaultValue = "org.eclipse.ceylon.compiler.java.test.model::MissingType"))
public class BogusTopLevelClass<T> {
    @TypeInfo("org.eclipse.ceylon.compiler.java.test.model::MissingType")
    public Object getGetter(){
        return null;
    }

    public long getSetter(){ return 1; } 
    
    public void setSetter(@TypeInfo("org.eclipse.ceylon.compiler.java.test.model::MissingType") Object arg){
    }

    @TypeInfo("org.eclipse.ceylon.compiler.java.test.model::MissingType")
    public Object method(@TypeInfo("org.eclipse.ceylon.compiler.java.test.model::MissingType") Object arg){
        return null;
    }
    
    public <T> void params(){}
}
