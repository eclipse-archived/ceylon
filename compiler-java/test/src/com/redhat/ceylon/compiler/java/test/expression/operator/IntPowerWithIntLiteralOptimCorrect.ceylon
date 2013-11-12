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
void intPowerWithIntLiteralOptimCorrect() {
    for (base in -4..4) {
        variable Integer opt;
        variable Integer unopt;
        try {
            // minus 3
            opt = base^(-3);
            try {
                unopt = base.power(-3);
                assert(opt == unopt);
            } catch (Exception e) {
                // one threw, the other not
            }
        } catch (Exception e) {
            try {
                unopt = base.power(-3);
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
                unopt = base.power(-2);
                assert(opt == unopt);
            } catch (Exception e) {
                // one threw, the other not
            }
        } catch (Exception e) {
            try {
                unopt = base.power(-2);
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
                unopt = base.power(-1);
                assert(opt == unopt);
            } catch (Exception e) {
                // one threw, the other not
            }
        } catch (Exception e) {
            try {
                unopt = base.power(-1);
                // one threw, the other not
                assert(false);
            } catch (Exception e2) {
                // both threw
                assert(e.message == e2.message);
            }
        }
        assert(base^(0) == base.power(0));
        assert(base^(1) == base.power(1));
        assert(base^(2) == base.power(2));
        assert(base^(3) == base.power(3));
    }
}
