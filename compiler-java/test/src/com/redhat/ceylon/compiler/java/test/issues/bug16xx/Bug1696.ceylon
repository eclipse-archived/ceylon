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
interface Bug1696<out Element, out Absent> {

    shared default [Element] paired => nothing;
    shared default [Element] paired2 => nothing;
    //shared formal [Element] foo;
}

@noanno
class Bug1696Extend<Other>() satisfies Bug1696<String|Other,String> {
    shared actual [String|Other] paired2 {
        [String|Other] r = super.paired;
        return r;
    }
}

// paired is in none of the hierarchy
@noanno
interface Bug1696MixinA1 satisfies Iterable<Anything> {
    // here it's with Object
}
@noanno
abstract class Bug1696MixinA2<T>() satisfies Bug1696MixinA1&Iterable<T> {
    // here it's with T
}

// paired is in the top of the hierarchy
@noanno
interface Bug1696MixinB1 satisfies Iterable<Anything> {
    // explicit with Object
    actual shared default {[Anything,Anything]*} paired => nothing;
}
@noanno
abstract class Bug1696MixinB2<T>() satisfies Bug1696MixinB1&Iterable<T> {
    // here it's with T
}

// paired is in the bottom of the hierarchy
@noanno
interface Bug1696MixinC1 satisfies Iterable<Anything> {
    // here it's with Object
}
@noanno
abstract class Bug1696MixinC2<T>() satisfies Bug1696MixinC1&Iterable<T> {
    // explicit with T
    actual shared default {[T,T]*} paired => nothing;
}

// paired is explicit everywhere
@noanno
interface Bug1696MixinD1 satisfies Iterable<Anything> {
    // explicit with Object
    actual shared default {[Anything,Anything]*} paired => nothing;
}
@noanno
abstract class Bug1696MixinD2<T>() satisfies Bug1696MixinD1&Iterable<T> {
    // explicit with T
    actual shared default {[T,T]*} paired => nothing;
}
