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
@noanno
class ExceptionAttributes() {
    void m(Exception e) {
        Throwable? c = e.cause;
        if (exists Throwable cause = e.cause) {
            Throwable? cc = cause.cause;
            String? message = cause.message;
        }
        String message = e.message;
        
        variable Throwable[] s = e.suppressed;
        e.addSuppressed(Error());
        MyError me = MyError();
        me.addSuppressed(Error());
        s = me.suppressed;
    }
    
    class MyError() extends Error() {
        class MyInnerError() extends Error() {
            void m() {
                value x = this.suppressed;
                value y = super.suppressed;
                value y2 = (super of Error).suppressed;
                value z = outer.suppressed;
            }
        }
    }
}