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
@nomodel
class OptionalInterface(JavaOptionalInterface x) satisfies JavaOptionalInterface {
    shared actual JavaOptionalInterface method(JavaOptionalInterface x){
        Object o1 = x.prop1;
        Object o2 = x.method(x);
        
        x.prop1 := x;
        x.prop1 := null;
        
        return x.prop1.prop1;
    }
    shared actual JavaOptionalInterface? method2(JavaOptionalInterface? x){
        if(exists x){
            if (exists z = x.prop1) {}
            Boolean b1 = exists x.prop1;
            JavaOptionalInterface[] existsInComprehension1 = {for (obj in {x}) if (exists z = obj.prop1) z};

            if (exists z = x.method(x)) {}
            Boolean b2 = exists x.method(x);
            JavaOptionalInterface[] existsInComprehension2 = {for (obj in {x}) if (exists z = obj.method(x)) z};

            return x.prop1.prop1;
        }
        return null;
    }
    
    shared actual variable JavaOptionalInterface prop1 := x;
    shared actual variable JavaOptionalInterface? prop2 := null;

    shared actual JavaOptionalInterface prop3 = x;
    shared actual JavaOptionalInterface? prop4 = x;
}