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
class AssertionConditionList() {
    Boolean m1(Integer x) {
        assert (x > 0, x < 10);
        return x == 1;
    }
    Boolean m2(Anything x, Integer z) {
        assert (is Integer y = x, y > 0, z < 10);
        return y == 1;
    }
    
    Boolean m3(Anything x, Integer z) {
        assert (z < 10, is Integer y = x, y > 0);
        return y == 1;
    }
    
    Boolean m4(Anything x, Integer z) {
        assert (z < 10, z > 0, is Integer y = x);
        return y == 1;
    }
    
    Boolean m5(Anything[] x) {
        assert (nonempty x, is Integer y = x[0], y > 0);
        return y == 1;
    }
    
    Boolean m6(Anything[] x) {
        assert (x[0] exists, is Integer y = x[0], y > 0);
        return y == 1;
    }
    
    /*Boolean m7(Integer[] x) {
        assert (exists x[0], exists x[1]);
        return x[0] + x[1] == 0;
    }
    
    Boolean m8(Integer[] x) {
        assert (exists x[0]);
        return x[0] == 0;
    }*/
    
    Boolean m9(Anything x, Anything y) {
        assert (is Integer x1 = x, is Integer y1 = y);
        return x1 + y1 == 0;
    }
}
