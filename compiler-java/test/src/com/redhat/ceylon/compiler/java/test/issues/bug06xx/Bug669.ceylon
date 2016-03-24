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
Boolean bug669_eq(Object? expected, Object? got) {
    if (exists expected) {
        if (exists got) {
            print("comparing " + expected.string + " and " + got.string);
            return expected==got;
        }
    }
    return got exists == expected exists;
}

"Fails the test if the two objects are not equal"
shared void bug669_assertEquals(Object? expected, Object? got,
        String? message=null,
        Boolean compare(Object? expected, Object? got) => bug669_eq(expected, got)) {
    if (!compare(expected,got)) {
        if (exists message) {
            throw Exception(message);
        }
        else {
            throw;
        }
    }
}

void bug669_testAssertEquals() {
    bug669_assertEquals(0,0);
}