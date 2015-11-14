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
class CharacterBoxing(){
    void m() {
        Character b1 = 'A';
        Character b2 = b1.lowercased;
        Character b3 = 'a'.uppercased;
        Character? b4 = 'a'.successor;
        Character b5 = successor(b3);
        Character? b6 = successor2(b4);
        Character b7 = successor3(b4);
        print(b7.string);
    }
    Character successor(Character c) {
        return c.successor;
    }
    Character? successor2(Character? c) {
        if (exists c) {
            return c.successor;
        }
        return null;
    }
    Character successor3(Character? c) {
        if (exists c) {
            return c.successor;
        } else {
            return ' ';
        }
    }
}