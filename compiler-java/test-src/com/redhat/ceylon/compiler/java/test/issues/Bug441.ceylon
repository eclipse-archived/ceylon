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
shared class Bug441<out Key, out Item>()
            satisfies Map<Key, Item> & Cloneable<Bug441<Key,Item>>
            given Key satisfies Object
            given Item satisfies Object {

    shared actual Integer hash = 0;
    
    shared actual Boolean empty { return true; }
    shared actual Integer size { return 0; }
    shared actual Item? item(Object key) { return null; }
    shared actual Boolean contains(Object element) { return false; }
    shared actual Boolean defines(Object key) { return false; }
    shared actual Bug441<Key, Item> clone { return this; }
    shared actual Iterator<Key->Item> iterator {
        object it satisfies Iterator<Key->Item> {
            shared actual Entry<Key,Item>|Finished next() { return exhausted; }
        }
        return it;
    }
    shared actual Boolean containsAny(Object... elements) { return false; }
    shared actual Boolean containsEvery(Object... elements) { return false; }
    shared actual Boolean definesAny(Object... keys) { return false; }
    shared actual Boolean definesEvery(Object... keys) { return false; }
    shared actual Integer count(Object element) { return 0; }
    shared actual Set<Key> keys { return bottom; }
    shared actual Collection<Item> values { return bottom; }
    shared actual Map<Item, Set<Key>> inverse { return bottom; }
    shared actual Item?[] items(Object... keys) { return {}; }
    shared actual Boolean equals(Object o){ return false; }
}
