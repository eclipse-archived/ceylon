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
void bug2304If() {
    value values = { 100, 110, 120 };
    value funcs = {
        for (v in values)
        if (exists w = true then (()=>v))
        w
    };
    value iter = funcs.sequence().iterator();
    assert(is Integer() x = iter.next(), 
        100 == x());
    assert(is Integer() y = iter.next(), 
        110 == y());
    assert(is Integer() z = iter.next(), 
        120 == z());
    assert(iter.next() is Finished);
}
@noanno
void bug2304For() {
    value values = { 100, 110, 120 };
    value funcs = {
        for (v in values)
        for (w in { ()=>v })
        w
    };
    value iter = funcs.sequence().iterator();
    assert(is Integer() x = iter.next(), 
        100 == x());
    assert(is Integer() y = iter.next(), 
        110 == y());
    assert(is Integer() z = iter.next(), 
        120 == z());
    assert(iter.next() is Finished);
}
@noanno
void bug2304() {
    bug2304If();
    bug2304For(); 
}