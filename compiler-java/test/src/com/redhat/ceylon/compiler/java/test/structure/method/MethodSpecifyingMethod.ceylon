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
class MethodSpecifierMethod() {
    MethodSpecifierMethod noParam() {
        throw;
    }
    void noParam1() => noParam();
    void noParam2() => noParam{};

    MethodSpecifierMethod oneParam(Integer i) {
        throw;
    }
    void oneParam1() => oneParam(1);
    void oneParam2() => oneParam{i = 1;};

    MethodSpecifierMethod oneDefaultedParam(Integer i = 2) {
        throw;
    }
    void oneDefaultedParam1() => oneDefaultedParam();
    void oneDefaultedParam2() => oneDefaultedParam{};

    MethodSpecifierMethod oneSequencedParam(Integer... i) {
        throw;
    }
    void oneSequencedParam1() => oneSequencedParam(1,2);
    void oneSequencedParam2() => oneSequencedParam{i=2;i=3;};
    void oneSequencedParam3() => oneSequencedParam(for (i in {}) i);
}
