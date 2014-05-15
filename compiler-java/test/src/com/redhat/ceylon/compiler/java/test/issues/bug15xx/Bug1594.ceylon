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
interface Bug1594Producer<out T> {
    shared formal T t; 
}
@noanno
interface Bug1594ProducerWithBound<out T> 
        given T satisfies Number<Integer> {
    shared formal T t;
}
@noanno
interface Bug1594ProducerAlias => Bug1594Producer<String>;
@noanno
interface Bug1594Consumer<in T> {
    shared formal void m(T t);
}
@noanno
interface Bug1594ConsumerWithBound<in T> 
        given T satisfies Number<Integer> {
    shared formal void m(T t);
}
@noanno
interface Bug1594Alias => List<Character>;

class Bug1594Class<T>() {
    
}
@noanno
class Bug1594OptimizedIs<X, Y, Y2, Z>(Anything o) 
        given X satisfies List<Anything> 
        given Y satisfies String
        given Y2 satisfies Bug1594Alias
        given Z satisfies Y {
    // cases we can optimize completely to an instanceof
    if (is Bug1594Producer<Anything> o) {
        print("Bug1594Producer<Anything>");
    }
    if (is Bug1594ProducerWithBound<Number<Integer>> o) {
        print("Bug1594ProducerWithBound<Number<Integer>>");
    }
    if (is Bug1594ProducerWithBound<Anything> o) {
        print("Bug1594ProducerWithBound<Anything>");
    }
    if (is List<Anything> o) {
        print("List<Anything>");
    }
    
    // cases we can do a short-circuit instanceof against the type itself
    if (is Bug1594Producer<Object> o) {
        print("Bug1594Producer<Object>");
    }
    if (is Bug1594Producer<Number<Integer>> o) {
        print("Bug1594Producer<Number<Integer>>");
    }
    if (is Bug1594ProducerAlias o) {
        print("Bug1594ProducerAlias");
    }
    if (is Bug1594Consumer<Nothing> o) {
        print("Bug1594Consumer<Nothing>");
    }
    if (is Bug1594Consumer<Anything> o) {
        print("Bug1594Consumer<Anything>");
    }
    if (is Bug1594ConsumerWithBound<Anything> o) {
        print("Bug1594ConsumerWithBound<Anything>");
    }
    if (is Bug1594ConsumerWithBound<Number<Integer>> o) {
        print("Bug1594ConsumerWithBound<Number<Integer>>");
    }
    
    // cases where can can do a short-circuit instanceof against the type parameter bounds
    if (is X o) {
        print("X");
    }
    if (is Y o) {
        print("Y");
    }
    if (is Y2 o) {
        print("Y2");
    }
    if (is Z o) {
        print("Z");
    }
    // ... but we don't in this case because there's no point
    String s = nothing;
    if (is X s) {
        print("X");
    }
    // cases where we can optimize the test by doing an instanceof 
    // against the complement type instead of a Util.isReified() on the given type
    String|Bug1594Class<Integer> o2 = nothing;
    if (is Bug1594Class<Integer> o2) {
        
    }
    String|Bug1594Producer<Integer> o3 = nothing;
    if (is Bug1594Producer<Integer> o3) {
        
    }
}