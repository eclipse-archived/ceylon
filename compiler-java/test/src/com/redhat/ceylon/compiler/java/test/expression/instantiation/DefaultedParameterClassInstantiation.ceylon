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
class DefaultedParameterClassInstantiation(Integer m = 1, Integer n = m + 1, Integer* seq){
    shared void test() {
        DefaultedParameterClassInstantiation a = DefaultedParameterClassInstantiation();
        DefaultedParameterClassInstantiation b = DefaultedParameterClassInstantiation(6);
        DefaultedParameterClassInstantiation c = DefaultedParameterClassInstantiation(6, 9);
        DefaultedParameterClassInstantiation d = DefaultedParameterClassInstantiation(6, 9, 1, 2, 3);
        DefaultedParameterClassInstantiation e = DefaultedParameterClassInstantiation{};
        DefaultedParameterClassInstantiation f = DefaultedParameterClassInstantiation{m=6;};
        DefaultedParameterClassInstantiation g = DefaultedParameterClassInstantiation{m=6; n=9;};
        DefaultedParameterClassInstantiation h = DefaultedParameterClassInstantiation{m=6; n=9; seq=[1, 2, 3];};
    }
}
