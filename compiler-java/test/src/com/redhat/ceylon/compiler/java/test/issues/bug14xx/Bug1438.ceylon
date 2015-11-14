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
shared void bug1438() {
    String foo;
    if (system.milliseconds % 2 == 0) {
        foo = "if";
    } else {
        for (i in 0..10) {
            if (system.milliseconds % 2 == 0) {
                foo = "break";
                break;
            }
        } else {
            foo = "else";
        }
    }
    print(foo); // <= compile error here
}

@noanno
shared void bug1438_2() {
    String foo;
    if (system.milliseconds % 2 == 0) {
        for (i in 0..10) {
            if (system.milliseconds % 2 == 0) {
                foo = "break"; // exception during compile here
                break;
            }
        } else {
            foo = "else";
        }
    } else {
        foo = "if";
    }
    print(foo);
}