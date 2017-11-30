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
import ceylon.language.meta{type}

shared
void bug2078() {
    
    class Cons<out Element>(first, rest=null) {
        shared Element first;
        shared Cons<Element>? rest;
        shared Cons<Element|Other> follow<Other>(Other head)
                =>  Cons(head, this);                   // 1
    }
    
    variable Cons<Integer|String> it = Cons("");    // 3
    for (i in 0:10k) {
        it = it.follow("" of Integer|String);
    }
    
    print(type(it)); // error
    assert((it of Anything) is Cons<Integer|String>);
}