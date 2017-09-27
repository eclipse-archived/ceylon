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
@noanno
class SingletonCtorDelegation {
    shared Float x;
    shared Float y;
    
    shared new cartesian(Float x, Float y) {
        this.x = x;
        this.y = y;
    }
    
    shared new origin
            extends cartesian(0.0, 0.0) {}
    shared new oneone
            extends cartesian(1.0, 1.0) {}
}
@noanno
class SingletonCtorAbstractDelegation {
    shared Float x;
    shared Float y;
    
    abstract new point(Float x, Float y) {
        this.x = x;
        this.y = y;
    }
    
    shared new origin
            extends point(0.0, 0.0) {}
    shared new oneone
            extends point(1.0, 1.0) {}
}
shared void singletonCtorDelegation() {
    assert(SingletonCtorDelegation.origin == SingletonCtorDelegation.origin);
    assert(SingletonCtorDelegation.oneone == SingletonCtorDelegation.oneone);
    assert(1.0 == SingletonCtorDelegation.oneone.x);
    assert(1.0 == SingletonCtorDelegation.oneone.y);
    assert(SingletonCtorAbstractDelegation.origin == SingletonCtorAbstractDelegation.origin);
    assert(SingletonCtorAbstractDelegation.oneone == SingletonCtorAbstractDelegation.oneone);
    assert(1.0 == SingletonCtorAbstractDelegation.oneone.x);
    assert(1.0 == SingletonCtorAbstractDelegation.oneone.y);
}