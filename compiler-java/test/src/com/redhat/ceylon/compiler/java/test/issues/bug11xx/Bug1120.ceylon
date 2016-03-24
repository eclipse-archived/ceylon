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
abstract class Bug1120Top() {
  shared formal Integer method();
  shared formal Integer attr1;
  shared formal Integer attr2;
}

@noanno
class Bug1120Bottom() extends Bug1120Top() {
    method() => 1;
    attr1 => 1;
    attr2 = 2;
}

@noanno
void bug1120() {
    Bug1120Top t3 = Bug1120Bottom();
    print(t3.method());
    print(t3.attr1);
    print(t3.attr2);

    Bug1120Bottom t4 = Bug1120Bottom();
    print(t4.method());
    print(t4.attr1);
    print(t4.attr2);
}