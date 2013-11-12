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

"Tests the correctness of the ^ optimization by comparing with the results of Integer.power()"
void floatPowerWithIntLiteralOptimCorrect() {
    Boolean identical(Float f1, Float f2) {
        if (f1.undefined) {
            return f2.undefined;
        }
        return f1 == f2 
                && f1.strictlyPositive == f2.strictlyPositive; // distinguish +0.0 from -0.0
    }
    {Float*} floats = {-4.0, -3.5, -3.0, -2.0, -1.0, -0.0, 
                    0.0, 1.0, 2.0, 3.0, 4.0,
                    infinity, -infinity, 0.0/0.0};
    for (base in floats) {
        variable Float opt;
        variable Float unopt;
        try {
            // minus 3
            opt = base^(-3);
            try {
                unopt = base.power(-3.0);
                assert(identical(opt, unopt));
            } catch (Exception e) {
                // one threw, the other not
            }
        } catch (Exception e) {
            try {
                unopt = base.power(-3.0);
                // one threw, the other not
                assert(false);
            } catch (Exception e2) {
                // both threw
                assert(e.message == e2.message);
            }
        }
        // minus 2
        try {
            opt = base^(-2);
            try {
                unopt = base.power(-2.0);
                assert(opt == unopt);
            } catch (Exception e) {
                // one threw, the other not
            }
        } catch (Exception e) {
            try {
                unopt = base.power(-2.0);
                // one threw, the other not
                assert(false);
            } catch (Exception e2) {
                // both threw
                assert(e.message == e2.message);
            }
        }
        // minus 1
        try {
            opt = base^(-1);
            try {
                unopt = base.power(-1.0);
                assert(opt == unopt);
            } catch (Exception e) {
                // one threw, the other not
            }
        } catch (Exception e) {
            try {
                unopt = base.power(-1.0);
                // one threw, the other not
                assert(false);
            } catch (Exception e2) {
                // both threw
                assert(e.message == e2.message);
            }
        }
        assert(identical(base^(0), base.power(0.0)));
        assert(identical(base^(1), base.power(1.0)));
        assert(identical(base^(2), base.power(2.0)));
        assert(identical(base^(3), base.power(3.0)));
    }
}
