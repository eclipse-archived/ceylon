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
@error
abstract class SwitchMatchTop() of switchMatch_foo | switchMatch_bar {}

@noanno
object switchMatch_foo extends SwitchMatchTop() {}
@noanno
object switchMatch_bar extends SwitchMatchTop() {}

@noanno
class SwitchMatch() {
    void onObject() {
        SwitchMatchTop fb = switchMatch_foo;
        switch (fb) 
        case (switchMatch_foo) { print("foo"); }
        case (switchMatch_bar) { print("bar"); }
    }
    void onBoolean() {
        Boolean b = true;
        switch (b)
        case (true) { print("true"); }
        case (false) { print("false"); }
    }
    
    T box<T>(T t){ return t; }
    
    void onBoxedBoolean() {
        Boolean b = true;
        switch (box(b)) 
        case (true) { print("true"); }
        case (false) { print("false"); }
    }

    void onNullableType() {
        Boolean? b = true;
        switch (b)
        case (true) { print("true"); }
        case (false) { print("false"); }
        case (null) { print("null"); }
    }
}