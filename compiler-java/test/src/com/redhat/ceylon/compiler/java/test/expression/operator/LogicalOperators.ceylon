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
shared abstract class LogicalOperatorsParent<B>(){
    shared formal variable B boxedB1;
    shared formal variable B boxedB2;
}

@nomodel
shared class LogicalOperators() extends LogicalOperatorsParent<Boolean>(){
    shared variable Boolean b1 = false;
    shared variable Boolean b2 = false;
    shared actual variable Boolean boxedB1 = b1;
    shared actual variable Boolean boxedB2 = b2;
        
    shared default void logical() {
        b1 = !b2;
        b1 = true || b2;
        b1 = false && b2;
        b1 = b1 ||= b2;
        b1 = b1 &&= b2;
        b1 = this.b1 ||= this.b2;
        b1 = this.b1 &&= this.b2;
    }

    shared default void logicalBoxed() {
        boxedB1 = !boxedB2;
        boxedB1 = true || boxedB2;
        boxedB1 = false && boxedB2;
        boxedB1 = boxedB1 ||= boxedB2;
        boxedB1 = boxedB1 &&= boxedB2;
        boxedB1 = this.boxedB1 ||= this.boxedB2;
        boxedB1 = this.boxedB1 &&= this.boxedB2;
    }
}

@nomodel
shared class LogicalOperatorsSub() extends LogicalOperators(){
    
    shared actual void logical() {
        b1 = super.b1 ||= super.b2;
        b1 = super.b1 &&= super.b2;
    }

    shared actual void logicalBoxed() {
        boxedB1 = super.boxedB1 ||= super.boxedB2;
        boxedB1 = super.boxedB1 &&= super.boxedB2;
    }
}