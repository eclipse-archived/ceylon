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
void anonFunctionNullary<T>(T t) {
    Callable<String, []> s1 = () => "";
    Callable<String?, []> s2 = () => "";
    Callable<String[], []> s3 = () => [""];
    
    Callable<Integer, []> i1 = () => 1;
    Callable<Integer?, []> i2 = () => 1;
    Callable<Integer[], []> i3 = () => [1];
    
    Callable<Float, []> f1 = () => 1.0;
    Callable<Float?, []> f2 = () => 1.0;
    Callable<Float[], []> f3 = () => [1.0];
    
    Callable<Sequence<Integer|Float>|Empty, []> if3 = () => [1, 1.0];
    
    Callable<Boolean, []> b1 = () => true;
    Callable<Boolean?, []> b2 = () => true;
    Callable<Boolean[], []> b3 = () => [true];
    
    Callable<Comparison, []> c1 = () => larger;
    Callable<Comparison?, []> c2 = () => larger;
    Callable<Comparison[], []> c3 = () => [larger, smaller];
    
    Callable<T, []> t1 = () => t;
    Callable<T?, []> t2 = () => t;
    Callable<T[], []> t3 = () => [t];
    
    Callable<Sequence<Integer|T>|Empty, []> it3 = () => [t, 1];   
}