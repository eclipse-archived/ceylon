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
void methodArgumentWithVariableParameter(void f(Integer x)){}
@noanno
void methodArgumentWithVariableParameter_fn() {
    methodArgumentWithVariableParameter{
        function f(variable Integer x) {
            x = 0;
            x++;
            x+=1;
            return x;
        }
    };
    
    methodArgumentWithVariableParameter{
        function f(variable Integer y) {
            function fn() {
                y = 0;
                y++;
                y+=1;
                return y;
            }
            return fn();
        }
    };
}