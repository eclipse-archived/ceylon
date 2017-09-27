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
import ceylon.language.meta.declaration{
    ClassDeclaration
}

typeOrConstructorTarget__CONSTRUCTOR
class AmbiguousClass(s2,s3) {
    
    
    fieldOrMethodTarget__FIELD
    shared String s = "";
    
    
    parameterOrFieldTarget__PARAMETER
    String s2;
    
    
    parameterOrFieldOrMethodTarget__PARAMETER
    shared String s3;
}

// Not supported anymore since https://github.com/ceylon/ceylon-compiler/issues/2109 made java constraints visible
//typeOrConstructorTarget__CONSTRUCTOR
fieldOrMethodTarget__FIELD
object ambiguousObject {}


typeOrAnnotationTypeTarget__ANNOTATION_TYPE
final annotation class AmbiguousAnnotationClass() satisfies OptionalAnnotation<AmbiguousAnnotationClass, ClassDeclaration> {}