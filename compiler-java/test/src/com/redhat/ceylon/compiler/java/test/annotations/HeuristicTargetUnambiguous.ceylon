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

"Targeting annotations on a class"
typeTarget
// Not supported anymore since https://github.com/ceylon/ceylon-compiler/issues/2109 made java constraints visible
//constructorTarget
class HeuristicTarget1() {
    
}

"Targeting annotations on parameters and attributes"
class HeuristicTarget2(
    parameterTarget
    fieldTarget
    methodTarget
    shared String s, s2) {
    
    parameterTarget
    fieldTarget
    methodTarget
    shared String s2;
    
    fieldTarget
    methodTarget
    shared variable String s3=s;
    
}
 
"Targeting annotations on object definitions"
// Not supported anymore since https://github.com/ceylon/ceylon-compiler/issues/2109 made java constraints visible
//typeTarget
//constructorTarget
fieldTarget
methodTarget
object heuristicTarget3 {
}

annotationTypeTarget
typeTarget
// Not supported anymore since https://github.com/ceylon/ceylon-compiler/issues/2109 made java constraints visible
//constructorTarget
final annotation class HeuristicAnnotationClass() 
        satisfies OptionalAnnotation<HeuristicAnnotationClass, ClassDeclaration> {
}
