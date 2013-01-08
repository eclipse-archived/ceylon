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
@error
void variance() {
    JavaVariance var = JavaVariance();
    // For M4: tested in Variance2
    //Class<Object> klass = var;
    //Interface<Object> interf = var;
    
    @type:"Callable<Object,Tuple<Null|Interface<Object>,Null|Interface<Object>,Empty>>"
    value interfaceMethod = var.interfaceMethod;
    @type:"Object" value ret1 = var.interfaceMethod(var);

    @type:"Callable<Object,Tuple<Null|Class<Object>,Null|Class<Object>,Empty>>"
    value classMethod = var.classMethod;
    @type:"Object" value ret2 = var.classMethod(var);
    
    @type:"JavaVariance" value m = var.method(var);
    
    @type:"Object" value v1 = var.roField;
    @type:"Object" value v2 = var.rwField;
    @type:"Object" value v3 = var.roProperty;
    @type:"Object" value v4 = var.rwProperty;
}