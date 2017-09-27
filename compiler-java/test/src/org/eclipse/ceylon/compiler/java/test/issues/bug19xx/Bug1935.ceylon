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
interface One1935<out Item> {
}
@noanno
class OneClass1935<Item>(One1935<Item> t) 
        satisfies One1935<Item&Object> {
}
@noanno
One1935<Item&Object> one1935<Item>(One1935<Item> t)
        => OneClass1935(t);


@noanno
interface Two1935<out Key,out Item>
        given Key satisfies Object {
}
@noanno
class TwoClass1935<Key,Item>(Two1935<Key, Item> t) 
        satisfies Two1935<Key,Item&Object>
        given Key satisfies Object {
}
@noanno
Two1935<Key,Item&Object> two1935<Key,Item>(Two1935<Key,Item> t)
        given Key satisfies Object
        => TwoClass1935(t);


interface Parameter1935<Key,Item,X,Y> 
        given Key satisfies Object {
    shared formal void oneObject(One1935<Item&Object> t);
    shared formal void oneXy(One1935<X&Y> t);
    shared formal void twoObject(Two1935<Key,Item&Object> t);
    shared formal void twoXy(Two1935<Key,X&Y> t);
}
class ParameterSub1935<Key,Item,X,Y>()
        satisfies Parameter1935<Key,Item,X,Y>
        given Key satisfies Object 
        given Item satisfies Object 
        given X satisfies Y{
    shared actual void oneObject(One1935<Item> t) {}
    shared actual void oneXy(One1935<X> t) {}
    shared actual void twoObject(Two1935<Key,Item> t) {}
    shared actual void twoXy(Two1935<Key,X> t) {}
}