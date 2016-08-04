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
interface Bug6409I {
    shared formal Anything boxed();
    shared formal Anything boxed2();
    shared formal Anything boxed3();
    shared formal Integer unboxed();
    shared formal Integer unboxed2();
    shared formal Integer unboxed3();
}

@noanno
object bug6409 satisfies Bug6409I {
    boxed = () { return 0; };
    boxed2 = () => 0;
    boxed3() => 0;
    unboxed = () { return 0; };
    unboxed2 = () => 0;
    unboxed3() => 0;
}
