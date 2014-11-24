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
class SwitchInlineVar() {
    shared String m1() {
        String name;
        switch(b=m1helper()) 
        case(is String) {
            name = "String: " + b;
        }
        case(is Exception) {
            name = "Exception: " + b.message;
        }
        return name;
    }
    String|Exception m1helper() {
        return "";
    }
    shared String m2() {
        String name;
        switch(b=m2helper()) 
        case("aap") {
            name = "Monkey: " + b;
        }
        case("noot") {
            name = "Nut: " + b;
        }
        else {
            name = b;
        }
        return name;
    }
    String m2helper() {
        return "";
    }
    shared String m3() {
        String name;
        switch(b=m3helper()) 
        case("aap") {
            name = "Monkey: " + (b else "?");
        }
        case(null) {
            name = "NULL";
        }
        else {
            name = b;
        }
        return name;
    }
    String? m3helper() {
        return "";
    }
}