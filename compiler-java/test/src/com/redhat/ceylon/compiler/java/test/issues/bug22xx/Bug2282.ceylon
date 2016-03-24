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
class Bug2282() extends Object() {
    hash = 1;
    equals = nothing;
}
@noanno
interface Bug2282Z {}
@noanno
interface Bug2282A satisfies Bug2282Z {}
@noanno
interface Bug2282B satisfies Bug2282Z {}
@noanno
Bug2282A&Bug2282B bug2282k() { return nothing;}
@noanno
abstract class Bug2282C() {
    @noanno
    shared formal Bug2282Z z();
}
@noanno
class Bug2282D() extends Bug2282C() {
    z = bug2282k;
}

@noanno
F bug2282M<F>(Object o) => nothing;
@noanno
class Bug2282M() extends Object() {
    hash = 1;
    equals = bug2282M<Nothing>;
}