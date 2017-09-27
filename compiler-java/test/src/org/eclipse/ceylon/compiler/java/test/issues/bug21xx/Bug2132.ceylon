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

import java.util { JDate = Date }

@noanno
shared void bug2132() {
    GenericOuter<JDate> dateOuter1 = GenericOuter<JDate>(JDate()); // ok
    GenericOuter<JDate>.Inner dateInner1 = dateOuter1.Inner(); // ok
    GenericOuter<JDate>.Inner dateInner2 = GenericOuter<JDate>.dateInner();
    GenericOuter<JDate>.Inner dateInner3 = GenericOuter<Object>.dateInner();
    GenericOuter<JDate>.Inner dateInner4 = DateInnerFactory().dateInner();
    GenericOuter<JDate>.Inner dateInner5 = DateInnerFactory().dateInner() of GenericOuter<JDate>.Inner;
}