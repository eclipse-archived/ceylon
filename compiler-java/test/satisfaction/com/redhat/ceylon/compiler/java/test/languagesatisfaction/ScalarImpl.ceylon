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
class ScalarImpl<Other>() of Other satisfies Scalar<Other>
        given Other satisfies Scalar<Other> {

    shared actual Other magnitude = nothing;
    
    shared actual Other fractionalPart = nothing;
    
    shared actual Other wholePart = nothing;

    shared actual Other plus(Other other) {
        return nothing;
    }
    
    shared actual Other minus(Other other) {
        return nothing;
    }
    
    shared actual Other times(Other other) {
        return nothing;
    }
    
    shared actual Other divided(Other other) {
        return nothing;
    }
    
    shared actual Integer integer = nothing;
    
    shared actual Float float = nothing;
    
    shared actual Integer sign = nothing;
    
    shared actual Boolean positive = nothing;
    
    shared actual Boolean negative = nothing;
    
    shared actual Other negativeValue = nothing;
    
    shared actual Other positiveValue = nothing;
    
    shared actual Comparison compare(Other other) {
        return nothing;
    }
    

}