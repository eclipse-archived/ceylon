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
import java.util { Map }

@noanno
void variance() {
    JavaVariance var = JavaVariance();
    // For M4: tested in Variance2
    Class<out Object> klass = var;
    Interface<out Object> interf = var;
    
    @type:"Callable<Interface<out Object>,Tuple<Null|Interface<out Object>,Null|Interface<out Object>,Empty>>"
    value interfaceMethod = var.interfaceMethod;
    @type:"Interface<out Object>" value ret1 = var.interfaceMethod(var);

    @type:"Callable<Class<out Object>,Tuple<Null|Class<out Object>,Null|Class<out Object>,Empty>>"
    value classMethod = var.classMethod;
    @type:"Class<out Object>" value ret2 = var.classMethod(var);
    
    @type:"JavaVariance" value m = var.method(var);
    
    @type:"Class<out Object>" value v1 = var.roField;
    @type:"Class<out Object>" value v2 = var.rwField;
    @type:"Class<out Object>" value v3 = var.roProperty;
    @type:"Class<out Object>" value v4 = var.rwProperty;

    @type:"Class<out Object>" value v5 = var.rwCovariantProperty;
    @type:"Class<in JavaVariance>" value v6 = var.rwContravariantProperty;
    @type:"Class<out Object>" value v7 = var.rwRawProperty;
}

@noanno
abstract class MyMap() satisfies Map<String, Object> {
    shared actual void putAll(Map<out String, out Object> map) {}
    shared actual formal Integer hash;
    shared actual formal Boolean equals(Object o);
}
