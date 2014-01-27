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
class IntegralImpl() satisfies Integral<IntegralImpl> {

    shared actual IntegralImpl plus(IntegralImpl other) {
        return nothing;
    }
    
    shared actual IntegralImpl minus(IntegralImpl other) {
        return nothing;
    }
    
    shared actual IntegralImpl times(IntegralImpl other) {
        return nothing;
    }
    
    shared actual IntegralImpl divided(IntegralImpl other) {
        return nothing;
    }
    
    shared actual IntegralImpl remainder(IntegralImpl other) {
        return nothing;
    }
    
    shared actual IntegralImpl successor {
        return nothing;
    }
    
    shared actual IntegralImpl predecessor {
        return nothing;
    }
    
    shared actual IntegralImpl negativeValue {
        return nothing;
    }
    
    shared actual IntegralImpl positiveValue {
        return nothing;
    }
    
    shared actual Boolean zero {
        return nothing;
    }
    
    shared actual Boolean unit {
        return nothing;
    }

    shared actual Integer integerValue => nothing;
    
    shared actual Comparison compare(IntegralImpl other) => nothing;
    
    shared actual Float float => nothing;
    shared actual Integer integer => nothing;
    
    shared actual IntegralImpl magnitude => nothing;
    
    shared actual IntegralImpl fractionalPart => nothing;
    
    shared actual IntegralImpl wholePart => nothing;
    
    shared actual Boolean positive => false;
    
    shared actual Boolean negative => false;
    
    shared actual Integer sign => nothing;

    shared actual IntegralImpl timesInteger(Integer integer) => nothing;

    shared actual IntegralImpl plusInteger(Integer integer) => nothing;
}