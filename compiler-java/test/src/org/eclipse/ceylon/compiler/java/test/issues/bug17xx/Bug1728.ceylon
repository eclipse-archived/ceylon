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
@noanno
abstract class Type1728() { shared default Type1728[] children = []; }
@noanno
class IntersectionType1728() extends Type1728() {}
@noanno
abstract class PrimaryType1728() extends Type1728() {}
@noanno
class OptionalType1728() extends PrimaryType1728() {}
@noanno
class BaseType1728() extends PrimaryType1728() {}
@noanno
class UnionType1728(shared actual [<IntersectionType1728|PrimaryType1728>+] children) extends Type1728() {}
@noanno
void use1728() {
    Bottom1728 bot1 = Bottom1728(Side1728());
    Bottom1728 bot2 = Bottom1728{child=bot1;};
    Bottom1728 bot3 = Bottom1728(bot2);
    UnionType1728 ut = UnionType1728([OptionalType1728(), BaseType1728()]);
}