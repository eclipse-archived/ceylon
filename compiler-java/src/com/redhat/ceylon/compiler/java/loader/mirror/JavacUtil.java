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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redhat.ceylon.model.loader.ModelResolutionException;
import com.redhat.ceylon.model.loader.mirror.AnnotationMirror;
import com.redhat.ceylon.model.loader.mirror.TypeParameterMirror;
import com.sun.tools.javac.code.Attribute.Compound;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.CompletionFailure;
import com.sun.tools.javac.code.Symbol.TypeSymbol;

public class JavacUtil {

    public static Map<String, AnnotationMirror> getAnnotations(Symbol symbol) {
        Map<String, AnnotationMirror> result;
        try{
            com.sun.tools.javac.util.List<Compound> annotations = symbol.getAnnotationMirrors();
            if(annotations.isEmpty()){
                result = Collections.<String,AnnotationMirror>emptyMap();
            }else{
                result = new HashMap<String, AnnotationMirror>(annotations.size());
                for(Compound annotation : annotations){
                    result.put(annotation.type.tsym.getQualifiedName().toString(), new JavacAnnotation(annotation));
                }
            }
        }catch(CompletionFailure x){
            // ignore, it will be logged somewhere else
            result = Collections.<String,AnnotationMirror>emptyMap();
        }
        return result;
    }

    public static List<TypeParameterMirror> getTypeParameters(Symbol symbol) {
        try{
            com.sun.tools.javac.util.List<TypeSymbol> typeParameters = symbol.getTypeParameters();
            List<TypeParameterMirror> ret = new ArrayList<TypeParameterMirror>(typeParameters.size());
            for(TypeSymbol typeParameter : typeParameters)
                ret.add(new JavacTypeParameter(typeParameter));
            return ret;
        }catch(CompletionFailure x){
            throw new ModelResolutionException("Failed to load type parameters", x);
        }
    }

}
