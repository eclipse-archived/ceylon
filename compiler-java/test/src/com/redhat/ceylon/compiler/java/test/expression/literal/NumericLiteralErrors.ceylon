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
class NumericLiteralErrors(){
    shared void m() {
        // make sure we test all the max/min limits
        Integer i1 = 9223372036854775808;
        Integer i2 = -9_223_372_036_854_775_809;
        
        Float f1 = 1.7976931348623159E308;
        Float f2 = -1.7976931348623159E308;
        Float f3 = 2.0E-324;
        Float f4 = -2.0E-324;
    }

    shared void hexBin() {
        Integer ex5 = #CAFEBABECAFEBABE1;

        Integer eb5 = $11011101110111011101110111011101110111011101110111011101110111011;
    }
}