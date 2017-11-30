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

shared class AssertionFailed(String message) extends Exception(message, null) {
}

shared class ComparisonFailed(String message, 
  expected, got) extends AssertionFailed(message) {
    shared Object? expected;
    shared Object? got;
}

shared void fail(String? message = null) {
    throw AssertionFailed(message else "fail");
}

shared void assertTrue(Boolean got, String? message = null) {
    if (!got) {
        throw AssertionFailed(message else "Expected true");
    }
}

shared void assertFalse(Boolean got, String? message = null) {
    if (got) {
        throw AssertionFailed(message else "Expected false");
    }
}

shared void assertEquals(Object? expect, Object? got, String? message = null) {
    if (exists expect) {
        if (exists got) {
            if (expect != got) {
                throw ComparisonFailed(message else "Expected `` expect `` but got `` got ``", 
                    expect, got);
            } else {
                return;
            }
        }
        throw ComparisonFailed(message else "Expected `` expect `` but got null", 
            expect, got);
    } else if (exists got) {
        throw ComparisonFailed(message else "Expected null but got `` got ``", 
            expect, got);
    }
}

shared void assertApproximatelyEquals(Float? expect, Float? got, Float tolerance, String? message = null) {
    if (exists expect) {
        if (exists got) {
            if ((expect - got).magnitude > tolerance) {
                throw ComparisonFailed(message else "Expected `` expect `` but got `` got `` (to within `` tolerance ``)", 
                    expect, got);
            } else {
                return;
            }
        }
        throw ComparisonFailed(message else "Expected `` expect `` but got null", 
            expect, got);
    } else if (exists got) {
        throw ComparisonFailed(message else "Expected null but got `` got ``", 
            expect, got);
    }
}