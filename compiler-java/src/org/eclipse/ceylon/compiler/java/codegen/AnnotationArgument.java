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
 * An argument in an annotation invocation
 */
public class AnnotationArgument implements AnnotationFieldName {

    private AnnotationTerm term;
    private Parameter parameter;
    public AnnotationArgument() {
        
    }
    /**
     * The value of the argument
     */
    public AnnotationTerm getTerm() {
        return term;
    }
    public void setTerm(AnnotationTerm term) {
        this.term = term;
    }
    /**
     * The formal parameter corresponding to this argument.
     */
    public Parameter getParameter() {
        return parameter;
    }
    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
    @Override
    public String toString() {
        return parameter.getName()+ " = " + term; 
    }
    @Override
    public String getFieldName() {
        return getParameter().getName();
    }
    @Override
    public Prefix getFieldNamePrefix() {
        return Prefix.$arg$;
    }
    @Override
    public Parameter getAnnotationField() {
        return getParameter();
    }
}
