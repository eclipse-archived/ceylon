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
Callable<String,[Integer]> methodSpecifyingCallable_f() {
    throw;
}
@nomodel
class MethodSpecifyingCallable() {
    String foo(Integer i) => methodSpecifyingCallable_f()(i);
    
    value noParam = function () nothing;
    void noParam1() => noParam();

    value oneParam = function (Integer i) nothing;
    void oneParam1() => oneParam(1);

    // FIXME: this will be supported soon but will likely be tested somewhere else
    //value oneDefaultedParam = function (Integer i = 2) nothing;
    // void oneDefaultedParam1() => oneDefaultedParam();

    value oneSequencedParam = function (Integer... i) nothing;
    void oneSequencedParam0() => oneSequencedParam();
    void oneSequencedParam1() => oneSequencedParam(1);
    void oneSequencedParam2() => oneSequencedParam(1, 2);
    void oneSequencedParam3() => oneSequencedParam(for (i in {}) i);
    void oneSequencedParam4() => oneSequencedParam({}...);

    value secondSequencedParam = function (Integer a, Integer... i) nothing;
    void secondSequencedParam0() => secondSequencedParam(1);
    void secondSequencedParam1() => secondSequencedParam(1, 2);
    void secondSequencedParam2() => secondSequencedParam(1, 2, 3);
    void secondSequencedParam3() => secondSequencedParam(1, for (i in {}) i);
    void secondSequencedParam4() => secondSequencedParam(1, {}...);
}