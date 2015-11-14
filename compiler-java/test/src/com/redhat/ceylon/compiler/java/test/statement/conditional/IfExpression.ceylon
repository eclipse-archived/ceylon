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
interface T {
    shared Integer f() => 2;
}
@noanno
interface A satisfies T {}
@noanno
interface B satisfies T {}

@noanno
void ifExpression() {
    String? boxed = "ok";
    String s1 = if(true) then "ok" else "fail";
    String? s2 = if(true) then boxed else "fail";
    String s3 = if(is String boxed) then boxed else "fail";
    String|Integer|Float erased = 2;
    String s4 = if(is String erased) then erased else "fail";
    String s5 = if(is Integer|String erased, is String erased) then erased else "fail";
    A|B|Null erased2 = nothing;
    Integer i1 = (if(is A|B erased2) then erased2 else (nothing of A)).f();
    Integer? arg = nothing;
    value x0 = if (exists arg) then arg.string else "0.0";
    value x1 = if (exists arg) then 1.0 else 0.0;
    value x2 = if (exists arg) then arg.string else 0.0;
    value x3 = if (exists arg) then arg.string else 0.0;
    
}
