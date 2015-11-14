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
class SwitchIsNonExhaustiveCheapestFirst<TP1, TP2>() 
        given TP1 satisfies Float
        given TP2 satisfies Boolean {
    String m(TP1|TP2|String|Integer b) {
        String name;
        switch(b)
        case(is String) {
            name = "String";
        }
        case(is TP1) {
            name = "TP1";
        }
        case(is Integer) {
            name = "Integer";
        }
        else {
            name = "Other";
        }
        return name;
    }
}