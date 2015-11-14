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

variable String bug1739CallSequence = "";

void bug1739CallTwice(Anything() callable) {
    callable();
    callable();
}

class Bug1739A() {
    bug1739CallSequence += "A";
    shared String s() {
        bug1739CallSequence += "s";
        return "s";
    }
}
class Bug1739B() {
    bug1739CallSequence += "B";
    shared Bug1739A a {
        bug1739CallSequence += "a";
        return Bug1739A();
    }
}
class Bug1739C() {
    bug1739CallSequence += "C";
    shared Bug1739B b {
        bug1739CallSequence += "b";
        return Bug1739B();
    }
}
class Bug1739D() {
    bug1739CallSequence += "D";
    shared Bug1739C c {
        bug1739CallSequence += "c";
        return Bug1739C();
    }
}

shared void bug1739() {
    bug1739CallTwice(Bug1739D().c.b.a.s);
    assert("DcCbBaAss"==bug1739CallSequence);
}