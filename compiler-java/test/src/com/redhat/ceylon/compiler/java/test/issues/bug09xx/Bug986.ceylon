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
shared void bug986() {
    value values = { 100, 110, 120 };
    class Fun(shared Integer() fun) {}
    value funcs = { for (v in values) Fun(()=>v) };
    assert (exists first=funcs.sequence[0]);
    assert (exists second=funcs.sequence[1]);
    assert (exists third=funcs.sequence[2]);
    if (first.fun() != 100) {
        print(first.fun());
        throw Exception();
    }
    if (second.fun() != 110) {
        print(second.fun());
        throw Exception();
    } 
    if (third.fun() != 120) {
        print(third.fun());
        throw Exception();
    }
}