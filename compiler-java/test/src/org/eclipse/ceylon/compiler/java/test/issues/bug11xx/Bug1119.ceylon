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
abstract class Bug1119Operation() of bug1119Addition {
    shared formal Integer evaluate(Integer left, Integer right);
    shared default Boolean isOperator(String token) {
        return string == token;
    }
}

@noanno
object bug1119Addition extends Bug1119Operation() {
    evaluate(Integer left, Integer right) => left + right;
    string = "+";
    shared actual Boolean isOperator(String token) {
        Boolean b1 = "+" == token || "and" == token; //this one works
        Boolean b2 = string == token || "and" == token; // doesn't work
        return b2;
    }
}

@noanno
shared void bug1119Test() {
    print("Should consider '+' as addition operator but returns ``bug1119Addition.isOperator("+")``"); 
}
