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
class ExponentiableImpl<This,Other>() of This satisfies Exponentiable<This,Other>  
        given This satisfies ExponentiableImpl<This,Other> 
        given Other satisfies Numeric<Other> {

    shared actual This power(Other exponent) {
        return nothing;
    }
        
    shared actual This plus(This other) {
        return nothing;
    }
    
    shared actual This minus(This other) {
        return nothing;
    }
    
    shared actual This times(This other) {
        return nothing;
    }
    
    shared actual This divided(This other) {
        return nothing;
    }
    
    shared actual This negativeValue {
        return nothing;
    }
    
    shared actual This positiveValue {
        return nothing;
    }
}