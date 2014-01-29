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
    value funcs = { for (v in values) ()=>v };
    assert({100, 110, 120} == { for (f in funcs) f() }.sequence);
    ifCaptures();
    nestedIfCaptures();
    nestedForCaptures();
}

@noanno
shared void ifCaptures() {
    value values = { 100, 110, 120, null };
    value funcs = { for (v in values) if (exists w=v) ()=>w };
    assert({100, 110, 120} == { for (f in funcs) f() }.sequence);
}

@noanno
shared void nestedIfCaptures() {
    value values = { 100, 110, 120, null, "a" };
    value funcs = { for (v in values) if (exists v2=v) if(is Integer v3 = v2) ()=>v2 };
    assert({100, 110, 120} == { for (f in funcs) f() }.sequence);
}

@noanno
shared void nestedForCaptures() {
    value values = { {100}, {110}, {120} };
    value funcs = { for (v in values) for (v2 in v) ()=>v };
    assert({{100}, {110}, {120}} == { for (f in funcs) f() }.sequence);
}
