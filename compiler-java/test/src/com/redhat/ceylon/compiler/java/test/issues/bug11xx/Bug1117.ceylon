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
@nomodel
abstract class Bug1117Tree() {
        shared formal Integer evaluate();
}

@nomodel
class Bug1117Constant(Integer val) extends Bug1117Tree() {

        evaluate() => val;
}

@nomodel
class Bug1117UnaryOperator(Bug1117Tree branch, Integer f(Integer i)) extends Bug1117Tree() {

        evaluate() => f(branch.evaluate());
}

@nomodel
class Bug1117BinaryOperator(Bug1117Tree left, Bug1117Tree right, Integer f(Integer i, Integer j)) extends Bug1117Tree() {

        evaluate() => f(left.evaluate(), right.evaluate());
}

@nomodel
class Bug1117UnaryMinus(Bug1117Tree t) extends Bug1117UnaryOperator(t, (Integer t) => -t) {}

@nomodel
class Bug1117Plus(Bug1117Tree left, Bug1117Tree right) extends 
  Bug1117BinaryOperator(left, right, (Integer l, Integer r) => l + r) {}

@nomodel
void bug1117() {
    value t = Bug1117Plus(Bug1117UnaryMinus(Bug1117Constant(2)), Bug1117Constant(3));
    print(t.evaluate()); // [Backend error] java primitive boxing/unboxing in ceylon-generated code!
}