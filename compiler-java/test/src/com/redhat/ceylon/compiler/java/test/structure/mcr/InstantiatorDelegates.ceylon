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
interface InstantiatorDelegates<X> {
    shared class Nullary() {
    }
    shared class Unary(Boolean b) {
    }
    shared class Binary(Boolean b, Integer i=1) {
    }
    shared class Binary2(Boolean b=true, Integer i=1) {
    }
    shared class Sequenced(Boolean b, Integer... i) {
    }
    shared class TypeArg(X x) {
    }
}
@nomodel
class InstantiatorDelegatesImpl<X>() satisfies InstantiatorDelegates<X> {
}
@nomodel
void instantiatorDelegates(InstantiatorDelegates<String> a) {
    a.Nullary();
    a.Nullary{};
    
    a.Unary(true);
    a.Unary{ b=true; };
    
    a.Binary(true);
    a.Binary{ b=true; };
    
    a.Binary(true, 2);
    a.Binary{ b=true; i=2; };
    
    a.Binary2();
    a.Binary2{};
    
    a.Binary2(true);
    a.Binary2{ b=true; };
    
    a.Binary2(true, 2);
    a.Binary2{ b=true; i=2; };
    
    a.Sequenced(true);
    a.Sequenced{ b=true; };
    
    a.Sequenced(true, 1);
    a.Sequenced{ b=true; i=[1]; };
    
    a.Sequenced(true, 1, 2);
    a.Sequenced{ b=true; i=[1, 2]; };
    
    a.TypeArg("");
    a.TypeArg{ x=""; };
}