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
package org.eclipse.ceylon.compiler.java.codegen;

import org.eclipse.ceylon.model.loader.NamingBase.Prefix;
import org.eclipse.ceylon.model.typechecker.model.Parameter;

/**
 * A parameter to an annotation constructor, 
 * recording information about its default argument.
 *
 */
public class AnnotationConstructorParameter implements AnnotationFieldName {

    private Parameter parameter;
    
    private AnnotationTerm defaultArgument;

    public AnnotationConstructorParameter() {}
    
    /**
     * The corresponding parameter of the annotation constructor {@code Function}
     * @return
     */
    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public AnnotationTerm getDefaultArgument() {
        return defaultArgument;
    }

    public void setDefaultArgument(AnnotationTerm defaultArgument) {
        this.defaultArgument = defaultArgument;
    }
    
    @Override
    public String toString() {
        return parameter.getName() + (defaultArgument != null ? "=" + defaultArgument : "");
    }

    @Override
    public String getFieldName() {
        return parameter.getName();
    }
    
    @Override
    public Prefix getFieldNamePrefix() {
        return Prefix.$default$;
    }

    @Override
    public Parameter getAnnotationField() {
        return getParameter();
    }
}
