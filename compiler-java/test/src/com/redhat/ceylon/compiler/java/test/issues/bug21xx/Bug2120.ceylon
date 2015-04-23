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
interface Bug2120X {
    shared actual default 
    Boolean equals(Object that) 
            => this==that;
}

@noanno
class Bug2120Z() satisfies Bug2120X {
    equals(Object other) => (super of Bug2120X).equals(other);

    void f(){
        value eq1 = equals;
        value eq2 = this.equals;
        value eq3 = (super of Bug2120X).equals;
    }
}

@noanno
class Bug2120Z2() {
    equals(Object other) => (super of Basic).equals(other);

    void f(){
        value eq3 = (super of Basic).equals;
    }
}

@noanno
interface Bug2120Z3 satisfies Bug2120X {
    void f(){
        value eq3 = (super of Bug2120X).equals;
    }
}
