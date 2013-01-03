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
class MethodArgumentNamedInvocationMPL() {
    void m1(Callable<Callable<String, [Integer]>, []> f) {
        String s = f()(1);
    }
    void m2(Callable<Callable<Void, [Integer]>, []> f) {
        Void v = f()(1);
    }
    void m3(String(Integer)() f) {
        String s = f()(1);
    }
    void m4(Void(Integer)() f) {
        Void v = f()(1);
    }
    void m5(void f()(Integer x)) {
        Void v = f()(1);
    }
    void m6(Void f()(Integer x)) {
        Void v = f()(1);
    }
    void m7(String f()(Integer x)) {
        String s = f()(1);
    }
    
    void callsite() {
        
        m1{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m2{
            void f()(Integer x) {
                return;
            }
        };
        m2{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m3{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m4{
            void f()(Integer x) {
                return;
            }
        };
        m4{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m5{
            void f()(Integer x) {
                return;
            }
        };
        m5{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m6{
            void f()(Integer x) {
                return;
            }
        };
        m6{
            function f()(Integer x) {
                return x.string;
            }
        };
        
        m7{
            function f()(Integer x) {
                return x.string;
            }
        };
        
    }
}