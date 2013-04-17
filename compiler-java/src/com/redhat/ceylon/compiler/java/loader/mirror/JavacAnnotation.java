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
package com.redhat.ceylon.compiler.java.loader.mirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.compiler.loader.mirror.AnnotationMirror;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.Pair;

public class JavacAnnotation implements AnnotationMirror {

    private Compound annotation;

    private Map<String, Object> attributes;
    
    public JavacAnnotation(Compound annotation) {
        this.annotation = annotation;
        attributes = new HashMap<String, Object>();
    }

    @Override
    public Object getValue(String fieldName) {
        Object result = attributes.get(fieldName);
        if (result == null) {
            Attribute attr = member(fieldName);
            result = attributeToRefl(attr);
            attributes.put(fieldName, result);
        }
        return result;
    }

    private Object attributeToRefl(Attribute attr) {
        if(attr == null)
            return null;
        if(attr instanceof Attribute.Constant)
            return attr.getValue();
        if(attr instanceof Attribute.Array){
            Attribute[] values = ((Attribute.Array)attr).values;
            List<Object> list = new ArrayList<Object>(values.length);
            for(Attribute elem : values){
                list.add(attributeToRefl(elem));
            }
            return list;
        }
        if(attr instanceof Attribute.Compound)
            return new JavacAnnotation((Compound) attr);
        if(attr instanceof Attribute.Enum)
            return ((Attribute.Enum)attr).getValue().name.toString();
        if(attr instanceof Attribute.Class)
            return new JavacType(((Attribute.Class)attr).getValue());
        // FIXME: turn into error
        throw new RuntimeException("Unknown attribute type: "+attr);
    }

    private Attribute member(String fieldName) {
        // copied from Attribute in order not to drag Names all the way down here
        for (Pair<MethodSymbol,Attribute> pair : annotation.values)
            if (pair.fst.name.toString().equals(fieldName)) return pair.snd;
        return null;
    }

    @Override
    public Object getValue() {
        return getValue("value");
    }

}
