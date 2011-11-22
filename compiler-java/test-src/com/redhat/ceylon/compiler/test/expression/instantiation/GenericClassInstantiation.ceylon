/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
@nomodel
class Single<Element>(Element e) {
}

@nomodel
shared class KlassTypeParams<U,V>(U u, V v) {
    shared U foo(U u, V v){
        return u;
    }
}

@nomodel
class KlassTypeParamsInstantiation(){
    shared KlassTypeParams<String, Natural> m() {
        return KlassTypeParams<String, Natural>("foo", 2);
    }
    shared String m2() {
        value k = KlassTypeParams<String, Natural>("foo", 2);
        return k.foo("hello", 1);
    }
    shared String m3() {
        value k = KlassTypeParams<String, Natural>("foo", 2);
        return k.foo{u = "hello"; v = 1;};
    }
    shared void typeArgumentInference(){
    	value s = Single(69);
    }
}