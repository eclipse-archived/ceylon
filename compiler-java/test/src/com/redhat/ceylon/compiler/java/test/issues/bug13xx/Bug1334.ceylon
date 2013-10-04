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
class Bug1334_1() {
    hash => 23;
}
@noanno
class Bug1334_2() {
    hash = 23;
}
@noanno
class Bug1334_3() {
    shared actual Integer hash = 23;
}
@noanno
class Bug1334_4() {
    shared actual Integer hash => 23;
}
@noanno
class Bug1334_5() {
    shared actual Integer hash { return 23; }
}
@noanno
class Bug1334_6(shared actual Integer hash) {
}
@noanno
class Bug1334_7(shared actual Integer hash = 23) {
}
@noanno
class Bug1334_8(hash) {
    shared actual Integer hash;
}
@noanno
class Bug1334_9(hash = 23) {
    shared actual Integer hash;
}
@noanno
class Bug1334_6_2(Integer h) extends Bug1334_6(h){}
@noanno
class Bug1334_10(){
    shared actual variable Integer hash = 23;
}
@noanno
interface Bug1334_11{
    shared actual Integer hash => 23;
}
@noanno
class Bug1334_12() extends Object() satisfies Bug1334_11 {
    shared actual Boolean equals(Object o) => false;
}
