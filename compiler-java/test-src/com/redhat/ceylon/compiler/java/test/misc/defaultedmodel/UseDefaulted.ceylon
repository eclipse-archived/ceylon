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
void useDefaulted() {
    DefineDefaulted f = DefineDefaulted();
    f.f1();
    f.f1(6);
    f.f2(1);
    f.f2(1, "foo");
    f.f3();
    f.f3(6);
    f.f3(6, 9);
    f.f4();
    f.f4(6);
    f.f4(6, 9);
    DefineDefaulted2 f2 = DefineDefaulted2();
    DefineDefaulted2 f3 = DefineDefaulted2(6);
    DefineDefaulted2 f4 = DefineDefaulted2(6, 9);
}
