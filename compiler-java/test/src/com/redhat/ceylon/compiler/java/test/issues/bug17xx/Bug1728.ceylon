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
abstract class Top1728() of Side1728 | Mid1728 {}
@noanno
class Side1728() extends Top1728() {}
@noanno
abstract class Mid1728() of Bottom1728 extends Top1728() {
    shared formal Top1728 child;
}
@noanno
class Bottom1728(child) extends Mid1728() {
    shared actual Side1728|Bottom1728 child;
    shared Bottom1728 copy(Side1728|Bottom1728 blob)
            => Bottom1728(blob);
}
