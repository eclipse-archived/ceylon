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
void compareBoxing<T>(
    Integer i, Float f, String s,
    Comparable<Integer> intSelf,
    Comparable<Float> floatSelf,
    Comparable<String> stringSelf,
    T t,
    Comparable<T> tSelf) given T satisfies Comparable<T>{
    
    variable Boolean cmp;
    variable Comparison ord;
    ord = i <=> i;
    cmp = i < i;
    
    ord = f <=> f;
    cmp = f < f;
    
    ord = s <=> s;
    cmp = s < s;
    
    ord = t <=> t;
    cmp = t < t;
    
    ord = intSelf <=> i;
    cmp = intSelf < i;
    
    ord = floatSelf <=> f;
    cmp = floatSelf < f;
    
    ord = stringSelf <=> s;
    cmp = stringSelf < s;
    
    ord = tSelf <=> t;
    cmp = tSelf < t;
    
    assert([1].first>0);
}