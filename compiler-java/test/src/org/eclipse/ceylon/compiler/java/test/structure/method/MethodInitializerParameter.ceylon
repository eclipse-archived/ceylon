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
class MethodInitializerParameter<T>(T t) {
    void unary(m) {
        void m(Integer i);
        m(1);
        m{
            i=1;
        };
    }
    void nary(m) {
        void m(Integer i1,Integer i2,Integer i3,Integer i4);
        m(1,2,3,4);
        m{
            i1=1;
            i2=2;
            i3=3;
            i4=4;
        };
    }
    void sequenced(m) {
        void m(Integer i, Integer* seq);
        m(1);
        m(1, 2);
        m(1, 2, 3);
        m{
            i=1;
        };
        m{
            seq=[1, 2, 3]; 
            i=1;
        };
    }
    void typeParameter(m) {
        T m(T t);
        m(t);
        m{
            t=t;
        };
    }
    
}