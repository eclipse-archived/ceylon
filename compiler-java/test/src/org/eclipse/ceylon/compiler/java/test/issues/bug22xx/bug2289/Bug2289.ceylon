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
import ceylon.collection{
    MutableMap, 
    MapMutator
}

@noanno
interface Top2289 {
    shared formal void top();
}
@noanno
interface Left2289 satisfies Top2289 {
}
@noanno
interface Right2289 satisfies Top2289 {
}

@noanno
void bug2289() {
    MutableMap<Object, Usable> mm = nothing;
    value defines1 = mm.defines; // Incompatible types
    value defines2 = (mm of Map<Object,Usable>).defines; // ok
    value defines3 = (mm of MapMutator<Object,Usable>).defines; // ok
    
    Left2289|Right2289 middle = nothing;
    value t = middle.top;
}
