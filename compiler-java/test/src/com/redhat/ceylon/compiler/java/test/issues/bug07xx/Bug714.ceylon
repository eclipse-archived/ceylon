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
import java.util { Map, Set, Collection }

@noanno
class MyMap() satisfies Map<String, Object> {
    shared actual Boolean equals(Object that) {
        return nothing;
    }
    shared actual Integer hash = 1;
    shared actual void clear() {}
    shared actual Boolean containsKey(Object? \iobject) {
        return nothing;
    }
    shared actual Boolean containsValue(Object? \iobject) {
        return nothing;
    }
    shared actual Boolean empty = nothing;
    shared actual Set<Map<String,Object>.Entry<String,Object>> entrySet() {
        return nothing;
    }
    shared actual Object get(Object? \iobject) {
        return nothing;
    }
    shared actual Set<String> keySet() {
        return nothing;
    }
    shared actual Object put(String? k, Object? v) {
        return nothing;
    }
    shared actual void putAll(Map<String,Object>? map) {}
    shared actual Object remove(Object? \iobject) {
        return nothing;
    }
    shared actual Integer size() {
        return nothing;
    }
    shared actual Collection<Object> values() {
        return nothing;
    }
}
