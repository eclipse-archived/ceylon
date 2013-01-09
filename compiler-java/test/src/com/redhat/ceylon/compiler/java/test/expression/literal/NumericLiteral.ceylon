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
class NumericLiteral(){
    shared void m() {
        // make sure we test all the max/min limits
        Integer i1 = 9223372036854775807;
        Integer i2 = -9_223_372_036_854_775_808;
        Integer i3 = +2147483647;
        Integer i4 = -2147483648;
        Integer i5 = 0;
        Integer i6 = -0;
        
        Float f1 = 1.7976931348623157E308;
        Float f2 = -1.7976931348623157E308;
        Float f3 = 4.9E-324;
        Float f4 = -4.9E-324;
        Float f5 = 0.0;
        Float f6 = -0.0;
    }
    
    shared void hexBin(){
        Integer x1 = #CAFEBABE;
        Integer x2 = #cafebabe;
        Integer x3 = #FFFFFFFFFFFFFFFF;

        Integer b1 = $1101;
        Integer b2 = $1101110111011101110111011101110111011101110111011101110111011101;
    }
}